package com.burkido.otp.sample.signin

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.burkido.otp.input.OtpInputDefaults
import com.burkido.otp.input.OtpInputField
import com.burkido.otp.input.clearOtp
import com.burkido.otp.input.fillOtp
import com.burkido.otp.input.isOtpComplete
import com.burkido.otp.input.otpValue
import com.burkido.otp.sample.R
import com.burkido.otp.sample.signin.DemoAuthServer.CODE_LENGTH
import com.burkido.otp.sample.ui.PulsingDot
import com.burkido.otp.sample.ui.fittedCellWidth
import com.burkido.otp.sms.SmsConsentError
import com.burkido.otp.sms.SmsUserConsentEffect
import com.burkido.otp.timer.CountdownTimer
import com.burkido.otp.timer.CountdownTimerDefaults
import com.burkido.otp.timer.CountdownTimerState
import com.burkido.otp.timer.rememberCountdownTimerState
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

private sealed interface SmsStatus {
    data object Listening : SmsStatus
    data object Received : SmsStatus
    data class Ended(val error: SmsConsentError) : SmsStatus
}

@Composable
fun VerifyStep(
    phone: String,
    expectedCode: String,
    expiresAt: Long,
    attempt: Int,
    onResend: () -> Unit,
    onFastForward: () -> Unit,
    onNumberEdit: () -> Unit,
    onVerify: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val code = rememberTextFieldState()
    val timer = rememberCountdownTimerState(endTimeMillis = expiresAt)
    var smsStatus by remember(attempt) { mutableStateOf<SmsStatus>(SmsStatus.Listening) }
    var isWrong by rememberSaveable { mutableStateOf(false) }
    var isVerifying by remember { mutableStateOf(false) }
    val isComplete by remember { derivedStateOf { code.isOtpComplete(CODE_LENGTH) } }
    val isExpired = timer.isFinished
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val currentExpectedCode by rememberUpdatedState(expectedCode)
    val currentOnVerify by rememberUpdatedState(onVerify)

    val verify: (String) -> Unit = { input ->
        scope.launch {
            isVerifying = true
            val accepted = DemoAuthServer.verify(input, currentExpectedCode)
            isVerifying = false
            if (accepted) currentOnVerify() else isWrong = true
        }
    }

    val fill: (String) -> Unit = { value ->
        val wasComplete = code.isOtpComplete(CODE_LENGTH)
        code.fillOtp(value, CODE_LENGTH)
        if (wasComplete) verify(code.otpValue)
    }

    val receiveSms: (String) -> Unit = { received ->
        smsStatus = SmsStatus.Received
        fill(received)
    }

    LaunchedEffect(code) {
        snapshotFlow { code.text.length }.collect { length -> if (length < CODE_LENGTH) isWrong = false }
    }

    LaunchedEffect(attempt) { focusRequester.requestFocus() }

    LaunchedEffect(isWrong) {
        if (isWrong) focusRequester.requestFocus()
    }

    SmsUserConsentEffect(
        codeLength = CODE_LENGTH,
        onCodeReceive = receiveSms,
        onError = { error -> if (error != SmsConsentError.UntrustedIntent) smsStatus = SmsStatus.Ended(error) },
        restartKey = attempt,
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                StepIndicator(step = 2, total = 2)
                Spacer(Modifier.height(24.dp))
                Text(text = "Enter the code", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(8.dp))
                SentToText(phone = phone, onNumberEdit = onNumberEdit)
            }
            Spacer(Modifier.height(32.dp))
            BoxWithConstraints(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                val available = maxWidth - GROUP_GAP + CELL_GAP
                val cellWidth = fittedCellWidth(available, CODE_LENGTH, CELL_GAP, preferred = 52.dp)
                OtpInputField(
                    state = code,
                    otpLength = CODE_LENGTH,
                    modifier = Modifier.focusRequester(focusRequester),
                    enabled = !isExpired && !isVerifying,
                    isError = isWrong,
                    dimensions = OtpInputDefaults.dimensions(
                        cellWidth = cellWidth,
                        cellHeight = 60.dp,
                        cornerRadius = 14.dp,
                        textSize = 26.sp,
                    ),
                    onComplete = verify,
                    separator = { afterIndex ->
                        Spacer(Modifier.width(if (afterIndex == CODE_LENGTH / 2 - 1) GROUP_GAP else CELL_GAP))
                    },
                )
            }
            Spacer(Modifier.height(16.dp))
            StatusLine(message = statusMessage(isWrong, isExpired, smsStatus))
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { verify(code.otpValue) },
                enabled = isComplete && !isVerifying && !isExpired,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                if (isVerifying) {
                    CircularProgressIndicator(
                        color = LocalContentColor.current,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp),
                    )
                } else {
                    Text("Verify")
                }
            }
            Spacer(Modifier.height(20.dp))
            ExpiryRow(
                timer = timer,
                onTimerFinish = {
                    code.clearOtp()
                    isWrong = false
                },
            )
            TextButton(
                onClick = {
                    code.clearOtp()
                    isWrong = false
                    onResend()
                },
                enabled = isExpired,
            ) {
                Icon(painterResource(R.drawable.ic_refresh), contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Resend code")
            }
        }
        DemoControls(
            expectedCode = expectedCode,
            enabled = !isExpired && !isVerifying,
            canFastForward = timer.remaining > FAST_FORWARD_THRESHOLD,
            onSmsSimulate = { receiveSms(expectedCode) },
            onWrongCodeEnter = { fill(DemoAuthServer.wrongCodeFor(expectedCode)) },
            onFastForward = onFastForward,
        )
    }
}

