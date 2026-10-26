package com.burkido.otp.input

import androidx.compose.runtime.Immutable

/**
 * What a single cell needs to know to draw itself. [OtpInputField] passes one to its `cell` slot.
 *
 * @property index Position of the cell, starting at 0.
 * @property char The character in this cell, or null while it's empty.
 * @property isFocused True for the cell the next digit goes into, while the field has focus.
 * @property isError True while the field is in the error state.
 * @property isEnabled False when the field is disabled.
 */
@Immutable
public class OtpCellState(
    public val index: Int,
    public val char: Char?,
    public val isFocused: Boolean,
    public val isError: Boolean,
    public val isEnabled: Boolean,
) {

    public val isFilled: Boolean get() = char != null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OtpCellState) return false
        return index == other.index &&
            char == other.char &&
            isFocused == other.isFocused &&
            isError == other.isError &&
            isEnabled == other.isEnabled
    }

    override fun hashCode(): Int {
        var result = index
        result = 31 * result + char.hashCode()
        result = 31 * result + isFocused.hashCode()
        result = 31 * result + isError.hashCode()
        result = 31 * result + isEnabled.hashCode()
        return result
    }

    override fun toString(): String =
        "OtpCellState(index=$index, char=$char, isFocused=$isFocused, isError=$isError, isEnabled=$isEnabled)"
}
