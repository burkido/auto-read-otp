package com.burkido.otpinputkit.entry.style

import androidx.compose.runtime.Immutable

/**
 * Configuration for masking (obscuring) characters in an OTP cell.
 *
 * When [isMasked] is true, each entered digit is shown briefly for [maskDelay]
 * milliseconds and then replaced by [maskCharacter] — mirroring the UX found
 * in native iOS/Android secure OTP fields.
 *
 * @param isMasked Whether to mask entered characters.
 * @param maskCharacter The character displayed once masking kicks in.
 * @param maskDelay How long (in ms) a digit stays visible before being masked.
 */
@Immutable
data class OtpMaskConfig(
    val isMasked: Boolean,
    val maskCharacter: Char,
    val maskDelay: Long,
)
