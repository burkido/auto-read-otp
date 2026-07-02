package com.burkido.autoreadotp

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

/**
 * Listens for an incoming OTP SMS using Google's
 * [SMS User Consent API](https://developers.google.com/identity/sms-retriever/user-consent/overview)
 * and reports the extracted code — no SMS permissions required.
 *
 * Drop this composable anywhere in your verification screen:
 * ```kotlin
 * SmsUserConsent(
 *     smsCodeLength = 6,
 *     onOtpReceived = { otp -> textFieldState.fillOtp(otp, 6) },
 *     onError = { error ->
 *         if (error is SmsConsentError.Timeout) showResendButton()
 *     },
 * )
 * ```
 *
 * The Consent API listens for a single SMS for up to 5 minutes. To listen
 * again (e.g. after the user taps "resend code"), change [retryKey]:
 * ```kotlin
 * var attempt by remember { mutableIntStateOf(0) }
 * SmsUserConsent(smsCodeLength = 6, onOtpReceived = { ... }, retryKey = attempt)
 * Button(onClick = { resendCode(); attempt++ }) { Text("Resend") }
 * ```
 *
 * @param smsCodeLength Number of consecutive digits expected in the SMS.
 * @param onOtpReceived Called with the extracted code after user consent.
 * @param onError Called with a [SmsConsentError] when the flow fails.
 * @param senderPhoneNumber Optionally restrict listening to SMS from this
 * phone number. `null` (default) listens for messages from any sender.
 * @param retryKey Changing this value restarts the listening window.
 */
@Composable
public fun SmsUserConsent(
    smsCodeLength: Int,
    onOtpReceived: (otp: String) -> Unit,
    onError: (error: SmsConsentError) -> Unit = {},
    senderPhoneNumber: String? = null,
    retryKey: Any? = Unit,
) {
    val context = LocalContext.current
    val currentOnOtpReceived by rememberUpdatedState(onOtpReceived)
    val currentOnError by rememberUpdatedState(onError)
    var listenForSms by remember { mutableStateOf(false) }

    LaunchedEffect(retryKey, senderPhoneNumber) {
        SmsRetriever
            .getClient(context)
            .startSmsUserConsent(senderPhoneNumber)
            .addOnSuccessListener { listenForSms = true }
            .addOnFailureListener { exception ->
                currentOnError(SmsConsentError.ServiceUnavailable(exception))
            }
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            listenForSms = false
            val message = result.data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
            if (result.resultCode == Activity.RESULT_OK && message != null) {
                val verificationCode = extractOtpFromSms(message, smsCodeLength)
                if (verificationCode != null) {
                    currentOnOtpReceived(verificationCode)
                } else {
                    currentOnError(SmsConsentError.OtpNotFound)
                }
            } else {
                currentOnError(SmsConsentError.ConsentDenied)
            }
        }

    if (listenForSms) {
        SystemBroadcastReceiver(
            systemAction = SmsRetriever.SMS_RETRIEVED_ACTION,
            broadcastPermission = SmsRetriever.SEND_PERMISSION,
        ) { intent ->
            if (intent?.action != SmsRetriever.SMS_RETRIEVED_ACTION) return@SystemBroadcastReceiver
            val extras = intent.extras ?: return@SystemBroadcastReceiver
            val status = extras.parcelable<Status>(SmsRetriever.EXTRA_STATUS)
                ?: return@SystemBroadcastReceiver

            when (status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val consentIntent = extras.parcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                        ?: return@SystemBroadcastReceiver
                    try {
                        // Shows the consent dialog. A timeout broadcast arrives
                        // after 5 minutes if the user never interacts with it.
                        launcher.launch(consentIntent)
                    } catch (_: ActivityNotFoundException) {
                        currentOnError(SmsConsentError.ConsentDialogNotFound)
                    }
                }

                CommonStatusCodes.TIMEOUT -> {
                    listenForSms = false
                    currentOnError(SmsConsentError.Timeout)
                }
            }
        }
    }
}

/**
 * Listens for an incoming OTP SMS using Google's SMS User Consent API.
 *
 * @param smsCodeLength Number of consecutive digits expected in the SMS.
 * @param onOTPReceived Called with the extracted code after user consent.
 * @param onError Called with a pre-localized error message.
 */
@Deprecated(
    message = "Use the overload with a typed onError: (SmsConsentError) -> Unit and " +
        "Kotlin-style naming (onOtpReceived). This overload will be removed in a future release.",
    replaceWith = ReplaceWith(
        "SmsUserConsent(smsCodeLength = smsCodeLength, onOtpReceived = onOTPReceived)",
    ),
)
@Composable
public fun SmsUserConsent(
    smsCodeLength: Int,
    onOTPReceived: (otp: String) -> Unit,
    onError: (error: String) -> Unit,
) {
    val context = LocalContext.current
    SmsUserConsent(
        smsCodeLength = smsCodeLength,
        onOtpReceived = onOTPReceived,
        onError = { error -> onError(error.toLocalizedMessage(context)) },
    )
}

private fun SmsConsentError.toLocalizedMessage(context: Context): String = when (this) {
    SmsConsentError.ConsentDenied -> context.getString(R.string.sms_retriever_error_consent_denied)
    SmsConsentError.Timeout -> context.getString(R.string.sms_timeout_error)
    SmsConsentError.ConsentDialogNotFound -> context.getString(R.string.activity_not_found_error)
    SmsConsentError.OtpNotFound -> context.getString(R.string.sms_otp_not_found_error)
    is SmsConsentError.ServiceUnavailable -> context.getString(R.string.sms_retriever_unavailable_error)
}

/**
 * Extracts the verification code from an SMS body by looking for exactly
 * [smsCodeLength] consecutive digits (not part of a longer digit run), so
 * other numbers in the message ("expires in 5 minutes") are not picked up.
 *
 * Returns `null` when no matching code is found.
 */
internal fun extractOtpFromSms(message: String, smsCodeLength: Int): String? {
    if (smsCodeLength <= 0) return null
    return Regex("(?<!\\d)\\d{$smsCodeLength}(?!\\d)").find(message)?.value
}
