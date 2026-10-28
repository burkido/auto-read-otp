package com.burkido.otp.sample.gallery

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.burkido.otp.input.OtpInputDefaults
import com.burkido.otp.input.OtpInputField
import com.burkido.otp.input.OutlinedOtpCell

private val Paper = Color(0xFFFFF4E0)
private val TicketInk = Color(0xFF1C1917)
private val Rust = Color(0xFFEA580C)

@Composable
internal fun TicketDesign(modifier: Modifier = Modifier) {
    val code = rememberTextFieldState()
    val label = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace)

    DesignFrame(background = SolidColor(Paper), modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row {
                Text(text = "FLIGHT AR 2048", style = label, color = TicketInk.copy(alpha = 0.6f))
                Spacer(Modifier.weight(1f))
                Text(text = "SEAT 14C", style = label, color = TicketInk.copy(alpha = 0.6f))
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = "IST → LIS",
                style = MaterialTheme.typography.headlineMedium.copy(fontFamily = FontFamily.Monospace),
                color = TicketInk,
            )
            DashedDivider(modifier = Modifier.padding(vertical = 18.dp))
            Text(text = "BOOKING PIN", style = label, color = Rust)
            Spacer(Modifier.height(10.dp))
            FittedCells(count = 6, spacing = 6.dp, preferred = 44.dp, extraWidth = 22.dp) { cellWidth ->
                OtpInputField(
                    state = code,
                    otpLength = 6,
                    separator = { afterIndex ->
                        if (afterIndex == 2) {
                            Text(
                                text = "–",
                                color = TicketInk,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(horizontal = 6.dp),
                            )
                        } else {
                            Spacer(Modifier.width(6.dp))
                        }
                    },
                ) { cell ->
                    OutlinedOtpCell(
                        state = cell,
                        colors = OtpInputDefaults.colors(
                            focusedBorderColor = Rust,
                            unfocusedBorderColor = TicketInk.copy(alpha = 0.2f),
                            filledBorderColor = TicketInk,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.Transparent,
                            filledContainerColor = Color.Transparent,
                            textColor = TicketInk,
                            cursorColor = Rust,
                        ),
                        dimensions = OtpInputDefaults.dimensions(cellWidth = cellWidth, cellHeight = 52.dp),
                        shape = RoundedCornerShape(8.dp),
                        textStyles = OtpInputDefaults.textStyles(
                            filledTextStyle = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                            ),
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun DashedDivider(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp),
    ) {
        drawLine(
            color = TicketInk.copy(alpha = 0.3f),
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = size.height,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f)),
        )
    }
}
