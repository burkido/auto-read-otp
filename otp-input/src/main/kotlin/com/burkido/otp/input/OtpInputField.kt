package com.burkido.otp.input

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.foundation.text.input.then
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.password
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.flow.drop

/**
 * An input for one-time codes that draws each digit in its own cell.
 *
 * Only ASCII digits are accepted and the text never grows past [otpLength], whether it's typed,
 * pasted, autofilled or set through [fillOtp]. The field is marked as an SMS one-time code for
 * autofill, so keyboards like Gboard can offer the code from a new message.
 *
 * ```kotlin
 * val otpState = rememberTextFieldState()
 *
 * OtpInputField(
 *     state = otpState,
 *     otpLength = 6,
 *     onComplete = { code -> viewModel.verify(code) },
 * )
 * ```
 *
 * Pass a `cell` to change how cells look, for example with [UnderlineOtpCell] or [OutlinedOtpCell].
 * The styling parameters here only affect the default cell.
 *
 * @param state Holds the entered text. Use `rememberTextFieldState()` so it survives configuration
 *   changes and process death.
 * @param otpLength Number of cells. Must be positive.
 * @param enabled When false, the field can't be focused or edited and cells are drawn faded.
 * @param isError Shows error colors. Switching it to true also shakes the cells, see [animationSpec].
 * @param placeholder Text drawn in empty cells that aren't focused.
 * @param maskConfig Hides digits after a short delay. When masking is on, accessibility services
 *   treat the field as a password and don't read the digits aloud.
 * @param inputTransformation Extra filtering applied after the built-in digits and length check.
 * @param onComplete Called each time the field goes from incomplete to holding [otpLength] digits.
 *   It isn't called for a value that was already complete when the field appeared, so restoring
 *   state after rotation doesn't submit the code twice.
 * @param separator Drawn between cells instead of a [OtpCellDimensions.spacing] gap. Receives the
 *   index of the cell on its left.
 * @param cell Draws one cell.
 */
@Composable
public fun OtpInputField(
    state: TextFieldState,
    otpLength: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    placeholder: String = "",
    colors: OtpCellColors = OtpInputDefaults.colors(),
    dimensions: OtpCellDimensions = OtpInputDefaults.dimensions(),
    animationSpec: OtpAnimationSpec = OtpInputDefaults.animationSpec(),
    shape: Shape = OtpInputDefaults.shape(dimensions.cornerRadius),
    maskConfig: OtpMaskConfig = OtpInputDefaults.maskConfig(),
    cursorConfig: OtpCursorConfig = OtpInputDefaults.cursorConfig(color = colors.cursorColor),
    textStyles: OtpTextStyles = OtpInputDefaults.textStyles(dimensions.textSize),
    contentPadding: OtpCellPadding = OtpInputDefaults.contentPadding(),
    keyboardOptions: KeyboardOptions = OtpInputDefaults.keyboardOptions,
    inputTransformation: InputTransformation? = null,
    onComplete: ((code: String) -> Unit)? = null,
    separator: (@Composable (afterIndex: Int) -> Unit)? = null,
    cell: @Composable (OtpCellState) -> Unit = { cellState ->
        DefaultOtpCell(
            state = cellState,
            placeholder = placeholder,
            colors = colors,
            dimensions = dimensions,
            animationSpec = animationSpec,
            shape = shape,
            maskConfig = maskConfig,
            cursorConfig = cursorConfig,
            textStyles = textStyles,
            contentPadding = contentPadding,
        )
    },
) {
    require(otpLength > 0) { "otpLength must be positive but was $otpLength" }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val transformation = remember(otpLength, inputTransformation) {
        val digitsOnly = OtpInputTransformation(otpLength)
        if (inputTransformation == null) digitsOnly else digitsOnly.then(inputTransformation)
    }

    CompletionEffect(key = state, otpLength = otpLength, text = { state.text }, onComplete = onComplete)

    // The text field underneath is invisible, so a tap would otherwise drop the cursor between
    // digits and the next keypress would land in the middle of the code.
    LaunchedEffect(state) {
        snapshotFlow { state.selection }.collect { selection ->
            if (selection != TextRange(state.text.length)) state.edit { placeCursorAtEnd() }
        }
    }

    CompositionLocalProvider(LocalTextSelectionColors provides HiddenSelectionColors) {
        BasicTextField(
            state = state,
            modifier = modifier.otpSemantics(maskConfig.isMasked),
            enabled = enabled,
            inputTransformation = transformation,
            textStyle = HiddenTextStyle,
            keyboardOptions = keyboardOptions,
            lineLimits = TextFieldLineLimits.SingleLine,
            interactionSource = interactionSource,
            cursorBrush = HiddenCursor,
            decorator = { innerTextField ->
                OtpCells(
                    text = state.text,
                    otpLength = otpLength,
                    isFocused = isFocused,
                    isError = isError,
                    enabled = enabled,
                    spacing = dimensions.spacing,
                    separator = separator,
                    cell = cell,
                    innerTextField = innerTextField,
                )
            },
        )
    }
}

