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
import com.burkido.otpinputkit.entry.cell.OutlinedOtpCell

/**
 * Outlined OTP input — circular cells with transparent backgrounds.
 */
@Composable
fun OutlinedOtpSample(
    textFieldState: TextFieldState,
    otpLength: Int,
    modifier: Modifier = Modifier,
    onComplete: (String) -> Unit = {},
) {
    Text("Outlined", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(12.dp))
    OtpInputField(
        textFieldState = textFieldState,
        otpLength = otpLength,
        modifier = modifier,
        onComplete = onComplete,
        cell = { _, char, focused, error ->
            OutlinedOtpCell(char = char, focused = focused, isError = error)
        },
    )
}
