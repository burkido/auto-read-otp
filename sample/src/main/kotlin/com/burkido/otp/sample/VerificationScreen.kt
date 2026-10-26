package com.burkido.otp.sample

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
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
import com.burkido.otp.input.fillOtp
import com.burkido.otp.sample.showcase.AmberTimerSample
import com.burkido.otp.sample.showcase.CompactOtpSample
import com.burkido.otp.sample.showcase.DangerTimerSample
import com.burkido.otp.sample.showcase.DefaultOtpSample
import com.burkido.otp.sample.showcase.DefaultTimerSample
import com.burkido.otp.sample.showcase.MidnightOtpSample
import com.burkido.otp.sample.showcase.MidnightTimerSample
import com.burkido.otp.sample.showcase.OutlinedOtpSample
import com.burkido.otp.sample.showcase.PillOtpSample
import com.burkido.otp.sample.showcase.UnderlineOtpSample
import com.burkido.otp.sample.showcase.VioletTimerSample
import com.burkido.otp.sms.SmsUserConsentEffect
import com.burkido.otp.timer.CountdownTimer
import com.burkido.otp.timer.rememberCountdownTimerState
import kotlin.time.Duration.Companion.minutes

@Composable
fun VerificationScreen(modifier: Modifier = Modifier) {
    val timerState = rememberCountdownTimerState(duration = CODE_LIFETIME)
    val defaultState = rememberTextFieldState()
    val outlinedState = rememberTextFieldState()
    val underlineState = rememberTextFieldState()
    val midnightState = rememberTextFieldState()
    val pillState = rememberTextFieldState()
    val compactState = rememberTextFieldState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .safeDrawingPadding()
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
            onTimerFinish = { Log.d(TAG, "Countdown finished") },
        )

        Spacer(modifier = Modifier.height(32.dp))

        DefaultOtpSample(
            defaultState,
            OTP_LENGTH,
            onComplete = { otp -> Log.d(TAG, "Default complete: $otp") },
        )

        SectionDivider()

        OutlinedOtpSample(
            outlinedState,
            OTP_LENGTH,
            onComplete = { otp -> Log.d(TAG, "Outlined complete: $otp") },
        )

        SectionDivider()

        UnderlineOtpSample(
            underlineState,
            OTP_LENGTH,
            onComplete = { otp -> Log.d(TAG, "Underline complete: $otp") },
        )

        SectionDivider()

        MidnightOtpSample(
            midnightState,
            OTP_LENGTH,
            onComplete = { otp -> Log.d(TAG, "Midnight complete: $otp") },
        )

        SectionDivider()

        PillOtpSample(
            pillState,
            OTP_LENGTH,
            onComplete = { otp -> Log.d(TAG, "Pill complete: $otp") },
        )

        SectionDivider()

        CompactOtpSample(
            compactState,
            OTP_LENGTH,
            onComplete = { otp -> Log.d(TAG, "Compact complete: $otp") },
        )

        SmsUserConsentEffect(
            codeLength = OTP_LENGTH,
            onCodeReceive = { code ->
                Log.d(TAG, "SMS received: $code")
                listOf(defaultState, outlinedState, underlineState, midnightState, pillState, compactState)
                    .forEach { it.fillOtp(code, OTP_LENGTH) }
            },
            onError = { error -> Log.w(TAG, "SMS consent ended: $error") },
        )

        SectionDivider()

        Text(text = "Countdown timers", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        DefaultTimerSample()
        Spacer(modifier = Modifier.height(24.dp))
        DangerTimerSample()
        Spacer(modifier = Modifier.height(24.dp))
        MidnightTimerSample()
        Spacer(modifier = Modifier.height(24.dp))
        AmberTimerSample()
        Spacer(modifier = Modifier.height(24.dp))
        VioletTimerSample()
    }
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(modifier = Modifier.padding(vertical = 32.dp))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun VerificationScreenPreview() {
    VerificationScreen()
}

private const val OTP_LENGTH = 6
private const val TAG = "VerificationScreen"
private val CODE_LIFETIME = 5.minutes
