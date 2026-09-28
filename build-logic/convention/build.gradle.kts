plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.dokka.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    implementation(libs.binary.compatibility.validator)
    implementation(libs.kotlin.metadata.jvm)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "burkido.android.application"
            implementationClass = "com.burkido.convention.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "burkido.android.library"
            implementationClass = "com.burkido.convention.AndroidLibraryConventionPlugin"
        }
    }
}
