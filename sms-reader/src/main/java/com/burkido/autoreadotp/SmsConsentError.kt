package com.burkido.autoreadotp

/**
 * Errors that can occur during the SMS User Consent flow.
 *
 * A typed error surface lets consumers react programmatically (e.g. show a
 * "resend code" button on [Timeout]) and localize messages themselves instead
 * of receiving pre-localized strings from the library.
 */
public sealed interface SmsConsentError {

    /** The user dismissed or denied the SMS consent dialog. */
    public data object ConsentDenied : SmsConsentError

    /** No matching SMS arrived within the 5-minute window of the Consent API. */
    public data object Timeout : SmsConsentError

    /** The consent dialog activity could not be started on this device. */
    public data object ConsentDialogNotFound : SmsConsentError

    /**
     * An SMS was received and consent was granted, but no code with the
     * requested length was found in the message body.
     */
    public data object OtpNotFound : SmsConsentError

    /**
     * The SMS User Consent API could not be started, e.g. because Google Play
     * services is unavailable or outdated on this device.
     */
    public data class ServiceUnavailable(public val cause: Throwable?) : SmsConsentError
}
