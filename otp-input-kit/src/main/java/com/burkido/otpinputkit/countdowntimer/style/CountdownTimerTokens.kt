package com.burkido.otpinputkit.countdowntimer.style

import androidx.compose.ui.unit.dp

/**
 * Single source of truth for all primitive default values used in the countdown timer.
 *
 * Follows the Material3 `*Tokens` pattern. These values are internal and not part
 * of the public API — consumers customise via [com.burkido.otpinputkit.countdowntimer.CountdownTimerDefaults].
 */
internal object CountdownTimerTokens {

    // ── Box dimensions ─────────────────────────────────────────────────────
    val VerticalPadding = 2.dp
    val HorizontalPadding = 4.dp
    val BoxSpacing = 1.dp

    // ── Shape ──────────────────────────────────────────────────────────────
    val BoxCornerRadius = 4.dp

    // ── Separator ──────────────────────────────────────────────────────────
    val SeparatorHorizontalPadding = 2.dp
}
