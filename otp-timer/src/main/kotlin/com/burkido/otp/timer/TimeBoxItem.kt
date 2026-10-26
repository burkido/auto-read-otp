package com.burkido.otp.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun TimeBoxItem(
    value: Long,
    style: CountdownTimerStyle,
    dimensions: CountdownTimerDimensions,
    modifier: Modifier = Modifier,
) {
    Text(
        text = value.toString().padStart(2, '0'),
        style = style.textStyle,
        maxLines = 1,
        modifier = modifier
            .background(style.backgroundColor, RoundedCornerShape(CountdownTimerTokens.BoxCornerRadius))
            .padding(horizontal = dimensions.horizontalPadding, vertical = dimensions.verticalPadding),
    )
}