@Composable
private fun SentToText(phone: String, onNumberEdit: () -> Unit, modifier: Modifier = Modifier) {
    val linkColor = MaterialTheme.colorScheme.primary
    val text = remember(phone, linkColor, onNumberEdit) {
        buildAnnotatedString {
            append("Sent to +1 $phone. ")
            withLink(
                LinkAnnotation.Clickable(
                    tag = "edit",
                    styles = TextLinkStyles(SpanStyle(color = linkColor, fontWeight = FontWeight.SemiBold)),
                    linkInteractionListener = { onNumberEdit() },
                ),
            ) {
                append("Change number")
            }
        }
    }
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
    )
}

@Composable
private fun ExpiryRow(timer: CountdownTimerState, onTimerFinish: () -> Unit, modifier: Modifier = Modifier) {
    val isExpired = timer.isFinished
    val urgent = timer.remaining <= URGENT_THRESHOLD
    val colors = MaterialTheme.colorScheme
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.height(32.dp),
    ) {
        Text(
            text = if (isExpired) "Code expired" else "Code expires in",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isExpired) colors.error else colors.onSurfaceVariant,
        )
        CountdownTimer(
            state = timer,
            style = CountdownTimerDefaults.style(
                backgroundColor = if (urgent) colors.errorContainer else colors.primaryContainer,
                textStyle = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (urgent) colors.onErrorContainer else colors.onPrimaryContainer,
                ),
                separatorColor = colors.onSurfaceVariant,
            ),
            dimensions = CountdownTimerDefaults.dimensions(verticalPadding = 4.dp, horizontalPadding = 6.dp),
            onTimerFinish = onTimerFinish,
        )
    }
}

private enum class Tone { Listening, Success, Error, Neutral }

private data class StatusMessage(val text: String, val tone: Tone)

private fun statusMessage(isWrong: Boolean, isExpired: Boolean, smsStatus: SmsStatus): StatusMessage = when {
    isWrong -> StatusMessage("That code doesn't match. Try again.", Tone.Error)

    isExpired -> StatusMessage("This code has expired. Ask for a new one.", Tone.Error)

    else -> when (smsStatus) {
        SmsStatus.Listening -> StatusMessage("Waiting for the SMS…", Tone.Listening)
        SmsStatus.Received -> StatusMessage("Code filled in from your SMS", Tone.Success)
        is SmsStatus.Ended -> StatusMessage(smsStatus.error.userMessage(), Tone.Neutral)
    }
}

private fun SmsConsentError.userMessage(): String = when (this) {
    SmsConsentError.Timeout -> "No SMS arrived. Type the code instead."
    SmsConsentError.ConsentDenied -> "You didn't share the SMS. Type the code instead."
    SmsConsentError.CodeNotFound -> "That SMS had no $CODE_LENGTH-digit code in it."
    SmsConsentError.PromptUnavailable -> "Couldn't ask to read the SMS. Type the code instead."
    SmsConsentError.UntrustedIntent -> "Ignored a message from an untrusted source."
    is SmsConsentError.StartFailed -> "Reading SMS needs Google Play services. Type the code instead."
}

@Composable
private fun StatusLine(message: StatusMessage, modifier: Modifier = Modifier) {
    AnimatedContent(
        targetState = message,
        transitionSpec = { fadeIn(tween(220, delayMillis = 90)) togetherWith fadeOut(tween(90)) },
        modifier = modifier.height(24.dp),
        label = "verify_status",
    ) { current ->
        val colors = MaterialTheme.colorScheme
        val color = when (current.tone) {
            Tone.Listening, Tone.Neutral -> colors.onSurfaceVariant
            Tone.Success -> colors.tertiary
            Tone.Error -> colors.error
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            when (current.tone) {
                Tone.Listening -> PulsingDot(color = colors.primary)

                Tone.Success -> Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp),
                )

                Tone.Error, Tone.Neutral -> Unit
            }
            Text(
                text = current.text,
                style = MaterialTheme.typography.bodyMedium,
                color = color,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private val CELL_GAP = 8.dp
private val GROUP_GAP = 20.dp
private val FAST_FORWARD_THRESHOLD = 6.seconds
private val URGENT_THRESHOLD = 10.seconds
