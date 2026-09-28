package com.burkido.otp.timer

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import kotlin.time.Duration.Companion.seconds

/**
 * Shows the time left on [state] as `HH : MM : SS` boxes, and hides itself when it reaches zero.
 *
 * ```kotlin
 * val timer = rememberCountdownTimerState(duration = 2.minutes)
 *
 * CountdownTimer(state = timer, onTimerFinish = { canResend = true })
 * ```
 *
 * Screen readers announce the time as a sentence, like "4 minutes, 32 seconds remaining", when
 * the timer is focused. It isn't a live region, so it won't interrupt the user every second.
 *
 * @param onTimerFinish Called once when the countdown reaches zero, including when [state]
 *   had already ended by the time this was first shown.
 */
@Composable
public fun CountdownTimer(
    state: CountdownTimerState,
    modifier: Modifier = Modifier,
    style: CountdownTimerStyle = CountdownTimerDefaults.style(),
    dimensions: CountdownTimerDimensions = CountdownTimerDefaults.dimensions(),
    onTimerFinish: () -> Unit = {},
) {
    val currentOnTimerFinish by rememberUpdatedState(onTimerFinish)

    LaunchedEffect(state) {
        state.run()
        if (!state.finishReported) {
            state.finishReported = true
            currentOnTimerFinish()
        }
    }

    if (state.isFinished) return

    val parts = state.remaining.toTimeParts()
    val description = remainingDescription(parts)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clearAndSetSemantics { contentDescription = description },
    ) {
        TimeBoxItem(value = parts.hours, style = style, dimensions = dimensions)
        Separator(style = style, dimensions = dimensions)
        TimeBoxItem(value = parts.minutes, style = style, dimensions = dimensions)
        Separator(style = style, dimensions = dimensions)
        TimeBoxItem(value = parts.seconds, style = style, dimensions = dimensions)
    }
}

@Composable
private fun Separator(style: CountdownTimerStyle, dimensions: CountdownTimerDimensions) {
    Spacer(Modifier.width(dimensions.boxSpacing))
    Text(
        text = ":",
        style = style.textStyle.copy(color = style.separatorColor),
        textAlign = TextAlign.Center,
        modifier = Modifier.width(CountdownTimerTokens.SeparatorWidth),
    )
    Spacer(Modifier.width(dimensions.boxSpacing))
}

@Composable
private fun remainingDescription(parts: TimeParts): String {
    val pieces = buildList {
        if (parts.hours > 0) add(pluralStringResource(R.plurals.otp_timer_hours, parts.hours.toInt(), parts.hours))
        if (parts.hours > 0 || parts.minutes > 0) {
            add(pluralStringResource(R.plurals.otp_timer_minutes, parts.minutes.toInt(), parts.minutes))
        }
        add(pluralStringResource(R.plurals.otp_timer_seconds, parts.seconds.toInt(), parts.seconds))
    }
    return stringResource(R.string.otp_timer_remaining, pieces.joinToString(", "))
}

@Preview(showBackground = true)
@Composable
private fun CountdownTimerPreview() {
    CountdownTimer(state = rememberCountdownTimerState(duration = 3_672.seconds))
}
