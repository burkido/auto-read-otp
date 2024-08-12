package com.burkido.verificationcodereader

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OtpTextField(
    modifier: Modifier = Modifier,
    value: String,
    length: Int,
    onValueChange: (String) -> Unit,
    onVerificationExplicitlyTriggered: () -> Unit
) {
    BasicTextField(
        modifier = modifier.clearFocusOnTap(),
        value = TextFieldValue(value, selection = TextRange(value.length)),
        onValueChange = { if (it.text.length <= length) onValueChange(it.text) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { onVerificationExplicitlyTriggered() }),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(length) { index ->
                    CharView(
                        index = index,
                        text = value
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
}

@Composable
private fun CharView(
    modifier: Modifier = Modifier,
    index: Int,
    text: String
) {
    val isFocused = index == text.length
    val char = text.getOrNull(index)?.toString().orEmpty()

    val borderColor by animateColorAsState(
        targetValue = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
        label = "border_color_animation"
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (char.isNotEmpty()) MaterialTheme.colorScheme.primaryContainer else Color.White,
        label = "background_color_animation"
    )

    Box(
        modifier = modifier
            .size(50.dp)
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = char,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun OtpTextFieldPreview() {
    OtpTextField(
        value = "1234",
        length = 4,
        onValueChange = {},
        onVerificationExplicitlyTriggered = {}
    )
}