package com.burkido.otpinputkit.entry.style

import com.burkido.otpinputkit.entry.OtpInputDefaults
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Unit tests for [OtpCellDimensions] data class defaults and copy behavior.
 */
class OtpCellDimensionsTest {

    @Test
    fun `default dimensions have expected values`() {
        val dims = OtpInputDefaults.dimensions()
        assertThat(dims.cellWidth.value).isEqualTo(48f)
        assertThat(dims.cellHeight.value).isEqualTo(56f)
        assertThat(dims.borderWidth.value).isEqualTo(1.5f)
        assertThat(dims.focusedBorderWidth.value).isEqualTo(2f)
        assertThat(dims.cornerRadius.value).isEqualTo(12f)
        assertThat(dims.spacing.value).isEqualTo(8f)
        assertThat(dims.textSize.value).isEqualTo(24f)
    }

    @Test
    fun `copy preserves unchanged values`() {
        val original = OtpInputDefaults.dimensions()
        val copied = original.copy(cellWidth = original.cellWidth * 2)
        assertThat(copied.cellHeight).isEqualTo(original.cellHeight)
        assertThat(copied.borderWidth).isEqualTo(original.borderWidth)
        assertThat(copied.spacing).isEqualTo(original.spacing)
    }

    @Test
    fun `data class equality works`() {
        val a = OtpInputDefaults.dimensions()
        val b = OtpInputDefaults.dimensions()
        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `data class inequality when values differ`() {
        val a = OtpInputDefaults.dimensions()
        val b = OtpInputDefaults.dimensions(cellWidth = a.cellWidth * 2)
        assertThat(a).isNotEqualTo(b)
    }
}
