plugins {
    id("burkido.android.library")
    id("burkido.android.compose")
    id("burkido.android.publish")
    id("burkido.detekt")
}

android {
    namespace = "com.burkido.autoreadotp"
}

dokka {
    moduleName.set("SMS Reader")
}

libraryPublish {
    artifactId.set("auto-read-otp")
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.activity.compose)

    // Play Services Auth
    implementation(libs.play.services.auth)

    // Unit tests
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.test.ext.junit)

    // Android / Compose UI tests
    androidTestImplementation(libs.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}