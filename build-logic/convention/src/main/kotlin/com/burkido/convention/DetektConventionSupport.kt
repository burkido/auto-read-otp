package com.burkido.convention

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

internal fun Project.configureDetektConvention() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        "detektPlugins"(libs.findLibrary("detekt-formatting").get())
    }
}