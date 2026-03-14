package com.burkido.otpinputkit.cell

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import com.burkido.otpinputkit.OtpInputDefaults
import com.burkido.otpinputkit.style.CellAnimation
import com.burkido.otpinputkit.style.OtpAnimationSpec
import com.burkido.otpinputkit.style.OtpCellColors
import com.burkido.otpinputkit.style.OtpCellDimensions
import com.burkido.otpinputkit.style.OtpCellPadding
import com.burkido.otpinputkit.style.OtpCursorConfig
import com.burkido.otpinputkit.style.OtpMaskConfig
import com.burkido.otpinputkit.style.OtpTextStyles
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/**
 * Default rounded-box OTP cell with animated border and background.
 *
 * This is the cell used by [com.burkido.otpinputkit.OtpInputField] when no
 * custom `cell` lambda is provided. You can also use it explicitly inside a
 * custom `cell` lambda to compose default behavior with your own additions.
 *
 * @param char The character to display, or `null` if the cell is empty.
 * @param focused Whether this cell is currently focused.
 * @param isError Whether the input is in an error state.
 * @param modifier Modifier for the root element.
 * @param placeholder Text shown in unfocused empty cells.
 * @param colors Color configuration — defaults to [OtpInputDefaults.colors].
 * @param dimensions Size configuration — defaults to [OtpInputDefaults.dimensions].
 * @param animationSpec Animation configuration.
 * @param shape Shape of the cell.
 * @param maskConfig Masking configuration for obscuring entered digits.
 * @param cursorConfig Cursor appearance and blink configuration.
 * @param textStyles State-aware text styles for filled and placeholder content.
 * @param contentPadding Inner padding applied inside the cell box.
 */
@Composable
fun DefaultOtpCell(
    char: Char?,
    focused: Boolean,
    isError: Boolean,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    colors: OtpCellColors = OtpInputDefaults.colors(),
    dimensions: OtpCellDimensions = OtpInputDefaults.dimensions(),
    animationSpec: OtpAnimationSpec = OtpInputDefaults.animationSpec(),
    shape: Shape = RoundedCornerShape(dimensions.cornerRadius),
    maskConfig: OtpMaskConfig = OtpInputDefaults.maskConfig(),
    cursorConfig: OtpCursorConfig = OtpCursorConfig(color = colors.cursorColor),
    textStyles: OtpTextStyles = OtpInputDefaults.textStyles(dimensions.textSize),
    contentPadding: OtpCellPadding = OtpInputDefaults.contentPadding(),
) {
    val borderColor by animateColorAsState(
        targetValue = colors.borderColor(focused, char != null, isError),
        animationSpec = tween(animationSpec.animationDuration),
        label = "otp_cell_border_color",
    )
    val containerColor by animateColorAsState(
        targetValue = colors.containerColor(focused, char != null, isError),
        animationSpec = tween(animationSpec.animationDuration),
        label = "otp_cell_container_color",
    )
    val borderWidth by animateDpAsState(
        targetValue = if (focused) dimensions.focusedBorderWidth else dimensions.borderWidth,
        animationSpec = tween(animationSpec.animationDuration),
        label = "otp_cell_border_width",
    )

    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(isError) {
        if (isError && animationSpec.shakeOnError) {
            val amplitude = animationSpec.shakeAmplitude.value
            repeat(animationSpec.shakeRepeatCount) { i ->
                shakeOffset.animateTo(
                    if (i % 2 == 0) amplitude else -amplitude,
                    tween(animationSpec.shakeStepDuration),
                )
            }
            shakeOffset.animateTo(0f, tween(animationSpec.shakeStepDuration))
        }
    }

    // Mask delay: show digit briefly then obscure it
    var showChar by remember { mutableStateOf(!maskConfig.isMasked) }
    LaunchedEffect(char) {
        if (maskConfig.isMasked && char != null) {
            showChar = true
            delay(maskConfig.maskDelay)
            showChar = false
        } else {
            showChar = true
        }
    }

    val enterTransition: EnterTransition = when (animationSpec.cellEntryAnimation) {
        CellAnimation.None -> EnterTransition.None
        CellAnimation.Scale -> scaleIn(tween(animationSpec.animationDuration))
        CellAnimation.FadeIn -> fadeIn(tween(animationSpec.animationDuration))
        CellAnimation.SlideUp -> slideInVertically(tween(animationSpec.animationDuration)) { it }
    }

    val displayText = when {
        char != null && maskConfig.isMasked && !showChar -> maskConfig.maskCharacter.toString()
        char != null -> char.toString()
        else -> ""
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .offset { IntOffset(shakeOffset.value.roundToInt(), 0) }
            .size(dimensions.cellWidth, dimensions.cellHeight)
            .border(borderWidth, borderColor, shape)
            .clip(shape)
            .background(containerColor)
            .padding(
                horizontal = contentPadding.horizontal,
                vertical = contentPadding.vertical,
            ),
    ) {
        when {
            char != null -> {
                AnimatedContent(
                    targetState = displayText,
                    transitionSpec = { enterTransition togetherWith ExitTransition.None },
                    label = "otp_cell_char",
                ) { text ->
                    Text(
                        text = text,
                        style = textStyles.filledTextStyle,
                        color = colors.textColor,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            focused -> {
                CursorIndicator(config = cursorConfig)
            }
            placeholder.isNotEmpty() -> {
                Text(
                    text = placeholder,
                    style = textStyles.placeholderTextStyle,
                    color = colors.placeholderColor,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
