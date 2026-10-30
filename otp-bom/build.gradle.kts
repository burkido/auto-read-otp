plugins {
    `java-platform`
    id("burkido.publish")
}

dependencies {
    constraints {
        api(projects.otpSms)
        api(projects.otpUi)
    }
}
