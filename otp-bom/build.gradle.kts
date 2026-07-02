plugins {
    `java-platform`
    id("burkido.publish")
}

// Constraints derive group/artifact/version from the sibling projects, so the
// BOM always pins the exact versions built from the same tag.
dependencies {
    constraints {
        api(project(":sms-reader"))
        api(project(":otp-input-kit"))
    }
}

publishing {
    publications {
        create<MavenPublication>("release") {
            from(components["javaPlatform"])
        }
    }
}
