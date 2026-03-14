package com.burkido.otpinputkit.style

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Single source of truth for all primitive default values used in the OTP input kit.
 *
 * Follows the Material3 `*Tokens` pattern (e.g. `ButtonTokens`). These values are
 * internal and not part of the public API — consumers interact only through
 * [com.burkido.otpinputkit.OtpInputDefaults].
 */
internal object OtpTokens {

    // ── Cell dimensions ───────────────────────────────────────────────────────
    val CellWidth = 48.dp
    val CellHeight = 56.dp
    val BorderWidth = 1.5.dp
    val FocusedBorderWidth = 2.dp
    val CornerRadius = 12.dp
    val Spacing = 8.dp
    val TextSize = 24.sp

    // ── Content padding ───────────────────────────────────────────────────────
    val ContentPaddingHorizontal = 8.dp
    val ContentPaddingVertical = 12.dp

    // ── Cursor ────────────────────────────────────────────────────────────────
    val CursorWidth = 2.dp
    val CursorHeight = 24.dp
    val CursorCornerRadius = 1.dp
    val CursorBlinkInterval = 500L

    // ── Animation ─────────────────────────────────────────────────────────────
    val AnimationDuration = 150
    val ShakeAmplitude = 8.dp
    val ShakeStepDuration = 50
    val ShakeRepeatCount = 3

    // ── Mask ──────────────────────────────────────────────────────────────────
    val MaskCharacter = '●'
    val MaskDelay = 300L
}
