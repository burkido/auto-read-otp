package com.burkido.otpinputkit.entry.style

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/**
 * Configures animations for OTP input cells.
 *
 * Use [com.burkido.otpinputkit.OtpInputDefaults.animationSpec] to create an instance
 * with sensible defaults.
 */
@Immutable
public data class OtpAnimationSpec(
    /** Whether to shake the input field on error. */
    public val shakeOnError: Boolean,
    /** Animation applied when a character enters a cell. */
    public val cellEntryAnimation: CellAnimation,
    /** Duration of entry/transition animations in milliseconds. */
    public val animationDuration: Int,
    /** Maximum horizontal displacement of the cell during a shake, in dp. */
    public val shakeAmplitude: Dp,
    /** Duration in milliseconds of each individual shake step. */
    public val shakeStepDuration: Int,
    /** Number of oscillations performed during a shake. */
    public val shakeRepeatCount: Int,
)

/** Possible animation styles when a new character is entered into a cell. */
public enum class CellAnimation {
    /** No animation. */
    None,
    /** The cell scales up briefly. */
    Scale,
    /** The character fades in. */
    FadeIn,
    /** The character slides up into view. */
    SlideUp,
}
