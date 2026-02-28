package com.burkido.verificationcodereader

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Unit tests for the app module's OTP helper / extension logic.
 */
class OtpTextFieldValueTest {

    /**
     * Mirrors the filtering logic used in [OtpTextField]'s onValueChange.
     * Only values up to [length] characters are accepted.
     */
    private fun filterOtpInput(text: String, length: Int): String =
        if (text.length <= length) text else text.take(length)

    @Test
    fun `accepts value within length limit`() {
        assertThat(filterOtpInput("1234", 6)).isEqualTo("1234")
    }

    @Test
    fun `rejects value exceeding length limit`() {
        assertThat(filterOtpInput("1234567", 6)).isEqualTo("123456")
    }

    @Test
    fun `accepts empty string`() {
        assertThat(filterOtpInput("", 6)).isEmpty()
    }

    @Test
    fun `accepts exactly length`() {
        assertThat(filterOtpInput("123456", 6)).isEqualTo("123456")
    }
}