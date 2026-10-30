package com.burkido.otp.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/**
 * Animation settings for OTP cells. Get one from [OtpInputDefaults.animationSpec].
 *
 * @property shakeOnError Shake the cells sideways when the field switches into the error state.
 * @property cellEntryAnimation How a newly typed character appears.
 * @property animationDuration Duration of color, border and entry animations, in milliseconds.
 * @property shakeAmplitude How far a cell moves to each side while shaking.
 * @property shakeStepDuration Duration of a single shake movement, in milliseconds.
 * @property shakeRepeatCount Number of side to side movements in one shake.
 */
@Immutable
public class OtpAnimationSpec(
    public val shakeOnError: Boolean,
    public val cellEntryAnimation: CellAnimation,
    public val animationDuration: Int,
    public val shakeAmplitude: Dp,
    public val shakeStepDuration: Int,
    public val shakeRepeatCount: Int,
) {

    public fun copy(
        shakeOnError: Boolean = this.shakeOnError,
        cellEntryAnimation: CellAnimation = this.cellEntryAnimation,
        animationDuration: Int = this.animationDuration,
        shakeAmplitude: Dp = this.shakeAmplitude,
        shakeStepDuration: Int = this.shakeStepDuration,
        shakeRepeatCount: Int = this.shakeRepeatCount,
    ): OtpAnimationSpec = OtpAnimationSpec(
        shakeOnError = shakeOnError,
        cellEntryAnimation = cellEntryAnimation,
        animationDuration = animationDuration,
        shakeAmplitude = shakeAmplitude,
        shakeStepDuration = shakeStepDuration,
        shakeRepeatCount = shakeRepeatCount,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OtpAnimationSpec) return false
        return shakeOnError == other.shakeOnError &&
            cellEntryAnimation == other.cellEntryAnimation &&
            animationDuration == other.animationDuration &&
            shakeAmplitude == other.shakeAmplitude &&
            shakeStepDuration == other.shakeStepDuration &&
            shakeRepeatCount == other.shakeRepeatCount
    }

    override fun hashCode(): Int {
        var result = shakeOnError.hashCode()
        result = 31 * result + cellEntryAnimation.hashCode()
        result = 31 * result + animationDuration
        result = 31 * result + shakeAmplitude.hashCode()
        result = 31 * result + shakeStepDuration
        result = 31 * result + shakeRepeatCount
        return result
    }
}

/** How a character appears when it's typed into a cell. */
public enum class CellAnimation {
    None,
    Scale,
    FadeIn,
    SlideUp,
}
