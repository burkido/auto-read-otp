package com.burkido.autoreadotp

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Tests for [extractOtpFromSms], which extracts exactly N consecutive digits
 * from an SMS body and returns null when no matching code exists.
 */
class GetVerificationCodeFromSmsTest {

    @Test
    fun `extracts 6-digit code from typical sms`() {
        val sms = "Your verification code is 123456. Do not share this code."
        val code = extractOtpFromSms(sms, 6)
        assertThat(code).isEqualTo("123456")
    }

    @Test
    fun `extracts 4-digit code from sms`() {
        val sms = "Your OTP is 9876"
        val code = extractOtpFromSms(sms, 4)
        assertThat(code).isEqualTo("9876")
    }

    @Test
    fun `returns null when sms has no digits`() {
        val sms = "Welcome to our service!"
        val code = extractOtpFromSms(sms, 6)
        assertThat(code).isNull()
    }

    @Test
    fun `returns null when sms has fewer digits than expected`() {
        val sms = "Code: 12"
        val code = extractOtpFromSms(sms, 6)
        assertThat(code).isNull()
    }

    @Test
    fun `ignores shorter digit runs before the code`() {
        val sms = "Your code expires in 5 minutes: 123456"
        val code = extractOtpFromSms(sms, 6)
        assertThat(code).isEqualTo("123456")
    }

    @Test
    fun `ignores digit runs mixed with letters that are too short`() {
        val sms = "Ref 12ab: your code is 4567"
        val code = extractOtpFromSms(sms, 4)
        assertThat(code).isEqualTo("4567")
    }

    @Test
    fun `does not match part of a longer digit run`() {
        val sms = "Order 123456789 confirmed. Code: 5544"
        val code = extractOtpFromSms(sms, 4)
        assertThat(code).isEqualTo("5544")
    }

    @Test
    fun `returns null when only longer digit runs exist`() {
        val sms = "Order 123456789 confirmed"
        val code = extractOtpFromSms(sms, 4)
        assertThat(code).isNull()
    }

    @Test
    fun `handles empty message`() {
        val code = extractOtpFromSms("", 6)
        assertThat(code).isNull()
    }

    @Test
    fun `handles zero code length`() {
        val sms = "Code: 123456"
        val code = extractOtpFromSms(sms, 0)
        assertThat(code).isNull()
    }

    @Test
    fun `handles negative code length`() {
        val code = extractOtpFromSms("Code: 123456", -1)
        assertThat(code).isNull()
    }

    @Test
    fun `handles unicode characters in message`() {
        val sms = "🔑 Your code: 567890 ✅"
        val code = extractOtpFromSms(sms, 6)
        assertThat(code).isEqualTo("567890")
    }

    @Test
    fun `extracts code at the start of the message`() {
        val sms = "123456 is your verification code"
        val code = extractOtpFromSms(sms, 6)
        assertThat(code).isEqualTo("123456")
    }

    @Test
    fun `extracts first match when multiple codes exist`() {
        val sms = "Code 1111 or 2222"
        val code = extractOtpFromSms(sms, 4)
        assertThat(code).isEqualTo("1111")
    }
}
