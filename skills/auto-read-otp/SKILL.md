---
name: auto-read-otp
description: Builds one-time code (OTP) verification screens in Jetpack Compose with the auto-read-otp libraries (io.github.burkido otp-ui, otp-sms), covering the per-digit code field, automatic SMS code reading through Google's SMS User Consent API, and the resend countdown timer. Use whenever an Android app needs a verification code, 2FA, phone number sign-in or email code screen, when code imports com.burkido.otp, when SMS code autofill or the consent sheet isn't working, or when migrating from 1.x (com.github.burkido, SmsUserConsent, otp-input-kit, JitPack) or 2.0 (otp-input, otp-timer, com.burkido.otp.input, com.burkido.otp.timer).
license: MIT
metadata:
  author: burkido
  version: "3.0.0" # x-release-please-version
  repository: https://github.com/burkido/auto-read-otp
---

# auto-read-otp

Two Compose libraries for one-time code screens, kept on matching versions by a BOM:

| Artifact | Package | Main API |
|---|---|---|
| `io.github.burkido:otp-ui` | `com.burkido.otp.ui` | `OtpInputField`, `fillOtp`, `clearOtp`, `CountdownTimer`, `rememberCountdownTimerState` |
| `io.github.burkido:otp-sms` | `com.burkido.otp.sms` | `SmsUserConsentEffect`, `SmsConsentError` |

This skill describes 3.x. Most code you remember for this library is probably 1.x. That code used JitPack, `com.github.burkido`, `SmsUserConsent(...)`, `otpinputkit` and `rememberOtpInputFieldState`, and none of it compiles against 3.x. If the project already uses those names, follow [references/migration.md](references/migration.md) instead.

## Match the project's version

This skill documents the library release in its `metadata.version`. The project may use an older one. Before writing code, find the version the project depends on: the `otp` entry in `gradle/libs.versions.toml`, or the `otp-bom` version in a `build.gradle.kts`.

Anything added after 3.0.0 is marked **Since x.y.z** in this skill. If the project's version is older, don't use that API. Use the older approach, or suggest raising the BOM version and let the user decide. A project without the dependency yet should use the version in the dependency snippet below, which is the newest release.

On 2.0.0, the code field is in `io.github.burkido:otp-input` (package `com.burkido.otp.input`) and the timer in `io.github.burkido:otp-timer` (package `com.burkido.otp.timer`). Everything else in this skill applies to 2.0.0 unchanged.

## Which reference to read

- Customizing the field, custom cells, masking, ViewModel state, UI tests: [references/code-field.md](references/code-field.md)
- SMS reading, error handling, resend, why the consent sheet doesn't appear, emulator testing: [references/otp-sms.md](references/otp-sms.md)
- Timer styling, backend expiry times, resend timing: [references/countdown-timer.md](references/countdown-timer.md)
- Upgrading from 1.x or 2.0: [references/migration.md](references/migration.md)

For a plain verification screen, this file is enough.

## Workflow

Copy this checklist and work through it:

```
- [ ] 1. Check the project's version, then add the BOM and only the artifacts the screen uses
- [ ] 2. Build the screen from the pattern below
- [ ] 3. Check the manifest has no SMS permissions
- [ ] 4. Compile the module and fix any errors
```

### 1. Dependencies

Requirements: `minSdk` 24 or higher, `compileSdk` 37 or higher, Jetpack Compose. The artifacts are on Maven Central, so no extra repository is needed.

If the project has `gradle/libs.versions.toml`, add:

<!-- x-release-please-start-version -->
```toml
[versions]
otp = "3.0.0"

[libraries]
otp-bom = { module = "io.github.burkido:otp-bom", version.ref = "otp" }
otp-ui = { module = "io.github.burkido:otp-ui" }
otp-sms = { module = "io.github.burkido:otp-sms" }
```
<!-- x-release-please-end -->

Then add to the app module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation(platform(libs.otp.bom))
    implementation(libs.otp.ui)
    implementation(libs.otp.sms)
}
```

Without a version catalog, use `implementation(platform("io.github.burkido:otp-bom:<version>"))` and the modules without versions. Check Maven Central for a newer version than the one shown here if the user wants the latest.

### 2. The verification screen

This pattern is correct for the common case. Adapt the layout and state ownership to the project, but keep the wiring:

```kotlin
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.burkido.otp.ui.CountdownTimer
import com.burkido.otp.ui.OtpInputField
import com.burkido.otp.ui.clearOtp
import com.burkido.otp.ui.fillOtp
import com.burkido.otp.ui.rememberCountdownTimerState
import com.burkido.otp.sms.SmsUserConsentEffect

