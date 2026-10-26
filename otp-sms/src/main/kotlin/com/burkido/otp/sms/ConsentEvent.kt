package com.burkido.otp.sms

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

internal sealed interface ConsentEvent {
    data class ShowPrompt(val intent: Intent) : ConsentEvent
    data class CodeReceived(val code: String) : ConsentEvent
    data class Failed(val error: SmsConsentError) : ConsentEvent
    data object Ignored : ConsentEvent
}

internal fun readSmsRetrievedBroadcast(intent: Intent, packageManager: PackageManager): ConsentEvent {
    if (intent.action != SmsRetriever.SMS_RETRIEVED_ACTION) return ConsentEvent.Ignored
    val extras = intent.extras ?: return ConsentEvent.Ignored
    val status = extras.parcelable<Status>(SmsRetriever.EXTRA_STATUS) ?: return ConsentEvent.Ignored

    return when (status.statusCode) {
        CommonStatusCodes.SUCCESS -> {
            val consentIntent = extras.parcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
            when {
                consentIntent == null -> ConsentEvent.Ignored
                consentIntent.isTrustedConsentIntent(packageManager) -> ConsentEvent.ShowPrompt(consentIntent)
                else -> ConsentEvent.Failed(SmsConsentError.UntrustedIntent)
            }
        }

        CommonStatusCodes.TIMEOUT -> ConsentEvent.Failed(SmsConsentError.Timeout)

        else -> ConsentEvent.Ignored
    }
}

internal fun readConsentResult(resultCode: Int, data: Intent?, codeLength: Int): ConsentEvent {
    if (resultCode != Activity.RESULT_OK) return ConsentEvent.Failed(SmsConsentError.ConsentDenied)
    val code = data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)?.let { extractOtpCode(it, codeLength) }
    return if (code != null) ConsentEvent.CodeReceived(code) else ConsentEvent.Failed(SmsConsentError.CodeNotFound)
}
