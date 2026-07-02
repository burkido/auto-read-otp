package com.burkido.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import java.net.URI

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        pluginManager.apply("dev.detekt")
        pluginManager.apply("org.jetbrains.dokka")

        // With release-variant publishing configured, Dokka discovers the
        // 'release' source set on its own — only source links need configuring.
        extensions.configure<DokkaExtension> {
            dokkaSourceSets.configureEach {
                sourceLink {
                    localDirectory.set(project.file("src/main/java"))
                    remoteUrl.set(URI("https://github.com/burkido/auto-read-otp/tree/main/${project.name}/src/main/java"))
                    remoteLineSuffix.set("#L")
                }
            }
        }

        configureDetektConvention()

        // Library modules must declare their public API surface consciously.
        // (AGP 9 built-in Kotlin blocks the KGP extension, so set the compiler
        // flag on the compile tasks directly.) Test compilations are excluded,
        // matching the behavior of KGP's explicitApi().
        tasks.withType<KotlinJvmCompile>().configureEach {
            if (!name.contains("Test")) {
                compilerOptions.freeCompilerArgs.add("-Xexplicit-api=strict")
            }
        }

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
