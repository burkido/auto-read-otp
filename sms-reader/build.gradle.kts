plugins {
    id("burkido.android.library")
    id("burkido.android.compose")
}

android {
    namespace = "com.burkido.autoreadotp"
}

libraryPublish {
    artifactId.set("auto-read-otp")
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.activity.compose)

    // Play Services Auth
    implementation(libs.play.services.auth)
}