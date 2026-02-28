package com.burkido.otpinputkit

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [OtpInputState].
 *
 * Covers: initial state, onValueChange filtering, focusedIndex tracking,
 * isComplete, clear, and fill.
 */
class OtpInputStateTest {

    private lateinit var state: OtpInputState

    @Before
    fun setUp() {
        state = OtpInputState(otpLength = 6)
    }

    // ── Initial state ──────────────────────────────────────────────

    @Test
    fun `initial otpValue is empty`() {
        assertThat(state.otpValue).isEmpty()
    }

    @Test
    fun `initial focusedIndex is 0`() {
        assertThat(state.focusedIndex).isEqualTo(0)
    }

    @Test
    fun `initial isComplete is false`() {
        assertThat(state.isComplete).isFalse()
    }

    @Test
    fun `initial state with pre-filled value`() {
        val prefilledState = OtpInputState(otpLength = 6, initialValue = "123")
        assertThat(prefilledState.otpValue).isEqualTo("123")
        assertThat(prefilledState.focusedIndex).isEqualTo(3)
        assertThat(prefilledState.isComplete).isFalse()
    }

    @Test
    fun `initial state with full pre-filled value`() {
        val fullState = OtpInputState(otpLength = 4, initialValue = "1234")
        assertThat(fullState.otpValue).isEqualTo("1234")
        assertThat(fullState.focusedIndex).isEqualTo(3) // coerced to otpLength - 1
        assertThat(fullState.isComplete).isTrue()
    }

    // ── onValueChange ──────────────────────────────────────────────

    @Test
    fun `onValueChange accepts digits`() {
        state.onValueChange("1")
        assertThat(state.otpValue).isEqualTo("1")
    }

    @Test
    fun `onValueChange filters non-digit characters`() {
        state.onValueChange("1a2b3c")
        assertThat(state.otpValue).isEqualTo("123")
    }

    @Test
    fun `onValueChange enforces max length`() {
        state.onValueChange("12345678")
        assertThat(state.otpValue).isEqualTo("123456")
    }

    @Test
    fun `onValueChange updates focusedIndex`() {
        state.onValueChange("12")
        assertThat(state.focusedIndex).isEqualTo(2)
    }

    @Test
    fun `onValueChange focusedIndex coerced to max index when full`() {
        state.onValueChange("123456")
        assertThat(state.focusedIndex).isEqualTo(5)
    }

    @Test
    fun `onValueChange with empty string resets focusedIndex to 0`() {
        state.onValueChange("123")
        state.onValueChange("")
        assertThat(state.focusedIndex).isEqualTo(0)
    }

    @Test
    fun `isComplete becomes true when all digits entered`() {
        state.onValueChange("123456")
        assertThat(state.isComplete).isTrue()
    }

    @Test
    fun `isComplete remains false when not all digits entered`() {
        state.onValueChange("12345")
        assertThat(state.isComplete).isFalse()
    }

    @Test
    fun `onValueChange filters letters only`() {
        state.onValueChange("abcdef")
        assertThat(state.otpValue).isEmpty()
    }

    @Test
    fun `onValueChange filters special characters`() {
        state.onValueChange("!@#$%^")
        assertThat(state.otpValue).isEmpty()
    }

    // ── clear ──────────────────────────────────────────────────────

    @Test
    fun `clear resets otpValue`() {
        state.onValueChange("123")
        state.clear()
        assertThat(state.otpValue).isEmpty()
    }

    @Test
    fun `clear resets focusedIndex to 0`() {
        state.onValueChange("123")
        state.clear()
        assertThat(state.focusedIndex).isEqualTo(0)
    }

    // ── fill ───────────────────────────────────────────────────────

    @Test
    fun `fill sets otp value`() {
        state.fill("654321")
        assertThat(state.otpValue).isEqualTo("654321")
    }

    @Test
    fun `fill filters non-digits`() {
        state.fill("6a5b4c")
        assertThat(state.otpValue).isEqualTo("654")
    }

    @Test
    fun `fill enforces max length`() {
        state.fill("12345678")
        assertThat(state.otpValue).isEqualTo("123456")
    }

    @Test
    fun `fill updates focusedIndex`() {
        state.fill("123")
        assertThat(state.focusedIndex).isEqualTo(3)
    }

    @Test
    fun `fill with empty string resets state`() {
        state.fill("123")
        state.fill("")
        assertThat(state.otpValue).isEmpty()
        assertThat(state.focusedIndex).isEqualTo(0)
    }

    // ── Custom otp length ──────────────────────────────────────────

    @Test
    fun `custom otpLength of 4 enforces limit`() {
        val shortState = OtpInputState(otpLength = 4)
        shortState.onValueChange("123456")
        assertThat(shortState.otpValue).isEqualTo("1234")
        assertThat(shortState.isComplete).isTrue()
    }

    @Test
    fun `custom otpLength of 8 allows longer codes`() {
        val longState = OtpInputState(otpLength = 8)
        longState.onValueChange("12345678")
        assertThat(longState.otpValue).isEqualTo("12345678")
        assertThat(longState.isComplete).isTrue()
    }
}
