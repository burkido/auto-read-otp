package com.burkido.autoreadotp

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ExtensionsTest {

    /** A simple Parcelable for testing. */
    data class TestParcelable(val value: String) : Parcelable {
        override fun describeContents(): Int = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(value)
        }

        companion object CREATOR : Parcelable.Creator<TestParcelable> {
            override fun createFromParcel(parcel: Parcel): TestParcelable =
                TestParcelable(parcel.readString()!!)

            override fun newArray(size: Int): Array<TestParcelable?> = arrayOfNulls(size)
        }
    }

    @Test
    fun `parcelable returns correct value when key exists`() {
        val expected = TestParcelable("hello")
        val bundle = Bundle().apply { putParcelable("key", expected) }

        val result = bundle.parcelable<TestParcelable>("key")

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `parcelable returns null when key does not exist`() {
        val bundle = Bundle()

        val result = bundle.parcelable<TestParcelable>("missing_key")

        assertThat(result).isNull()
    }

    @Test
    fun `parcelable returns null for wrong type`() {
        val bundle = Bundle().apply { putString("key", "not a parcelable") }

        val result = bundle.parcelable<TestParcelable>("key")

        assertThat(result).isNull()
    }
}
