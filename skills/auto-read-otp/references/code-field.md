# Code field

Part of `otp-ui`, package `com.burkido.otp.ui`. In 2.0.0 the artifact was `io.github.burkido:otp-input`, with package `com.burkido.otp.input`.

## Contents

- Two overloads
- What the field does on its own
- Styling the default cell
- Built-in cell styles
- Custom cells
- Separators
- Masking
- Clearing the error on edit
- Fitting narrow screens
- UI tests

## Two overloads

```kotlin
OtpInputField(state: TextFieldState, otpLength: Int, ..., onComplete: ((String) -> Unit)? = null, separator = null, cell = { DefaultOtpCell(...) })
OtpInputField(value: String, onValueChange: (String) -> Unit, otpLength: Int, ..., onComplete = null, separator = null, cell = { ... })
```

Prefer the `TextFieldState` overload with `rememberTextFieldState()` from `androidx.compose.foundation.text.input`. It handles IME edits more reliably and saves itself across process death. Use the `String` overload when the screen's state already lives in a ViewModel:

```kotlin
OtpInputField(
    value = uiState.code,
    onValueChange = viewModel::onCodeChange,
    otpLength = 6,
    isError = uiState.isWrongCode,
    onComplete = viewModel::verify,
)
```

Every value passed to `onValueChange` is already filtered, so the ViewModel can store it as is. To put an SMS code into this kind of state, call `viewModel.onCodeChange(code)` from `onCodeReceive`. `fillOtp` only works on `TextFieldState`.

Shared parameters: `modifier`, `enabled`, `isError`, `placeholder`, `colors`, `dimensions`, `animationSpec`, `shape`, `maskConfig`, `cursorConfig`, `textStyles`, `contentPadding`, `keyboardOptions`. Only the `TextFieldState` overload has `inputTransformation`, which runs after the built-in digit filter.

`TextFieldState` helpers: `fillOtp(code, length)`, `clearOtp()`, `otpValue` and `isOtpComplete(length)`.

## What the field does on its own

Don't rebuild any of this:

- Accepts ASCII digits only and never more than `otpLength`. Pasting "Your code is 123-456" leaves `123456`.
- Marks itself as an SMS one-time code for autofill, so Gboard can suggest a code from a new message even without `otp-sms`.
- Shows a number keyboard with autocorrect off.
- Keeps the cursor at the end, so a tap can't put a digit in the middle.
- Calls `onComplete` each time the value goes from incomplete to complete, but not for a complete value restored after rotation.
- Announces itself once to screen readers instead of cell by cell.
- Shakes once when `isError` turns true, and fades its cells when `enabled` is false.

Disable the field while a verification request is in flight (`enabled = !isVerifying`) so the user can't edit a code that's being checked.

## Styling the default cell

Every style value comes from a function on `OtpInputDefaults`. Pass only what changes:

```kotlin
OtpInputField(
    state = code,
    otpLength = 6,
    placeholder = "-",
    colors = OtpInputDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.tertiary),
    dimensions = OtpInputDefaults.dimensions(cellWidth = 52.dp, cellHeight = 60.dp, spacing = 10.dp),
    animationSpec = OtpInputDefaults.animationSpec(cellEntryAnimation = CellAnimation.SlideUp),
    shape = OtpInputDefaults.shape(cornerRadius = 16.dp),
    textStyles = OtpInputDefaults.textStyles(textSize = 28.sp),
)
```

| Function | Parameters |
|---|---|
| `colors()` (composable) | `focusedBorderColor`, `unfocusedBorderColor`, `errorBorderColor`, `filledBorderColor`, `focusedContainerColor`, `unfocusedContainerColor`, `errorContainerColor`, `filledContainerColor`, `textColor`, `cursorColor`, `placeholderColor` |
| `dimensions()` | `cellWidth` (48.dp), `cellHeight` (56.dp), `borderWidth`, `focusedBorderWidth`, `cornerRadius` (12.dp), `spacing` (8.dp), `textSize` (24.sp) |
| `animationSpec()` | `shakeOnError`, `cellEntryAnimation` (`CellAnimation.Scale`, `FadeIn`, `SlideUp`, `None`), `animationDuration`, `shakeAmplitude`, `shakeStepDuration`, `shakeRepeatCount` |
| `maskConfig()` | `isMasked`, `maskCharacter`, `maskDelay` |
| `cursorConfig()` (composable) | `color`, `width`, `height`, `cornerRadius`, `blinkInterval` |
| `textStyles()` | `textSize`, `filledTextStyle`, `placeholderTextStyle` |
| `contentPadding()` | `horizontal`, `vertical` |
| `shape()` | `cornerRadius` |

