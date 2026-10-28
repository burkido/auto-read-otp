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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.burkido.otp.input.CellAnimation
import com.burkido.otp.input.OtpInputDefaults
import com.burkido.otp.input.OtpInputField
import com.burkido.otp.input.UnderlineOtpCell

@Composable
internal fun MinimalDesign(modifier: Modifier = Modifier) {
    val code = rememberTextFieldState()
    val scheme = MaterialTheme.colorScheme

    DesignFrame(background = SolidColor(scheme.surfaceContainerLow), modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Check your inbox", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "We sent a 4-digit code to ada@example.com",
                style = MaterialTheme.typography.bodyMedium,
                color = scheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(20.dp))
            OtpInputField(
                state = code,
                otpLength = 4,
                dimensions = OtpInputDefaults.dimensions(spacing = 20.dp),
            ) { cell ->
                UnderlineOtpCell(
                    state = cell,
                    colors = OtpInputDefaults.colors(
                        unfocusedBorderColor = scheme.outline,
                        focusedBorderColor = scheme.onSurface,
                        filledBorderColor = scheme.onSurface,
                        cursorColor = scheme.onSurface,
                    ),
                    dimensions = OtpInputDefaults.dimensions(
                        cellWidth = 52.dp,
                        cellHeight = 60.dp,
                        borderWidth = 1.dp,
                        focusedBorderWidth = 2.dp,
                    ),
                    animationSpec = OtpInputDefaults.animationSpec(
                        cellEntryAnimation = CellAnimation.SlideUp,
                        animationDuration = 220,
                    ),
                    textStyles = OtpInputDefaults.textStyles(
                        filledTextStyle = TextStyle(fontSize = 38.sp, fontWeight = FontWeight.Light),
                    ),
                )
            }
        }
    }
}
