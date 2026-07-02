package com.burkido.otpinputkit.entry

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.KeyboardType
import com.burkido.otpinputkit.entry.cell.DefaultOtpCell
import com.burkido.otpinputkit.entry.style.OtpAnimationSpec
import com.burkido.otpinputkit.entry.style.OtpCellColors
import com.burkido.otpinputkit.entry.style.OtpCellDimensions
import com.burkido.otpinputkit.entry.style.OtpCellPadding
import com.burkido.otpinputkit.entry.style.OtpCursorConfig
import com.burkido.otpinputkit.entry.style.OtpMaskConfig
import com.burkido.otpinputkit.entry.style.OtpTextStyles
import kotlinx.coroutines.flow.collectLatest

// ────────────────────────────────────────────────────────────────────
// State-based overload (recommended)
// ────────────────────────────────────────────────────────────────────

/**
 * A composable OTP input field that uses the modern [TextFieldState] API.
 *
 * This is the **recommended** overload. The [TextFieldState] survives
 * recomposition, configuration changes, and process death automatically.
 *
 * ### Basic usage
 * ```kotlin
 * val textFieldState = rememberTextFieldState()
 * OtpInputField(
 *     textFieldState = textFieldState,
 *     otpLength = 6,
 *     onComplete = { otp -> verifyOtp(otp) }
 * )
 * ```
 *
 * ### Custom cells
 * ```kotlin
 * OtpInputField(
 *     textFieldState = textFieldState,
 *     otpLength = 6,
 *     cell = { index, char, focused, error ->
 *         UnderlineOtpCell(char = char, focused = focused, isError = error)
 *     }
 * )
 * ```
 *
 * @param textFieldState A [TextFieldState] that manages the OTP text.
 * @param otpLength Number of OTP digits.
 * @param modifier Modifier for the root layout.
 * @param enabled Whether the input is enabled.
 * @param isError Whether to show error styling.
 * @param placeholder Text shown in unfocused empty cells.
 * @param colors Color configuration.
 * @param dimensions Size configuration.
 * @param animationSpec Animation configuration.
 * @param shape Shape of default cells.
 * @param maskConfig Masking configuration for obscuring entered digits.
 * @param textStyles State-aware text styles for filled and placeholder content.
 * @param contentPadding Inner padding applied inside each cell box.
 * @param keyboardOptions Keyboard configuration. Defaults to number keyboard.
 * @param inputTransformation Input filter. Defaults to digits-only + max length.
 * @param onComplete Called when all digits have been entered.
 * @param cell Slot for rendering each individual cell. Receives (index, char, focused, error).
 * @param separator Optional slot for rendering separators between cells.
 */
@Composable
public fun OtpInputField(
    textFieldState: TextFieldState,
    otpLength: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    placeholder: String = "",
    colors: OtpCellColors = OtpInputDefaults.colors(),
    dimensions: OtpCellDimensions = OtpInputDefaults.dimensions(),
    animationSpec: OtpAnimationSpec = OtpInputDefaults.animationSpec(),
    shape: Shape = RoundedCornerShape(dimensions.cornerRadius),
    maskConfig: OtpMaskConfig = OtpInputDefaults.maskConfig(),
    textStyles: OtpTextStyles = OtpInputDefaults.textStyles(dimensions.textSize),
    contentPadding: OtpCellPadding = OtpInputDefaults.contentPadding(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    inputTransformation: InputTransformation = OtpInputTransformation(otpLength),
    onComplete: ((String) -> Unit)? = null,
    cell: @Composable (index: Int, char: Char?, focused: Boolean, error: Boolean) -> Unit = { _, char, focused, error ->
        DefaultOtpCell(
            char = char,
            focused = focused,
            isError = error,
            placeholder = placeholder,
            colors = colors,
            dimensions = dimensions,
            animationSpec = animationSpec,
            shape = shape,
            maskConfig = maskConfig,
            cursorConfig = OtpCursorConfig(color = colors.cursorColor),
            textStyles = textStyles,
            contentPadding = contentPadding,
        )
    },
    separator: (@Composable (index: Int) -> Unit)? = null,
) {
    val focusRequester = remember { FocusRequester() }
    var focusedIndex by remember { mutableIntStateOf(0) }

    // Track text changes to update focused index and fire onComplete
    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text.toString() }
            .collectLatest { currentText ->
                focusedIndex = currentText.length.coerceAtMost(otpLength - 1)
                if (currentText.length == otpLength) {
                    onComplete?.invoke(currentText)
                }
            }
    }

    OtpInputFieldLayout(
        text = textFieldState.text.toString(),
        otpLength = otpLength,
        focusedIndex = focusedIndex,
        isError = isError,
        dimensions = dimensions,
        focusRequester = focusRequester,
        cell = cell,
        separator = separator,
        modifier = modifier,
    ) {
        BasicTextField(
            state = textFieldState,
            keyboardOptions = keyboardOptions,
            inputTransformation = inputTransformation,
            enabled = enabled,
            modifier = Modifier
                .focusRequester(focusRequester)
                .matchParentSize()
                .alpha(0f),
        )
    }
}

