package com.burkido.verificationcodereader

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.burkido.autoreadotp.SmsUserConsent

@Composable
fun VerificationScreen() {
    var code by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OtpTextField(
            value = code,
            length = OTP_LENGTH,
            onValueChange = { code = it },
            onVerificationExplicitlyTriggered = {
                // trigger a function to verify the code
            }
        )

        SmsUserConsent(
            smsCodeLength = OTP_LENGTH,
            onOTPReceived = { otp ->
                Log.d("MainActivity", "SMS Received: $otp")
                code = otp
            },
            onError = { error ->
                Log.e("MainActivity", "Error: $error")
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VerificationScreenPreview() {
    VerificationScreen()
}

private const val OTP_LENGTH = 6