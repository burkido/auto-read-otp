package com.burkido.otp.sample.gallery

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.burkido.otp.input.OtpInputDefaults
import com.burkido.otp.input.OtpInputField

private val AuroraBackground = Brush.linearGradient(
    listOf(Color(0xFF5B21B6), Color(0xFFDB2777), Color(0xFFF59E0B)),
)

@Composable
internal fun AuroraDesign(modifier: Modifier = Modifier) {
    val code = rememberTextFieldState()

    DesignFrame(background = AuroraBackground, modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Welcome back", style = MaterialTheme.typography.headlineSmall, color = Color.White)
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Enter the code from your authenticator app",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(24.dp))
            FittedCells(count = 6, spacing = 10.dp, preferred = 46.dp) { cellWidth ->
                OtpInputField(
                    state = code,
                    otpLength = 6,
                    placeholder = "·",
                    colors = OtpInputDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
                        filledBorderColor = Color.White.copy(alpha = 0.55f),
                        focusedContainerColor = Color.White.copy(alpha = 0.24f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.12f),
                        filledContainerColor = Color.White.copy(alpha = 0.18f),
                        textColor = Color.White,
                        cursorColor = Color.White,
                        placeholderColor = Color.White.copy(alpha = 0.6f),
                    ),
                    dimensions = OtpInputDefaults.dimensions(
                        cellWidth = cellWidth,
                        cellHeight = 58.dp,
                        cornerRadius = 16.dp,
                        spacing = 10.dp,
                    ),
                )
            }
        }
    }
}
