package com.burkido.otp.sample.signin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun PhoneStep(phone: String, onPhoneChange: (String) -> Unit, onCodeSend: () -> Unit, modifier: Modifier = Modifier) {
    var isSending by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val canSend = phone.count(Char::isDigit) >= MIN_PHONE_DIGITS && !isSending
    val send: () -> Unit = {
        scope.launch {
            isSending = true
            DemoAuthServer.sendCode()
            isSending = false
            onCodeSend()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
    ) {
        StepIndicator(step = 1, total = 2)
        Spacer(Modifier.height(24.dp))
        Text(text = "What's your number?", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "We'll text you a ${DemoAuthServer.CODE_LENGTH}-digit code to make sure it's you.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(32.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = { value ->
                onPhoneChange(
                    value.filter { it.isDigit() || it == ' ' }.take(MAX_PHONE_LENGTH),
                )
            },
            label = { Text("Phone number") },
            prefix = { Text("+1 ") },
            singleLine = true,
            enabled = !isSending,
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { if (canSend) send() }),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = send,
            enabled = canSend,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            if (isSending) {
                CircularProgressIndicator(
                    color = LocalContentColor.current,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp),
                )
            } else {
                Text("Send code")
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Nothing is actually sent. The next screen shows the demo code and lets you simulate the SMS, " +
                "or you can send a real one to this device.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private const val MIN_PHONE_DIGITS = 7
private const val MAX_PHONE_LENGTH = 16
