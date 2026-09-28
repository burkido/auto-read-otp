package com.burkido.otp.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.phone.SmsRetriever

@Composable
internal fun SmsRetrievedReceiver(onReceive: (Intent) -> Unit) {
    val context = LocalContext.current
    val currentOnReceive by rememberUpdatedState(onReceive)

    DisposableEffect(context) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                currentOnReceive(intent)
            }
        }

        // Play services sends this from its own process, so the receiver has to be exported.
        // Requiring its send permission is what stops other apps from faking the broadcast.
        ContextCompat.registerReceiver(
            context,
            receiver,
            IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION),
            SmsRetriever.SEND_PERMISSION,
            null,
            ContextCompat.RECEIVER_EXPORTED,
        )

        onDispose { context.unregisterReceiver(receiver) }
    }
}
