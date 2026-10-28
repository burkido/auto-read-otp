package com.burkido.otp.sample.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.burkido.otp.sample.ui.DemoScaffold

private enum class GalleryDesign(val title: String, val recipe: String) {
    LockScreen("Lock screen", "Custom dot cell, driven by an on-screen keypad through the String overload"),
    Vault("Vault", "DefaultOtpCell with dark colors and masking, plus a themed CountdownTimer"),
    Aurora("Aurora", "DefaultOtpCell with translucent colors over a gradient"),
    Terminal("Terminal", "Custom cell with a blinking block cursor and a green CountdownTimer"),
    Keycaps("Keycaps", "Custom cell that springs down like a key press"),
    Ticket("Ticket", "OutlinedOtpCell with a separator slot and a monospace font"),
    Minimal("Minimal", "UnderlineOtpCell with large type and a slide-up entry"),
}

@Composable
fun GalleryScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    DemoScaffold(title = "Gallery", onBack = onBack, modifier = modifier) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp),
            modifier = Modifier.padding(padding),
        ) {
            item {
                Text(
                    text = "Every design here is one OtpInputField. The look comes from its style parameters or " +
                        "from the cell slot, which receives an OtpCellState and can draw anything.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            items(GalleryDesign.entries, key = { it.name }) { design ->
                GalleryItem(design = design)
            }
        }
    }
}

@Composable
private fun GalleryItem(design: GalleryDesign, modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = modifier) {
        when (design) {
            GalleryDesign.LockScreen -> LockScreenDesign()
            GalleryDesign.Vault -> VaultDesign()
            GalleryDesign.Aurora -> AuroraDesign()
            GalleryDesign.Terminal -> TerminalDesign()
            GalleryDesign.Keycaps -> KeycapsDesign()
            GalleryDesign.Ticket -> TicketDesign()
            GalleryDesign.Minimal -> MinimalDesign()
        }
        Column(modifier = Modifier.padding(horizontal = 4.dp)) {
            Text(text = design.title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = design.recipe,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
internal fun DesignFrame(background: Brush, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val shape = RoundedCornerShape(28.dp)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(background)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
            .padding(horizontal = 20.dp, vertical = 28.dp),
    ) {
        content()
    }
}
