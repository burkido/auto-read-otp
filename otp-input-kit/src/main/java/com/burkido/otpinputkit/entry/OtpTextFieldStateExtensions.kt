package com.burkido.otpinputkit.entry

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.placeCursorAtEnd

/**
 * Extension functions on [TextFieldState] to make it easy to work with OTP flows.
 *
 * These are useful when combining the OTP input kit with the SMS reader library:
 * ```kotlin
 * val textFieldState = rememberTextFieldState()
 *
 * SmsUserConsent(smsCodeLength = 6,
 *     onOTPReceived = { otp -> textFieldState.fillOtp(otp, 6) },
 *     onError = { }
 * )
 *
 * OtpInputField(
 *     textFieldState = textFieldState,
 *     otpLength = 6,
 *     onComplete = { verifyOtp(it) }
 * )
 * ```
 */

/** The current OTP value as a plain [String]. */
val TextFieldState.otpValue: String
    get() = text.toString()

/** Whether the OTP is complete (all digits filled). */
fun TextFieldState.isOtpComplete(length: Int): Boolean =
    text.length == length

/**
 * Fills the text field with the given OTP string.
 * Filters to digits only and limits to [length] characters.
 */
fun TextFieldState.fillOtp(otp: String, length: Int) {
    val filtered = otp.filter { it.isDigit() }.take(length)
    edit {
        replace(0, this.length, filtered)
        placeCursorAtEnd()
    }
}

/** Clears the OTP text field. */
fun TextFieldState.clearOtp() {
    clearText()
}
