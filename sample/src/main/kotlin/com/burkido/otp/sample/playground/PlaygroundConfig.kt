package com.burkido.otp.sample.playground

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.burkido.otp.ui.CellAnimation

enum class CellKind(val label: String, val composable: String) {
    Default("Default", "DefaultOtpCell"),
    Outlined("Outlined", "OutlinedOtpCell"),
    Underline("Underline", "UnderlineOtpCell"),
}

enum class Accent(val argb: Long?) {
    Theme(null),
    Emerald(0xFF10B981),
    Coral(0xFFF2545B),
    Amber(0xFFF59E0B),
    Violet(0xFF8B5CF6),
    ;

    val color: Color? get() = argb?.let(::Color)
}

data class PlaygroundConfig(
    val length: Int = 6,
    val cell: CellKind = CellKind.Default,
    val animation: CellAnimation = CellAnimation.Scale,
    val accent: Accent = Accent.Theme,
    val cornerRadius: Int = DEFAULT_CORNER_RADIUS,
    val masked: Boolean = false,
    val grouped: Boolean = false,
    val placeholder: Boolean = false,
    val isError: Boolean = false,
    val enabled: Boolean = true,
) {

    val groupAfterIndex: Int get() = length / 2 - 1

    fun cellHeight(cellWidth: Dp): Dp = if (cell == CellKind.Outlined) cellWidth else DEFAULT_CELL_HEIGHT

    fun snippet(cellWidth: Dp): String {
        val style = buildList {
            if (placeholder) add("placeholder = \"$PLACEHOLDER\"")
            accent.argb?.let {
                add(
                    """
                    colors = OtpInputDefaults.colors(
                        focusedBorderColor = accent,
                        filledBorderColor = accent,
                        cursorColor = accent,
                    )
                    """.trimIndent(),
                )
            }
            dimensionsArgument(cellWidth)?.let(::add)
            if (animation != CellAnimation.Scale) {
                val entry = "cellEntryAnimation = CellAnimation.${animation.name}"
                add("animationSpec = OtpInputDefaults.animationSpec($entry)")
            }
            if (masked) add(MASK_ARGUMENT)
        }
        val field = buildList {
            add("state = code")
            add("otpLength = $length")
            if (!enabled) add("enabled = false")
            if (isError) add("isError = true")
            when {
                cell == CellKind.Default -> addAll(style)
                masked -> add(MASK_ARGUMENT)
            }
            if (grouped) {
                add(
                    """
                    separator = { afterIndex ->
                        Spacer(Modifier.width(if (afterIndex == $groupAfterIndex) 20.dp else 8.dp))
                    }
                    """.trimIndent(),
                )
            }
            add("onComplete = { otp -> viewModel.verify(otp) }")
        }
        return buildString {
            appendLine("val code = rememberTextFieldState()")
            accent.argb?.let { appendLine("val accent = Color(0x${it.toString(16).uppercase()})") }
            appendLine()
            append(call("OtpInputField", field))
            if (cell != CellKind.Default) {
                appendLine(" { cell ->")
                appendLine(call(cell.composable, listOf("state = cell") + style).prependIndent(INDENT))
                append("}")
            }
        }
    }

    private fun dimensionsArgument(cellWidth: Dp): String? {
        val values = buildList {
            if (cellWidth != DEFAULT_CELL_WIDTH) {
                add("cellWidth = ${cellWidth.value.toInt()}.dp")
                if (cell == CellKind.Outlined) add("cellHeight = ${cellWidth.value.toInt()}.dp")
            }
            val customCorner = cell == CellKind.Default && cornerRadius != DEFAULT_CORNER_RADIUS
            if (customCorner) add("cornerRadius = $cornerRadius.dp")
        }
        return if (values.isEmpty()) null else "dimensions = OtpInputDefaults.dimensions(${values.joinToString()})"
    }

    companion object {
        const val DEFAULT_CORNER_RADIUS = 12
        const val PLACEHOLDER = "-"
        val DEFAULT_CELL_WIDTH = 48.dp
        val DEFAULT_CELL_HEIGHT = 56.dp

        val Saver: Saver<PlaygroundConfig, Any> = listSaver(
            save = {
                listOf(
                    it.length,
                    it.cell.ordinal,
                    it.animation.ordinal,
                    it.accent.ordinal,
                    it.cornerRadius,
                    it.masked,
                    it.grouped,
                    it.placeholder,
                    it.isError,
                    it.enabled,
                )
            },
            restore = {
                PlaygroundConfig(
                    length = it[0] as Int,
                    cell = CellKind.entries[it[1] as Int],
                    animation = CellAnimation.entries[it[2] as Int],
                    accent = Accent.entries[it[3] as Int],
                    cornerRadius = it[4] as Int,
                    masked = it[5] as Boolean,
                    grouped = it[6] as Boolean,
                    placeholder = it[7] as Boolean,
                    isError = it[8] as Boolean,
                    enabled = it[9] as Boolean,
                )
            },
        )
    }
}

private fun call(name: String, arguments: List<String>): String = buildString {
    appendLine("$name(")
    arguments.forEach { appendLine("$it,".prependIndent(INDENT)) }
    append(")")
}

private const val INDENT = "    "
private const val MASK_ARGUMENT = "maskConfig = OtpInputDefaults.maskConfig(isMasked = true)"