/**
 * [OtpInputField] for state you keep as a String, for example in a ViewModel's UI state.
 *
 * Prefer the [TextFieldState] overload when you can. It handles IME edits more reliably and saves
 * its own state. Every value passed to [onValueChange] is already filtered to digits and trimmed
 * to [otpLength].
 *
 * ```kotlin
 * OtpInputField(
 *     value = uiState.code,
 *     onValueChange = viewModel::onCodeChange,
 *     otpLength = 6,
 * )
 * ```
 *
 * See the other overload for the remaining parameters.
 */
@Composable
public fun OtpInputField(
    value: String,
    onValueChange: (String) -> Unit,
    otpLength: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    placeholder: String = "",
    colors: OtpCellColors = OtpInputDefaults.colors(),
    dimensions: OtpCellDimensions = OtpInputDefaults.dimensions(),
    animationSpec: OtpAnimationSpec = OtpInputDefaults.animationSpec(),
    shape: Shape = OtpInputDefaults.shape(dimensions.cornerRadius),
    maskConfig: OtpMaskConfig = OtpInputDefaults.maskConfig(),
    cursorConfig: OtpCursorConfig = OtpInputDefaults.cursorConfig(color = colors.cursorColor),
    textStyles: OtpTextStyles = OtpInputDefaults.textStyles(dimensions.textSize),
    contentPadding: OtpCellPadding = OtpInputDefaults.contentPadding(),
    keyboardOptions: KeyboardOptions = OtpInputDefaults.keyboardOptions,
    onComplete: ((code: String) -> Unit)? = null,
    separator: (@Composable (afterIndex: Int) -> Unit)? = null,
    cell: @Composable (OtpCellState) -> Unit = { cellState ->
        DefaultOtpCell(
            state = cellState,
            placeholder = placeholder,
            colors = colors,
            dimensions = dimensions,
            animationSpec = animationSpec,
            shape = shape,
            maskConfig = maskConfig,
            cursorConfig = cursorConfig,
            textStyles = textStyles,
            contentPadding = contentPadding,
        )
    },
) {
    require(otpLength > 0) { "otpLength must be positive but was $otpLength" }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val text = sanitizeOtp(value, otpLength)

    CompletionEffect(key = Unit, otpLength = otpLength, text = { text }, onComplete = onComplete)

    CompositionLocalProvider(LocalTextSelectionColors provides HiddenSelectionColors) {
        BasicTextField(
            // Pinning the selection to the end keeps taps from moving the invisible cursor.
            value = TextFieldValue(text, selection = TextRange(text.length)),
            onValueChange = { newValue ->
                val sanitized = sanitizeOtp(newValue.text, otpLength)
                if (sanitized != text) onValueChange(sanitized)
            },
            modifier = modifier.otpSemantics(maskConfig.isMasked),
            enabled = enabled,
            textStyle = HiddenTextStyle,
            keyboardOptions = keyboardOptions,
            singleLine = true,
            interactionSource = interactionSource,
            cursorBrush = HiddenCursor,
            decorationBox = { innerTextField ->
                OtpCells(
                    text = text,
                    otpLength = otpLength,
                    isFocused = isFocused,
                    isError = isError,
                    enabled = enabled,
                    spacing = dimensions.spacing,
                    separator = separator,
                    cell = cell,
                    innerTextField = innerTextField,
                )
            },
        )
    }
}

@Composable
private fun CompletionEffect(key: Any, otpLength: Int, text: () -> CharSequence, onComplete: ((String) -> Unit)?) {
    val currentText by rememberUpdatedState(text)
    val currentOnComplete by rememberUpdatedState(onComplete)

    LaunchedEffect(key, otpLength) {
        snapshotFlow { currentText().length == otpLength }
            // The first value describes how the field started out, not something the user just did.
            .drop(1)
            .collect { isComplete ->
                if (isComplete) currentOnComplete?.invoke(currentText().toString())
            }
    }
}

@Composable
private fun OtpCells(
    text: CharSequence,
    otpLength: Int,
    isFocused: Boolean,
    isError: Boolean,
    enabled: Boolean,
    spacing: Dp,
    separator: (@Composable (afterIndex: Int) -> Unit)?,
    cell: @Composable (OtpCellState) -> Unit,
    innerTextField: @Composable () -> Unit,
) {
    val activeIndex = text.length.coerceAtMost(otpLength - 1)

    Box(contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.matchParentSize(), contentAlignment = Alignment.Center) {
            innerTextField()
        }

        // The text field already exposes the code to accessibility services. Without this the
        // cells would be announced a second time, one digit at a time.
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clearAndSetSemantics {},
        ) {
            repeat(otpLength) { index ->
                cell(
                    OtpCellState(
                        index = index,
                        char = text.getOrNull(index),
                        isFocused = isFocused && index == activeIndex,
                        isError = isError,
                        isEnabled = enabled,
                    ),
                )
                if (index < otpLength - 1) {
                    if (separator != null) separator(index) else Spacer(Modifier.width(spacing))
                }
            }
        }
    }
}

private fun Modifier.otpSemantics(isMasked: Boolean): Modifier = semantics {
    contentType = ContentType.SmsOtpCode
    if (isMasked) password()
}

private val HiddenTextStyle = TextStyle(color = Color.Transparent)
private val HiddenCursor = SolidColor(Color.Transparent)
private val HiddenSelectionColors = TextSelectionColors(
    handleColor = Color.Transparent,
    backgroundColor = Color.Transparent,
)
