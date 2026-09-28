plugins {
    id("burkido.android.library")
}

android {
    namespace = "com.burkido.otp.sms"
    resourcePrefix = "otp_sms_"
}

dokka {
    moduleName.set("otp-sms")
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.activity.compose)
    implementation(libs.play.services.auth.api.phone)

    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.test.ext.junit)
}
