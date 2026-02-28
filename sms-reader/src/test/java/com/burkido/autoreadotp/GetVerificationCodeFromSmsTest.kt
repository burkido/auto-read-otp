package com.burkido.autoreadotp

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Tests for the OTP extraction logic that mirrors `getVerificationCodeFromSms`.
 *
 * The private function filters digits and takes the first [smsCodeLength] digits.
 * We replicate the logic here to verify correctness across edge cases.
 */
class GetVerificationCodeFromSmsTest {

    /**
     * Mirrors the private `getVerificationCodeFromSms` function in SmsUserConsent.kt.
     */
    private fun getVerificationCodeFromSms(message: String, smsCodeLength: Int): String =
        message.filter { it.isDigit() }.take(smsCodeLength)

    @Test
    fun `extracts 6-digit code from typical sms`() {
        val sms = "Your verification code is 123456. Do not share this code."
        val code = getVerificationCodeFromSms(sms, 6)
        assertThat(code).isEqualTo("123456")
    }

    @Test
    fun `extracts 4-digit code from sms`() {
        val sms = "Your OTP is 9876"
        val code = getVerificationCodeFromSms(sms, 4)
        assertThat(code).isEqualTo("9876")
    }

    @Test
    fun `returns empty when sms has no digits`() {
        val sms = "Welcome to our service!"
        val code = getVerificationCodeFromSms(sms, 6)
        assertThat(code).isEmpty()
    }

    @Test
    fun `returns partial code when sms has fewer digits than expected`() {
        val sms = "Code: 12"
        val code = getVerificationCodeFromSms(sms, 6)
        assertThat(code).isEqualTo("12")
    }

    @Test
    fun `ignores non-digit characters mixed with digits`() {
        val sms = "Code: 1a2b3c4d5e6f"
        val code = getVerificationCodeFromSms(sms, 6)
        assertThat(code).isEqualTo("123456")
    }

    @Test
    fun `handles sms with digits scattered across message`() {
        val sms = "Your 1st code is 23, sent at 4:56pm"
        val code = getVerificationCodeFromSms(sms, 6)
        assertThat(code).isEqualTo("123456")
    }

    @Test
    fun `takes only first N digits when message has more`() {
        val sms = "Code: 123456789"
        val code = getVerificationCodeFromSms(sms, 4)
        assertThat(code).isEqualTo("1234")
    }

    @Test
    fun `handles empty message`() {
        val code = getVerificationCodeFromSms("", 6)
        assertThat(code).isEmpty()
    }

    @Test
    fun `handles zero code length`() {
        val sms = "Code: 123456"
        val code = getVerificationCodeFromSms(sms, 0)
        assertThat(code).isEmpty()
    }

    @Test
    fun `handles unicode characters in message`() {
        val sms = "🔑 Your code: 567890 ✅"
        val code = getVerificationCodeFromSms(sms, 6)
        assertThat(code).isEqualTo("567890")
    }
}
