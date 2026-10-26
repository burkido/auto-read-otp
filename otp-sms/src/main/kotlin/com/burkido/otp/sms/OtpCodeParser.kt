package com.burkido.otp.sms

/**
 * Returns the first run of exactly [codeLength] ASCII digits in [message], or null if there isn't one.
 *
 * Digits that belong to a longer number are skipped, so "Order 7781, code 482913" gives "482913"
 * for a six digit code instead of stitching digits together from the whole message.
 */
internal fun extractOtpCode(message: String, codeLength: Int): String? {
    require(codeLength > 0) { "codeLength must be positive but was $codeLength" }
    return Regex("(?<![0-9])[0-9]{$codeLength}(?![0-9])").find(message)?.value
}
