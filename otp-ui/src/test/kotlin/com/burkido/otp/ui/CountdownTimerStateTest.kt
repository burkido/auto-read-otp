package com.burkido.otp.ui

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class CountdownTimerStateTest {

    private fun TestScope.timerEndingAt(endTimeMillis: Long) =
        CountdownTimerState(endTimeMillis = endTimeMillis, clock = { currentTime })

    @Test
    fun `remaining time counts down with the clock`() = runTest {
        val state = timerEndingAt(5_000)
        backgroundScope.launch { state.run() }
        runCurrent()
        assertThat(state.remaining).isEqualTo(5.seconds)

        advanceTimeBy(2_000)
        runCurrent()

        assertThat(state.remaining).isEqualTo(3.seconds)
        assertThat(state.isFinished).isFalse()
    }

    @Test
    fun `run returns once the end time is reached`() = runTest {
        val state = timerEndingAt(3_000)

        state.run()

        assertThat(currentTime).isEqualTo(3_000)
        assertThat(state.isFinished).isTrue()
        assertThat(state.remaining).isEqualTo(Duration.ZERO)
    }

    @Test
    fun `ticks line up with whole seconds when started mid second`() = runTest {
        val state = timerEndingAt(2_500)
        backgroundScope.launch { state.run() }
        runCurrent()

        advanceTimeBy(500)
        runCurrent()

        assertThat(state.remaining).isEqualTo(2.seconds)
    }

    @Test
    fun `restarting after a cancel keeps the original end time`() = runTest {
        val state = timerEndingAt(10_000)
        val firstRun = backgroundScope.launch { state.run() }
        advanceTimeBy(2_000)
        firstRun.cancel()

        // Simulates the timer leaving the screen for a while before being shown again.
        advanceTimeBy(5_000)
        backgroundScope.launch { state.run() }
        runCurrent()

        assertThat(state.remaining).isEqualTo(3.seconds)
    }

    @Test
    fun `a timer that already ended is finished from the start`() = runTest {
        advanceTimeBy(10_000)
        val state = timerEndingAt(5_000)

        assertThat(state.isFinished).isTrue()
        state.run()
        assertThat(currentTime).isEqualTo(10_000)
    }

    @Test
    fun `time parts round seconds up`() {
        val parts = (1.hours + 2.minutes + 3.seconds + 1.milliseconds).toTimeParts()

        assertThat(listOf(parts.hours, parts.minutes, parts.seconds)).containsExactly(1L, 2L, 4L).inOrder()
    }

    @Test
    fun `time parts show zero only when nothing is left`() {
        val almostDone = 1.milliseconds.toTimeParts()
        val done = Duration.ZERO.toTimeParts()

        assertThat(almostDone.seconds).isEqualTo(1)
        assertThat(done.seconds).isEqualTo(0)
    }

    @Test
    fun `saver keeps the end time`() {
        val state = CountdownTimerState(endTimeMillis = 1_234L)

        val restored = CountdownTimerState.Saver.restore(1_234L)

        assertThat(restored?.endTimeMillis).isEqualTo(state.endTimeMillis)
    }
}
