package com.burkido.otp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "w320dp-h120dp-xhdpi")
class CountdownTimerScreenshotTest {

    @get:Rule
    val rule = createComposeRule()

    private val state = CountdownTimerState(endTimeMillis = 3_723_000, clock = { 0L })

    @Test
    fun defaultStyle() {
        rule.setContent {
            MaterialTheme {
                Surface(modifier = Modifier.padding(8.dp)) { CountdownTimer(state = state) }
            }
        }
        rule.onRoot().captureRoboImage("src/test/screenshots/timer_default.png")
    }

    @Test
    fun customStyle() {
        rule.setContent {
            MaterialTheme {
                Surface(modifier = Modifier.padding(8.dp)) {
                    CountdownTimer(
                        state = state,
                        style = CountdownTimerDefaults.style(
                            backgroundColor = Color(0xFFFFEBEE),
                            textSize = 20.sp,
                            separatorColor = Color(0xFFD32F2F),
                        ),
                        dimensions = CountdownTimerDefaults.dimensions(
                            verticalPadding = 6.dp,
                            horizontalPadding = 10.dp,
                        ),
                    )
                }
            }
        }
        rule.onRoot().captureRoboImage("src/test/screenshots/timer_custom.png")
    }
}
