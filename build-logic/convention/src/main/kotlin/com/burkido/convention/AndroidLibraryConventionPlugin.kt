package com.burkido.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.dokka.gradle.DokkaExtension
import java.net.URI

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        pluginManager.apply("dev.detekt")
        pluginManager.apply("org.jetbrains.dokka")

        // AGP 9.x bundles Kotlin support and blocks applying org.jetbrains.kotlin.android,
        // so Dokka cannot auto-discover source sets via KGP. Register them manually instead.
        extensions.configure<DokkaExtension> {
            dokkaSourceSets.register("main") {
                sourceRoots.from(file("src/main/java"), file("src/main/kotlin"))
                sourceLink {
                    localDirectory.set(project.file("src/main/kotlin"))
                    remoteUrl.set(URI("https://github.com/burkido/auto-read-otp/tree/main/${project.name}/src/main/kotlin"))
                    remoteLineSuffix.set("#L")
                }
            }
        }

        configureDetektConvention()

        extensions.configure<LibraryExtension> {
            compileSdk = Configs.COMPILE_SDK

            defaultConfig {
                minSdk = Configs.MIN_SDK
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                consumerProguardFiles("consumer-rules.pro")
            }

            buildTypes {
                release {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            buildFeatures {
                compose = true
            }
        }
    }
}
