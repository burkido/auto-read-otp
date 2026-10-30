package com.burkido.otp.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/** Padding inside each OTP cell. Get one from [OtpInputDefaults.contentPadding]. */
@Immutable
public class OtpCellPadding(public val horizontal: Dp, public val vertical: Dp) {

    public fun copy(horizontal: Dp = this.horizontal, vertical: Dp = this.vertical): OtpCellPadding =
        OtpCellPadding(horizontal = horizontal, vertical = vertical)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OtpCellPadding) return false
        return horizontal == other.horizontal && vertical == other.vertical
    }

    override fun hashCode(): Int = 31 * horizontal.hashCode() + vertical.hashCode()
}
