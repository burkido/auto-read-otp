plugins {
    id("burkido.android.library")
}

android {
    namespace = "com.burkido.otp.timer"
    resourcePrefix = "otp_timer_"
}

dokka {
    moduleName.set("otp-timer")
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)

    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.test.ext.junit)
}