// ────────────────────────────────────────────────────────────────────
// Value-based overload (legacy compat)
// ────────────────────────────────────────────────────────────────────

/**
 * A composable OTP input field that uses the value-based (callback) API.
 *
 * Consider using the [TextFieldState] overload instead for better state
 * management. This overload is provided for backward compatibility or
 * simpler use cases.
 *
 * ### Basic usage
 * ```kotlin
 * var code by remember { mutableStateOf("") }
 * OtpInputField(
 *     value = code,
 *     onValueChange = { code = it },
 *     otpLength = 6,
 *     onComplete = { otp -> verifyOtp(otp) }
 * )
 * ```
 *
 * @param value Current OTP text value.
 * @param onValueChange Callback when the OTP text changes.
 * @param otpLength Number of OTP digits.
 * @param modifier Modifier for the root layout.
 * @param enabled Whether the input is enabled.
 * @param isError Whether to show error styling.
 * @param placeholder Text shown in unfocused empty cells.
 * @param colors Color configuration.
 * @param dimensions Size configuration.
 * @param animationSpec Animation configuration.
 * @param shape Shape of default cells.
 * @param maskConfig Masking configuration for obscuring entered digits.
 * @param textStyles State-aware text styles for filled and placeholder content.
 * @param contentPadding Inner padding applied inside each cell box.
 * @param keyboardOptions Keyboard configuration. Defaults to number keyboard.
 * @param onComplete Called when all digits have been entered.
 * @param cell Slot for rendering each individual cell. Receives (index, char, focused, error).
 * @param separator Optional slot for rendering separators between cells.
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
    shape: Shape = RoundedCornerShape(dimensions.cornerRadius),
    maskConfig: OtpMaskConfig = OtpInputDefaults.maskConfig(),
    textStyles: OtpTextStyles = OtpInputDefaults.textStyles(dimensions.textSize),
    contentPadding: OtpCellPadding = OtpInputDefaults.contentPadding(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    onComplete: ((String) -> Unit)? = null,
    cell: @Composable (index: Int, char: Char?, focused: Boolean, error: Boolean) -> Unit = { _, char, focused, error ->
        DefaultOtpCell(
            char = char,
            focused = focused,
            isError = error,
            placeholder = placeholder,
            colors = colors,
            dimensions = dimensions,
            animationSpec = animationSpec,
            shape = shape,
            maskConfig = maskConfig,
            cursorConfig = OtpCursorConfig(color = colors.cursorColor),
            textStyles = textStyles,
            contentPadding = contentPadding,
        )
    },
    separator: (@Composable (index: Int) -> Unit)? = null,
) {
    val focusRequester = remember { FocusRequester() }
    val focusedIndex = value.length.coerceAtMost(otpLength - 1)

    OtpInputFieldLayout(
        text = value,
        otpLength = otpLength,
        focusedIndex = focusedIndex,
        isError = isError,
        dimensions = dimensions,
        focusRequester = focusRequester,
        cell = cell,
        separator = separator,
        modifier = modifier,
    ) {
        @Suppress("DEPRECATION")
        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                val filtered = newValue.filter { it.isDigit() }.take(otpLength)
                onValueChange(filtered)
                if (filtered.length == otpLength) {
                    onComplete?.invoke(filtered)
                }
            },
            keyboardOptions = keyboardOptions,
            enabled = enabled,
            modifier = Modifier
                .focusRequester(focusRequester)
                .matchParentSize()
                .alpha(0f),
        )
    }
}

// ────────────────────────────────────────────────────────────────────
// Shared internal layout
// ────────────────────────────────────────────────────────────────────

/**
 * Internal layout shared by both OtpInputField overloads.
 * Contains the visual cell Row and overlays the hidden text field.
 */
@Composable
private fun OtpInputFieldLayout(
    text: String,
    otpLength: Int,
    focusedIndex: Int,
    isError: Boolean,
    dimensions: OtpCellDimensions,
    focusRequester: FocusRequester,
    cell: @Composable (index: Int, char: Char?, focused: Boolean, error: Boolean) -> Unit,
    separator: (@Composable (index: Int) -> Unit)?,
    modifier: Modifier = Modifier,
    hiddenTextField: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier) {
        // Hidden text field to capture keyboard input
        hiddenTextField()

        // Visual cells
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) {
                    focusRequester.requestFocus()
                },
        ) {
            repeat(otpLength) { index ->
                val char = text.getOrNull(index)
                val focused = focusedIndex == index

                cell(index, char, focused, isError)

                // Separator between cells (not after last cell)
                if (index < otpLength - 1) {
                    separator?.invoke(index) ?: Spacer(
                        modifier = Modifier.width(dimensions.spacing),
                    )
                }
            }
        }
    }
}
