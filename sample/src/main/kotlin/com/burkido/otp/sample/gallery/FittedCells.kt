package com.burkido.otp.sample.gallery

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.burkido.otp.sample.ui.fittedCellWidth

@Composable
internal fun FittedCells(
    count: Int,
    spacing: Dp,
    preferred: Dp,
    modifier: Modifier = Modifier,
    extraWidth: Dp = Dp.Hairline,
    content: @Composable (cellWidth: Dp) -> Unit,
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        content(fittedCellWidth(maxWidth - extraWidth, count, spacing, preferred))
    }
}
