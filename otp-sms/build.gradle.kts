plugins {
    id("burkido.android.library")
    id("burkido.publish")
}

android {
    namespace = "com.burkido.otp.sms"
    resourcePrefix = "otp_sms_"
}

dokka {
    moduleName.set("otp-sms")
}

dependencies {
    api(platform(libs.compose.bom))
    api(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(libs.activity.compose)
    implementation(libs.core.ktx)
    implementation(libs.play.services.auth.api.phone)

    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.test.ext.junit)
    testImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.test.manifest)
}
