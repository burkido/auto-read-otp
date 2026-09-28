package com.burkido.otp.sample.showcase

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.burkido.otp.input.OtpInputField

@Composable
fun DefaultOtpSample(
    state: TextFieldState,
    otpLength: Int,
    modifier: Modifier = Modifier,
    onComplete: (String) -> Unit = {},
) {
    SampleSection(title = "Default", modifier = modifier) {
        OtpInputField(
            state = state,
            otpLength = otpLength,
            onComplete = onComplete,
        )
    }
}
