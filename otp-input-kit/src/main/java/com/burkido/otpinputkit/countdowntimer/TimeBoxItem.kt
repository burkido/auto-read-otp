package com.burkido.otpinputkit.countdowntimer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.burkido.otpinputkit.countdowntimer.style.CountdownTimerSize
import com.burkido.otpinputkit.countdowntimer.style.CountdownTimerStyle
import com.burkido.otpinputkit.countdowntimer.style.CountdownTimerTokens

/**
 * Renders a single time unit (hours, minutes, or seconds) as a styled rounded box.
 *
 * @param time The numeric value to display (e.g. 5 → "05").
 * @param style Visual configuration.
 * @param size Size/padding configuration.
 * @param backgroundAlpha Alpha multiplier for the box background. Defaults to `1f`.
 */
@Composable
internal fun TimeBoxItem(
    time: Long,
    style: CountdownTimerStyle,
    size: CountdownTimerSize,
    modifier: Modifier = Modifier,
    backgroundAlpha: Float = 1f,
) {
    Text(
        text = String.format(TIME_FORMAT, time),
        style = style.textStyle.copy(fontSize = size.textSize),
        maxLines = 1,
        modifier = modifier
            .wrapContentSize()
            .background(
                color = style.backgroundColor.copy(alpha = backgroundAlpha),
                shape = RoundedCornerShape(CountdownTimerTokens.BoxCornerRadius),
            )
            .padding(
                horizontal = size.horizontalPadding,
                vertical = size.verticalPadding,
            ),
    )
}

private const val TIME_FORMAT = "%02d"
