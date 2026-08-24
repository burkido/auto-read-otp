package com.burkido.otpinputkit.countdowntimer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.burkido.otpinputkit.countdowntimer.style.CountdownTimerSize
import com.burkido.otpinputkit.countdowntimer.style.CountdownTimerTokens

/**
 * Renders a single time unit (hours, minutes, or seconds) as a styled rounded box.
 *
 * @param time The numeric value to display (e.g. 5 → "05").
 * @param textStyle Fully resolved text style, including the effective font size.
 * @param backgroundColor Background color of the box.
 * @param size Size/padding configuration.
 * @param backgroundAlpha Alpha multiplier for the box background. Defaults to `1f`.
 */
@Composable
internal fun TimeBoxItem(
    time: Long,
    textStyle: TextStyle,
    backgroundColor: Color,
    size: CountdownTimerSize,
    modifier: Modifier = Modifier,
    backgroundAlpha: Float = 1f,
) {
    Text(
        text = String.format(TIME_FORMAT, time),
        style = textStyle,
        maxLines = 1,
        modifier = modifier
            .wrapContentSize()
            .background(
                color = backgroundColor.copy(alpha = backgroundAlpha),
                shape = RoundedCornerShape(CountdownTimerTokens.BoxCornerRadius),
            )
            .padding(
                horizontal = size.horizontalPadding,
                vertical = size.verticalPadding,
            ),
    )
}

private const val TIME_FORMAT = "%02d"
