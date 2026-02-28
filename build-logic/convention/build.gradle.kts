plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.dokka.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
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
        register("androidPublish") {
            id = "burkido.android.publish"
            implementationClass = "com.burkido.convention.PublishConventionPlugin"
        }
        register("detekt") {
            id = "burkido.detekt"
            implementationClass = "com.burkido.convention.DetektConventionPlugin"
        }
    }
}
