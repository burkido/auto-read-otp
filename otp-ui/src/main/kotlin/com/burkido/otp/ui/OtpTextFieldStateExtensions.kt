package com.burkido.otp.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.placeCursorAtEnd

/** The current text as a plain String. */
public val TextFieldState.otpValue: String
    get() = text.toString()

/** True once the field holds [length] characters. */
public fun TextFieldState.isOtpComplete(length: Int): Boolean = text.length == length

/**
 * Replaces the content with the first [length] digits of [otp], for example a code read from SMS.
 *
 * ```kotlin
 * SmsUserConsentEffect(
 *     codeLength = 6,
 *     onCodeReceive = { code -> otpState.fillOtp(code, 6) },
 *     onError = {},
 * )
 * ```
 */
public fun TextFieldState.fillOtp(otp: String, length: Int) {
    val sanitized = sanitizeOtp(otp, length)
    edit {
        replace(0, this.length, sanitized)
        placeCursorAtEnd()
    }
}

public fun TextFieldState.clearOtp() {
    clearText()
}
