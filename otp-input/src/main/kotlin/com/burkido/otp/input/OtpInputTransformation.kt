package com.burkido.otp.input

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.placeCursorAtEnd

/**
 * Keeps a text field to at most [otpLength] ASCII digits.
 *
 * Anything else in an edit is dropped instead of rejecting the whole edit, so pasting
 * "Code: 123-456" leaves "123456". The cursor always ends up after the last digit.
 * [OtpInputField] applies this for you. Use it directly only with your own BasicTextField.
 */
public class OtpInputTransformation(private val otpLength: Int) : InputTransformation {

    init {
        require(otpLength > 0) { "otpLength must be positive but was $otpLength" }
    }

    override fun TextFieldBuffer.transformInput() {
        val sanitized = sanitizeOtp(asCharSequence(), otpLength)
        if (!asCharSequence().contentEquals(sanitized)) {
            replace(0, length, sanitized)
        }
        placeCursorAtEnd()
    }

    override fun equals(other: Any?): Boolean = other is OtpInputTransformation && other.otpLength == otpLength

    override fun hashCode(): Int = otpLength

    override fun toString(): String = "OtpInputTransformation(otpLength=$otpLength)"
}
