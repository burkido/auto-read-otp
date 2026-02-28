package com.burkido.otpinputkit.style

import androidx.compose.runtime.Immutable

/**
 * Configures animations for OTP input cells.
 *
 * Use [com.burkido.otpinputkit.OtpInputDefaults.animationSpec] to create an instance
 * with sensible defaults.
 */
@Immutable
data class OtpAnimationSpec(
    /** Cursor blink interval in milliseconds. */
    val cursorBlinkInterval: Long = 500L,
    /** Whether to shake the input field on error. */
    val shakeOnError: Boolean = true,
    /** Animation applied when a character enters a cell. */
    val cellEntryAnimation: CellAnimation = CellAnimation.Scale,
    /** Duration of entry/transition animations in milliseconds. */
    val animationDuration: Int = 150,
)

/** Possible animation styles when a new character is entered into a cell. */
enum class CellAnimation {
    /** No animation. */
    None,
    /** The cell scales up briefly. */
    Scale,
    /** The character fades in. */
    FadeIn,
    /** The character slides up into view. */
    SlideUp,
}
