package com.burkido.otp.input

import androidx.compose.runtime.Immutable

/**
 * Hides entered digits behind [maskCharacter] after they have been visible for [maskDelay]
 * milliseconds, the way password fields on phones do. Get one from [OtpInputDefaults.maskConfig].
 */
@Immutable
public class OtpMaskConfig(
    public val isMasked: Boolean,
    public val maskCharacter: Char,
    public val maskDelay: Long,
) {

    public fun copy(
        isMasked: Boolean = this.isMasked,
        maskCharacter: Char = this.maskCharacter,
        maskDelay: Long = this.maskDelay,
    ): OtpMaskConfig = OtpMaskConfig(isMasked = isMasked, maskCharacter = maskCharacter, maskDelay = maskDelay)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OtpMaskConfig) return false
        return isMasked == other.isMasked && maskCharacter == other.maskCharacter && maskDelay == other.maskDelay
    }

    override fun hashCode(): Int {
        var result = isMasked.hashCode()
        result = 31 * result + maskCharacter.hashCode()
        result = 31 * result + maskDelay.hashCode()
        return result
    }
}
