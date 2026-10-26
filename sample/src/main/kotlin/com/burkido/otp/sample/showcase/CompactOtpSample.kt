package com.burkido.otp.sample.showcase

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.burkido.otp.input.OtpInputDefaults
import com.burkido.otp.input.OtpInputField

private val Violet = Color(0xFF7C4DFF)
private val Lilac = Color(0xFFCE93D8)

/** Small violet cells split into two groups of three, like a PIN. */
@Composable
fun CompactOtpSample(
    state: TextFieldState,
    otpLength: Int,
    modifier: Modifier = Modifier,
    onComplete: (String) -> Unit = {},
) {
    SampleSection(title = "Compact", modifier = modifier) {
        OtpInputField(
            state = state,
            otpLength = otpLength,
            colors = OtpInputDefaults.colors(
                focusedBorderColor = Violet,
                unfocusedBorderColor = Lilac,
                filledBorderColor = Violet,
                errorBorderColor = Color(0xFFD32F2F),
                focusedContainerColor = Color(0xFFF3E5F5),
                unfocusedContainerColor = Color(0xFFFAFAFA),
                filledContainerColor = Color(0xFFEDE7F6),
                errorContainerColor = Color(0xFFFFEBEE),
                textColor = Color(0xFF4A148C),
                cursorColor = Violet,
                placeholderColor = Lilac,
            ),
            dimensions = OtpInputDefaults.dimensions(
                cellWidth = 40.dp,
                cellHeight = 44.dp,
                cornerRadius = 8.dp,
                spacing = 4.dp,
            ),
            onComplete = onComplete,
            separator = { afterIndex ->
                if (afterIndex == otpLength / 2 - 1) {
                    Text(
                        text = "-",
                        color = Lilac,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 6.dp),
                    )
                } else {
                    Spacer(Modifier.width(4.dp))
                }
            },
        )
    }
}
