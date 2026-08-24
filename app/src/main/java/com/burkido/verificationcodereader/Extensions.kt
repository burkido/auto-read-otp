package com.burkido.verificationcodereader

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.SuspendingPointerInputModifierNode
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.platform.LocalFocusManager

/**
 * Clears focus (dismissing the keyboard) when the user taps this element,
 * without consuming the event — children still receive their own gestures,
 * so tapping an input field inside this element re-focuses it as usual.
 */
fun Modifier.clearFocusOnTap(): Modifier = this then ClearFocusOnTapElement

private data object ClearFocusOnTapElement : ModifierNodeElement<ClearFocusOnTapNode>() {
    override fun create() = ClearFocusOnTapNode()
    override fun update(node: ClearFocusOnTapNode) = Unit
}

private class ClearFocusOnTapNode : DelegatingNode(), CompositionLocalConsumerModifierNode {
    init {
        delegate(
            SuspendingPointerInputModifierNode {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        currentValueOf(LocalFocusManager).clearFocus()
                    }
                }
            },
        )
    }
}
