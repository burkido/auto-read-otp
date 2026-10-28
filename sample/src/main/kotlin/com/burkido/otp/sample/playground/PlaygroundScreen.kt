package com.burkido.otp.sample.playground

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.burkido.otp.input.DefaultOtpCell
import com.burkido.otp.input.OtpCellColors
import com.burkido.otp.input.OtpInputDefaults
import com.burkido.otp.input.OtpInputField
import com.burkido.otp.input.OutlinedOtpCell
import com.burkido.otp.input.UnderlineOtpCell
import com.burkido.otp.input.clearOtp
import com.burkido.otp.sample.ui.CodeBlock
import com.burkido.otp.sample.ui.DemoScaffold
import com.burkido.otp.sample.ui.SampleCard
import com.burkido.otp.sample.ui.SectionLabel
import com.burkido.otp.sample.ui.fittedCellWidth

@Composable
fun PlaygroundScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    var config by rememberSaveable(stateSaver = PlaygroundConfig.Saver) { mutableStateOf(PlaygroundConfig()) }
    var completedCode by rememberSaveable { mutableStateOf<String?>(null) }
    val code = rememberTextFieldState()

    LaunchedEffect(code) {
        snapshotFlow { code.text.length }.collect { length -> if (length < config.length) completedCode = null }
    }

    DemoScaffold(title = "Playground", onBack = onBack, modifier = modifier) { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            val groupExtra = if (config.grouped) GROUP_GAP - CELL_GAP else 0.dp
            val cellWidth = fittedCellWidth(maxWidth - PREVIEW_INSET - groupExtra, config.length, CELL_GAP)

            Column(modifier = Modifier.fillMaxSize()) {
                PreviewPanel(
                    config = config,
                    code = code,
                    cellWidth = cellWidth,
                    completedCode = completedCode,
                    onComplete = { completedCode = it },
                    onClear = {
                        code.clearOtp()
                        completedCode = null
                    },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                ) {
                    PlaygroundControls(
                        config = config,
                        onConfigChange = { updated ->
                            if (updated.length != config.length) code.clearOtp()
                            config = updated
                        },
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SectionLabel(text = "Code for this setup")
                        CodeBlock(code = config.snippet(cellWidth))
                    }
                }
            }
        }
    }
}

@Composable
private fun PreviewPanel(
    config: PlaygroundConfig,
    code: TextFieldState,
    cellWidth: Dp,
    completedCode: String?,
    onComplete: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isComplete = completedCode != null
    val scheme = MaterialTheme.colorScheme
    SampleCard(modifier = modifier, contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp)) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
        ) {
            PlaygroundField(config = config, code = code, cellWidth = cellWidth, onComplete = onComplete)
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = completedCode?.let { "onComplete(\"$it\")" } ?: "Tap the field and type a code",
                style = if (isComplete) {
                    MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace)
                } else {
                    MaterialTheme.typography.bodySmall
                },
                color = if (isComplete) scheme.tertiary else scheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
            TextButton(onClick = onClear) { Text("Clear") }
        }
    }
}

@Composable
private fun PlaygroundField(
    config: PlaygroundConfig,
    code: TextFieldState,
    cellWidth: Dp,
    onComplete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val placeholder = if (config.placeholder) PlaygroundConfig.PLACEHOLDER else ""
    val colors = playgroundColors(config)
    val dimensions = OtpInputDefaults.dimensions(
        cellWidth = cellWidth,
        cellHeight = config.cellHeight(cellWidth),
        cornerRadius = config.cornerRadius.dp,
    )
    val animationSpec = OtpInputDefaults.animationSpec(cellEntryAnimation = config.animation)
    val maskConfig = OtpInputDefaults.maskConfig(isMasked = config.masked)

    OtpInputField(
        state = code,
        otpLength = config.length,
        modifier = modifier,
        enabled = config.enabled,
        isError = config.isError,
        maskConfig = maskConfig,
        onComplete = onComplete,
        separator = if (config.grouped) {
            { afterIndex -> Spacer(Modifier.width(if (afterIndex == config.groupAfterIndex) GROUP_GAP else CELL_GAP)) }
        } else {
            null
        },
    ) { cell ->
        when (config.cell) {
            CellKind.Default -> DefaultOtpCell(
                state = cell,
                placeholder = placeholder,
                colors = colors,
                dimensions = dimensions,
                animationSpec = animationSpec,
                maskConfig = maskConfig,
            )

            CellKind.Outlined -> OutlinedOtpCell(
                state = cell,
                placeholder = placeholder,
                colors = colors,
                dimensions = dimensions,
                animationSpec = animationSpec,
                maskConfig = maskConfig,
            )

            CellKind.Underline -> UnderlineOtpCell(
                state = cell,
                placeholder = placeholder,
                colors = colors,
                dimensions = dimensions,
                animationSpec = animationSpec,
                maskConfig = maskConfig,
            )
        }
    }
}

@Composable
private fun playgroundColors(config: PlaygroundConfig): OtpCellColors {
    val scheme = MaterialTheme.colorScheme
    val accent = config.accent.color
    val transparent = config.cell == CellKind.Outlined && accent == null
    return OtpInputDefaults.colors(
        focusedBorderColor = accent ?: scheme.primary,
        filledBorderColor = accent ?: scheme.primary.copy(alpha = 0.7f),
        cursorColor = accent ?: scheme.primary,
        focusedContainerColor = if (transparent) Color.Transparent else scheme.surface,
        unfocusedContainerColor = if (transparent) Color.Transparent else scheme.surface,
        filledContainerColor = if (transparent) Color.Transparent else scheme.primaryContainer.copy(alpha = 0.3f),
        errorContainerColor = if (transparent) Color.Transparent else scheme.errorContainer,
    )
}

private val CELL_GAP = 8.dp
private val GROUP_GAP = 20.dp
private val PREVIEW_INSET = 80.dp
