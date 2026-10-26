package com.burkido.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal fun Project.configureAndroidCommon(extension: CommonExtension) {
    pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
    configureDetekt()

    extension.compileSdk = Configs.COMPILE_SDK
    extension.defaultConfig.minSdk = Configs.MIN_SDK

    extension.compileOptions.sourceCompatibility = JavaVersion.VERSION_17
    extension.compileOptions.targetCompatibility = JavaVersion.VERSION_17

    extension.buildFeatures.compose = true

    // Robolectric needs merged resources to inflate Compose and resolve strings.
    extension.testOptions.unitTests.isIncludeAndroidResources = true

    extension.lint.apply {
        warningsAsErrors = true
        abortOnError = true
        lintConfig = rootDir.resolve("config/lint/lint.xml")
    }
}
