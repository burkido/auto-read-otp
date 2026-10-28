package com.burkido.otp.sample.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.burkido.otp.sample.ui.SectionLabel

@Composable
fun DemoControls(
    expectedCode: String,
    enabled: Boolean,
    canFastForward: Boolean,
    onSmsSimulate: () -> Unit,
    onWrongCodeEnter: () -> Unit,
    onFastForward: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(color = MaterialTheme.colorScheme.surfaceContainer, modifier = modifier.fillMaxWidth()) {
        Column {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SectionLabel(text = "Demo controls", modifier = Modifier.weight(1f))
                    Text(
                        text = "Server code ",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = expectedCode,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DemoButton(label = "Simulate SMS", enabled = enabled, onClick = onSmsSimulate)
                    DemoButton(label = "Wrong code", enabled = enabled, onClick = onWrongCodeEnter)
                    DemoButton(label = "Skip to 0:05", enabled = enabled && canFastForward, onClick = onFastForward)
                }
            }
        }
    }
}

@Composable
private fun RowScope.DemoButton(label: String, enabled: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        modifier = modifier.weight(1f),
    ) {
        Text(text = label, style = MaterialTheme.typography.labelLarge, maxLines = 1, textAlign = TextAlign.Center)
    }
}
