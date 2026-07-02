# Auto Read OTP

A Jetpack Compose library suite for OTP input UI and automatic SMS reading via Google's SMS Consent API.

[![Jitpack](https://jitpack.io/v/burkido/auto-read-otp.svg)](https://jitpack.io/#burkido/auto-read-otp)

## Modules

| Module | Description |
|---|---|
| `sms-reader` | Auto-reads OTP from incoming SMS using SMS Consent API |
| `otp-input-kit` | Customizable OTP input field with multiple cell styles |
| `otp-bom` | Bill of Materials for consistent versioning |

## Gradle

Add JitPack repository to your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

All modules are published from a single Git tag under the JitPack multi-module
group `com.github.burkido.auto-read-otp`, so every module shares the same version.

### Using Version Catalog (Recommended)

Define the version once in `gradle/libs.versions.toml`:

```toml
[versions]
auto-read-otp = "v2.0.0"

[libraries]
otp-bom = { module = "com.github.burkido.auto-read-otp:otp-bom", version.ref = "auto-read-otp" }
sms-reader = { module = "com.github.burkido.auto-read-otp:sms-reader" }
otp-input-kit = { module = "com.github.burkido.auto-read-otp:otp-input-kit" }
```

Then reference them in your **module**'s `build.gradle.kts`:

```kotlin
dependencies {
    implementation(platform(libs.otp.bom))
    implementation(libs.sms.reader)
    implementation(libs.otp.input.kit)
}
```

**Benefits:**
- Single source of truth for the version (the BOM pins all modules)
- Type-safe dependency management via generated accessors
- Works seamlessly with IDE autocomplete

### Direct Dependency (Without Version Catalog)

Alternatively, add dependencies directly to your **module**'s `build.gradle.kts`:

```kotlin
dependencies {
    // BOM (recommended)
    implementation(platform("com.github.burkido.auto-read-otp:otp-bom:v2.0.0"))
    implementation("com.github.burkido.auto-read-otp:sms-reader")
    implementation("com.github.burkido.auto-read-otp:otp-input-kit")

    // Or individually
    implementation("com.github.burkido.auto-read-otp:sms-reader:v2.0.0")
    implementation("com.github.burkido.auto-read-otp:otp-input-kit:v2.0.0")
}
```

> **Note:** Versions `1.0.x` were published under the legacy single-module
> coordinate `com.github.burkido:auto-read-otp`. Starting with `v2.0.0` use the
> coordinates above.

## Usage

### OTP Input Field

```kotlin
val textFieldState = rememberTextFieldState()

OtpInputField(
    textFieldState = textFieldState,
    otpLength = 6,
    onComplete = { otp -> /* OTP entered */ },
)
```

#### Cell Styles

Use `cell` parameter to switch between built-in styles:

```kotlin
// Default (rounded rectangle)
OtpInputField(textFieldState = state, otpLength = 6)

// Outlined (circle)
OtpInputField(
    textFieldState = state,
    otpLength = 6,
    cell = { index, char, focused, error ->
        OutlinedOtpCell(char = char, focused = focused, isError = error)
    },
)

// Underline
OtpInputField(
    textFieldState = state,
    otpLength = 6,
    cell = { index, char, focused, error ->
        UnderlineOtpCell(char = char, focused = focused, isError = error)
    },
)
```

#### Customization

```kotlin
OtpInputField(
    textFieldState = state,
    otpLength = 6,
    obscureText = true,           // Mask input with "●"
    placeholder = "-",
    colors = OtpInputDefaults.colors(
        focusedBorderColor = Color.Blue,
        errorBorderColor = Color.Red,
    ),
    dimensions = OtpInputDefaults.dimensions(
        cellWidth = 52.dp,
        cellHeight = 60.dp,
    ),
    animationSpec = OtpInputDefaults.animationSpec(
        shakeOnError = true,
        cellEntryAnimation = CellAnimation.Scale,
    ),
)
```

#### Separator

```kotlin
OtpInputField(
    textFieldState = state,
    otpLength = 6,
    separator = { index ->
        if (index == 2) Text("-")
    },
)
```

#### TextFieldState Extensions

```kotlin
textFieldState.otpValue         // Current OTP string
textFieldState.isOtpComplete(6) // Whether all digits filled
textFieldState.fillOtp(otp, 6)  // Programmatically fill OTP
textFieldState.clearOtp()       // Clear input
```

### SMS Auto-Read

Uses Google's SMS User Consent API — no SMS permissions required.

```kotlin
SmsUserConsent(
    smsCodeLength = 6,
    onOtpReceived = { otp -> textFieldState.fillOtp(otp, 6) },
    onError = { error ->
        when (error) {
            SmsConsentError.Timeout -> { /* show resend button */ }
            SmsConsentError.ConsentDenied -> { /* user dismissed the dialog */ }
            else -> Log.e("OTP", "SMS consent failed: $error")
        }
    },
)
```

Optional parameters:

```kotlin
SmsUserConsent(
    smsCodeLength = 6,
    onOtpReceived = { otp -> /* ... */ },
    senderPhoneNumber = "+901234567890", // only listen to this sender
    retryKey = attempt,                  // change to restart the 5-minute window
)
```

The consent window lasts 5 minutes and is one-shot. To listen again (e.g. after
a "resend code" tap), bump `retryKey`:

```kotlin
var attempt by remember { mutableIntStateOf(0) }
SmsUserConsent(smsCodeLength = 6, onOtpReceived = { /* ... */ }, retryKey = attempt)
Button(onClick = { resendCode(); attempt++ }) { Text("Resend") }
```

### Full Example

```kotlin
@Composable
fun VerificationScreen() {
    val textFieldState = rememberTextFieldState()

    Column {
        OtpInputField(
            textFieldState = textFieldState,
            otpLength = 6,
            onComplete = { otp -> /* verify */ },
        )

        SmsUserConsent(
            smsCodeLength = 6,
            onOtpReceived = { otp -> textFieldState.fillOtp(otp, 6) },
            onError = { error -> Log.e("OTP", "SMS consent failed: $error") },
        )
    }
}
```

https://github.com/user-attachments/assets/81cc0afd-e6f7-4bce-bc6a-ee2b5acc7fa2

## Contributing

Pull requests are welcome. PR titles must follow the [Conventional Commits](https://www.conventionalcommits.org/) format, which is enforced automatically by CI:

```
<type>: <subject>
```

**Allowed types:**

| Type | When to use |
|---|---|
| `feat` | New feature |
| `fix` | Bug fix |
| `docs` | Documentation changes only |
| `refactor` | Code change that is neither a fix nor a feature |
| `perf` | Performance improvement |
| `test` | Adding or updating tests |
| `build` | Changes to the build system or dependencies |
| `ci` | Changes to CI configuration |
| `chore` | Maintenance tasks |
| `style` | Code style / formatting (no logic change) |
| `revert` | Reverts a previous commit |

**Rules:**
- Scope is optional: `feat(sms-reader): ...` is valid but not required
- The subject must **not** start with an uppercase letter

**Examples:**

```
feat: add underline cell animation
fix: prevent crash on empty SMS body
docs: update installation instructions
refactor(otp-input-kit): simplify cell drawing logic
```

## License

```
MIT License - see LICENSE file for details.
```
