plugins {
    `java-platform`
    id("maven-publish")
}

dependencies {
    constraints {
        api(projects.otpSms)
        api(projects.otpInput)
        api(projects.otpTimer)
    }
}

publishing {
    publications {
        create<MavenPublication>("release") {
            from(components["javaPlatform"])
        }
    }
}
