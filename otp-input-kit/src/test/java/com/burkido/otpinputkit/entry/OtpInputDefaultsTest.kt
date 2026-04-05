package com.burkido.otpinputkit.entry

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
        assertThat(spec.shakeOnError).isTrue()
        assertThat(spec.animationDuration).isEqualTo(150)
    }

    @Test
    fun `animationSpec respects custom values`() {
        val spec = OtpInputDefaults.animationSpec(
            shakeOnError = false,
        )
        assertThat(spec.shakeOnError).isFalse()
    }

    // ── shape ──────────────────────────────────────────────────────

    @Test
    fun `shape returns non-null shape`() {
        val shape = OtpInputDefaults.shape()
        assertThat(shape).isNotNull()
    }

    // ── maskConfig ─────────────────────────────────────────────────

    @Test
    fun `maskConfig returns defaults when no args provided`() {
        val config = OtpInputDefaults.maskConfig()
        assertThat(config.isMasked).isFalse()
        assertThat(config.maskCharacter).isEqualTo('●')
        assertThat(config.maskDelay).isEqualTo(300L)
    }

    @Test
    fun `maskConfig respects custom values`() {
        val config = OtpInputDefaults.maskConfig(isMasked = true, maskDelay = 500L)
        assertThat(config.isMasked).isTrue()
        assertThat(config.maskDelay).isEqualTo(500L)
    }

    // ── contentPadding ─────────────────────────────────────────────

    @Test
    fun `contentPadding returns defaults when no args provided`() {
        val padding = OtpInputDefaults.contentPadding()
        assertThat(padding.horizontal.value).isEqualTo(8f)
        assertThat(padding.vertical.value).isEqualTo(12f)
    }

    @Test
    fun `contentPadding respects custom values`() {
        val padding = OtpInputDefaults.contentPadding(horizontal = 16.dp)
        assertThat(padding.horizontal.value).isEqualTo(16f)
        assertThat(padding.vertical.value).isEqualTo(12f)
    }
}

// Helpers to create Dp/Sp literals outside Compose context
private inline val Number.dp get() = androidx.compose.ui.unit.Dp(this.toFloat())
private inline val Number.sp get() = androidx.compose.ui.unit.TextUnit(this.toFloat(), androidx.compose.ui.unit.TextUnitType.Sp)
