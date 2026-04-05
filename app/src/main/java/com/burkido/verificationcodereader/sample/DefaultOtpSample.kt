package com.burkido.verificationcodereader.sample

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.burkido.otpinputkit.entry.OtpInputField

/**
 * Default OTP input — rounded box cells with Material3 theming.
 */
@Composable
fun DefaultOtpSample(
    textFieldState: TextFieldState,
    otpLength: Int,
    modifier: Modifier = Modifier,
    onComplete: (String) -> Unit = {},
) {
    Text("Default", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(12.dp))
    OtpInputField(
        textFieldState = textFieldState,
        otpLength = otpLength,
        modifier = modifier,
        onComplete = onComplete,
    )
}
