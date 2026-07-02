package com.burkido.otpinputkit.countdowntimer

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.burkido.otpinputkit.countdowntimer.style.CountdownTimerSize
import com.burkido.otpinputkit.countdowntimer.style.CountdownTimerStyle
import com.burkido.otpinputkit.countdowntimer.style.CountdownTimerTokens
import java.util.concurrent.TimeUnit

/**
 * Displays a countdown timer as `HH : MM : SS` time boxes.
 *
 * The composable manages the timer lifecycle automatically: it starts the clock
 * when first composed and cancels it when removed from the composition.
 * The entire row hides itself when the timer finishes.
 *
 * ### Basic usage
 * ```kotlin
 * val state = rememberCountdownTimerState(
 *     endDate = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)
 * )
 *
 * CountdownTimer(state = state)
 * ```
 *
 * ### Custom style
 * ```kotlin
 * CountdownTimer(
 *     state = state,
 *     style = CountdownTimerDefaults.style(
 *         backgroundColor = MaterialTheme.colorScheme.errorContainer
 *     ),
 *     size = CountdownTimerSize.Large,
 *     onTimerFinish = { /* resend button becomes enabled */ }
 * )
 * ```
 *
 * @param state A [CountdownTimerState] created via [rememberCountdownTimerState].
 * @param modifier Modifier for the root [Row].
 * @param style Visual configuration. Defaults to [CountdownTimerDefaults.style].
 * @param size Size/padding configuration. Defaults to [CountdownTimerDefaults.size] ([CountdownTimerSize.Medium]).
 * @param backgroundAlpha Alpha multiplier for the time-box backgrounds (0f–1f).
 * @param onTimerFinish Called once when the countdown reaches zero.
 */
@Composable
public fun CountdownTimer(
    state: CountdownTimerState,
    modifier: Modifier = Modifier,
    style: CountdownTimerStyle = CountdownTimerDefaults.style(),
    size: CountdownTimerSize = CountdownTimerDefaults.size(),
    backgroundAlpha: Float = 1f,
    onTimerFinish: () -> Unit = {},
) {
    val latestOnTimerFinish by rememberUpdatedState(onTimerFinish)

    DisposableEffect(state) {
        state.setOnTimerFinishListener { latestOnTimerFinish() }
        state.startTimer()
        onDispose { state.cancelTimer() }
    }

    if (state.isFinished) return

    val remainingTimes by remember(state) {
        derivedStateOf { state.getRemainingTimes(state.remainingTime) }
    }
    val (hours, minutes, seconds) = remainingTimes

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TimeBoxItem(time = hours, style = style, size = size, backgroundAlpha = backgroundAlpha)
        Spacer(modifier = Modifier.width(size.boxSpacing))
        Separator(style = style)
        Spacer(modifier = Modifier.width(size.boxSpacing))
        TimeBoxItem(time = minutes, style = style, size = size, backgroundAlpha = backgroundAlpha)
        Spacer(modifier = Modifier.width(size.boxSpacing))
        Separator(style = style)
        Spacer(modifier = Modifier.width(size.boxSpacing))
        TimeBoxItem(time = seconds, style = style, size = size, backgroundAlpha = backgroundAlpha)
    }
}

@Composable
private fun Separator(style: CountdownTimerStyle) {
    Text(
        text = ":",
        style = style.textStyle.copy(color = style.separatorColor),
        modifier = Modifier.width(CountdownTimerTokens.SeparatorHorizontalPadding * 2),
    )
}

@Preview(showBackground = true)
@Composable
private fun CountdownTimerLargePreview() {
    val state = rememberCountdownTimerState(
        endDate = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(PREVIEW_DURATION),
    )
    CountdownTimer(state = state, size = CountdownTimerDefaults.size(textSize = 16.sp, verticalPadding = 6.dp, horizontalPadding = 8.dp))
}

@Preview(showBackground = true)
@Composable
private fun CountdownTimerMediumPreview() {
    val state = rememberCountdownTimerState(
        endDate = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(PREVIEW_DURATION),
    )
    CountdownTimer(state = state)
}

@Preview(showBackground = true)
@Composable
private fun CountdownTimerSmallPreview() {
    val state = rememberCountdownTimerState(
        endDate = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(PREVIEW_DURATION),
    )
    CountdownTimer(state = state, size = CountdownTimerDefaults.size(textSize = 12.sp, verticalPadding = 1.dp, horizontalPadding = 5.dp))
}

private const val PREVIEW_DURATION = 3672L // 1h 1m 12s
