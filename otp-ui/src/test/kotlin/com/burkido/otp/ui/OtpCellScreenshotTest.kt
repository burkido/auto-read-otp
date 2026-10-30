package com.burkido.otp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "w480dp-h160dp-xhdpi")
class OtpCellScreenshotTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun defaultCell() = captureCells("default_cell") { DefaultOtpCell(state = it) }

    @Test
    fun outlinedCell() = captureCells("outlined_cell") { OutlinedOtpCell(state = it) }

    @Test
    fun underlineCell() = captureCells("underline_cell") { UnderlineOtpCell(state = it) }

    @Test
    fun maskedField() = capture("masked_field") {
        OtpInputField(
            state = TextFieldState("1234"),
            otpLength = 6,
            maskConfig = OtpInputDefaults.maskConfig(isMasked = true),
        )
    }

    private fun captureCells(name: String, cell: @Composable (OtpCellState) -> Unit) = capture(name) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CellStates.forEach { cell(it) }
        }
    }

    private fun capture(name: String, content: @Composable () -> Unit) {
        // The cursor blinks and error cells shake, so freeze time at a point where both are settled.
        rule.mainClock.autoAdvance = false
        rule.setContent {
            MaterialTheme {
                Surface(modifier = Modifier.padding(8.dp)) { content() }
            }
        }
        rule.mainClock.advanceTimeBy(SETTLE_TIME_MILLIS)
        rule.onRoot().captureRoboImage("src/test/screenshots/$name.png")
    }

    private companion object {
        const val SETTLE_TIME_MILLIS = 2_000L

        val CellStates = listOf(
            OtpCellState(index = 0, char = '4', isFocused = false, isError = false, isEnabled = true),
            OtpCellState(index = 1, char = null, isFocused = true, isError = false, isEnabled = true),
            OtpCellState(index = 2, char = null, isFocused = false, isError = false, isEnabled = true),
            OtpCellState(index = 3, char = '7', isFocused = false, isError = true, isEnabled = true),
            OtpCellState(index = 4, char = '2', isFocused = false, isError = false, isEnabled = false),
        )
    }
}
