package com.burkido.verificationcodereader

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.burkido.autoreadotp.SmsUserConsent
import com.burkido.otpinputkit.countdowntimer.CountdownTimer
import com.burkido.otpinputkit.countdowntimer.CountdownTimerDefaults
import com.burkido.otpinputkit.countdowntimer.rememberCountdownTimerState
import com.burkido.otpinputkit.entry.fillOtp
import com.burkido.verificationcodereader.sample.CompactOtpSample
import com.burkido.verificationcodereader.sample.DefaultOtpSample
import com.burkido.verificationcodereader.sample.MidnightOtpSample
import com.burkido.verificationcodereader.sample.OutlinedOtpSample
import com.burkido.verificationcodereader.sample.PillOtpSample
import com.burkido.verificationcodereader.sample.UnderlineOtpSample

@Composable
fun VerificationScreen() {
    val timerState = rememberCountdownTimerState(
        endDate = System.currentTimeMillis() + java.util.concurrent.TimeUnit.MINUTES.toMillis(TIMER_MINUTES)
    )
    val defaultState = rememberTextFieldState()
    val outlinedState = rememberTextFieldState()
    val underlineState = rememberTextFieldState()
    val midnightState = rememberTextFieldState()
    val pillState = rememberTextFieldState()
    val compactState = rememberTextFieldState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clearFocusOnTap()
            .verticalScroll(rememberScrollState())
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "OTP Design Examples",
            style = MaterialTheme.typography.headlineSmall,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Code expires in:",
            style = MaterialTheme.typography.bodySmall,
        )
        Spacer(modifier = Modifier.height(8.dp))
        CountdownTimer(
            state = timerState,
            size = CountdownTimerDefaults.size(),
            onTimerFinish = { Log.d(TAG, "Countdown finished") },
        )

        Spacer(modifier = Modifier.height(32.dp))

        DefaultOtpSample(defaultState, OTP_LENGTH,
            onComplete = { otp -> Log.d(TAG, "Default complete: $otp") })

        SectionDivider()

        OutlinedOtpSample(outlinedState, OTP_LENGTH,
            onComplete = { otp -> Log.d(TAG, "Outlined complete: $otp") })

        SectionDivider()

        UnderlineOtpSample(underlineState, OTP_LENGTH,
            onComplete = { otp -> Log.d(TAG, "Underline complete: $otp") })

        SectionDivider()

        MidnightOtpSample(midnightState, OTP_LENGTH,
            onComplete = { otp -> Log.d(TAG, "Midnight complete: $otp") })

        SectionDivider()

        PillOtpSample(pillState, OTP_LENGTH,
            onComplete = { otp -> Log.d(TAG, "Pill complete: $otp") })

        SectionDivider()

        CompactOtpSample(compactState, OTP_LENGTH,
            onComplete = { otp -> Log.d(TAG, "Compact complete: $otp") })

        // SMS auto-read — fills all 6 designs simultaneously
        SmsUserConsent(
            smsCodeLength = OTP_LENGTH,
            onOtpReceived = { otp ->
                Log.d(TAG, "SMS received: $otp")
                listOf(defaultState, outlinedState, underlineState, midnightState, pillState, compactState)
                    .forEach { it.fillOtp(otp, OTP_LENGTH) }
            },
            onError = { error -> Log.e(TAG, "SMS consent error: $error") },
        )
    }
}

@Composable
private fun SectionDivider() {
    Spacer(modifier = Modifier.height(32.dp))
    HorizontalDivider()
    Spacer(modifier = Modifier.height(32.dp))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun VerificationScreenPreview() {
    VerificationScreen()
}

private const val OTP_LENGTH = 6
private const val TAG = "VerificationScreen"
private const val TIMER_MINUTES = 5L
