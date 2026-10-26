package com.burkido.otp.sms

import android.content.ComponentName
import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf

@RunWith(AndroidJUnit4::class)
class ConsentIntentValidatorTest {

    private val packageManager get() = appContext.packageManager

    @Before
    fun setUp() {
        installPlayServicesConsentActivity()
    }

    @Test
    fun `accepts the play services consent activity`() {
        assertThat(consentIntent().isTrustedConsentIntent(packageManager)).isTrue()
    }

    @Test
    fun `rejects an intent that opens another app`() {
        shadowOf(packageManager).addActivityIfNotPresent(ComponentName("com.evil.app", "com.evil.app.Steal"))

        val intent = consentIntent(packageName = "com.evil.app", className = "com.evil.app.Steal")

        assertThat(intent.isTrustedConsentIntent(packageManager)).isFalse()
    }

    @Test
    fun `rejects another activity inside play services`() {
        val otherActivity = "com.google.android.gms.SomethingElse"
        shadowOf(packageManager).addActivityIfNotPresent(ComponentName(PLAY_SERVICES_PACKAGE, otherActivity))

        val intent = consentIntent(className = otherActivity)

        assertThat(intent.isTrustedConsentIntent(packageManager)).isFalse()
    }

    @Test
    fun `rejects an intent nothing can handle`() {
        val intent = Intent("com.example.NOTHING_HANDLES_THIS")

        assertThat(intent.isTrustedConsentIntent(packageManager)).isFalse()
    }

    @Test
    fun `rejects read uri grants`() {
        val intent = consentIntent().addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        assertThat(intent.isTrustedConsentIntent(packageManager)).isFalse()
    }

    @Test
    fun `rejects write uri grants`() {
        val intent = consentIntent().addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

        assertThat(intent.isTrustedConsentIntent(packageManager)).isFalse()
    }
}
