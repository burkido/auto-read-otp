package com.burkido.otp.sms

import android.app.Activity
import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConsentEventTest {

    private val packageManager get() = appContext.packageManager

    @Before
    fun setUp() {
        installPlayServicesConsentActivity()
    }

    @Test
    fun `a trusted consent intent asks for the prompt`() {
        val consent = consentIntent()

        val event = readSmsRetrievedBroadcast(smsRetrievedIntent(CommonStatusCodes.SUCCESS, consent), packageManager)

        assertThat(event).isEqualTo(ConsentEvent.ShowPrompt(consent))
    }

    @Test
    fun `an untrusted consent intent is reported`() {
        val intent = smsRetrievedIntent(CommonStatusCodes.SUCCESS, consentIntent(packageName = "com.evil.app"))

        val event = readSmsRetrievedBroadcast(intent, packageManager)

        assertThat(event).isEqualTo(ConsentEvent.Failed(SmsConsentError.UntrustedIntent))
    }

    @Test
    fun `a timeout is reported`() {
        val event = readSmsRetrievedBroadcast(smsRetrievedIntent(CommonStatusCodes.TIMEOUT), packageManager)

        assertThat(event).isEqualTo(ConsentEvent.Failed(SmsConsentError.Timeout))
    }

    @Test
    fun `broadcasts missing what we need are ignored`() {
        val noConsentIntent = smsRetrievedIntent(CommonStatusCodes.SUCCESS)
        val noStatus = Intent(SmsRetriever.SMS_RETRIEVED_ACTION)
        val otherStatus = smsRetrievedIntent(CommonStatusCodes.NETWORK_ERROR)
        val otherAction = Intent("com.example.OTHER").putExtras(smsRetrievedIntent(CommonStatusCodes.TIMEOUT))

        listOf(noConsentIntent, noStatus, otherStatus, otherAction).forEach { intent ->
            assertThat(readSmsRetrievedBroadcast(intent, packageManager)).isEqualTo(ConsentEvent.Ignored)
        }
    }

    @Test
    fun `an allowed message with a code gives the code`() {
        val data = Intent().putExtra(SmsRetriever.EXTRA_SMS_MESSAGE, "Order #77, your code is 482913")

        val event = readConsentResult(Activity.RESULT_OK, data, codeLength = 6)

        assertThat(event).isEqualTo(ConsentEvent.CodeReceived("482913"))
    }

    @Test
    fun `an allowed message without a code is reported`() {
        val data = Intent().putExtra(SmsRetriever.EXTRA_SMS_MESSAGE, "Welcome aboard!")

        assertThat(readConsentResult(Activity.RESULT_OK, data, codeLength = 6))
            .isEqualTo(ConsentEvent.Failed(SmsConsentError.CodeNotFound))
        assertThat(readConsentResult(Activity.RESULT_OK, data = null, codeLength = 6))
            .isEqualTo(ConsentEvent.Failed(SmsConsentError.CodeNotFound))
    }

    @Test
    fun `a dismissed prompt is reported as denied`() {
        val event = readConsentResult(Activity.RESULT_CANCELED, data = null, codeLength = 6)

        assertThat(event).isEqualTo(ConsentEvent.Failed(SmsConsentError.ConsentDenied))
    }
}
