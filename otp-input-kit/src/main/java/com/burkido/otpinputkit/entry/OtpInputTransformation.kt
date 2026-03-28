package com.burkido.otpinputkit.entry

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.then
import androidx.core.text.isDigitsOnly

/**
 * An [InputTransformation] that restricts input to digits only
 * and enforces a maximum length. Used internally by the state-based
 * [OtpInputField] overload.
 *
 * Developers can also use this directly:
 * ```kotlin
 * BasicTextField(
 *     state = textFieldState,
 *     inputTransformation = OtpInputTransformation(otpLength = 6)
 * )
 * ```
 */
class OtpInputTransformation(
    otpLength: Int,
) : InputTransformation {

    private val maxLengthTransformation = InputTransformation.maxLength(otpLength)

    override fun TextFieldBuffer.transformInput() {
        // First apply max length
        with(maxLengthTransformation) { transformInput() }

        // Then filter non-digits
        if (!asCharSequence().isDigitsOnly()) {
            revertAllChanges()
        }
    }
}