The style classes are regular classes with `copy`. They aren't data classes, so destructuring doesn't work.

## Built-in cell styles

`DefaultOtpCell`, `OutlinedOtpCell` (circles by default) and `UnderlineOtpCell`. Switch with the trailing `cell` lambda:

```kotlin
OtpInputField(state = code, otpLength = 6) { cell ->
    UnderlineOtpCell(state = cell)
}
```

When you pass a `cell`, the style parameters on `OtpInputField` (`colors`, `placeholder`, `textStyles` and the rest) no longer reach the cell. Pass them to the cell instead. Only `dimensions.spacing` on the field still applies, because the field draws the gaps.

```kotlin
val colors = OtpInputDefaults.colors(focusedBorderColor = brand)
OtpInputField(state = code, otpLength = 6) { cell ->
    OutlinedOtpCell(state = cell, colors = colors, placeholder = "•")
}
```

`UnderlineOtpCell` has no `shape` parameter. The other two accept the same style parameters as the field.

## Custom cells

`cell` receives an `OtpCellState` with `index`, `char` (null when empty), `isFilled`, `isFocused` (true only for the cell the next digit goes into, while focused), `isError` and `isEnabled`.

```kotlin
OtpInputField(state = code, otpLength = 4) { cell ->
    val borderColor = when {
        cell.isError -> MaterialTheme.colorScheme.error
        cell.isFocused -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(56.dp)
            .alpha(if (cell.isEnabled) 1f else 0.38f)
            .border(if (cell.isFocused) 2.dp else 1.dp, borderColor, RoundedCornerShape(8.dp)),
    ) {
        Text(text = cell.char?.toString().orEmpty(), style = MaterialTheme.typography.headlineSmall)
    }
}
```

A custom cell draws everything itself, including the cursor, the error shake and the mask. The built-in cells handle those for you, so start from a built-in cell with changed colors if that's enough.

## Separators

`separator` is drawn between cells instead of the spacing gap and receives the index of the cell on its left:

```kotlin
separator = { afterIndex ->
    if (afterIndex == 2) Text("-", modifier = Modifier.padding(horizontal = 8.dp)) else Spacer(Modifier.width(8.dp))
}
```

## Masking

`maskConfig = OtpInputDefaults.maskConfig(isMasked = true)` shows each digit for `maskDelay` milliseconds, then replaces it with `maskCharacter`. Screen readers then treat the field as a password and don't read the digits aloud.

## Clearing the error on edit

`isError` shakes the cells only when it turns from false to true. Reset the error as soon as the user changes the code, so the next wrong code shakes again. With `TextFieldState`:

```kotlin
LaunchedEffect(code) {
    snapshotFlow { code.text.toString() }
        .drop(1)
        .collect { onCodeEdit() }
}
```

Imports: `androidx.compose.runtime.LaunchedEffect`, `androidx.compose.runtime.snapshotFlow`, `kotlinx.coroutines.flow.drop`. With the `String` overload, reset the error inside the ViewModel's `onCodeChange`.

If the screen clears the field after a wrong code, clearing counts as an edit. Clear first, then set the error.

## Fitting narrow screens

Six default cells need 6 × 48.dp + 5 × 8.dp = 328.dp. On narrow screens or with large separators, work out `cellWidth` from the available width:

```kotlin
BoxWithConstraints {
    val gap = 8.dp
    val cellWidth = ((maxWidth - gap * (CODE_LENGTH - 1)) / CODE_LENGTH).coerceAtMost(52.dp)
    OtpInputField(
        state = code,
        otpLength = CODE_LENGTH,
        dimensions = OtpInputDefaults.dimensions(cellWidth = cellWidth, spacing = gap),
    )
}
```

## UI tests

The cells are hidden from accessibility, so tests find the field through its text input node:

```kotlin
composeTestRule.onNode(hasSetTextAction()).performTextInput("123456")
```

This goes through the same digit filter as typing, and `onComplete` fires when the last digit goes in.
