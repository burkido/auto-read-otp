package com.burkido.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureDetekt() {
    pluginManager.apply("dev.detekt")
    dependencies {
        "detektPlugins"(libs.findLibrary("detekt-formatting").get())
    }
}
