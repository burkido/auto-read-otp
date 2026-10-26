package com.burkido.otp.sample.showcase

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.burkido.otp.input.CellAnimation
import com.burkido.otp.input.OtpInputDefaults
import com.burkido.otp.input.OtpInputField

private val Amber = Color(0xFFFF6F00)
private val LightAmber = Color(0xFFFFCC80)

/** Warm amber pills that mask each digit shortly after it's typed. */
@Composable
fun PillOtpSample(
    state: TextFieldState,
    otpLength: Int,
    modifier: Modifier = Modifier,
    onComplete: (String) -> Unit = {},
) {
    SampleSection(title = "Pill, masked", modifier = modifier) {
        OtpInputField(
            state = state,
            otpLength = otpLength,
            colors = OtpInputDefaults.colors(
                focusedBorderColor = Amber,
                unfocusedBorderColor = LightAmber,
                filledBorderColor = Amber,
                errorBorderColor = Color(0xFFD32F2F),
                focusedContainerColor = Color(0xFFFFF3E0),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                filledContainerColor = Color(0xFFFFE0B2),
                errorContainerColor = Color(0xFFFFEBEE),
                textColor = Color(0xFF4A2C00),
                cursorColor = Amber,
                placeholderColor = LightAmber,
            ),
            dimensions = OtpInputDefaults.dimensions(cornerRadius = 28.dp),
            animationSpec = OtpInputDefaults.animationSpec(cellEntryAnimation = CellAnimation.FadeIn),
            maskConfig = OtpInputDefaults.maskConfig(isMasked = true),
            onComplete = onComplete,
        )
    }
}
