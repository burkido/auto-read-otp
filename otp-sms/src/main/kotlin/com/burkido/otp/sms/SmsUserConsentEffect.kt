package com.burkido.otp.sms

import android.content.ActivityNotFoundException
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
import kotlinx.coroutines.CancellationException

/**
 * Reads a one-time code from the next incoming SMS using Google's SMS User Consent API.
 *
 * When a message arrives, Play services shows a sheet asking the user to share it. If they agree,
 * the first run of [codeLength] digits is passed to [onCodeReceived]. No SMS permission is needed.
 *
 * The flow is one shot: after a code, an error or a five minute timeout it stops listening.
 * Change [restartKey] to start again, for example after the user asks for a new code.
 *
 * ```kotlin
 * var attempt by remember { mutableIntStateOf(0) }
 *
 * SmsUserConsentEffect(
 *     codeLength = 6,
 *     onCodeReceived = { code -> otpState.fillOtp(code, 6) },
 *     onError = { error -> Log.w("Otp", "SMS consent ended: $error") },
 *     restartKey = attempt,
 * )
 * ```
 *
 * @param codeLength Number of digits in the code. Must be positive.
 * @param onCodeReceived Called with the extracted code.
 * @param onError Called when the flow ends without a code. See [SmsConsentError].
 * @param senderPhoneNumber Only react to messages from this number. Null accepts any sender.
 * @param restartKey Any value. A new value starts a fresh listening session.
 */
@Composable
public fun SmsUserConsentEffect(
    codeLength: Int,
    onCodeReceived: (code: String) -> Unit,
    onError: (error: SmsConsentError) -> Unit,
    senderPhoneNumber: String? = null,
    restartKey: Any? = Unit,
) {
    SmsUserConsentEffect(
        codeLength = codeLength,
        onCodeReceived = onCodeReceived,
        onError = onError,
        senderPhoneNumber = senderPhoneNumber,
        restartKey = restartKey,
        starter = PlayServicesConsentStarter,
    )
}

// Play services reports start failures as plain Exceptions, so there's nothing narrower to catch.
@Suppress("TooGenericExceptionCaught")
@Composable
internal fun SmsUserConsentEffect(
    codeLength: Int,
    onCodeReceived: (code: String) -> Unit,
    onError: (error: SmsConsentError) -> Unit,
    senderPhoneNumber: String?,
    restartKey: Any?,
    starter: SmsConsentStarter,
) {
    require(codeLength > 0) { "codeLength must be positive but was $codeLength" }

    val context = LocalContext.current
    val currentCodeLength by rememberUpdatedState(codeLength)
    val currentOnCodeReceived by rememberUpdatedState(onCodeReceived)
    val currentOnError by rememberUpdatedState(onError)
    var isListening by remember(restartKey, senderPhoneNumber) { mutableStateOf(false) }

    fun dispatch(event: ConsentEvent) {
        when (event) {
            is ConsentEvent.CodeReceived -> currentOnCodeReceived(event.code)
            is ConsentEvent.Failed -> currentOnError(event.error)
            is ConsentEvent.ShowPrompt, ConsentEvent.Ignored -> Unit
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        dispatch(readConsentResult(result.resultCode, result.data, currentCodeLength))
    }

    LaunchedEffect(restartKey, senderPhoneNumber) {
        try {
            starter.start(context, senderPhoneNumber)
            isListening = true
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            currentOnError(SmsConsentError.StartFailed(e))
        }
    }

    if (isListening) {
        SmsRetrievedReceiver { intent ->
            when (val event = readSmsRetrievedBroadcast(intent, context.packageManager)) {
                is ConsentEvent.ShowPrompt -> {
                    // Play services delivers a single message per start call, so this session is done.
                    isListening = false
                    try {
                        launcher.launch(event.intent)
                    } catch (e: ActivityNotFoundException) {
                        currentOnError(SmsConsentError.PromptUnavailable)
                    }
                }
                is ConsentEvent.Failed -> {
                    if (event.error != SmsConsentError.UntrustedIntent) isListening = false
                    dispatch(event)
                }
                else -> dispatch(event)
            }
        }
    }
}
