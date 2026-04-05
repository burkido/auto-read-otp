package com.burkido.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.dokka.gradle.DokkaExtension
import java.net.URI


class PublishConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.dokka")

        // AGP 9.x bundles Kotlin support and blocks applying org.jetbrains.kotlin.android,
        // so Dokka cannot auto-discover source sets via KGP. Register them manually instead.
        extensions.configure<DokkaExtension> {
            dokkaSourceSets.register("main") {
                sourceRoots.from(file("src/main/java"), file("src/main/kotlin"))
                sourceLink {
                    localDirectory.set(project.file("src/main/java"))
                    remoteUrl.set(URI("https://github.com/burkido/auto-read-otp/tree/main/${project.name}/src/main/java"))
                    remoteLineSuffix.set("#L")
                }
            }
        }
    }
}
