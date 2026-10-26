package com.burkido.otp.sample.showcase

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.burkido.otp.input.OtpInputField
import com.burkido.otp.input.UnderlineOtpCell

@Composable
fun UnderlineOtpSample(
    state: TextFieldState,
    otpLength: Int,
    modifier: Modifier = Modifier,
    onComplete: (String) -> Unit = {},
) {
    SampleSection(title = "Underline", modifier = modifier) {
        OtpInputField(
            state = state,
            otpLength = otpLength,
            onComplete = onComplete,
        ) { cell ->
            UnderlineOtpCell(state = cell)
        }
    }
}
