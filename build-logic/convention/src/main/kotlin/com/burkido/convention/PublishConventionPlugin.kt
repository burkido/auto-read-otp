package com.burkido.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get


class PublishConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("maven-publish")
            apply("org.jetbrains.dokka")
        }

        extensions.configure<LibraryExtension> {
            publishing {
                singleVariant("release") {
                    withSourcesJar()
                }
            }
        }

        val publishExtension = extensions.create<LibraryPublishExtension>("libraryPublish").apply {
            groupId.convention("com.github.burkido")
            artifactId.convention(project.name)
            version.convention("1.0.0")
        }

        // Package Dokka HTML output as javadoc.jar for Maven publishing
        val dokkaHtmlJar = tasks.register("dokkaHtmlJar", Jar::class.java) {
            description = "A Javadoc JAR containing Dokka HTML documentation"
            from(tasks.named("dokkaGeneratePublicationHtml").map { it.outputs })
            archiveClassifier.set("javadoc")
        }

        afterEvaluate {
            extensions.configure<PublishingExtension> {
                publications {
                    create<MavenPublication>("release") {
                        from(components["release"])
                        artifact(dokkaHtmlJar)
                        groupId = publishExtension.groupId.get()
                        artifactId = publishExtension.artifactId.get()
                        version = publishExtension.version.get()
                    }
                }
            }
        }
    }
}
