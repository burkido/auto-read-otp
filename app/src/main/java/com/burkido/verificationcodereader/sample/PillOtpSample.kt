package com.burkido.verificationcodereader.sample

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.burkido.otpinputkit.OtpInputDefaults
import com.burkido.otpinputkit.OtpInputField
import com.burkido.otpinputkit.cell.DefaultOtpCell
import com.burkido.otpinputkit.style.CellAnimation
import com.burkido.otpinputkit.style.OtpCursorConfig

private val PillColors
    @Composable get() = OtpInputDefaults.colors(
        focusedBorderColor = Color(0xFFFF6F00),
        unfocusedBorderColor = Color(0xFFFFCC80),
        filledBorderColor = Color(0xFFFF6F00),
        errorBorderColor = Color(0xFFD32F2F),
        focusedContainerColor = Color(0xFFFFF3E0),
        unfocusedContainerColor = Color(0xFFF5F5F5),
        filledContainerColor = Color(0xFFFFE0B2),
        errorContainerColor = Color(0xFFFFEBEE),
        textColor = Color(0xFF4A2C00),
        cursorColor = Color(0xFFFF6F00),
        placeholderColor = Color(0xFFFFCC80),
    )

private val PillDimensions
    @Composable get() = OtpInputDefaults.dimensions(cornerRadius = 28.dp)

private val PillAnimationSpec
    @Composable get() = OtpInputDefaults.animationSpec(cellEntryAnimation = CellAnimation.FadeIn)

private val PillMaskConfig
    @Composable get() = OtpInputDefaults.maskConfig(isMasked = true)

/**
 * Pill OTP input — warm amber palette, fully-rounded pill shape,
 * digits auto-masked after 300ms, FadeIn cell animation.
 */
@Composable
fun PillOtpSample(
    textFieldState: TextFieldState,
    otpLength: Int,
    modifier: Modifier = Modifier,
    onComplete: (String) -> Unit = {},
) {
    val colors = PillColors
    val dimensions = PillDimensions
    val animationSpec = PillAnimationSpec
    val maskConfig = PillMaskConfig

    Text("Pill · Masked", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(12.dp))
    OtpInputField(
        textFieldState = textFieldState,
        otpLength = otpLength,
        modifier = modifier,
        dimensions = dimensions,
        onComplete = onComplete,
        cell = { _, char, focused, error ->
            DefaultOtpCell(
                char = char,
                focused = focused,
                isError = error,
                colors = colors,
                dimensions = dimensions,
                animationSpec = animationSpec,
                maskConfig = maskConfig,
                cursorConfig = OtpCursorConfig(color = Color(0xFFFF6F00)),
            )
        },
    )
}
