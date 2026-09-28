plugins {
    id("burkido.android.application")
}

android {
    namespace = "com.burkido.otp.sample"

    defaultConfig {
        applicationId = "com.burkido.otp.sample"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(projects.otpSms)
    implementation(projects.otpInput)
    implementation(projects.otpTimer)

    implementation(libs.core.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
}
