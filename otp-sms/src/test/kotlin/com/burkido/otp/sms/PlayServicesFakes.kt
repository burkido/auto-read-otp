package com.burkido.otp.sms

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.Status
import org.robolectric.Shadows.shadowOf

internal val appContext: Context get() = ApplicationProvider.getApplicationContext()

/** Makes the real consent activity resolvable, as it would be on a device with Play services. */
internal fun installPlayServicesConsentActivity() {
    shadowOf(appContext.packageManager).addActivityIfNotPresent(ComponentName(PLAY_SERVICES_PACKAGE, CONSENT_ACTIVITY))
}

internal fun consentIntent(packageName: String = PLAY_SERVICES_PACKAGE, className: String = CONSENT_ACTIVITY): Intent =
    Intent().setComponent(ComponentName(packageName, className))

internal fun smsRetrievedIntent(statusCode: Int, consentIntent: Intent? = null): Intent =
    Intent(SmsRetriever.SMS_RETRIEVED_ACTION).apply {
        putExtra(SmsRetriever.EXTRA_STATUS, Status(statusCode))
        if (consentIntent != null) putExtra(SmsRetriever.EXTRA_CONSENT_INTENT, consentIntent)
    }