private const val CODE_LENGTH = 6

@Composable
fun VerifyCodeScreen(
    expiresAtMillis: Long,
    isWrongCode: Boolean,
    onVerify: (code: String) -> Unit,
    onResend: () -> Unit,
) {
    val code = rememberTextFieldState()
    var attempt by rememberSaveable { mutableIntStateOf(0) }
    val timer = rememberCountdownTimerState(endTimeMillis = expiresAtMillis)

    SmsUserConsentEffect(
        codeLength = CODE_LENGTH,
        onCodeReceive = { received -> code.fillOtp(received, CODE_LENGTH) },
        onError = { },
        restartKey = attempt,
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OtpInputField(
            state = code,
            otpLength = CODE_LENGTH,
            isError = isWrongCode,
            onComplete = onVerify,
        )
        CountdownTimer(state = timer)
        TextButton(
            enabled = timer.isFinished,
            onClick = {
                code.clearOtp()
                attempt++
                onResend()
            },
        ) {
            Text("Resend code")
        }
    }
}
```

The caller owns `expiresAtMillis`. Take it from the backend's response, or compute `System.currentTimeMillis() + 2.minutes.inWholeMilliseconds` when the code is sent and keep it in saveable state or a ViewModel. On resend, set a new value, which creates a fresh timer.

### Rules that matter

These are the mistakes that break real apps. Each one has a reason. Use the reason to handle cases the pattern doesn't cover.

- **Verify in `onComplete` only.** `fillOtp` puts the SMS code into the field, and filling the last cell fires `onComplete`. If `onCodeReceive` also calls the verify function, the code is submitted twice. `onComplete` doesn't fire again when a complete value is restored after rotation.
- **Never add `RECEIVE_SMS` or `READ_SMS`.** The User Consent API needs no SMS permission, and Google Play rejects apps that request SMS permissions without being the default SMS handler. Don't add an SMS Retriever app hash either. That belongs to a different API.
- **Keep one code length everywhere.** Use the same constant for `otpLength`, `codeLength` and `fillOtp`, and make it match what the backend sends. The SMS parser takes the first run of exactly `codeLength` digits and skips longer numbers, such as an order number.
- **Start listening before the SMS is sent.** Play services only forwards messages that arrive after `SmsUserConsentEffect` starts. Compose the effect on the screen that's visible while the code is requested, and don't put it behind a condition that becomes true later.
- **Change `restartKey` on resend.** Each listening session ends after one message, an error or five minutes. Without a new key, the resent SMS is never read.
- **Treat SMS errors as non-fatal.** The user can always type the code, so `onError` should never block the screen or show an error dialog. `UntrustedIntent` doesn't even end the session. See [references/otp-sms.md](references/otp-sms.md) for what each error is worth showing.
- **Clear `isError` when the user edits the code.** The cells shake when `isError` turns from false to true. If it stays true, the next wrong code won't shake. [references/code-field.md](references/code-field.md) shows how to observe edits.
- **Use the `endTimeMillis` overload when the user can resend.** `rememberCountdownTimerState(duration = ...)` saves its end time and only restarts when `duration` changes, so a second countdown of the same length would show as already finished.
- **Use `rememberTextFieldState()` from `androidx.compose.foundation.text.input`.** If state already lives in a ViewModel as a `String`, use the `value` and `onValueChange` overload instead. Both overloads filter to ASCII digits and cap the length, so don't add your own filter.

### 3. Manifest check

Search the app's `AndroidManifest.xml` files for `RECEIVE_SMS` and `READ_SMS`, and remove them if this feature was the only reason they were added. `otp-sms` adds its own `<queries>` entry for Play services through manifest merging, so nothing else is needed.

### 4. Compile

Run the module's Kotlin compile task, for example `./gradlew :app:compileDebugKotlin`. If a symbol doesn't resolve, check the package table at the top before assuming the API changed. Common causes are a 1.x import or a missing `fillOtp` or `clearOtp` extension import from `com.burkido.otp.ui`.

## API reference

The KDoc for every public symbol is at https://burkido.github.io/auto-read-otp/. Source and a sample app are at https://github.com/burkido/auto-read-otp.
