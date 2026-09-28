package com.burkido.otp.sample.showcase

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.burkido.otp.timer.CountdownTimer
import com.burkido.otp.timer.CountdownTimerDefaults
import com.burkido.otp.timer.rememberCountdownTimerState
import kotlin.time.Duration.Companion.minutes

private val SampleDuration = 30.minutes

@Composable
fun DefaultTimerSample() {
    val state = rememberCountdownTimerState(duration = SampleDuration)
    Text("Default", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(12.dp))
    CountdownTimer(state = state)
}

/** Larger red boxes for a code that's about to expire. */
@Composable
fun DangerTimerSample() {
    val state = rememberCountdownTimerState(duration = SampleDuration)
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
            textSize = 16.sp,
            separatorColor = Color(0xFFD32F2F),
        ),
        dimensions = CountdownTimerDefaults.dimensions(
            verticalPadding = 6.dp,
            horizontalPadding = 10.dp,
        ),
    )
}

/** Matches the Midnight OTP design. */
@Composable
fun MidnightTimerSample() {
    val state = rememberCountdownTimerState(duration = SampleDuration)
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

/** Small and warm, to match the Pill OTP design. */
@Composable
fun AmberTimerSample() {
    val state = rememberCountdownTimerState(duration = SampleDuration)
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
            textSize = 12.sp,
            separatorColor = Color(0xFFFF6F00),
        ),
        dimensions = CountdownTimerDefaults.dimensions(
            verticalPadding = 1.dp,
            horizontalPadding = 5.dp,
        ),
    )
}

/** Matches the Compact OTP design, with a little more room around the colons. */
@Composable
fun VioletTimerSample() {
    val state = rememberCountdownTimerState(duration = SampleDuration)
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
        dimensions = CountdownTimerDefaults.dimensions(
            verticalPadding = 4.dp,
            horizontalPadding = 8.dp,
            boxSpacing = 2.dp,
        ),
    )
}
