package com.burkido.otp.sample.gallery

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.burkido.otp.input.OtpCellState
import com.burkido.otp.input.OtpInputDefaults
import com.burkido.otp.input.OtpInputField
import com.burkido.otp.input.isOtpComplete
import com.burkido.otp.timer.CountdownTimer
import com.burkido.otp.timer.CountdownTimerDefaults
import com.burkido.otp.timer.rememberCountdownTimerState
import kotlin.time.Duration.Companion.minutes

private val Phosphor = Color(0xFF3DFF8A)
private val Screen = Color(0xFF06100A)
private val Mono = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp, color = Phosphor)

@Composable
internal fun TerminalDesign(modifier: Modifier = Modifier) {
    val code = rememberTextFieldState()
    val timer = rememberCountdownTimerState(duration = 2.minutes)
    val isComplete by remember { derivedStateOf { code.isOtpComplete(6) } }
    val focusRequester = remember { FocusRequester() }

    DesignFrame(background = SolidColor(Screen), modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(interactionSource = null, indication = null) { focusRequester.requestFocus() },
        ) {
            Text(text = "$ ssh deploy@prod-eu-1", style = Mono.copy(color = Phosphor.copy(alpha = 0.6f)))
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Verification code: ", style = Mono)
                OtpInputField(
                    state = code,
                    otpLength = 6,
                    modifier = Modifier.focusRequester(focusRequester),
                    dimensions = OtpInputDefaults.dimensions(spacing = 1.dp),
                ) { cell ->
                    TerminalCell(cell = cell)
                }
            }
            AnimatedVisibility(
                visible = isComplete,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut(),
            ) {
                Text(text = "> Access granted. Welcome, deploy.", style = Mono, modifier = Modifier.padding(top = 6.dp))
            }
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (timer.isFinished) "# session expired" else "# session expires in ",
                    style = Mono.copy(color = Phosphor.copy(alpha = 0.45f), fontSize = 12.sp),
                )
                CountdownTimer(
                    state = timer,
                    style = CountdownTimerDefaults.style(
                        backgroundColor = Color.Transparent,
                        textStyle = Mono.copy(fontSize = 12.sp, color = Phosphor.copy(alpha = 0.7f)),
                        separatorColor = Phosphor.copy(alpha = 0.45f),
                    ),
                    dimensions = CountdownTimerDefaults.dimensions(
                        verticalPadding = 0.dp,
                        horizontalPadding = 0.dp,
                        boxSpacing = 0.dp,
                    ),
                )
            }
        }
    }
}

@Composable
private fun TerminalCell(cell: OtpCellState, modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.size(width = 13.dp, height = 24.dp)) {
        when {
            cell.isFilled -> Text(text = cell.char.toString(), style = Mono.copy(fontSize = 18.sp))
            cell.isFocused -> BlinkingBlock()
            else -> Text(text = "_", style = Mono.copy(fontSize = 18.sp, color = Phosphor.copy(alpha = 0.3f)))
        }
    }
}

@Composable
private fun BlinkingBlock(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "terminal_cursor")
    val alpha by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1_060
                1f at 0
                1f at 529
                0f at 530
                0f at 1_059
            },
            repeatMode = RepeatMode.Restart,
        ),
        label = "terminal_cursor_alpha",
    )
    Box(
        modifier = modifier
            .width(10.dp)
            .height(18.dp)
            .alpha(alpha)
            .background(Phosphor),
    )
}
