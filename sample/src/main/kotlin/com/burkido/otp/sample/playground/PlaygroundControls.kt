package com.burkido.otp.sample.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.burkido.otp.input.CellAnimation
import com.burkido.otp.sample.R
import com.burkido.otp.sample.ui.ChoiceRow
import com.burkido.otp.sample.ui.SampleCard
import com.burkido.otp.sample.ui.SectionLabel
import kotlin.math.roundToInt

@Composable
fun PlaygroundControls(
    config: PlaygroundConfig,
    onConfigChange: (PlaygroundConfig) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp), modifier = modifier) {
        Control(label = "Length") {
            ChoiceRow(
                options = LENGTHS,
                selected = config.length,
                label = { "$it digits" },
                onSelect = { onConfigChange(config.copy(length = it)) },
            )
        }
        Control(label = "Cell") {
            ChoiceRow(
                options = CellKind.entries,
                selected = config.cell,
                label = CellKind::label,
                onSelect = { onConfigChange(config.copy(cell = it)) },
            )
        }
        Control(label = "Entry animation") {
            ChoiceRow(
                options = CellAnimation.entries,
                selected = config.animation,
                label = { it.name.removeSuffix("In").removeSuffix("Up") },
                onSelect = { onConfigChange(config.copy(animation = it)) },
            )
        }
        Control(label = "Accent") {
            AccentPicker(selected = config.accent, onSelect = { onConfigChange(config.copy(accent = it)) })
        }
        Control(label = "Corner radius · ${config.cornerRadius} dp") {
            Slider(
                value = config.cornerRadius.toFloat(),
                onValueChange = { onConfigChange(config.copy(cornerRadius = it.roundToInt())) },
                valueRange = 0f..MAX_CORNER_RADIUS,
                enabled = config.cell == CellKind.Default,
            )
        }
        SampleCard(contentPadding = PaddingValues(vertical = 4.dp)) {
            SwitchRow(
                title = "Masked",
                description = "Each digit turns into a dot after 300 ms",
                checked = config.masked,
                onCheckedChange = { onConfigChange(config.copy(masked = it)) },
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            SwitchRow(
                title = "Grouped",
                description = "A wider gap splits the code in half",
                checked = config.grouped,
                onCheckedChange = { onConfigChange(config.copy(grouped = it)) },
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            SwitchRow(
                title = "Placeholder",
                description = "A dash in empty cells",
                checked = config.placeholder,
                onCheckedChange = { onConfigChange(config.copy(placeholder = it)) },
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            SwitchRow(
                title = "Error",
                description = "Error colors, and one shake when it turns on",
                checked = config.isError,
                onCheckedChange = { onConfigChange(config.copy(isError = it)) },
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            SwitchRow(
                title = "Enabled",
                description = "A disabled field can't be focused",
                checked = config.enabled,
                onCheckedChange = { onConfigChange(config.copy(enabled = it)) },
            )
        }
    }
}

@Composable
private fun Control(label: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = modifier) {
        SectionLabel(text = label)
        content()
    }
}

@Composable
private fun AccentPicker(selected: Accent, onSelect: (Accent) -> Unit, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = modifier) {
        Accent.entries.forEach { accent ->
            val color = accent.color ?: MaterialTheme.colorScheme.primary
            val isSelected = accent == selected
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .border(
                        width = 2.dp,
                        color = if (isSelected) color else Color.Transparent,
                        shape = CircleShape,
                    )
                    .padding(4.dp)
                    .background(color, CircleShape)
                    .clickable(role = Role.RadioButton) { onSelect(accent) }
                    .semantics {
                        contentDescription = accent.name
                        this.selected = isSelected
                    },
            ) {
                if (isSelected) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = if (color.luminance() > 0.4f) Color.Black else Color.White,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun SwitchRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .toggleable(value = checked, role = Role.Switch, onValueChange = onCheckedChange)
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Switch(checked = checked, onCheckedChange = null)
    }
}

private val LENGTHS = listOf(4, 6, 8)
private const val MAX_CORNER_RADIUS = 28f
