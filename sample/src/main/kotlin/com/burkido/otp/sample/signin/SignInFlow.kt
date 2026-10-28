package com.burkido.otp.sample.signin

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.burkido.otp.sample.ui.DemoScaffold

private enum class SignInStep { Phone, Verify, Verified }

@Composable
fun SignInFlow(onClose: () -> Unit, modifier: Modifier = Modifier) {
    var step by rememberSaveable { mutableStateOf(SignInStep.Phone) }
    var phone by rememberSaveable { mutableStateOf("555 010 0199") }
    var expectedCode by rememberSaveable { mutableStateOf("") }
    var expiresAt by rememberSaveable { mutableLongStateOf(0L) }
    var attempt by rememberSaveable { mutableIntStateOf(0) }

    val issueCode: () -> Unit = {
        expectedCode = DemoAuthServer.issueCode()
        expiresAt = System.currentTimeMillis() + DemoAuthServer.codeLifetime.inWholeMilliseconds
        attempt++
    }

    BackHandler(enabled = step == SignInStep.Verify) { step = SignInStep.Phone }

    DemoScaffold(
        title = "Sign-in flow",
        onBack = { if (step == SignInStep.Verify) step = SignInStep.Phone else onClose() },
        modifier = modifier,
    ) { padding ->
        AnimatedContent(
            targetState = step,
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding),
            transitionSpec = {
                val forward = targetState > initialState
                val enter = slideInHorizontally(tween(300)) { if (forward) it / 4 else -it / 4 } + fadeIn(tween(300))
                val exit = slideOutHorizontally(tween(300)) { if (forward) -it / 4 else it / 4 } + fadeOut(tween(180))
                enter togetherWith exit
            },
            label = "sign_in_step",
        ) { current ->
            when (current) {
                SignInStep.Phone -> PhoneStep(
                    phone = phone,
                    onPhoneChange = { phone = it },
                    onCodeSend = {
                        issueCode()
                        step = SignInStep.Verify
                    },
                )

                SignInStep.Verify -> VerifyStep(
                    phone = phone,
                    expectedCode = expectedCode,
                    expiresAt = expiresAt,
                    attempt = attempt,
                    onResend = issueCode,
                    onFastForward = { expiresAt = System.currentTimeMillis() + FAST_FORWARD_MILLIS },
                    onNumberEdit = { step = SignInStep.Phone },
                    onVerify = { step = SignInStep.Verified },
                )

                SignInStep.Verified -> VerifiedStep(
                    phone = phone,
                    onDone = onClose,
                    onRestart = { step = SignInStep.Phone },
                )
            }
        }
    }
}

@Composable
internal fun StepIndicator(step: Int, total: Int, modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier,
    ) {
        repeat(total) { index ->
            val color = if (index < step) scheme.primary else scheme.outlineVariant
            Box(
                modifier = Modifier
                    .size(width = 24.dp, height = 4.dp)
                    .background(color, RoundedCornerShape(2.dp)),
            )
        }
        Text(
            text = "Step $step of $total",
            style = MaterialTheme.typography.labelMedium,
            color = scheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}

private const val FAST_FORWARD_MILLIS = 5_000L
