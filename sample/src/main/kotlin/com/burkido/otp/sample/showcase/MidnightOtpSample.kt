package com.burkido.otp.sample.showcase

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.burkido.otp.input.CellAnimation
import com.burkido.otp.input.OtpInputDefaults
import com.burkido.otp.input.OtpInputField

private val Cyan = Color(0xFF00D4FF)

/** Dark navy cells with a cyan accent. Digits slide up as they're typed. */
@Composable
fun MidnightOtpSample(
    state: TextFieldState,
    otpLength: Int,
    modifier: Modifier = Modifier,
    onComplete: (String) -> Unit = {},
) {
    SampleSection(title = "Midnight", modifier = modifier) {
        OtpInputField(
            state = state,
            otpLength = otpLength,
            colors = OtpInputDefaults.colors(
                focusedBorderColor = Cyan,
                unfocusedBorderColor = Color(0xFF3A3A5C),
                filledBorderColor = Cyan,
                errorBorderColor = Color(0xFFFF4444),
                focusedContainerColor = Color(0xFF16213E),
                unfocusedContainerColor = Color(0xFF1A1A2E),
                filledContainerColor = Color(0xFF0F3460),
                errorContainerColor = Color(0xFF2E1A1A),
                textColor = Cyan,
                cursorColor = Cyan,
                placeholderColor = Color(0xFF3A3A5C),
            ),
            animationSpec = OtpInputDefaults.animationSpec(cellEntryAnimation = CellAnimation.SlideUp),
            onComplete = onComplete,
        )
    }
}
