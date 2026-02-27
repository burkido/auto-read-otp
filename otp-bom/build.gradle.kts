plugins {
    `java-platform`
    id("maven-publish")
}

javaPlatform {
    allowDependencies()
}

dependencies {
    constraints {
        api("com.github.burkido:auto-read-otp:1.0.3")
        api("com.github.burkido:otp-input-kit:1.0.0")
    }
}

publishing {
    publications {
        create<MavenPublication>("release") {
            from(components["javaPlatform"])
            groupId = "com.github.burkido"
            artifactId = "otp-bom"
            version = "1.0.0"
        }
    }
}
