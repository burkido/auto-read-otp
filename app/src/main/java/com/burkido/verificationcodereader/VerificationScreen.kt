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
            onValueChange = { code = it },
            length = 5,
            onVerificationExplicitlyTriggered = {
                // send the code to the server
            }
        )
        SmsUserConsent(
            onSmsReceived = { sms ->
                Log.d("MainActivity", "SMS Received: $sms")
                code = sms
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