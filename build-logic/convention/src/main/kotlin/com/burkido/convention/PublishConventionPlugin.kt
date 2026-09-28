package com.burkido.convention

import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.JavaPlatform
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SourcesJar
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Publishes a module to Maven Central. Coordinates and POM details come from gradle.properties:
 * GROUP and VERSION_NAME at the root, POM_ARTIFACT_ID and POM_NAME in each module.
 */
class PublishConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.vanniktech.maven.publish.base")

        extensions.configure<MavenPublishBaseExtension> {
            publishToMavenCentral(automaticRelease = true)
            pomFromGradleProperties()

            // Only the release workflow has the key. Pull request builds still run publishToMavenLocal
            // to prove the artifacts assemble, and Gradle would refuse to publish them unsigned.
            if (providers.gradleProperty("signingInMemoryKey").isPresent) {
                signAllPublications()
            }
        }

        pluginManager.withPlugin("com.android.library") {
            extensions.configure<MavenPublishBaseExtension> {
                configure(
                    AndroidSingleVariantLibrary(
                        javadocJar = JavadocJar.Dokka("dokkaGeneratePublicationHtml"),
                        sourcesJar = SourcesJar.Sources(),
                    ),
                )
            }
        }

        pluginManager.withPlugin("java-platform") {
            extensions.configure<MavenPublishBaseExtension> {
                configure(JavaPlatform())
            }
        }
    }
}
