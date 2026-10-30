package com.burkido.otp.sample.smslab

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.burkido.otp.sample.R
import com.burkido.otp.sample.ui.ChoiceRow
import com.burkido.otp.sample.ui.CodeBlock
import com.burkido.otp.sample.ui.DemoScaffold
import com.burkido.otp.sample.ui.PulsingDot
import com.burkido.otp.sample.ui.SampleCard
import com.burkido.otp.sample.ui.SectionLabel
import com.burkido.otp.sample.ui.fittedCellWidth
import com.burkido.otp.sms.SmsConsentError
import com.burkido.otp.sms.SmsUserConsentEffect
import com.burkido.otp.ui.OtpInputDefaults
import com.burkido.otp.ui.OtpInputField
import com.burkido.otp.ui.clearOtp
import com.burkido.otp.ui.fillOtp

@Composable
fun SmsLabScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    var session by rememberSaveable { mutableIntStateOf(1) }
    var codeLength by rememberSaveable { mutableIntStateOf(6) }
    var isListening by remember { mutableStateOf(true) }
    val events = remember { mutableStateListOf<LabEvent>(LabEvent.Started(session = 1)) }
    val code = rememberTextFieldState()

    SmsUserConsentEffect(
        codeLength = codeLength,
        onCodeReceive = { received ->
            isListening = false
            code.fillOtp(received, codeLength)
            events.add(0, LabEvent.CodeReceived(received))
        },
        onError = { error ->
            if (error != SmsConsentError.UntrustedIntent) isListening = false
            events.add(0, LabEvent.Failed(error))
        },
        restartKey = session,
    )

    DemoScaffold(title = "SMS lab", onBack = onBack, modifier = modifier) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(padding),
        ) {
            item {
                Text(
                    text = "SmsUserConsentEffect listens for one SMS at a time. When it arrives, Play services asks " +
                        "the user to share it, and the first run of exactly codeLength digits is extracted. " +
                        "No SMS permission is involved.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            item {
                SessionCard(
                    session = session,
                    isListening = isListening,
                    code = code,
                    codeLength = codeLength,
                    onCodeLengthChange = {
                        code.clearOtp()
                        codeLength = it
                    },
                    onRestart = {
                        code.clearOtp()
                        session++
                        isListening = true
                        events.add(0, LabEvent.Started(session))
                    },
                )
            }
            item { TestMessageCard(codeLength = codeLength) }
            item {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                    SectionLabel(text = "Events", modifier = Modifier.weight(1f))
                    TextButton(onClick = { events.clear() }, enabled = events.isNotEmpty()) { Text("Clear") }
                }
            }
            items(events, key = LabEvent::id) { event ->
                EventRow(event = event, modifier = Modifier.animateItem())
            }
            item {
                SectionLabel(text = "Errors you can get", modifier = Modifier.padding(top = 16.dp))
            }
            item { ErrorReferenceCard() }
        }
    }
}

@Composable
private fun SessionCard(
    session: Int,
    isListening: Boolean,
    code: TextFieldState,
    codeLength: Int,
    onCodeLengthChange: (Int) -> Unit,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scheme = MaterialTheme.colorScheme
    SampleCard(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            PulsingDot(
                color = if (isListening) scheme.primary else scheme.outline,
                size = 10.dp,
                isPulsing = isListening,
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = if (isListening) "Listening for the next SMS" else "Session ended",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "Session $session · restartKey = $session",
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                    color = scheme.onSurfaceVariant,
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        SectionLabel(text = "Extracted code")
        Spacer(Modifier.height(10.dp))
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            OtpInputField(
                state = code,
                otpLength = codeLength,
                dimensions = OtpInputDefaults.dimensions(
                    cellWidth = fittedCellWidth(maxWidth, codeLength, 8.dp, preferred = 44.dp),
                    cellHeight = 52.dp,
                ),
            )
        }
        Spacer(Modifier.height(20.dp))
        SectionLabel(text = "Code length")
        Spacer(Modifier.height(10.dp))
        ChoiceRow(
            options = listOf(4, 6, 8),
            selected = codeLength,
            label = { "$it digits" },
            onSelect = onCodeLengthChange,
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = onRestart, enabled = !isListening, modifier = Modifier.fillMaxWidth()) {
            Icon(painterResource(R.drawable.ic_refresh), contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Listen again")
        }
    }
}

@Composable
private fun TestMessageCard(codeLength: Int, modifier: Modifier = Modifier) {
    val sampleCode = SAMPLE_DIGITS.take(codeLength)
    val orderNumber = if (codeLength == ORDER_NUMBER.length) "77810" else ORDER_NUMBER
    val message = "Order $orderNumber is ready. Your code is $sampleCode"
    SampleCard(modifier = modifier) {
        SectionLabel(text = "Send a test message")
        Spacer(Modifier.height(10.dp))
        Text(
            text = "On an emulator, run this on your computer:",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(10.dp))
        CodeBlock(code = "adb emu sms send 5551234 \"$message\"", language = "Shell", softWrap = true)
        Spacer(Modifier.height(10.dp))
        Text(
            text = "You can also paste the message into Extended controls › Phone, or text this device from " +
                "another phone. Only $sampleCode is extracted, because the order number isn't exactly " +
                "$codeLength digits long.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun EventRow(event: LabEvent, modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    val (title, detail, color) = when (event) {
        is LabEvent.Started -> Triple("Session ${event.session} started", "Waiting for one SMS", scheme.primary)

        is LabEvent.CodeReceived -> Triple(
            "onCodeReceive(\"${event.code}\")",
            "The user shared the message and the code was found",
            scheme.tertiary,
        )

        is LabEvent.Failed -> Triple(
            "onError(${event.error.displayName})",
            event.error.description,
            if (event.error == SmsConsentError.UntrustedIntent) scheme.onSurfaceVariant else scheme.error,
        )
    }
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            text = DateFormat.format("HH:mm:ss", event.timeMillis).toString(),
            style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
            color = scheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp),
        )
        PulsingDot(color = color, isPulsing = false, size = 6.dp, modifier = Modifier.padding(horizontal = 6.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                color = color,
            )
            Text(text = detail, style = MaterialTheme.typography.bodySmall, color = scheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ErrorReferenceCard(modifier: Modifier = Modifier) {
    SampleCard(modifier = modifier, contentPadding = PaddingValues(vertical = 8.dp)) {
        ErrorReference.forEachIndexed { index, (name, description) ->
            if (index > 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private const val SAMPLE_DIGITS = "48291375"
private const val ORDER_NUMBER = "7781"
