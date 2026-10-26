package com.burkido.otp.input

import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class OtpInputDefaultsTest {

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

    @Test
    fun `shape returns non-null shape`() {
        val shape = OtpInputDefaults.shape()
        assertThat(shape).isNotNull()
    }

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
