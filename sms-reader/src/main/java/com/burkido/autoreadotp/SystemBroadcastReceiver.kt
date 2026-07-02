package com.burkido.autoreadotp

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

// https://developer.android.com/develop/ui/compose/migrate/interoperability-apis/views-in-compose#case-study-broadcastreceivers
@Composable
internal fun SystemBroadcastReceiver(
    systemAction: String,
    broadcastPermission: String? = null,
    onSystemEvent: (intent: Intent?) -> Unit,
) {
    // Grab the current context in this part of the UI tree
    val context = LocalContext.current

    // Safely use the latest onSystemEvent lambda passed to the function
    val currentOnSystemEvent by rememberUpdatedState(onSystemEvent)

    // If either context or systemAction changes, unregister and register again
    DisposableEffect(context, systemAction, broadcastPermission) {
        val intentFilter = IntentFilter(systemAction)
        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                currentOnSystemEvent(intent)
            }
        }

        // Guarding with broadcastPermission ensures only senders holding that
        // permission (e.g. Google Play services for SmsRetriever.SEND_PERMISSION)
        // can deliver intents to this receiver, even though it is exported.
        ContextCompat.registerReceiver(
            context,
            broadcast,
            intentFilter,
            broadcastPermission,
            null,
            ContextCompat.RECEIVER_EXPORTED,
        )

        // When the effect leaves the Composition, remove the callback
        onDispose {
            context.unregisterReceiver(broadcast)
        }
    }
}
