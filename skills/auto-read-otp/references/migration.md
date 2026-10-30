# Migrating to 3.x

## From 2.0

2.0.0 had the code field in `otp-input` and the countdown timer in `otp-timer`. 3.0.0 merged both into `otp-ui`, and nothing else changed:

1. Replace `io.github.burkido:otp-input` and `io.github.burkido:otp-timer` with `io.github.burkido:otp-ui` in the build files and the version catalog.
2. Replace `import com.burkido.otp.input.` and `import com.burkido.otp.timer.` with `import com.burkido.otp.ui.`.

The script below finds both.

## From 1.x

2.0 moved to Maven Central and changed the public API, and 3.0 puts the code field and the countdown timer in `otp-ui`. Everything below stops compiling until it's updated.

### Workflow

```
- [ ] 1. Find every 1.x usage with the script
- [ ] 2. Replace dependencies
- [ ] 3. Replace imports
- [ ] 4. Update SMS, input and timer call sites
- [ ] 5. Rerun the script until it reports nothing, then compile
```

Run from the project root, with the path to this skill's directory:

```bash
sh <skill-dir>/scripts/find-1x-usages.sh .
```

It lists each file and line that still uses a 1.x or 2.0 name, and exits with status 1 while any remain. Some patterns, such as `remainingTime`, can match the project's own code. Check each hit before changing it.

### Dependencies

Remove the JitPack repository (`maven("https://jitpack.io")`) only if nothing else in the build uses it.

| 1.x | 3.x |
|---|---|
| `com.github.burkido:otp-bom` | `io.github.burkido:otp-bom` |
| `com.github.burkido:auto-read-otp` | `io.github.burkido:otp-sms` |
| `com.github.burkido:otp-input-kit` | `io.github.burkido:otp-ui` |

### Packages

| 1.x | 3.x |
|---|---|
| `com.burkido.autoreadotp` | `com.burkido.otp.sms` |
| `com.burkido.otpinputkit.entry`, `.entry.cell`, `.entry.style` | `com.burkido.otp.ui` |
| `com.burkido.otpinputkit.countdowntimer`, `.countdowntimer.style` | `com.burkido.otp.ui` |

Each library has a single package now.

### SMS

```kotlin
SmsUserConsent(
    smsCodeLength = 6,
    onOTPReceived = { otp -> state.fillOtp(otp, 6) },
    onError = { message -> showError(message) },
)
```

becomes

```kotlin
SmsUserConsentEffect(
    codeLength = 6,
    onCodeReceive = { code -> state.fillOtp(code, 6) },
    onError = { error -> handle(error) },
    restartKey = attempt,
)
```

- `onError` receives a `SmsConsentError` instead of an English sentence. Don't pass it to the UI as text. Decide per case, see [otp-sms.md](otp-sms.md). Most cases need no message at all.
- `restartKey` is new. Add a resend counter, because without one a resent SMS is never read.
- Code extraction changed. 1.x joined every digit in the message, so "Order 77, code 482913" gave `774829`. 3.x returns the first run of exactly `codeLength` digits, and reports `CodeNotFound` when there isn't one. If the backend's message format relied on the old behavior, `codeLength` must now match the real code length.

### OTP input

| 1.x | 3.x |
|---|---|
| `OtpInputField(textFieldState = ...)` | `OtpInputField(state = ...)` |
| `cell = { index, char, focused, error -> ... }` | `cell = { cell -> ... }` with an `OtpCellState` (`cell.index`, `cell.char`, `cell.isFocused`, `cell.isError`) |
| `DefaultOtpCell(char = c, focused = f, isError = e)` | `DefaultOtpCell(state = cell)` |
| `OtpCursorConfig(color = ...)` | `OtpInputDefaults.cursorConfig(color = ...)`, or the field's `cursorConfig` parameter |
| `inputTransformation` replaced the digit filter | `inputTransformation` runs after the built-in digit filter |
| `OtpInputFieldState`, `rememberOtpInputFieldState` | Removed. Use `rememberTextFieldState()`, or the `value` and `onValueChange` overload |

`cell` is now the last parameter, so it can be a trailing lambda:

```kotlin
OtpInputField(state = code, otpLength = 6) { cell ->
    UnderlineOtpCell(state = cell)
}
```

`OtpCellColors`, `OtpCellDimensions` and the other style types are regular classes instead of data classes. `copy` still works, destructuring doesn't. `OtpInputDefaults.textStyles()` is no longer `@Composable`.

Behavior changes to mention to the user, because tests may depend on them:

- A paste containing non-digits keeps the digits instead of being rejected.
- Only ASCII digits are accepted. Arabic-Indic and other Unicode digits are dropped.
- `onComplete` doesn't fire for a value that was already complete when the field appeared, for example after rotation.
- The cursor only shows while the field has focus.
- `UnderlineOtpCell` respects `cellHeight`.

### Countdown timer

| 1.x | 3.x |
|---|---|
| `rememberCountdownTimerState(endDate = ...)` | `rememberCountdownTimerState(endTimeMillis = ...)`, or `rememberCountdownTimerState(duration = 5.minutes)` |
| `CountdownTimer(size = CountdownTimerDefaults.size(...))` | `CountdownTimer(dimensions = CountdownTimerDefaults.dimensions(...))` |
| `CountdownTimerDefaults.size(textSize = ...)` | `CountdownTimerDefaults.style(textSize = ...)` |
| `backgroundAlpha = 0.5f` | `style = CountdownTimerDefaults.style(backgroundColor = color.copy(alpha = 0.5f))` |
| `state.remainingTime` (Long millis) | `state.remaining` (`Duration`) |
| `state.getRemainingTimes(...)` | Removed. Use `state.remaining.inWholeMinutes` and similar |
| `state.startTimer()`, `cancelTimer()`, `setOnTimerFinishListener()` | Removed. `CountdownTimer` runs the state, and its `onTimerFinish` replaces the listener |

The digits now round up, so `00:00:00` only shows at the very moment the timer ends.
