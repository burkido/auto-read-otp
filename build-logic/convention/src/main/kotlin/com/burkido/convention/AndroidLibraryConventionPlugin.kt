package com.burkido.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.dokka.gradle.DokkaExtension

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.dokka")

        extensions.configure<LibraryExtension> {
            configureAndroidCommon(this)
        }

        configureDokka()
    }
}

private fun Project.configureDokka() {
    val modulePath = projectDir.relativeTo(rootDir).invariantSeparatorsPath

    // AGP 9 ships its own Kotlin integration, so Dokka can't discover source sets through KGP.
    extensions.configure<DokkaExtension> {
        dokkaSourceSets.register("main") {
            sourceRoots.from(file("src/main/kotlin"))
            sourceLink {
                localDirectory.set(file("src/main/kotlin"))
                remoteUrl("$REPOSITORY_URL/blob/main/$modulePath/src/main/kotlin")
                remoteLineSuffix.set("#L")
            }
        }
    }
}

private const val REPOSITORY_URL = "https://github.com/burkido/auto-read-otp"
