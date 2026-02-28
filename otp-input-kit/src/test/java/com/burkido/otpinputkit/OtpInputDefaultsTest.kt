package com.burkido.otpinputkit

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Unit tests for [OtpInputDefaults] factory functions (non-composable ones).
 */
class OtpInputDefaultsTest {

    // ── dimensions ─────────────────────────────────────────────────

    @Test
    fun `dimensions returns defaults when no args provided`() {
        val dims = OtpInputDefaults.dimensions()
        assertThat(dims.cellWidth.value).isEqualTo(48f)
        assertThat(dims.cellHeight.value).isEqualTo(56f)
        assertThat(dims.spacing.value).isEqualTo(8f)
    }

    @Test
    fun `dimensions respects custom values`() {
        val dims = OtpInputDefaults.dimensions(cellWidth = 64.dp, spacing = 16.dp)
        assertThat(dims.cellWidth.value).isEqualTo(64f)
        assertThat(dims.spacing.value).isEqualTo(16f)
        // Unchanged values remain default
        assertThat(dims.cellHeight.value).isEqualTo(56f)
    }

    // ── animationSpec ──────────────────────────────────────────────

    @Test
    fun `animationSpec returns defaults when no args provided`() {
        val spec = OtpInputDefaults.animationSpec()
        assertThat(spec.cursorBlinkInterval).isEqualTo(500L)
        assertThat(spec.shakeOnError).isTrue()
    }

    @Test
    fun `animationSpec respects custom values`() {
        val spec = OtpInputDefaults.animationSpec(
            cursorBlinkInterval = 1000L,
            shakeOnError = false,
        )
        assertThat(spec.cursorBlinkInterval).isEqualTo(1000L)
        assertThat(spec.shakeOnError).isFalse()
    }

    // ── shape ──────────────────────────────────────────────────────

    @Test
    fun `shape returns non-null shape`() {
        val shape = OtpInputDefaults.shape()
        assertThat(shape).isNotNull()
    }

    // ── textStyle ──────────────────────────────────────────────────

    @Test
    fun `textStyle returns style with correct font size`() {
        val style = OtpInputDefaults.textStyle()
        assertThat(style.fontSize.value).isEqualTo(24f)
    }

    @Test
    fun `textStyle respects custom font size`() {
        val style = OtpInputDefaults.textStyle(textSize = 32.sp)
        assertThat(style.fontSize.value).isEqualTo(32f)
    }
}

// Helpers to create Dp/Sp literals outside Compose context
private inline val Number.dp get() = androidx.compose.ui.unit.Dp(this.toFloat())
private inline val Number.sp get() = androidx.compose.ui.unit.TextUnit(this.toFloat(), androidx.compose.ui.unit.TextUnitType.Sp)
