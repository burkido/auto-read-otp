package com.burkido.otp.sample

enum class Demo(val title: String, val summary: String, val modules: List<String>) {
    SignIn(
        title = "Sign-in flow",
        summary = "Phone verification end to end: SMS autofill, expiry, resend, a wrong code and success.",
        modules = listOf("otp-ui", "otp-sms"),
    ),
    Playground(
        title = "Playground",
        summary = "Change every option live and copy the code that draws it.",
        modules = listOf("otp-ui"),
    ),
    Gallery(
        title = "Gallery",
        summary = "Seven designs built on the cell slot, from a PIN pad to a terminal.",
        modules = listOf("otp-ui"),
    ),
    SmsLab(
        title = "SMS lab",
        summary = "Watch the consent flow event by event and send yourself a test message.",
        modules = listOf("otp-sms"),
    ),
}
