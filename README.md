# SMS Consent API Library

**SMS Consent API Library** simplifies the process of integrating Google's SMS Consent API into your Android application. This library handles the complexities involved in requesting and retrieving SMS messages containing one-time passwords (OTP) and displays them within the app without interrupting the user experience.

[![Jitpack](https://jitpack.io/v/burkido/auto-read-otp.svg)](https://jitpack.io/#burkido/auto-read-otp)

### Gradle
Add the dependency below to your **module**'s `build.gradle` file:

```kotlin
dependencies {
    implementation("com.github.burkido:auto-read-otp:1.0.3")
}
```

## Usage

Use the `SmsUserConsent` composable where you want to obtain SMS consent, such as in a screen with an OTP text field. You can see a sample implementation in the example app [here](https://github.com/burkido/auto-read-otp/blob/main/app/src/main/java/com/burkido/verificationcodereader/VerificationScreen.kt).

```kotlin
SmsUserConsent(
    smsCodeLength = OTP_LENGTH,
    onOTPReceived = { otp ->
        Log.d("MainActivity", "SMS Received: $otp")
        code = otp
    },
    onError = { error ->
        Log.e("MainActivity", "Error: $error")
    }
)
```

https://github.com/user-attachments/assets/81cc0afd-e6f7-4bce-bc6a-ee2b5acc7fa2
