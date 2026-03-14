plugins {
    id("burkido.android.library")
}

android {
    namespace = "com.burkido.autoreadotp"
}

dokka {
    moduleName.set("SMS Reader")
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