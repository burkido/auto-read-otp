# Countdown timer

Part of `otp-ui`, package `com.burkido.otp.ui`. In 2.0.0 it was a separate artifact, `io.github.burkido:otp-timer` with package `com.burkido.otp.timer`, and the API was otherwise the same.

## Creating the state

```kotlin
val timer = rememberCountdownTimerState(endTimeMillis = response.expiresAtMillis)
val timer = rememberCountdownTimerState(duration = 2.minutes)
```

Both are saveable and work from the wall clock, not from ticks. The countdown stays correct after rotation, after the user leaves and comes back, and after process death.

- `endTimeMillis`: an epoch millisecond time. A new value creates a new timer. Use this when the backend returns an expiry time, and whenever the user can resend.
- `duration`: the end time is fixed when the state is first created and saved. It only resets when `duration` itself changes, so it can't restart a countdown of the same length after a resend.

For resend without a backend expiry time, keep the end time yourself:

```kotlin
var expiresAt by rememberSaveable { mutableLongStateOf(System.currentTimeMillis() + 2.minutes.inWholeMilliseconds) }
val timer = rememberCountdownTimerState(endTimeMillis = expiresAt)

TextButton(
    enabled = timer.isFinished,
    onClick = {
        expiresAt = System.currentTimeMillis() + 2.minutes.inWholeMilliseconds
        onResend()
    },
) { Text("Resend code") }
```

`2.minutes` needs `import kotlin.time.Duration.Companion.minutes`.

## State properties

- `remaining: Duration` is never negative. It updates once per second while a `CountdownTimer` shows this state.
- `isFinished: Boolean` is true once `remaining` reaches zero.
- `endTimeMillis: Long`

The state only ticks while a `CountdownTimer` that uses it is in composition. Without one, `remaining` keeps the value it had when the state was created.

## The composable

```kotlin
CountdownTimer(
    state = timer,
    modifier = Modifier,
    style = CountdownTimerDefaults.style(),
    dimensions = CountdownTimerDefaults.dimensions(),
    onTimerFinish = { },
)
```

It shows `HH : MM : SS` boxes and hides itself when the time is up. `onTimerFinish` is called once per state, including when the state had already expired when it was first shown. To drive UI such as the resend button, read `timer.isFinished` directly. Use `onTimerFinish` for one-off side effects, such as analytics or disabling the field when the code expires.

Screen readers hear "4 minutes, 32 seconds remaining" when the timer is focused. It isn't a live region, so it doesn't interrupt every second.

## Styling

```kotlin
CountdownTimer(
    state = timer,
    style = CountdownTimerDefaults.style(
        backgroundColor = MaterialTheme.colorScheme.errorContainer,
        textSize = 18.sp,
    ),
    dimensions = CountdownTimerDefaults.dimensions(horizontalPadding = 6.dp, boxSpacing = 2.dp),
)
```

- `style()` (composable): `backgroundColor`, `textStyle`, `textSize` (shortcut for the font size of `textStyle`), `separatorColor`
- `dimensions()`: `verticalPadding`, `horizontalPadding`, `boxSpacing`

The library has no text-only format such as "Resend in 0:42". Because the state only ticks inside `CountdownTimer`, a text-only countdown has to compute its own time left from the end time in a `LaunchedEffect` loop. Keep the same end time value that feeds `rememberCountdownTimerState` if both are on screen.
