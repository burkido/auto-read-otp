package com.burkido.otp.sample

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.burkido.otp.sample.gallery.GalleryScreen
import com.burkido.otp.sample.home.HomeScreen
import com.burkido.otp.sample.playground.PlaygroundScreen
import com.burkido.otp.sample.signin.SignInFlow
import com.burkido.otp.sample.smslab.SmsLabScreen

@Composable
fun SampleApp(modifier: Modifier = Modifier) {
    var current by rememberSaveable { mutableStateOf<Demo?>(null) }
    val close = { current = null }
    val stateHolder = rememberSaveableStateHolder()

    BackHandler(enabled = current != null, onBack = close)

    AnimatedContent(
        targetState = current,
        modifier = modifier,
        transitionSpec = {
            val forward = targetState != null
            val enter = slideInHorizontally(tween(320)) { if (forward) it / 5 else -it / 5 } + fadeIn(tween(320))
            val exit = slideOutHorizontally(tween(320)) { if (forward) -it / 5 else it / 5 } + fadeOut(tween(200))
            enter togetherWith exit
        },
        label = "demo",
    ) { demo ->
        when (demo) {
            null -> stateHolder.SaveableStateProvider(HOME_KEY) {
                HomeScreen(onOpen = { current = it })
            }

            Demo.SignIn -> SignInFlow(onClose = close)

            Demo.Playground -> PlaygroundScreen(onBack = close)

            Demo.Gallery -> GalleryScreen(onBack = close)

            Demo.SmsLab -> SmsLabScreen(onBack = close)
        }
    }
}

private const val HOME_KEY = "home"
