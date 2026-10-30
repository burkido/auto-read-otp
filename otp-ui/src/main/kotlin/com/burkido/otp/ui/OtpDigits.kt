package com.burkido.otp.ui

/**
 * Keeps the first [length] ASCII digits of [input] and drops everything else.
 *
 * Char.isDigit() would also accept Arabic-Indic and other Unicode digits, which most backends
 * reject, so the check is limited to '0'..'9' on purpose.
 */
internal fun sanitizeOtp(input: CharSequence, length: Int): String = buildString(length) {
    for (char in input) {
        if (this.length == length) break
        if (char in '0'..'9') append(char)
    }
}
