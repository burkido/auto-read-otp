package com.burkido.otpinputkit.entry.style

import androidx.compose.ui.graphics.Color
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Unit tests for [OtpCellColors.borderColor] and [OtpCellColors.containerColor] resolution logic.
 */
class OtpCellColorsTest {

    private val colors = OtpCellColors(
        focusedBorderColor = Color.Blue,
        unfocusedBorderColor = Color.Gray,
        errorBorderColor = Color.Red,
        filledBorderColor = Color.Green,
        focusedContainerColor = Color.Cyan,
        unfocusedContainerColor = Color.White,
        errorContainerColor = Color(0xFFFFCDD2),
        filledContainerColor = Color(0xFFE8F5E9),
        textColor = Color.Black,
        cursorColor = Color.Blue,
        placeholderColor = Color.LightGray,
    )

    // ── borderColor ────────────────────────────────────────────────

    @Test
    fun `borderColor returns error color when error is true`() {
        assertThat(colors.borderColor(focused = false, filled = false, error = true))
            .isEqualTo(Color.Red)
    }

    @Test
    fun `borderColor returns error color even when focused and filled`() {
        assertThat(colors.borderColor(focused = true, filled = true, error = true))
            .isEqualTo(Color.Red)
    }

    @Test
    fun `borderColor returns focused color when focused`() {
        assertThat(colors.borderColor(focused = true, filled = false, error = false))
            .isEqualTo(Color.Blue)
    }

    @Test
    fun `borderColor returns filled color when filled but not focused`() {
        assertThat(colors.borderColor(focused = false, filled = true, error = false))
            .isEqualTo(Color.Green)
    }

    @Test
    fun `borderColor returns unfocused color as default`() {
        assertThat(colors.borderColor(focused = false, filled = false, error = false))
            .isEqualTo(Color.Gray)
    }

    @Test
    fun `borderColor prefers focused over filled`() {
        assertThat(colors.borderColor(focused = true, filled = true, error = false))
            .isEqualTo(Color.Blue)
    }

    // ── containerColor ─────────────────────────────────────────────

    @Test
    fun `containerColor returns error color when error is true`() {
        assertThat(colors.containerColor(focused = false, filled = false, error = true))
            .isEqualTo(Color(0xFFFFCDD2))
    }

    @Test
    fun `containerColor returns error color even when focused`() {
        assertThat(colors.containerColor(focused = true, filled = false, error = true))
            .isEqualTo(Color(0xFFFFCDD2))
    }

    @Test
    fun `containerColor returns focused color when focused`() {
        assertThat(colors.containerColor(focused = true, filled = false, error = false))
            .isEqualTo(Color.Cyan)
    }

    @Test
    fun `containerColor returns filled color when filled but not focused`() {
        assertThat(colors.containerColor(focused = false, filled = true, error = false))
            .isEqualTo(Color(0xFFE8F5E9))
    }

    @Test
    fun `containerColor returns unfocused color as default`() {
        assertThat(colors.containerColor(focused = false, filled = false, error = false))
            .isEqualTo(Color.White)
    }

    @Test
    fun `containerColor prefers focused over filled`() {
        assertThat(colors.containerColor(focused = true, filled = true, error = false))
            .isEqualTo(Color.Cyan)
    }
}
