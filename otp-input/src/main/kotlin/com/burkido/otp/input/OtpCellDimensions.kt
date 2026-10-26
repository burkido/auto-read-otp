package com.burkido.otp.input

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

/**
 * Sizes used by an OTP cell. Get one from [OtpInputDefaults.dimensions].
 *
 * @property spacing Gap between cells when no separator slot is given.
 */
@Immutable
public class OtpCellDimensions(
    public val cellWidth: Dp,
    public val cellHeight: Dp,
    public val borderWidth: Dp,
    public val focusedBorderWidth: Dp,
    public val cornerRadius: Dp,
    public val spacing: Dp,
    public val textSize: TextUnit,
) {

    public fun copy(
        cellWidth: Dp = this.cellWidth,
        cellHeight: Dp = this.cellHeight,
        borderWidth: Dp = this.borderWidth,
        focusedBorderWidth: Dp = this.focusedBorderWidth,
        cornerRadius: Dp = this.cornerRadius,
        spacing: Dp = this.spacing,
        textSize: TextUnit = this.textSize,
    ): OtpCellDimensions = OtpCellDimensions(
        cellWidth = cellWidth,
        cellHeight = cellHeight,
        borderWidth = borderWidth,
        focusedBorderWidth = focusedBorderWidth,
        cornerRadius = cornerRadius,
        spacing = spacing,
        textSize = textSize,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OtpCellDimensions) return false
        return cellWidth == other.cellWidth &&
            cellHeight == other.cellHeight &&
            borderWidth == other.borderWidth &&
            focusedBorderWidth == other.focusedBorderWidth &&
            cornerRadius == other.cornerRadius &&
            spacing == other.spacing &&
            textSize == other.textSize
    }

    override fun hashCode(): Int {
        var result = cellWidth.hashCode()
        result = 31 * result + cellHeight.hashCode()
        result = 31 * result + borderWidth.hashCode()
        result = 31 * result + focusedBorderWidth.hashCode()
        result = 31 * result + cornerRadius.hashCode()
        result = 31 * result + spacing.hashCode()
        result = 31 * result + textSize.hashCode()
        return result
    }
}
