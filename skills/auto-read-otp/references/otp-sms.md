# otp-sms

Package `com.burkido.otp.sms`.

## Contents

- How it works
- Signature
- Handling errors
- Resend
- Restricting the sender
- When the consent sheet never appears
- Testing on an emulator or device
- Things not to add

## How it works

`SmsUserConsentEffect` wraps Google's SMS User Consent API. While it's in composition it asks Play services to watch for the next SMS. When a matching message arrives, Play services shows a bottom sheet asking the user to share that one message. If the user allows it, the effect extracts the code and calls `onCodeReceive`. The app needs Google Play services but no SMS permission.

It is a composable with no UI. Put it in the verification screen next to `OtpInputField`. It must be inside an Activity that supports activity results, which every `ComponentActivity` using `setContent` does.

## Signature

```kotlin
@Composable
fun SmsUserConsentEffect(
    codeLength: Int,
    onCodeReceive: (code: String) -> Unit,
    onError: (error: SmsConsentError) -> Unit,
    senderPhoneNumber: String? = null,
    restartKey: Any? = Unit,
)
```

`codeLength` must be positive and should equal the field's `otpLength`. The code passed to `onCodeReceive` is the first run of exactly `codeLength` ASCII digits in the message. Digits that are part of a longer number are skipped, so "Order 7781, code 482913" gives `482913` for a six digit code.

## Handling errors

`onError` receives a sealed `SmsConsentError`. None of these are failures of the sign-in itself, because the user can still type the code. Don't block the screen or show a dialog for them.

| Error | Meaning | Listening | What to show |
|---|---|---|---|
| `StartFailed(cause)` | Play services couldn't start, usually missing or outdated | Ended | Nothing. Log `cause` |
| `Timeout` | No SMS arrived within five minutes | Ended | Nothing, or a hint to resend |
| `ConsentDenied` | The user closed the consent sheet | Ended | Nothing. They chose to type |
| `PromptUnavailable` | The consent sheet couldn't open on this device | Ended | Nothing |
| `CodeNotFound` | The shared message had no run of `codeLength` digits | Ended | Optionally "Couldn't read the code, please type it" |
| `UntrustedIntent` | A spoofed broadcast was dropped | Continues | Nothing. Ignore it |

```kotlin
SmsUserConsentEffect(
    codeLength = CODE_LENGTH,
    onCodeReceive = { received -> code.fillOtp(received, CODE_LENGTH) },
    onError = { error ->
        when (error) {
            is SmsConsentError.StartFailed -> Log.w("VerifyCode", "SMS consent unavailable", error.cause)
            SmsConsentError.CodeNotFound -> smsHint = "Couldn't read the code, please type it"
            SmsConsentError.UntrustedIntent -> Unit
            else -> Log.d("VerifyCode", "SMS consent ended: $error")
        }
    },
    restartKey = attempt,
)
```

`onCodeReceive` should only fill the field. Filling the last cell fires the field's `onComplete`, which is where verification belongs. Calling verify in both places submits the code twice.

## Resend

Each session handles one message, then stops after a code, an error or the five minute timeout. Any new value for `restartKey` starts a fresh session. Keep a counter in `rememberSaveable` or the ViewModel, and increment it in the same click handler that asks the backend for a new code:

```kotlin
onClick = {
    code.clearOtp()
    attempt++
    onResend()
}
```

The effect restarts on the next frame, well before a new SMS can arrive.

## Restricting the sender

`senderPhoneNumber = "+15551234567"` only reacts to messages from that number. Leave it null unless the backend always uses one fixed number. Many SMS providers rotate numbers or send from short codes, and a wrong number means the sheet never appears. Changing `senderPhoneNumber` also starts a fresh session.

## When the consent sheet never appears

Check these in order:

1. **The SMS arrived before listening started.** Play services only forwards messages received after the effect starts. The effect has to be in composition while the code is being requested. It can't be behind a condition that turns true later.
2. **The message doesn't qualify.** Google only prompts for a message containing a 4 to 10 character alphanumeric string with at least one digit. Messages from senders saved in the user's contacts are skipped.
3. **The session already ended.** After a code, an error or a timeout, nothing more is read until `restartKey` changes.
4. **`senderPhoneNumber` doesn't match** the number the message actually came from.
5. **No Play services.** The device or emulator image has no Google Play services, or an outdated version. `onError` receives `StartFailed`.
6. **The code length is wrong.** The sheet appears, but `onError` receives `CodeNotFound` because `codeLength` doesn't match the code in the message.

## Testing on an emulator or device

Use an emulator image with Google Play services. Images labeled "Google APIs" or "Google Play" have it. Open the verification screen first, so it's listening, then send a message from the computer:

```bash
adb emu sms send 5551234 "Your code is 482913"
```

Pasting a message into the emulator's Extended controls → Phone works too. The consent sheet can take a few seconds to appear after the message arrives. Send from a number that isn't saved in the emulator's contacts.

On a real device, send the SMS from another phone that isn't in the device's contacts, after the screen is open.

## Things not to add

- **`RECEIVE_SMS` or `READ_SMS` permissions.** They aren't needed, and Google Play restricts them to default SMS apps.
- **An app hash or `SmsRetriever.startSmsRetriever()`.** That's the separate SMS Retriever API, which needs a hash in the message. User Consent doesn't.
- **A manual `BroadcastReceiver` or `<receiver>` entry.** The effect registers its own receiver, protected by Play services' send permission, and removes it when it leaves composition.
- **A `<queries>` element for Play services.** The library's manifest already merges one in.
