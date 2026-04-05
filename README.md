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

### Using Version Catalog (Recommended)

Define versions in `gradle/libs.versions.toml`:

```toml
[versions]
auto-read-otp = "1.0.3"
otp-input-kit = "1.0.0"
otp-bom = "1.0.0"

[libraries]
auto-read-otp = { module = "com.github.burkido:auto-read-otp", version.ref = "auto-read-otp" }
otp-input-kit = { module = "com.github.burkido:otp-input-kit", version.ref = "otp-input-kit" }
otp-bom = { module = "com.github.burkido:otp-bom", version.ref = "otp-bom" }
```

Then reference them in your **module**'s `build.gradle.kts`:

```kotlin
dependencies {
    // Using BOM (recommended)
    implementation(platform(libs.otp.bom))
    implementation(libs.auto.read.otp)
    implementation(libs.otp.input.kit)
}
```

**Benefits:**
- Single source of truth for versions across all modules
- Easy to bump versions (edit `libs.versions.toml` once, all modules update)
- Type-safe dependency management via generated accessors
- Works seamlessly with IDE autocomplete

### Direct Dependency (Without Version Catalog)

Alternatively, add dependencies directly to your **module**'s `build.gradle.kts`:

```kotlin
dependencies {
    // BOM (recommended)
    implementation(platform("com.github.burkido:otp-bom:1.0.0"))
    implementation("com.github.burkido:auto-read-otp")
    implementation("com.github.burkido:otp-input-kit")

    // Or individually
    implementation("com.github.burkido:auto-read-otp:1.0.3")
    implementation("com.github.burkido:otp-input-kit:1.0.0")
}
```

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

```kotlin
SmsUserConsent(
    smsCodeLength = 6,
    onOTPReceived = { otp -> textFieldState.fillOtp(otp, 6) },
    onError = { error -> Log.e("OTP", error) },
)
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
            onOTPReceived = { otp -> textFieldState.fillOtp(otp, 6) },
            onError = { error -> Log.e("OTP", error) },
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
