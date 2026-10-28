# Auto Read OTP

[![Maven Central](https://img.shields.io/maven-central/v/io.github.burkido/otp-bom)](https://central.sonatype.com/namespace/io.github.burkido)
[![CI](https://github.com/burkido/auto-read-otp/actions/workflows/ci.yml/badge.svg)](https://github.com/burkido/auto-read-otp/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Jetpack Compose building blocks for one-time code screens: an OTP input with a cell per digit, automatic SMS reading through Google's SMS User Consent API, and a countdown timer for code expiry.

https://github.com/user-attachments/assets/81cc0afd-e6f7-4bce-bc6a-ee2b5acc7fa2

| Artifact | What it gives you |
|---|---|
| `otp-input` | `OtpInputField` with default, outlined and underline cells, masking, autofill and screen reader support |
| `otp-sms` | `SmsUserConsentEffect`, which reads the code from the next SMS after the user agrees. No SMS permission needed |
| `otp-timer` | `CountdownTimer`, a `HH : MM : SS` countdown that hides itself when time is up |
| `otp-bom` | Keeps the three on matching versions |

API reference: [burkido.github.io/auto-read-otp](https://burkido.github.io/auto-read-otp/)

## Installation

The libraries are on Maven Central, so no extra repository is needed. They require `minSdk` 24, `compileSdk` 37 and Jetpack Compose.

In `gradle/libs.versions.toml`:

<!-- x-release-please-start-version -->
```toml
[versions]
otp = "1.2.0"

[libraries]
otp-bom = { module = "io.github.burkido:otp-bom", version.ref = "otp" }
otp-input = { module = "io.github.burkido:otp-input" }
otp-sms = { module = "io.github.burkido:otp-sms" }
otp-timer = { module = "io.github.burkido:otp-timer" }
```
<!-- x-release-please-end -->

In your module's `build.gradle.kts`, add only the parts you use:

```kotlin
dependencies {
    implementation(platform(libs.otp.bom))
    implementation(libs.otp.input)
    implementation(libs.otp.sms)
    implementation(libs.otp.timer)
}
```

Without a version catalog:

<!-- x-release-please-start-version -->
```kotlin
dependencies {
    implementation(platform("io.github.burkido:otp-bom:1.2.0"))
    implementation("io.github.burkido:otp-input")
    implementation("io.github.burkido:otp-sms")
    implementation("io.github.burkido:otp-timer")
}
```
<!-- x-release-please-end -->

Coming from 1.x? Coordinates, packages and several APIs changed in 2.0. See the [migration guide](docs/MIGRATION.md).

## A complete verification screen

```kotlin
@Composable
fun VerifyScreen(onVerify: (String) -> Unit, onResend: () -> Unit) {
    val code = rememberTextFieldState()
    var attempt by rememberSaveable { mutableIntStateOf(0) }
    var canResend by rememberSaveable { mutableStateOf(false) }
    var expiresAt by rememberSaveable { mutableLongStateOf(System.currentTimeMillis() + 2.minutes.inWholeMilliseconds) }
    val timer = rememberCountdownTimerState(endTimeMillis = expiresAt)

    OtpInputField(
        state = code,
        otpLength = 6,
        onComplete = onVerify,
    )

    SmsUserConsentEffect(
        codeLength = 6,
        onCodeReceive = { received -> code.fillOtp(received, 6) },
        onError = { error -> Log.w("Verify", "No code from SMS: $error") },
        restartKey = attempt,
    )

    CountdownTimer(state = timer, onTimerFinish = { canResend = true })

    TextButton(
        enabled = canResend,
        onClick = {
            code.clearOtp()
            attempt++
            expiresAt = System.currentTimeMillis() + 2.minutes.inWholeMilliseconds
            canResend = false
            onResend()
        },
    ) {
        Text("Resend code")
    }
}
```

## OTP input

```kotlin
val code = rememberTextFieldState()

OtpInputField(
    state = code,
    otpLength = 6,
    onComplete = { otp -> viewModel.verify(otp) },
)
```

The field only accepts ASCII digits and never holds more than `otpLength` of them, however the text arrives. Pasting "Your code is 123-456" leaves `123456`. `onComplete` runs when the last digit goes in. It doesn't run again for a value restored after rotation or process death, so the code isn't submitted twice.

Useful helpers on `TextFieldState`: `fillOtp(code, length)`, `clearOtp()`, `otpValue` and `isOtpComplete(length)`.

### Cell styles

Pass a `cell` to switch the look. The built-in cells are `DefaultOtpCell`, `OutlinedOtpCell` and `UnderlineOtpCell`.

```kotlin
OtpInputField(state = code, otpLength = 6) { cell ->
    UnderlineOtpCell(state = cell)
}
```

`cell` receives an `OtpCellState` with `index`, `char`, `isFocused`, `isError` and `isEnabled`, so you can draw anything you like.

### Customizing

Every style value comes from `OtpInputDefaults`, and each function lets you override just what you need:

```kotlin
OtpInputField(
    state = code,
    otpLength = 6,
    isError = uiState.wrongCode,
    placeholder = "-",
    colors = OtpInputDefaults.colors(focusedBorderColor = Color(0xFF00D4FF)),
    dimensions = OtpInputDefaults.dimensions(cellWidth = 52.dp, cellHeight = 60.dp),
    animationSpec = OtpInputDefaults.animationSpec(cellEntryAnimation = CellAnimation.SlideUp),
    maskConfig = OtpInputDefaults.maskConfig(isMasked = true),
    separator = { afterIndex -> if (afterIndex == 2) Text("-") else Spacer(Modifier.width(8.dp)) },
)
```

Setting `isError` to true shakes the cells once. Masking shows each digit briefly, then replaces it with a dot.

### String based state

If your screen state is a plain `String`, for example in a ViewModel, use the value overload. Every value you get back is already filtered.

```kotlin
OtpInputField(
    value = uiState.code,
    onValueChange = viewModel::onCodeChange,
    otpLength = 6,
)
```

### Autofill and accessibility

The field is marked as an SMS one-time code, so keyboards such as Gboard can offer a code from a new message even without `otp-sms`. Screen readers announce the field once instead of cell by cell, and masked fields are exposed as passwords so the digits aren't read aloud.

## Reading the code from SMS

```kotlin
SmsUserConsentEffect(
    codeLength = 6,
    onCodeReceive = { received -> code.fillOtp(received, 6) },
    onError = { error -> /* see below */ },
)
```

When the next SMS arrives, Play services asks the user whether your app may read it. If they agree, the first run of exactly `codeLength` digits is passed to `onCodeReceive`. Other numbers in the message, like an order number, are skipped. The app needs Google Play services, but no SMS permission.

Each session handles one message. After a code, an error or the five minute timeout, change `restartKey` to listen again, for example when the user taps "Resend". Pass `senderPhoneNumber` to only react to messages from that number.

`onError` receives a `SmsConsentError`:

| Error | Meaning |
|---|---|
| `StartFailed` | Play services couldn't start listening, usually because it's missing or outdated. `cause` has the details |
| `Timeout` | No message arrived within five minutes |
| `ConsentDenied` | The user closed the consent sheet |
| `PromptUnavailable` | The consent sheet couldn't be shown on this device |
| `CodeNotFound` | The message had no code with `codeLength` digits |
| `UntrustedIntent` | A broadcast pretended to come from Play services and was ignored. Listening continues |

### Security

The broadcast receiver requires Play services' send permission, so other apps can't fake the "SMS retrieved" broadcast. The consent intent inside it is only launched after checking that it opens the Play services consent screen and grants no URI access, as Google recommends. The library adds a `<queries>` entry for Play services to your merged manifest so that check works on Android 11 and later.

## Countdown timer

```kotlin
val timer = rememberCountdownTimerState(duration = 5.minutes)
// or, with an expiry time from your backend:
val timer = rememberCountdownTimerState(endTimeMillis = response.expiresAt)

CountdownTimer(
    state = timer,
    style = CountdownTimerDefaults.style(textSize = 18.sp),
    onTimerFinish = { canResend = true },
)
```

The remaining time is worked out from the clock, so the timer stays correct after the screen is left and revisited or the process is recreated. Screen readers hear "4 minutes, 32 seconds remaining" instead of a row of numbers.

## Contributing

Bug reports and pull requests are welcome. See [CONTRIBUTING.md](CONTRIBUTING.md) for how to build, test and name your changes.

## License

MIT. See [LICENSE](LICENSE).
