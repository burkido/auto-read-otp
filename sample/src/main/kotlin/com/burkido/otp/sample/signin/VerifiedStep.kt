package com.burkido.otp.sample.signin

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.burkido.otp.sample.R
import kotlinx.coroutines.launch

@Composable
fun VerifiedStep(phone: String, onDone: () -> Unit, onRestart: () -> Unit, modifier: Modifier = Modifier) {
    val badgeScale = remember { Animatable(0.3f) }
    val ring = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch { badgeScale.animateTo(1f, spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessLow)) }
        ring.animateTo(1f, tween(durationMillis = 900, delayMillis = 120, easing = FastOutSlowInEasing))
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Spacer(Modifier.weight(1f))
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(160.dp)) {
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .graphicsLayer {
                        val scale = 1f + ring.value * 0.45f
                        scaleX = scale
                        scaleY = scale
                        alpha = 1f - ring.value
                    }
                    .border(3.dp, MaterialTheme.colorScheme.tertiary, CircleShape),
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(112.dp)
                    .graphicsLayer {
                        scaleX = badgeScale.value
                        scaleY = badgeScale.value
                    }
                    .background(MaterialTheme.colorScheme.tertiaryContainer, CircleShape),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.size(56.dp),
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(text = "You're verified", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "+1 $phone is confirmed.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.weight(1f))
        Button(
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            Text("Back to demos")
        }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onRestart) { Text("Run it again") }
    }
}
