package com.burkido.otp.input

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class OtpDigitsTest {

    @Test
    fun `keeps digits and drops everything else`() {
        assertThat(sanitizeOtp("Code: 123-456", 6)).isEqualTo("123456")
    }

    @Test
    fun `stops at the requested length`() {
        assertThat(sanitizeOtp("12345678", 4)).isEqualTo("1234")
    }

    @Test
    fun `keeps a partial code as it is`() {
        assertThat(sanitizeOtp("12", 6)).isEqualTo("12")
    }

    @Test
    fun `drops unicode digits that are not ascii`() {
        assertThat(sanitizeOtp("١٢٣ 4", 6)).isEqualTo("4")
    }

    @Test
    fun `returns empty text when there are no digits`() {
        assertThat(sanitizeOtp("no digits here", 6)).isEmpty()
    }
}
