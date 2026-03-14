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

private val MidnightColors
    @Composable get() = OtpInputDefaults.colors(
        focusedBorderColor = Color(0xFF00D4FF),
        unfocusedBorderColor = Color(0xFF3A3A5C),
        filledBorderColor = Color(0xFF00D4FF),
        errorBorderColor = Color(0xFFFF4444),
        focusedContainerColor = Color(0xFF16213E),
        unfocusedContainerColor = Color(0xFF1A1A2E),
        filledContainerColor = Color(0xFF0F3460),
        errorContainerColor = Color(0xFF2E1A1A),
        textColor = Color(0xFF00D4FF),
        cursorColor = Color(0xFF00D4FF),
        placeholderColor = Color(0xFF3A3A5C),
    )

private val MidnightAnimationSpec
    @Composable get() = OtpInputDefaults.animationSpec(
        cellEntryAnimation = CellAnimation.SlideUp,
    )

/**
 * Midnight OTP input — dark navy containers with cyan accent, SlideUp cell animation.
 */
@Composable
fun MidnightOtpSample(
    textFieldState: TextFieldState,
    otpLength: Int,
    modifier: Modifier = Modifier,
    onComplete: (String) -> Unit = {},
) {
    val colors = MidnightColors
    val animationSpec = MidnightAnimationSpec

    Text("Midnight", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(12.dp))
    OtpInputField(
        textFieldState = textFieldState,
        otpLength = otpLength,
        modifier = modifier,
        onComplete = onComplete,
        cell = { _, char, focused, error ->
            DefaultOtpCell(
                char = char,
                focused = focused,
                isError = error,
                colors = colors,
                animationSpec = animationSpec,
                cursorConfig = OtpCursorConfig(color = Color(0xFF00D4FF)),
            )
        },
    )
}
