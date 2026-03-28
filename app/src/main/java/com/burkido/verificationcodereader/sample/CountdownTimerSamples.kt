package com.burkido.verificationcodereader.sample

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.burkido.otpinputkit.countdowntimer.CountdownTimer
import com.burkido.otpinputkit.countdowntimer.CountdownTimerDefaults
import com.burkido.otpinputkit.countdowntimer.rememberCountdownTimerState
import java.util.concurrent.TimeUnit

private val SAMPLE_DURATION_MS = TimeUnit.MINUTES.toMillis(30L)

/**
 * Default countdown timer — pure Material3 theme with no overrides.
 */
@Composable
fun DefaultTimerSample() {
    val endDate = remember { System.currentTimeMillis() + SAMPLE_DURATION_MS }
    val state = rememberCountdownTimerState(endDate = endDate)
    Text("Default", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(12.dp))
    CountdownTimer(state = state)
}

/**
 * Danger timer — large red boxes signalling urgency (e.g. code about to expire).
 */
@Composable
fun DangerTimerSample() {
    val endDate = remember { System.currentTimeMillis() + SAMPLE_DURATION_MS }
    val state = rememberCountdownTimerState(endDate = endDate)
    Text("Danger", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(12.dp))
    CountdownTimer(
        state = state,
        style = CountdownTimerDefaults.style(
            backgroundColor = Color(0xFFFFEBEE),
            textStyle = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD32F2F),
            ),
            separatorColor = Color(0xFFD32F2F),
        ),
        size = CountdownTimerDefaults.size(
            textSize = 16.sp,
            verticalPadding = 6.dp,
            horizontalPadding = 10.dp,
        ),
    )
}

/**
 * Midnight timer — dark navy background with a cyan accent,
 * matching the Midnight OTP design.
 */
@Composable
fun MidnightTimerSample() {
    val endDate = remember { System.currentTimeMillis() + SAMPLE_DURATION_MS }
    val state = rememberCountdownTimerState(endDate = endDate)
    Text("Midnight", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(12.dp))
    CountdownTimer(
        state = state,
        style = CountdownTimerDefaults.style(
            backgroundColor = Color(0xFF16213E),
            textStyle = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00D4FF),
            ),
            separatorColor = Color(0xFF00D4FF),
        ),
    )
}

/**
 * Amber timer — warm orange palette (small/compact size),
 * matching the Pill OTP design.
 */
@Composable
fun AmberTimerSample() {
    val endDate = remember { System.currentTimeMillis() + SAMPLE_DURATION_MS }
    val state = rememberCountdownTimerState(endDate = endDate)
    Text("Amber", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(12.dp))
    CountdownTimer(
        state = state,
        style = CountdownTimerDefaults.style(
            backgroundColor = Color(0xFFFFF3E0),
            textStyle = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6F00),
            ),
            separatorColor = Color(0xFFFF6F00),
        ),
        size = CountdownTimerDefaults.size(
            textSize = 12.sp,
            verticalPadding = 1.dp,
            horizontalPadding = 5.dp,
        ),
    )
}

/**
 * Violet timer — purple boxes with extra spacing,
 * matching the Compact/violet OTP design.
 */
@Composable
fun VioletTimerSample() {
    val endDate = remember { System.currentTimeMillis() + SAMPLE_DURATION_MS }
    val state = rememberCountdownTimerState(endDate = endDate)
    Text("Violet", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(12.dp))
    CountdownTimer(
        state = state,
        style = CountdownTimerDefaults.style(
            backgroundColor = Color(0xFFEDE7F6),
            textStyle = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7C4DFF),
            ),
            separatorColor = Color(0xFF7C4DFF),
        ),
        size = CountdownTimerDefaults.size(
            textSize = 14.sp,
            verticalPadding = 4.dp,
            horizontalPadding = 8.dp,
            boxSpacing = 2.dp,
        ),
    )
}
