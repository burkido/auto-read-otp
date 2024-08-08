package com.burkido.sms_reader

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.BundleCompat

internal inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? =
    BundleCompat.getParcelable(this, key, T::class.java)