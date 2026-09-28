package com.burkido.otp.sample.showcase

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.burkido.otp.input.OtpInputField
import com.burkido.otp.input.OutlinedOtpCell

@Composable
fun OutlinedOtpSample(
    state: TextFieldState,
    otpLength: Int,
    modifier: Modifier = Modifier,
    onComplete: (String) -> Unit = {},
) {
    Text("Outlined", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(12.dp))
    OtpInputField(
        state = state,
        otpLength = otpLength,
        modifier = modifier,
        onComplete = onComplete,
    ) { cell ->
        OutlinedOtpCell(state = cell)
    }
}
