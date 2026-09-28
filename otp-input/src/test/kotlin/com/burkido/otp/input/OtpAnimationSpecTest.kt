package com.burkido.otp.input

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class OtpAnimationSpecTest {

    @Test
    fun `default animation spec has expected values`() {
        val spec = OtpInputDefaults.animationSpec()
        assertThat(spec.shakeOnError).isTrue()
        assertThat(spec.cellEntryAnimation).isEqualTo(CellAnimation.Scale)
        assertThat(spec.animationDuration).isEqualTo(150)
        assertThat(spec.shakeAmplitude).isEqualTo(OtpTokens.ShakeAmplitude)
        assertThat(spec.shakeStepDuration).isEqualTo(OtpTokens.SHAKE_STEP_DURATION)
        assertThat(spec.shakeRepeatCount).isEqualTo(OtpTokens.SHAKE_REPEAT_COUNT)
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
    fun `equal values are equal`() {
        val a = OtpInputDefaults.animationSpec()
        val b = OtpInputDefaults.animationSpec()
        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `different values are not equal`() {
        val a = OtpInputDefaults.animationSpec()
        val b = OtpInputDefaults.animationSpec(cellEntryAnimation = CellAnimation.FadeIn)
        assertThat(a).isNotEqualTo(b)
    }
}
