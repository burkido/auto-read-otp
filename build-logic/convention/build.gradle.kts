plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "burkido.android.library"
            implementationClass = "com.burkido.convention.AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "burkido.android.compose"
            implementationClass = "com.burkido.convention.ComposeConventionPlugin"
        }
    }
}
