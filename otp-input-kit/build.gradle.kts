plugins {
    id("burkido.android.library")
    id("burkido.android.compose")
}

android {
    namespace = "com.burkido.otpinputkit"
}

libraryPublish {
    artifactId.set("otp-input-kit")
}

dependencies {
    implementation(libs.core.ktx)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.animation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
}
