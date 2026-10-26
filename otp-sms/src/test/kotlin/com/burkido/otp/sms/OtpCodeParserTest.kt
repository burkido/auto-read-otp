package com.burkido.otp.sms

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class OtpCodeParserTest {

    @Test
    fun `finds the code in a typical message`() {
        assertThat(extractOtpCode("Your verification code is 482913. Don't share it.", 6)).isEqualTo("482913")
    }

    @Test
    fun `skips other numbers in the message instead of joining them`() {
        assertThat(extractOtpCode("Order #77, your code is 482913", 6)).isEqualTo("482913")
    }

    @Test
    fun `ignores numbers longer than the code`() {
        assertThat(extractOtpCode("Call 0212555443322 or use 482913", 6)).isEqualTo("482913")
    }

    @Test
    fun `finds a code glued to letters and punctuation`() {
        assertThat(extractOtpCode("G-482913 is your code", 6)).isEqualTo("482913")
    }

    @Test
    fun `returns the first match when there are several`() {
        assertThat(extractOtpCode("Use 1111 or 2222", 4)).isEqualTo("1111")
    }

    @Test
    fun `returns null when only a shorter number is present`() {
        assertThat(extractOtpCode("Code: 1234", 6)).isNull()
    }

    @Test
    fun `returns null when only a longer number is present`() {
        assertThat(extractOtpCode("Code: 12345678", 6)).isNull()
    }

    @Test
    fun `returns null when there are no digits`() {
        assertThat(extractOtpCode("Welcome aboard!", 6)).isNull()
    }

    @Test
    fun `does not treat unicode digits as a code`() {
        assertThat(extractOtpCode("Code ٤٨٢٩١٣", 6)).isNull()
    }

    @Test
    fun `rejects a non positive length`() {
        assertThrows(IllegalArgumentException::class.java) { extractOtpCode("123456", 0) }
    }
}
