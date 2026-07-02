package com.burkido.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get

/**
 * Configures Maven publishing so JitPack can serve this module as an artifact.
 *
 * JitPack builds multi-module Gradle projects by running `publishToMavenLocal`
 * and serves every published module under the group `com.github.<user>.<repo>`
 * with the module name as artifactId. The tag being built is exposed through
 * the `VERSION` environment variable.
 */
class PublishConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("maven-publish")

        group = Publishing.GROUP_ID
        version = Publishing.resolveVersion()

        // Android library modules publish their release variant with sources.
        // The BOM module (java-platform) declares its own publication instead.
        pluginManager.withPlugin("com.android.library") {
            extensions.configure<LibraryExtension> {
                publishing {
                    singleVariant("release") {
                        withSourcesJar()
                    }
                }
            }

            afterEvaluate {
                extensions.configure<PublishingExtension> {
                    publications.create<MavenPublication>("release") {
                        from(components["release"])
                        Publishing.configurePom(this, project.name)
                    }
                }
            }
        }
    }
}

object Publishing {
    /** JitPack multi-module group: com.github.<user>.<repo> */
    const val GROUP_ID = "com.github.burkido.auto-read-otp"

    /**
     * Resolves the artifact version:
     * 1. `VERSION` — set by JitPack to the tag being built
     * 2. `RELEASE_VERSION` — set by the GitHub release workflow for verification builds
     * 3. fallback for local development
     */
    fun resolveVersion(): String =
        System.getenv("VERSION")
            ?: System.getenv("RELEASE_VERSION")
            ?: "local-SNAPSHOT"

    fun configurePom(publication: MavenPublication, moduleName: String) {
        publication.pom {
            name.set(moduleName)
            description.set("Auto Read OTP — Jetpack Compose OTP input and SMS Consent auto-read ($moduleName)")
            url.set("https://github.com/burkido/auto-read-otp")
            licenses {
                license {
                    name.set("MIT License")
                    url.set("https://github.com/burkido/auto-read-otp/blob/main/LICENSE")
                }
            }
            developers {
                developer {
                    id.set("burkido")
                    name.set("Burak Karaduman")
                }
            }
            scm {
                connection.set("scm:git:github.com/burkido/auto-read-otp.git")
                developerConnection.set("scm:git:ssh://github.com/burkido/auto-read-otp.git")
                url.set("https://github.com/burkido/auto-read-otp")
            }
        }
    }
}
