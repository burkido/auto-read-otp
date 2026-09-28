package com.burkido.otp.timer

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Single source of truth for all primitive default values used in the countdown timer.
 *
 * Follows the Material3 `*Tokens` pattern. These values are internal and not part
 * of the public API — consumers customise via [CountdownTimerDefaults].
 */
internal object CountdownTimerTokens {

    // ── Box dimensions ─────────────────────────────────────────────────────
    val TextSize = 14.sp
    val VerticalPadding = 2.dp
    val HorizontalPadding = 4.dp
    val BoxSpacing = 1.dp

    // ── Shape ──────────────────────────────────────────────────────────────
    val BoxCornerRadius = 4.dp

    // ── Separator ──────────────────────────────────────────────────────────
    val SeparatorHorizontalPadding = 2.dp
}
