package com.burkido.otp.sms

/**
 * Reasons the SMS User Consent flow can end without a code.
 *
 * After any of these, change `restartKey` on [SmsUserConsentEffect] to listen again, for example
 * when the user taps "Resend code".
 */
public sealed interface SmsConsentError {

    /** Play services refused to start listening. It's usually missing, disabled or out of date. */
    public class StartFailed(public val cause: Throwable) : SmsConsentError {
        override fun toString(): String = "StartFailed(cause=$cause)"
    }

    /** No matching SMS arrived within the five minutes Play services waits for one. */
    public data object Timeout : SmsConsentError

    /** The user closed the consent sheet without allowing access to the message. */
    public data object ConsentDenied : SmsConsentError

    /** The consent sheet couldn't be opened on this device. */
    public data object PromptUnavailable : SmsConsentError

    /**
     * A broadcast carried a consent intent that doesn't point at Play services, so it was dropped.
     * Listening continues, because the real message may still arrive.
     */
    public data object UntrustedIntent : SmsConsentError

    /** The user allowed access, but the message had no code with the expected number of digits. */
    public data object CodeNotFound : SmsConsentError
}
