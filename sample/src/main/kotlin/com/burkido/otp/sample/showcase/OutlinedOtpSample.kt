package com.burkido.otp.sample.showcase

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.burkido.otp.input.OtpInputField
import com.burkido.otp.input.OutlinedOtpCell

@Composable
fun OutlinedOtpSample(
    state: TextFieldState,
    otpLength: Int,
    modifier: Modifier = Modifier,
    onComplete: (String) -> Unit = {},
) {
    SampleSection(title = "Outlined", modifier = modifier) {
        OtpInputField(
            state = state,
            otpLength = otpLength,
            onComplete = onComplete,
        ) { cell ->
            OutlinedOtpCell(state = cell)
        }
    }
}
