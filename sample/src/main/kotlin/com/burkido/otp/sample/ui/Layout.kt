package com.burkido.otp.sample.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun fittedCellWidth(available: Dp, count: Int, spacing: Dp, preferred: Dp = 48.dp): Dp =
    minOf(preferred, (available - spacing * (count - 1)) / count)
