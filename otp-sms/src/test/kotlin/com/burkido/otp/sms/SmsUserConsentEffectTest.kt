package com.burkido.otp.sms

import android.app.Activity
import android.content.Intent
import android.os.Looper
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.core.app.ActivityOptionsCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf

@RunWith(AndroidJUnit4::class)
class SmsUserConsentEffectTest {

    @get:Rule
    val rule = createComposeRule()

    private val registry = RecordingResultRegistry()
    private val codes = mutableListOf<String>()
    private val errors = mutableListOf<SmsConsentError>()
    private var starts = 0
    private var restartKey by mutableIntStateOf(0)

    @Before
    fun setUp() {
        installPlayServicesConsentActivity()
    }

    private fun setContent(starter: SmsConsentStarter = SmsConsentStarter { _, _ -> starts++ }) {
        rule.setContent {
            val owner = object : ActivityResultRegistryOwner {
                override val activityResultRegistry: ActivityResultRegistry = registry
            }
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides owner) {
                SmsUserConsentEffect(
                    codeLength = 6,
                    onCodeReceive = { codes += it },
                    onError = { errors += it },
                    senderPhoneNumber = null,
                    restartKey = restartKey,
                    starter = starter,
                )
            }
        }
        rule.waitForIdle()
    }

    private fun broadcast(intent: Intent, permission: String? = SmsRetriever.SEND_PERMISSION) {
        appContext.sendBroadcast(intent, permission)
        shadowOf(Looper.getMainLooper()).idle()
        rule.waitForIdle()
    }

    private fun smsArrives() = broadcast(smsRetrievedIntent(CommonStatusCodes.SUCCESS, consentIntent()))

    @Test
    fun `shows the consent prompt when a message arrives`() {
        setContent()

        smsArrives()

        assertThat(starts).isEqualTo(1)
        assertThat(registry.launched).containsExactly(consentIntent().component)
    }

    @Test
    fun `delivers the code once the user allows access`() {
        setContent()
        smsArrives()

        registry.finishLast(Activity.RESULT_OK, Intent().putExtra(SmsRetriever.EXTRA_SMS_MESSAGE, "Code 482913"))
        rule.waitForIdle()

        assertThat(codes).containsExactly("482913")
        assertThat(errors).isEmpty()
    }

    @Test
    fun `reports a dismissed prompt`() {
        setContent()
        smsArrives()

        registry.finishLast(Activity.RESULT_CANCELED, null)
        rule.waitForIdle()

        assertThat(errors).containsExactly(SmsConsentError.ConsentDenied)
    }

    @Test
    fun `reports a failure to start`() {
        val failure = IllegalStateException("Play services missing")
        setContent(starter = { _, _ -> throw failure })

        val error = errors.single()
        assertThat(error).isInstanceOf(SmsConsentError.StartFailed::class.java)
        assertThat((error as SmsConsentError.StartFailed).cause).isSameInstanceAs(failure)
    }

    @Test
    fun `ignores a broadcast sent without the play services permission`() {
        setContent()

        broadcast(smsRetrievedIntent(CommonStatusCodes.SUCCESS, consentIntent()), permission = null)

        assertThat(registry.launched).isEmpty()
    }

    @Test
    fun `refuses to launch an untrusted intent and keeps listening`() {
        setContent()

        broadcast(smsRetrievedIntent(CommonStatusCodes.SUCCESS, consentIntent(packageName = "com.evil.app")))
        assertThat(registry.launched).isEmpty()
        assertThat(errors).containsExactly(SmsConsentError.UntrustedIntent)

        smsArrives()
        assertThat(registry.launched).hasSize(1)
    }

    @Test
    fun `stops listening after a timeout`() {
        setContent()

        broadcast(smsRetrievedIntent(CommonStatusCodes.TIMEOUT))
        smsArrives()

        assertThat(errors).containsExactly(SmsConsentError.Timeout)
        assertThat(registry.launched).isEmpty()
    }

    @Test
    fun `a new restart key starts listening again`() {
        setContent()
        broadcast(smsRetrievedIntent(CommonStatusCodes.TIMEOUT))

        restartKey++
        rule.waitForIdle()
        smsArrives()

        assertThat(starts).isEqualTo(2)
        assertThat(registry.launched).hasSize(1)
    }

    private class RecordingResultRegistry : ActivityResultRegistry() {
        private val requestCodes = mutableListOf<Int>()
        val launched = mutableListOf<Any?>()

        override fun <I, O> onLaunch(
            requestCode: Int,
            contract: ActivityResultContract<I, O>,
            input: I,
            options: ActivityOptionsCompat?,
        ) {
            requestCodes += requestCode
            launched += (input as? Intent)?.component
        }

        fun finishLast(resultCode: Int, data: Intent?) {
            dispatchResult(requestCodes.last(), resultCode, data)
        }
    }
}
