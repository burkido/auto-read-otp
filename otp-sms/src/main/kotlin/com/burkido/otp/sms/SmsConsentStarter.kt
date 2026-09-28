package com.burkido.otp.sms

import android.content.Context
import com.google.android.gms.auth.api.phone.SmsRetriever
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal fun interface SmsConsentStarter {
    suspend fun start(context: Context, senderPhoneNumber: String?)
}

internal object PlayServicesConsentStarter : SmsConsentStarter {
    override suspend fun start(context: Context, senderPhoneNumber: String?) {
        suspendCancellableCoroutine { continuation ->
            SmsRetriever.getClient(context)
                .startSmsUserConsent(senderPhoneNumber)
                .addOnSuccessListener { continuation.resume(Unit) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }
}
