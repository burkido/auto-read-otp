# Migrating from 1.x to 2.0

2.0 moves the libraries to Maven Central, splits the countdown timer into its own artifact and cleans up the public API. The steps below cover everything that stops compiling.

## Dependencies

Remove the JitPack repository if nothing else needs it, then replace the dependencies:

| 1.x | 2.0 |
|---|---|
| `com.github.burkido:otp-bom` | `io.github.burkido:otp-bom` |
| `com.github.burkido:auto-read-otp` | `io.github.burkido:otp-sms` |
| `com.github.burkido:otp-input-kit` | `io.github.burkido:otp-input`, plus `io.github.burkido:otp-timer` if you use `CountdownTimer` |

## Packages

| 1.x | 2.0 |
|---|---|
| `com.burkido.autoreadotp` | `com.burkido.otp.sms` |
| `com.burkido.otpinputkit.entry`, `.entry.cell`, `.entry.style` | `com.burkido.otp.input` |
| `com.burkido.otpinputkit.countdowntimer`, `.countdowntimer.style` | `com.burkido.otp.timer` |

Each library now has a single package, so most files need one import line per library.

## SMS

```kotlin
// 1.x
SmsUserConsent(
    smsCodeLength = 6,
    onOTPReceived = { otp -> state.fillOtp(otp, 6) },
    onError = { message -> showError(message) },
)

// 2.0
SmsUserConsentEffect(
    codeLength = 6,
    onCodeReceive = { code -> state.fillOtp(code, 6) },
    onError = { error -> showError(error) },
)
```

`onError` now receives a `SmsConsentError` instead of an English sentence, so you can decide what to show for each case and translate it. To listen again after a timeout or a resend, change the new `restartKey` parameter.

The extracted code also changed. 1.x joined every digit in the message, so "Order 77, code 482913" gave `774829`. 2.0 returns the first run of exactly `codeLength` digits, and reports `CodeNotFound` when there isn't one.

## OTP input

| 1.x | 2.0 |
|---|---|
| `OtpInputField(textFieldState = ...)` | `OtpInputField(state = ...)` |
| `cell = { index, char, focused, error -> ... }` | `cell = { cell -> ... }` with an `OtpCellState` |
| `DefaultOtpCell(char = c, focused = f, isError = e)` | `DefaultOtpCell(state = cell)` |
| `OtpCursorConfig(color = ...)` | `OtpInputDefaults.cursorConfig(color = ...)`, or the field's new `cursorConfig` parameter |
| `inputTransformation` replaced the digit filter | `inputTransformation` runs after the built-in digit filter |
| `OtpInputFieldState`, `rememberOtpInputFieldState` | Removed. Use `rememberTextFieldState()`, or the `value` and `onValueChange` overload |

The `cell` slot is now the last parameter, so a custom cell can be passed as a trailing lambda:

```kotlin
OtpInputField(state = code, otpLength = 6) { cell ->
    UnderlineOtpCell(state = cell)
}
```

Style types such as `OtpCellColors` and `OtpCellDimensions` are regular classes instead of data classes. `copy` still works. Destructuring doesn't. `OtpInputDefaults.textStyles()` is no longer `@Composable`.

Behavior changes you may notice:

* Pasting text that contains non-digits keeps the digits instead of rejecting the whole paste.
* Only ASCII digits are accepted. Arabic-Indic and other Unicode digits are dropped.
* `onComplete` doesn't fire for a value that was already complete when the field appeared, for example after rotation.
* The cursor only shows while the field has focus.
* `UnderlineOtpCell` respects `cellHeight`.

## Countdown timer

| 1.x | 2.0 |
|---|---|
| `rememberCountdownTimerState(endDate = ...)` | `rememberCountdownTimerState(endTimeMillis = ...)`, or `rememberCountdownTimerState(duration = 5.minutes)` |
| `CountdownTimer(size = CountdownTimerDefaults.size(...))` | `CountdownTimer(dimensions = CountdownTimerDefaults.dimensions(...))` |
| `CountdownTimerDefaults.size(textSize = ...)` | `CountdownTimerDefaults.style(textSize = ...)` |
| `backgroundAlpha = 0.5f` | `style = CountdownTimerDefaults.style(backgroundColor = color.copy(alpha = 0.5f))` |
| `state.remainingTime` (Long millis) | `state.remaining` (`Duration`) |
| `state.getRemainingTimes(...)` | Removed. Use `state.remaining.inWholeMinutes` and friends |
| `state.startTimer()`, `cancelTimer()`, `setOnTimerFinishListener()` | Removed. `CountdownTimer` runs the state, and `onTimerFinish` replaces the listener |

The digits now round up, so `00:00:00` only shows at the very moment the timer ends.
