plugins {
    id("burkido.android.library")
    id("burkido.publish")
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.burkido.otp.timer"
    resourcePrefix = "otp_timer_"
}

roborazzi {
    outputDir.set(file("src/test/screenshots"))
}

dokka {
    moduleName.set("otp-timer")
}

dependencies {
    api(platform(libs.compose.bom))
    api(libs.compose.runtime)
    api(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)

    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.test.ext.junit)
    testImplementation(libs.espresso.core)
    testImplementation(libs.compose.ui.test.junit4)
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.compose)
    debugImplementation(libs.compose.ui.test.manifest)
}
