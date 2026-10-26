package com.burkido.otp.timer

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/**
 * Spacing for [CountdownTimer]. Get one from [CountdownTimerDefaults.dimensions].
 *
 * @property verticalPadding Padding above and below the digits inside each box.
 * @property horizontalPadding Padding left and right of the digits inside each box.
 * @property boxSpacing Gap between a box and the colon next to it.
 */
@Immutable
public class CountdownTimerDimensions(
    public val verticalPadding: Dp,
    public val horizontalPadding: Dp,
    public val boxSpacing: Dp,
) {

    public fun copy(
        verticalPadding: Dp = this.verticalPadding,
        horizontalPadding: Dp = this.horizontalPadding,
        boxSpacing: Dp = this.boxSpacing,
    ): CountdownTimerDimensions = CountdownTimerDimensions(
        verticalPadding = verticalPadding,
        horizontalPadding = horizontalPadding,
        boxSpacing = boxSpacing,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CountdownTimerDimensions) return false
        return verticalPadding == other.verticalPadding &&
            horizontalPadding == other.horizontalPadding &&
            boxSpacing == other.boxSpacing
    }

    override fun hashCode(): Int {
        var result = verticalPadding.hashCode()
        result = 31 * result + horizontalPadding.hashCode()
        result = 31 * result + boxSpacing.hashCode()
        return result
    }
}
