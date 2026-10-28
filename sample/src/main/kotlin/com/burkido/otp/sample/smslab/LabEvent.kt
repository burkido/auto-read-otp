package com.burkido.otp.sample.smslab

import com.burkido.otp.sms.SmsConsentError
import java.util.concurrent.atomic.AtomicLong

sealed class LabEvent {
    val id: Long = nextId.incrementAndGet()
    val timeMillis: Long = System.currentTimeMillis()

    class Started(val session: Int) : LabEvent()

    class CodeReceived(val code: String) : LabEvent()

    class Failed(val error: SmsConsentError) : LabEvent()
}

private val nextId = AtomicLong()

val SmsConsentError.displayName: String
    get() = when (this) {
        is SmsConsentError.StartFailed -> "StartFailed"
        SmsConsentError.Timeout -> "Timeout"
        SmsConsentError.ConsentDenied -> "ConsentDenied"
        SmsConsentError.PromptUnavailable -> "PromptUnavailable"
        SmsConsentError.CodeNotFound -> "CodeNotFound"
        SmsConsentError.UntrustedIntent -> "UntrustedIntent"
    }

val SmsConsentError.description: String
    get() {
        val summary = ErrorReference.first { it.first == displayName }.second
        return if (this is SmsConsentError.StartFailed) "$summary ${cause.message.orEmpty()}".trim() else summary
    }

val ErrorReference: List<Pair<String, String>> = listOf(
    "StartFailed" to "Play services couldn't start listening, usually because it's missing or outdated.",
    "Timeout" to "No message arrived within five minutes.",
    "ConsentDenied" to "The user closed the consent sheet.",
    "PromptUnavailable" to "The consent sheet couldn't be shown on this device.",
    "CodeNotFound" to "The user allowed access, but the message had no code with codeLength digits.",
    "UntrustedIntent" to "A broadcast pretended to come from Play services and was ignored. Listening continues.",
)
