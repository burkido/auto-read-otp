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
    implementation(libs.play.services.auth.api.phone)

    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.test.ext.junit)
}
