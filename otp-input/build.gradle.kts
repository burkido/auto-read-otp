plugins {
    id("burkido.android.library")
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.burkido.otp.input"
    resourcePrefix = "otp_input_"
}

roborazzi {
    outputDir.set(file("src/test/screenshots"))
}

dokka {
    moduleName.set("otp-input")
}

dependencies {
    api(platform(libs.compose.bom))
    api(libs.compose.foundation)
    api(libs.compose.ui)
    implementation(libs.compose.animation)
    implementation(libs.compose.material3)

    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.test.ext.junit)
    testImplementation(libs.compose.ui.test.junit4)
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.compose)
    debugImplementation(libs.compose.ui.test.manifest)
}
