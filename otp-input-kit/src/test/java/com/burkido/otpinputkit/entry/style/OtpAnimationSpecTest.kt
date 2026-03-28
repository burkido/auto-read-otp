package com.burkido.otpinputkit.entry.style

import com.burkido.otpinputkit.entry.OtpInputDefaults
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Unit tests for [OtpAnimationSpec] data class defaults and enum values.
 */
class OtpAnimationSpecTest {

    @Test
    fun `default animation spec has expected values`() {
        val spec = OtpInputDefaults.animationSpec()
        assertThat(spec.shakeOnError).isTrue()
        assertThat(spec.cellEntryAnimation).isEqualTo(CellAnimation.Scale)
        assertThat(spec.animationDuration).isEqualTo(150)
        assertThat(spec.shakeAmplitude).isEqualTo(OtpTokens.ShakeAmplitude)
        assertThat(spec.shakeStepDuration).isEqualTo(OtpTokens.ShakeStepDuration)
        assertThat(spec.shakeRepeatCount).isEqualTo(OtpTokens.ShakeRepeatCount)
    }

    @Test
    fun `copy preserves unchanged values`() {
        val original = OtpInputDefaults.animationSpec()
        val copied = original.copy(shakeOnError = false)
        assertThat(copied.cellEntryAnimation).isEqualTo(original.cellEntryAnimation)
        assertThat(copied.animationDuration).isEqualTo(original.animationDuration)
        assertThat(copied.shakeOnError).isFalse()
    }

    @Test
    fun `all CellAnimation enum values exist`() {
        val values = CellAnimation.entries
        assertThat(values).containsExactly(
            CellAnimation.None,
            CellAnimation.Scale,
            CellAnimation.FadeIn,
            CellAnimation.SlideUp,
        )
    }

    @Test
    fun `data class equality works`() {
        val a = OtpInputDefaults.animationSpec()
        val b = OtpInputDefaults.animationSpec()
        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `data class inequality works`() {
        val a = OtpInputDefaults.animationSpec()
        val b = OtpInputDefaults.animationSpec(cellEntryAnimation = CellAnimation.FadeIn)
        assertThat(a).isNotEqualTo(b)
    }
}
