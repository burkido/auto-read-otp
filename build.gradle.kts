plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.kover)
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.maven.publish) apply false
}

dokka {
    moduleName.set("Auto Read OTP")
}

dependencies {
    dokka(projects.otpSms)
    dokka(projects.otpUi)

    kover(projects.otpSms)
    kover(projects.otpUi)
}

kover {
    reports {
        verify {
            rule {
                // A little under the current line coverage, so it only fails when coverage really drops.
                minBound(75)
            }
        }
        filters {
            excludes {
                // Previews are for the IDE and never run in tests.
                annotatedBy("androidx.compose.ui.tooling.preview.Preview")
            }
        }
    }
}
