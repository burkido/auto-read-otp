package com.burkido.otp.sms

import android.content.Intent
import android.content.pm.PackageManager

/**
 * Checks that a consent intent really opens the Play services consent sheet.
 *
 * The intent arrives inside a broadcast, and launching whatever it points at would let another app
 * start activities or read content URIs with our identity. Google's guidance is to check the target
 * component and refuse any URI grant flags before calling startActivity.
 */
internal fun Intent.isTrustedConsentIntent(packageManager: PackageManager): Boolean {
    val target = resolveActivity(packageManager) ?: return false
    val grantsUriAccess = (flags and URI_GRANT_FLAGS) != 0
    return target.packageName == PLAY_SERVICES_PACKAGE &&
        target.className == CONSENT_ACTIVITY &&
        !grantsUriAccess
}

internal const val PLAY_SERVICES_PACKAGE = "com.google.android.gms"
internal const val CONSENT_ACTIVITY = "com.google.android.gms.auth.api.phone.ui.UserConsentPromptActivity"

private const val URI_GRANT_FLAGS =
    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
