package com.burkido.autoreadotp

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

@Composable
fun SmsUserConsent(
    smsCodeLength: Int,
    onOTPReceived: (otp: String) -> Unit,
    onError: (error: String) -> Unit,
) {
    val context = LocalContext.current
    var shouldRegisterReceiver by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        SmsRetriever
            .getClient(context)
            .startSmsUserConsent(null)
            .addOnSuccessListener {
                shouldRegisterReceiver = true
            }
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                val message: String? = it.data!!.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                message?.let {
                    val verificationCode = getVerificationCodeFromSms(message, smsCodeLength)
                    onOTPReceived(verificationCode)
                }
                shouldRegisterReceiver = false
            } else {
                onError(context.getString(R.string.sms_retriever_error_consent_denied))
            }
        }

    if (shouldRegisterReceiver) {
        SystemBroadcastReceiver(systemAction = SmsRetriever.SMS_RETRIEVED_ACTION) { intent ->
            if (intent != null && SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras
                val smsRetrieverStatus = extras?.parcelable<Status>(SmsRetriever.EXTRA_STATUS) as Status

                when (smsRetrieverStatus.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val consentIntent = extras.parcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)

                        try {
                            // Start consent dialog. Timeout intent will be sent after 5 minutes
                            consentIntent?.let { launcher.launch(it) }
                        } catch (e: ActivityNotFoundException) {
                            onError(context.getString(R.string.activity_not_found_error))
                        }
                    }
                    CommonStatusCodes.TIMEOUT -> onError(context.getString(R.string.sms_timeout_error))
                }
            }
        }
    }
}

private fun getVerificationCodeFromSms(message: String, smsCodeLength: Int): String =
    message.filter { it.isDigit() }.take(smsCodeLength)