package com.burkido.otp.sample.gallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.burkido.otp.input.CellAnimation
import com.burkido.otp.input.OtpInputDefaults
import com.burkido.otp.input.OtpInputField
import com.burkido.otp.timer.CountdownTimer
import com.burkido.otp.timer.CountdownTimerDefaults
import com.burkido.otp.timer.rememberCountdownTimerState
import kotlin.time.Duration.Companion.minutes

private val Gold = Color(0xFFE7C27D)
private val Ivory = Color(0xFFF4E9D0)
private val Navy = Color(0xFF16223D)
private val NavyLight = Color(0xFF1F2E52)
private val VaultBackground = Brush.linearGradient(listOf(Color(0xFF0A101E), Navy))

@Composable
internal fun VaultDesign(modifier: Modifier = Modifier) {
    val code = rememberTextFieldState()
    val timer = rememberCountdownTimerState(duration = 5.minutes)

    DesignFrame(background = VaultBackground, modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "CONFIRM TRANSFER", style = MaterialTheme.typography.labelSmall, color = Gold)
            Spacer(Modifier.height(8.dp))
            Text(text = "€2,400.00", style = MaterialTheme.typography.headlineMedium, color = Ivory)
            Text(
                text = "to Atlas Studio Ltd.",
                style = MaterialTheme.typography.bodyMedium,
                color = Ivory.copy(alpha = 0.6f),
            )
            Spacer(Modifier.height(24.dp))
            FittedCells(count = 6, spacing = 8.dp, preferred = 46.dp) { cellWidth ->
                OtpInputField(
                    state = code,
                    otpLength = 6,
                    colors = OtpInputDefaults.colors(
                        focusedBorderColor = Gold,
                        unfocusedBorderColor = NavyLight,
                        filledBorderColor = Gold.copy(alpha = 0.45f),
                        focusedContainerColor = NavyLight,
                        unfocusedContainerColor = Navy,
                        filledContainerColor = Navy,
                        textColor = Gold,
                        cursorColor = Gold,
                    ),
                    dimensions = OtpInputDefaults.dimensions(
                        cellWidth = cellWidth,
                        cellHeight = 56.dp,
                        cornerRadius = 10.dp,
                        borderWidth = 1.dp,
                        focusedBorderWidth = 1.5.dp,
                    ),
                    animationSpec = OtpInputDefaults.animationSpec(cellEntryAnimation = CellAnimation.FadeIn),
                    maskConfig = OtpInputDefaults.maskConfig(isMasked = true, maskCharacter = '✱'),
                )
            }
            Spacer(Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = if (timer.isFinished) "Request expired" else "Expires in",
                    style = MaterialTheme.typography.bodySmall,
                    color = Ivory.copy(alpha = 0.6f),
                )
                CountdownTimer(
                    state = timer,
                    style = CountdownTimerDefaults.style(
                        backgroundColor = NavyLight,
                        textStyle = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Gold,
                        ),
                        separatorColor = Gold.copy(alpha = 0.6f),
                    ),
                )
            }
        }
    }
}
