package com.burkido.convention

import dev.detekt.gradle.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureDetekt() {
    pluginManager.apply("dev.detekt")

    extensions.configure<DetektExtension> {
        // Our file only lists overrides. Everything else, including the Compose rules, keeps its defaults.
        buildUponDefaultConfig.set(true)
        config.setFrom(rootDir.resolve("config/detekt/detekt.yml"))
    }

    dependencies {
        "detektPlugins"(libs.findLibrary("detekt-formatting").get())
        "detektPlugins"(libs.findLibrary("detekt-compose-rules").get())
    }
}
