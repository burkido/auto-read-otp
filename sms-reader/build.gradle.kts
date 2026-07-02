plugins {
    id("burkido.android.library")
    id("burkido.publish")
}

android {
    namespace = "com.burkido.autoreadotp"
}

dokka {
    moduleName.set("SMS Reader")
}

dependencies {
    // @Composable in the public API surface -> consumers need these at compile time
    api(platform(libs.compose.bom))
    api(libs.compose.runtime)

    implementation(libs.compose.ui)
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