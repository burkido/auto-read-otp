package com.burkido.otp.timer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Counts down to [endTimeMillis], a wall clock time in milliseconds since the epoch.
 *
 * The remaining time is always worked out from the clock rather than from ticks, so the timer stays
 * correct after it leaves the screen and comes back, or after the process is recreated.
 * Create it with [rememberCountdownTimerState] and pass it to [CountdownTimer], which keeps it running.
 */
@Stable
public class CountdownTimerState internal constructor(
    public val endTimeMillis: Long,
    private val clock: () -> Long,
) {

    public constructor(endTimeMillis: Long) : this(endTimeMillis, System::currentTimeMillis)

    /** Time left, never negative. Updates once per second while a [CountdownTimer] shows this state. */
    public var remaining: Duration by mutableStateOf(timeLeft())
        private set

    public val isFinished: Boolean
        get() = remaining == Duration.ZERO

    internal var finishReported: Boolean = false

    /** Updates [remaining] on every whole second until the end time is reached. */
    internal suspend fun run() {
        while (true) {
            val left = timeLeft()
            remaining = left
            if (left == Duration.ZERO) return

            // Wake up exactly when the displayed second changes instead of drifting with a fixed delay.
            val untilNextSecond = left.inWholeMilliseconds % MILLIS_PER_SECOND
            delay(if (untilNextSecond == 0L) MILLIS_PER_SECOND else untilNextSecond)
        }
    }

    private fun timeLeft(): Duration = (endTimeMillis - clock()).coerceAtLeast(0L).milliseconds

    internal companion object {
        val Saver: Saver<CountdownTimerState, Long> = Saver(
            save = { it.endTimeMillis },
            restore = { CountdownTimerState(it) },
        )
    }
}

/**
 * Remembers a timer that ends at [endTimeMillis], for example an expiry time sent by your backend.
 * A different [endTimeMillis] creates a new timer.
 */
@Composable
public fun rememberCountdownTimerState(endTimeMillis: Long): CountdownTimerState =
    rememberSaveable(endTimeMillis, saver = CountdownTimerState.Saver) {
        CountdownTimerState(endTimeMillis)
    }

/**
 * Remembers a timer that ends [duration] after it's first created. The end time is saved, so
 * rotating the device or coming back to the screen doesn't restart the countdown.
 */
@Composable
public fun rememberCountdownTimerState(duration: Duration): CountdownTimerState =
    rememberSaveable(duration, saver = CountdownTimerState.Saver) {
        CountdownTimerState(System.currentTimeMillis() + duration.inWholeMilliseconds)
    }

/** Remaining time split for display. Seconds are rounded up so 00:00:00 only shows at the very end. */
internal class TimeParts(val hours: Long, val minutes: Long, val seconds: Long)

internal fun Duration.toTimeParts(): TimeParts {
    val totalSeconds = (inWholeMilliseconds + MILLIS_PER_SECOND - 1) / MILLIS_PER_SECOND
    return TimeParts(
        hours = totalSeconds / SECONDS_PER_HOUR,
        minutes = totalSeconds % SECONDS_PER_HOUR / SECONDS_PER_MINUTE,
        seconds = totalSeconds % SECONDS_PER_MINUTE,
    )
}

private const val MILLIS_PER_SECOND = 1_000L
private const val SECONDS_PER_MINUTE = 60L
private const val SECONDS_PER_HOUR = 3_600L
