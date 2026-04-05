package com.burkido.otpinputkit.countdowntimer

import android.os.CountDownTimer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import java.util.concurrent.TimeUnit

/**
 * State holder for [CountdownTimer] that owns the underlying [CountDownTimer] lifecycle.
 *
 * Create instances via [rememberCountdownTimerState] so the state survives
 * configuration changes through [rememberSaveable].
 *
 * @param endDate The countdown target time in milliseconds since epoch
 *   (e.g. `System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)`).
 *
 * @property remainingTime Remaining milliseconds. Updates every second and triggers
 *   recomposition automatically.
 * @property isFinished `true` once the timer reaches zero.
 */
@Stable
class CountdownTimerState(val endDate: Long) {

    private var onTimerFinish: () -> Unit = {}

    private val countDownTimer: CountDownTimer by lazy { createCountDownTimer() }

    var remainingTime: Long by mutableLongStateOf(
        (endDate - System.currentTimeMillis()).coerceAtLeast(0L)
    )
        private set

    var isFinished: Boolean by mutableStateOf(endDate <= 0L || endDate <= System.currentTimeMillis())
        private set

    /** Starts the countdown. If [endDate] is already in the past the timer finishes immediately. */
    fun startTimer() {
        if (endDate <= 0L || endDate <= System.currentTimeMillis()) {
            remainingTime = 0L
            isFinished = true
            onTimerFinish()
            return
        }
        countDownTimer.start()
    }

    /** Cancels the countdown without triggering [onTimerFinish]. */
    fun cancelTimer() {
        countDownTimer.cancel()
    }

    /**
     * Registers a callback that fires once when the timer reaches zero.
     * Call this before [startTimer]. Replacing the listener does not restart the timer.
     */
    fun setOnTimerFinishListener(action: () -> Unit) {
        onTimerFinish = action
    }

    /**
     * Converts [time] (milliseconds) into a [Triple] of `(hours, minutes, seconds)`.
     */
    fun getRemainingTimes(time: Long): Triple<Long, Long, Long> {
        val totalSeconds = time / ONE_SECOND_MS
        val hours = totalSeconds / ONE_HOUR_SECONDS
        val minutes = (totalSeconds % ONE_HOUR_SECONDS) / ONE_MINUTE_SECONDS
        val seconds = totalSeconds % ONE_MINUTE_SECONDS
        return Triple(hours, minutes, seconds)
    }

    private fun createCountDownTimer(): CountDownTimer =
        object : CountDownTimer(endDate - System.currentTimeMillis(), TICK_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = (endDate - System.currentTimeMillis()).coerceAtLeast(0L)
            }

            override fun onFinish() {
                remainingTime = 0L
                isFinished = true
                onTimerFinish()
            }
        }

    companion object {
        private val TICK_INTERVAL = TimeUnit.SECONDS.toMillis(1)
        private const val ONE_HOUR_SECONDS = 3600L
        private const val ONE_MINUTE_SECONDS = 60L
        private const val ONE_SECOND_MS = 1000L

        internal val Saver = listSaver(
            save = { listOf(it.endDate) },
            restore = { CountdownTimerState(endDate = it[0] as Long) },
        )
    }
}

/**
 * Creates and remembers a [CountdownTimerState] that survives configuration changes.
 *
 * The state is keyed on [endDate] — changing it creates a new timer.
 *
 * @param endDate The countdown target time in milliseconds since epoch.
 */
@Composable
fun rememberCountdownTimerState(endDate: Long): CountdownTimerState =
    rememberSaveable(endDate, saver = CountdownTimerState.Saver) {
        CountdownTimerState(endDate = endDate)
    }
