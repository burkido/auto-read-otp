package com.burkido.verificationcodereader

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.burkido.autoreadotp.SmsUserConsent
import com.burkido.otpinputkit.OtpInputField
import com.burkido.otpinputkit.fillOtp

@Composable
fun VerificationScreen() {
    val textFieldState = rememberTextFieldState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Verification Code",
            style = MaterialTheme.typography.headlineSmall,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // OTP Input using TextFieldState (recommended)
        OtpInputField(
            textFieldState = textFieldState,
            otpLength = OTP_LENGTH,
            onComplete = { otp ->
                Log.d("VerificationScreen", "OTP Complete: $otp")
                // trigger verification
            },
        )

        // SMS auto-read integration
        SmsUserConsent(
            smsCodeLength = OTP_LENGTH,
            onOTPReceived = { otp ->
                Log.d("VerificationScreen", "SMS Received: $otp")
                textFieldState.fillOtp(otp, OTP_LENGTH)
            },
            onError = { error ->
                Log.e("VerificationScreen", "Error: $error")
            },
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun VerificationScreenPreview() {
    VerificationScreen()
}

private const val OTP_LENGTH = 6