package com.burkido.otpinputkit.entry.cell

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.burkido.otpinputkit.entry.OtpInputDefaults
import com.burkido.otpinputkit.entry.style.CellAnimation
import com.burkido.otpinputkit.entry.style.OtpAnimationSpec
import com.burkido.otpinputkit.entry.style.OtpCellColors
import com.burkido.otpinputkit.entry.style.OtpCellDimensions
import com.burkido.otpinputkit.entry.style.OtpCellPadding
import com.burkido.otpinputkit.entry.style.OtpCursorConfig
import com.burkido.otpinputkit.entry.style.OtpMaskConfig
import com.burkido.otpinputkit.entry.style.OtpTextStyles
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/**
 * Underline-style OTP cell — a digit displayed above an animated underline.
 *
 * Use this inside the `cell` lambda of [com.burkido.otpinputkit.OtpInputField]:
 * ```kotlin
 * OtpInputField(
 *     state = state,
 *     otpLength = 6,
 *     cell = { index, char, focused, error ->
 *         UnderlineOtpCell(char = char, focused = focused, isError = error)
 *     }
 * )
 * ```
 */
@Composable
public fun UnderlineOtpCell(
    char: Char?,
    focused: Boolean,
    isError: Boolean,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    colors: OtpCellColors = OtpInputDefaults.colors(),
    dimensions: OtpCellDimensions = OtpInputDefaults.dimensions(),
    animationSpec: OtpAnimationSpec = OtpInputDefaults.animationSpec(),
    maskConfig: OtpMaskConfig = OtpInputDefaults.maskConfig(),
    cursorConfig: OtpCursorConfig = OtpCursorConfig(color = colors.cursorColor),
    textStyles: OtpTextStyles = OtpInputDefaults.textStyles(dimensions.textSize),
    contentPadding: OtpCellPadding = OtpInputDefaults.contentPadding(),
) {
    val lineColor by animateColorAsState(
        targetValue = colors.borderColor(focused, char != null, isError),
        animationSpec = tween(animationSpec.animationDuration),
        label = "underline_color",
    )
    val lineHeight by animateDpAsState(
        targetValue = if (focused) dimensions.focusedBorderWidth else dimensions.borderWidth,
        animationSpec = tween(animationSpec.animationDuration),
        label = "underline_height",
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .offset { IntOffset(shakeOffset.value.roundToInt(), 0) }
            .width(dimensions.cellWidth),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
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
                        label = "otp_underline_char",
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
                    CursorIndicator(
                        config = cursorConfig,
                    )
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

        // Underline
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(lineHeight)
                .background(lineColor),
        )
    }
}
