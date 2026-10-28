package com.burkido.otp.sample.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.burkido.otp.input.CellAnimation
import com.burkido.otp.input.OtpInputDefaults
import com.burkido.otp.input.OtpInputField
import com.burkido.otp.sample.R
import com.burkido.otp.sample.ui.PulsingDot
import com.burkido.otp.sample.ui.SampleCard
import com.burkido.otp.sample.ui.fittedCellWidth
import kotlinx.coroutines.delay

private enum class HeroPhase { Waiting, Arrived, Filling, Verified }

private const val HERO_CODE = "482913"

@Composable
fun HeroDemo(modifier: Modifier = Modifier) {
    var phase by remember { mutableStateOf(HeroPhase.Waiting) }
    var code by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            phase = HeroPhase.Waiting
            code = ""
            delay(1_400)
            phase = HeroPhase.Arrived
            delay(1_200)
            phase = HeroPhase.Filling
            for (digit in HERO_CODE) {
                code += digit
                delay(80)
            }
            delay(450)
            phase = HeroPhase.Verified
            delay(2_600)
        }
    }

    SampleCard(modifier = modifier.clearAndSetSemantics {}) {
        SmsBubble(visible = phase != HeroPhase.Waiting)
        Spacer(Modifier.height(20.dp))
        BoxWithConstraints(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            val verified = phase == HeroPhase.Verified
            val colors = MaterialTheme.colorScheme
            OtpInputField(
                value = code,
                onValueChange = {},
                otpLength = HERO_CODE.length,
                modifier = Modifier.focusProperties { canFocus = false },
                colors = OtpInputDefaults.colors(
                    filledBorderColor = if (verified) colors.tertiary else colors.primary.copy(alpha = 0.7f),
                    filledContainerColor = if (verified) colors.tertiaryContainer else colors.primaryContainer,
                ),
                dimensions = OtpInputDefaults.dimensions(
                    cellWidth = fittedCellWidth(maxWidth, HERO_CODE.length, 8.dp),
                    cellHeight = 56.dp,
                ),
                animationSpec = OtpInputDefaults.animationSpec(
                    cellEntryAnimation = CellAnimation.SlideUp,
                    animationDuration = 260,
                ),
            )
        }
        Spacer(Modifier.height(20.dp))
        HeroStatus(phase = phase, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
private fun SmsBubble(visible: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically { -it / 2 } + fadeIn(),
            exit = fadeOut(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(18.dp))
                    .padding(12.dp),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_sms),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp),
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Messages · now",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "$HERO_CODE is your Acme verification code.",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroStatus(phase: HeroPhase, modifier: Modifier = Modifier) {
    AnimatedContent(
        targetState = phase,
        transitionSpec = { fadeIn(tween(220, delayMillis = 90)) togetherWith fadeOut(tween(90)) },
        modifier = modifier,
        label = "hero_status",
    ) { current ->
        val colors = MaterialTheme.colorScheme
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (current == HeroPhase.Verified) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = colors.tertiary,
                        modifier = Modifier.size(18.dp),
                    )
                } else {
                    PulsingDot(color = colors.primary, isPulsing = current == HeroPhase.Waiting)
                }
                Text(
                    text = when (current) {
                        HeroPhase.Waiting -> "Waiting for the SMS"
                        HeroPhase.Arrived -> "User allowed access to the message"
                        HeroPhase.Filling -> "Filling in the code"
                        HeroPhase.Verified -> "Verified"
                    },
                    style = MaterialTheme.typography.titleSmall,
                    color = if (current == HeroPhase.Verified) colors.tertiary else colors.onSurface,
                )
            }
            Text(
                text = when (current) {
                    HeroPhase.Waiting -> "SmsUserConsentEffect(codeLength = 6, …)"
                    HeroPhase.Arrived -> "onCodeReceive(\"$HERO_CODE\")"
                    HeroPhase.Filling -> "code.fillOtp(\"$HERO_CODE\", 6)"
                    HeroPhase.Verified -> "onComplete(\"$HERO_CODE\")"
                },
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                color = colors.onSurfaceVariant,
            )
        }
    }
}
