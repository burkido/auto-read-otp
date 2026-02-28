package com.burkido.otpinputkit.style

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Unit tests for [OtpAnimationSpec] data class defaults and enum values.
 */
class OtpAnimationSpecTest {

    @Test
    fun `default animation spec has expected values`() {
        val spec = OtpAnimationSpec()
        assertThat(spec.cursorBlinkInterval).isEqualTo(500L)
        assertThat(spec.shakeOnError).isTrue()
        assertThat(spec.cellEntryAnimation).isEqualTo(CellAnimation.Scale)
        assertThat(spec.animationDuration).isEqualTo(150)
    }

    @Test
    fun `copy preserves unchanged values`() {
        val original = OtpAnimationSpec()
        val copied = original.copy(shakeOnError = false)
        assertThat(copied.cursorBlinkInterval).isEqualTo(original.cursorBlinkInterval)
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
        val a = OtpAnimationSpec()
        val b = OtpAnimationSpec()
        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `data class inequality works`() {
        val a = OtpAnimationSpec()
        val b = OtpAnimationSpec(cellEntryAnimation = CellAnimation.FadeIn)
        assertThat(a).isNotEqualTo(b)
    }
}
