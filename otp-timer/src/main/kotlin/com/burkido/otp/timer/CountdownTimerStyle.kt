package com.burkido.otp.timer

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

/**
 * Colors and text for [CountdownTimer]. Get one from [CountdownTimerDefaults.style].
 *
 * @property backgroundColor Fill of each time box.
 * @property textStyle Style of the digits, including their size and color.
 * @property separatorColor Color of the colons between boxes.
 */
@Immutable
public class CountdownTimerStyle(
    public val backgroundColor: Color,
    public val textStyle: TextStyle,
    public val separatorColor: Color,
) {

    public fun copy(
        backgroundColor: Color = this.backgroundColor,
        textStyle: TextStyle = this.textStyle,
        separatorColor: Color = this.separatorColor,
    ): CountdownTimerStyle = CountdownTimerStyle(
        backgroundColor = backgroundColor,
        textStyle = textStyle,
        separatorColor = separatorColor,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CountdownTimerStyle) return false
        return backgroundColor == other.backgroundColor &&
            textStyle == other.textStyle &&
            separatorColor == other.separatorColor
    }

    override fun hashCode(): Int {
        var result = backgroundColor.hashCode()
        result = 31 * result + textStyle.hashCode()
        result = 31 * result + separatorColor.hashCode()
        return result
    }
}
