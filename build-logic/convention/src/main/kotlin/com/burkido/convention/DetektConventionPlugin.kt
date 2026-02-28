package com.burkido.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("dev.detekt")

//        extensions.configure<DetektExtension> {
//            buildUponDefaultConfig = true
//            allRules = false
//            parallel = true
//            config.setFrom("${rootProject.projectDir}/config/detekt/detekt.yml")
//            basePath.set(rootProject.projectDir)
//        }

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        dependencies {
            "detektPlugins"(libs.findLibrary("detekt-formatting").get())
        }
    }
}
