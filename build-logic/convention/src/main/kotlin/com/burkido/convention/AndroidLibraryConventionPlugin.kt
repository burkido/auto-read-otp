package com.burkido.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.dokka")
        pluginManager.apply("org.jetbrains.kotlinx.kover")

        extensions.configure<LibraryExtension> {
            configureAndroidCommon(this)
        }

        configureDokka()
        configureApiGuardrails()
    }
}

private fun Project.configureApiGuardrails() {
    extensions.configure<KotlinAndroidProjectExtension> {
        explicitApi()
    }
    configureApiValidation()
}

private fun Project.configureDokka() {
    val modulePath = projectDir.relativeTo(rootDir).invariantSeparatorsPath

    extensions.configure<DokkaExtension> {
        dokkaSourceSets.configureEach {
            sourceLink {
                localDirectory.set(file("src/main/kotlin"))
                remoteUrl("$REPOSITORY_URL/blob/main/$modulePath/src/main/kotlin")
                remoteLineSuffix.set("#L")
            }
        }
    }
}

private const val REPOSITORY_URL = "https://github.com/burkido/auto-read-otp"
