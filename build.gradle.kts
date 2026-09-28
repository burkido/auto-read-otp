plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.dokka)
}

dokka {
    moduleName.set("Auto Read OTP")
}

dependencies {
    dokka(projects.otpSms)
    dokka(projects.otpInput)
    dokka(projects.otpTimer)
}
