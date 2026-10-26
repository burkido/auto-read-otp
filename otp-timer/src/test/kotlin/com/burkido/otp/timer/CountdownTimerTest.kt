package com.burkido.otp.timer

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CountdownTimerTest {

    @get:Rule
    val rule = createComposeRule()

    private var now = 0L
    private fun timerEndingAt(endTimeMillis: Long) = CountdownTimerState(endTimeMillis, clock = { now })

    @Test
    fun `shows hours minutes and seconds in two digit boxes`() {
        rule.setContent { CountdownTimer(state = timerEndingAt(3_723_000)) }

        rule.onAllNodesWithText("01", useUnmergedTree = true).assertCountEquals(1)
        rule.onAllNodesWithText("02", useUnmergedTree = true).assertCountEquals(1)
        rule.onAllNodesWithText("03", useUnmergedTree = true).assertCountEquals(1)
    }

    @Test
    fun `describes the remaining time as a sentence`() {
        rule.setContent { CountdownTimer(state = timerEndingAt(272_000)) }

        rule.onNodeWithContentDescription("4 minutes, 32 seconds remaining").assertExists()
    }

    @Test
    fun `hides itself and reports once when the time is up`() {
        var finishCalls = 0
        rule.setContent { CountdownTimer(state = timerEndingAt(-1), onTimerFinish = { finishCalls++ }) }
        rule.waitForIdle()

        assertThat(finishCalls).isEqualTo(1)
        rule.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsProperties.ContentDescription)).assertCountEquals(0)
    }
}
