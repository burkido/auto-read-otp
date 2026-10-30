package com.burkido.otp.sample.gallery

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.burkido.otp.sample.R
import com.burkido.otp.ui.OtpCellState
import com.burkido.otp.ui.OtpInputField
import kotlinx.coroutines.delay

private const val PASSCODE = "2580"

private val LockBackground = Brush.verticalGradient(listOf(Color(0xFF1E293B), Color(0xFF0B1120)))
private val Unlocked = Color(0xFF4ADE80)
private val Wrong = Color(0xFFF87171)

@Composable
internal fun LockScreenDesign(modifier: Modifier = Modifier) {
    var passcode by rememberSaveable { mutableStateOf("") }
    var unlocked by remember { mutableStateOf<Boolean?>(null) }
    val shake = remember { Animatable(0f) }

    LaunchedEffect(unlocked) {
        when (unlocked) {
            false -> {
                for (offset in listOf(-14f, 14f, -10f, 10f, -5f, 0f)) shake.animateTo(offset, tween(45))
                passcode = ""
                unlocked = null
            }

            true -> {
                delay(1_600)
                passcode = ""
                unlocked = null
            }

            null -> Unit
        }
    }

    DesignFrame(background = LockBackground, modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(R.drawable.ic_lock),
                contentDescription = null,
                tint = if (unlocked == true) Unlocked else Color.White.copy(alpha = 0.8f),
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = if (unlocked == true) "Unlocked" else "Enter passcode",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
            )
            Spacer(Modifier.height(20.dp))
            OtpInputField(
                value = passcode,
                onValueChange = { passcode = it },
                otpLength = PASSCODE.length,
                modifier = Modifier
                    .focusProperties { canFocus = false }
                    .graphicsLayer { translationX = shake.value.dp.toPx() },
                isError = unlocked == false,
                onComplete = { unlocked = it == PASSCODE },
            ) { cell ->
                PasscodeDot(cell = cell, isNext = cell.index == passcode.length, isUnlocked = unlocked == true)
            }
            Spacer(Modifier.height(28.dp))
            Keypad(
                onDigit = { digit -> if (unlocked == null) passcode = (passcode + digit).take(PASSCODE.length) },
                onDelete = { if (unlocked == null) passcode = passcode.dropLast(1) },
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Hint: $PASSCODE",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.45f),
            )
        }
    }
}

@Composable
private fun PasscodeDot(cell: OtpCellState, isNext: Boolean, isUnlocked: Boolean, modifier: Modifier = Modifier) {
    val fill by animateFloatAsState(
        targetValue = if (cell.isFilled) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = 700f),
        label = "dot_fill",
    )
    val color by animateColorAsState(
        targetValue = when {
            cell.isError -> Wrong
            isUnlocked -> Unlocked
            else -> Color.White
        },
        label = "dot_color",
    )
    Box(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .size(16.dp)
            .border(1.5.dp, color.copy(alpha = if (isNext) 0.9f else 0.45f), CircleShape),
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    scaleX = fill
                    scaleY = fill
                }
                .background(color, CircleShape),
        )
    }
}

@Composable
private fun Keypad(onDigit: (Char) -> Unit, onDelete: () -> Unit, modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = modifier) {
        listOf("123", "456", "789").forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                row.forEach { digit -> KeypadKey(label = digit.toString(), onClick = { onDigit(digit) }) }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            Spacer(Modifier.size(KeySize))
            KeypadKey(label = "0", onClick = { onDigit('0') })
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(KeySize)
                    .clip(CircleShape)
                    .clickable(onClick = onDelete),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_backspace),
                    contentDescription = "Delete",
                    tint = Color.White.copy(alpha = 0.8f),
                )
            }
        }
    }
}

@Composable
private fun KeypadKey(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(KeySize)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.09f))
            .clickable(onClick = onClick),
    ) {
        Text(text = label, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Light)
    }
}

private val KeySize = 64.dp
