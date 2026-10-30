package com.burkido.otp.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/** Horizontal offset in pixels that shakes the cell each time [isError] turns true. */
@Composable
internal fun rememberErrorShake(isError: Boolean, spec: OtpAnimationSpec): Animatable<Float, AnimationVector1D> {
    val offset = remember { Animatable(0f) }
    val amplitude = with(LocalDensity.current) { spec.shakeAmplitude.toPx() }

    LaunchedEffect(isError) {
        if (!isError || !spec.shakeOnError) return@LaunchedEffect
        repeat(spec.shakeRepeatCount) { step ->
            offset.animateTo(if (step % 2 == 0) amplitude else -amplitude, tween(spec.shakeStepDuration))
        }
        offset.animateTo(0f, tween(spec.shakeStepDuration))
    }

    return offset
}

internal fun Modifier.horizontalOffset(offset: Animatable<Float, AnimationVector1D>): Modifier =
    offset { IntOffset(offset.value.roundToInt(), 0) }

internal fun Modifier.disabledAlpha(isEnabled: Boolean): Modifier = if (isEnabled) this else alpha(DISABLED_ALPHA)

/**
 * The text a cell should show right now. With masking on, a new digit stays readable for
 * [OtpMaskConfig.maskDelay] and is then swapped for the mask character.
 */
@Composable
internal fun rememberDisplayText(char: Char?, maskConfig: OtpMaskConfig): String {
    // Keyed on char so a new digit is visible on its very first frame instead of flashing the mask.
    var revealed by remember(char) { mutableStateOf(true) }

    if (maskConfig.isMasked && char != null) {
        LaunchedEffect(char, maskConfig.maskDelay) {
            delay(maskConfig.maskDelay)
            revealed = false
        }
    }

    return when {
        char == null -> ""
        maskConfig.isMasked && !revealed -> maskConfig.maskCharacter.toString()
        else -> char.toString()
    }
}

internal fun enterTransitionFor(spec: OtpAnimationSpec): EnterTransition {
    val tween = tween<Float>(spec.animationDuration)
    return when (spec.cellEntryAnimation) {
        CellAnimation.None -> EnterTransition.None
        CellAnimation.Scale -> scaleIn(tween)
        CellAnimation.FadeIn -> fadeIn(tween)
        CellAnimation.SlideUp -> slideInVertically(tween(spec.animationDuration)) { it }
    }
}

private const val DISABLED_ALPHA = 0.38f
