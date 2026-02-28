package com.burkido.otpinputkit

import androidx.compose.foundation.text.input.TextFieldState
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Unit tests for [TextFieldState] OTP extension functions:
 * [otpValue], [isOtpComplete], [fillOtp], [clearOtp].
 */
class OtpTextFieldStateExtensionsTest {

    // ── otpValue ───────────────────────────────────────────────────

    @Test
    fun `otpValue returns empty string for new state`() {
        val state = TextFieldState()
        assertThat(state.otpValue).isEmpty()
    }

    @Test
    fun `otpValue returns current text`() {
        val state = TextFieldState(initialText = "1234")
        assertThat(state.otpValue).isEqualTo("1234")
    }

    // ── isOtpComplete ──────────────────────────────────────────────

    @Test
    fun `isOtpComplete returns false when text is shorter than length`() {
        val state = TextFieldState(initialText = "12")
        assertThat(state.isOtpComplete(6)).isFalse()
    }

    @Test
    fun `isOtpComplete returns true when text matches length`() {
        val state = TextFieldState(initialText = "123456")
        assertThat(state.isOtpComplete(6)).isTrue()
    }

    @Test
    fun `isOtpComplete returns false when text is empty`() {
        val state = TextFieldState()
        assertThat(state.isOtpComplete(6)).isFalse()
    }

    @Test
    fun `isOtpComplete returns false when text is longer than length`() {
        val state = TextFieldState(initialText = "1234567")
        assertThat(state.isOtpComplete(6)).isFalse()
    }

    // ── fillOtp ────────────────────────────────────────────────────

    @Test
    fun `fillOtp sets digits into the state`() {
        val state = TextFieldState()
        state.fillOtp("123456", 6)
        assertThat(state.otpValue).isEqualTo("123456")
    }

    @Test
    fun `fillOtp filters non-digit characters`() {
        val state = TextFieldState()
        state.fillOtp("1a2b3c", 6)
        assertThat(state.otpValue).isEqualTo("123")
    }

    @Test
    fun `fillOtp respects max length`() {
        val state = TextFieldState()
        state.fillOtp("123456789", 4)
        assertThat(state.otpValue).isEqualTo("1234")
    }

    @Test
    fun `fillOtp with empty string clears text`() {
        val state = TextFieldState(initialText = "123")
        state.fillOtp("", 6)
        assertThat(state.otpValue).isEmpty()
    }

    @Test
    fun `fillOtp replaces existing content`() {
        val state = TextFieldState(initialText = "111")
        state.fillOtp("222333", 6)
        assertThat(state.otpValue).isEqualTo("222333")
    }

    // ── clearOtp ───────────────────────────────────────────────────

    @Test
    fun `clearOtp removes all text`() {
        val state = TextFieldState(initialText = "123456")
        state.clearOtp()
        assertThat(state.otpValue).isEmpty()
    }

    @Test
    fun `clearOtp on empty state is no-op`() {
        val state = TextFieldState()
        state.clearOtp()
        assertThat(state.otpValue).isEmpty()
    }
}
