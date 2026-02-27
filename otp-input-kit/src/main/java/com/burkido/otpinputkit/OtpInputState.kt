package com.burkido.otpinputkit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * State holder for the **value-based** OTP input API.
 *
 * If you prefer the modern TextFieldState-based API, use the `OtpInputField`
 * overload that accepts a `TextFieldState` instead.
 *
 * Create via [rememberOtpInputState].
 */
@Stable
class OtpInputState(
    val otpLength: Int = 6,
    initialValue: String = "",
) {
    /** Current OTP text. */
    var otpValue by mutableStateOf(initialValue)
        private set

    /** Index of the currently focused cell. */
    var focusedIndex by mutableIntStateOf(initialValue.length.coerceAtMost(otpLength - 1))
        private set

    /** Whether all cells have been filled. */
    val isComplete: Boolean
        get() = otpValue.length == otpLength

    /**
     * Call this from the text field's onValueChange callback.
     * Filters to digits only and enforces the length limit.
     */
    fun onValueChange(value: String) {
        val filtered = value.filter { it.isDigit() }.take(otpLength)
        otpValue = filtered
        focusedIndex = filtered.length.coerceAtMost(otpLength - 1)
    }

    /** Clears the OTP value and resets focus to the first cell. */
    fun clear() {
        otpValue = ""
        focusedIndex = 0
    }

    /** Fills the OTP with the given string (e.g. from SMS auto-read). */
    fun fill(otp: String) {
        val filtered = otp.filter { it.isDigit() }.take(otpLength)
        otpValue = filtered
        focusedIndex = filtered.length.coerceAtMost(otpLength - 1)
    }
}

/**
 * Creates and remembers an [OtpInputState].
 *
 * @param otpLength Number of OTP digits (default: 6).
 * @param initialValue Optional initial value to pre-fill.
 */
@Composable
fun rememberOtpInputState(
    otpLength: Int = 6,
    initialValue: String = "",
): OtpInputState = remember {
    OtpInputState(otpLength = otpLength, initialValue = initialValue)
}
