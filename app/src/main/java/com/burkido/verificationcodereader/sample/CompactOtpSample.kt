package com.burkido.verificationcodereader.sample

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.burkido.otpinputkit.OtpInputDefaults
import com.burkido.otpinputkit.OtpInputField
import com.burkido.otpinputkit.cell.DefaultOtpCell
import com.burkido.otpinputkit.style.OtpCursorConfig

private val CompactColors
    @Composable get() = OtpInputDefaults.colors(
        focusedBorderColor = Color(0xFF7C4DFF),
        unfocusedBorderColor = Color(0xFFCE93D8),
        filledBorderColor = Color(0xFF7C4DFF),
        errorBorderColor = Color(0xFFD32F2F),
        focusedContainerColor = Color(0xFFF3E5F5),
        unfocusedContainerColor = Color(0xFFFAFAFA),
        filledContainerColor = Color(0xFFEDE7F6),
        errorContainerColor = Color(0xFFFFEBEE),
        textColor = Color(0xFF4A148C),
        cursorColor = Color(0xFF7C4DFF),
        placeholderColor = Color(0xFFCE93D8),
    )

private val CompactDimensions
    @Composable get() = OtpInputDefaults.dimensions(
        cellWidth = 40.dp,
        cellHeight = 44.dp,
        cornerRadius = 8.dp,
        spacing = 4.dp,
    )

/**
 * Compact OTP input — small violet cells (40×44dp) with a center em-dash
 * separator splitting the 6 digits into two groups of 3 (PIN-style).
 */
@Composable
fun CompactOtpSample(
    textFieldState: TextFieldState,
    otpLength: Int,
    modifier: Modifier = Modifier,
    onComplete: (String) -> Unit = {},
) {
    val colors = CompactColors
    val dimensions = CompactDimensions

    Text("Compact", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(12.dp))
    OtpInputField(
        textFieldState = textFieldState,
        otpLength = otpLength,
        modifier = modifier,
        dimensions = dimensions,
        onComplete = onComplete,
        cell = { _, char, focused, error ->
            DefaultOtpCell(
                char = char,
                focused = focused,
                isError = error,
                colors = colors,
                dimensions = dimensions,
                cursorConfig = OtpCursorConfig(color = Color(0xFF7C4DFF)),
            )
        },
        separator = { index ->
            if (index == 2) {
                Text(
                    text = "—",
                    color = Color(0xFFCE93D8),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
            }
        },
    )
}
