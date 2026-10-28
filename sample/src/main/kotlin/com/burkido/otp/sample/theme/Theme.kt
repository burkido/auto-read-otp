package com.burkido.otp.sample.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val LightColors = lightColorScheme(
    primary = Color(0xFF3550FF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE2E6FF),
    onPrimaryContainer = Color(0xFF0C1A80),
    secondary = Color(0xFF5B6170),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE6E8EE),
    onSecondaryContainer = Color(0xFF1B1F2A),
    tertiary = Color(0xFF0E9F6E),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD5F5E8),
    onTertiaryContainer = Color(0xFF04402B),
    error = Color(0xFFD92D20),
    onError = Color.White,
    errorContainer = Color(0xFFFEE4E2),
    onErrorContainer = Color(0xFF7A271A),
    background = Color(0xFFF6F6F3),
    onBackground = Color(0xFF14161B),
    surface = Color(0xFFF6F6F3),
    onSurface = Color(0xFF14161B),
    surfaceVariant = Color(0xFFE6E7EB),
    onSurfaceVariant = Color(0xFF5C606B),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color.White,
    surfaceContainer = Color(0xFFEFEFEB),
    surfaceContainerHigh = Color(0xFFE8E8E3),
    surfaceContainerHighest = Color(0xFFE1E1DB),
    outline = Color(0xFFC5C7CE),
    outlineVariant = Color(0xFFE2E3E7),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9DACFF),
    onPrimary = Color(0xFF0A1660),
    primaryContainer = Color(0xFF26369E),
    onPrimaryContainer = Color(0xFFE2E6FF),
    secondary = Color(0xFFB9BECB),
    onSecondary = Color(0xFF252A35),
    secondaryContainer = Color(0xFF2C3038),
    onSecondaryContainer = Color(0xFFE6E8EE),
    tertiary = Color(0xFF4ADE9F),
    onTertiary = Color(0xFF003824),
    tertiaryContainer = Color(0xFF0B4D36),
    onTertiaryContainer = Color(0xFFD5F5E8),
    error = Color(0xFFFF8A80),
    onError = Color(0xFF5C1A14),
    errorContainer = Color(0xFF5C1A14),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF0F1115),
    onBackground = Color(0xFFE8E9ED),
    surface = Color(0xFF0F1115),
    onSurface = Color(0xFFE8E9ED),
    surfaceVariant = Color(0xFF23262D),
    onSurfaceVariant = Color(0xFFA3A7B3),
    surfaceContainerLowest = Color(0xFF0A0B0E),
    surfaceContainerLow = Color(0xFF16181D),
    surfaceContainer = Color(0xFF1B1E24),
    surfaceContainerHigh = Color(0xFF23262D),
    surfaceContainerHighest = Color(0xFF2C3038),
    outline = Color(0xFF4A4F5C),
    outlineVariant = Color(0xFF2C3038),
)

private val BaseTypography = Typography()

private val SampleTypography = Typography(
    displaySmall = BaseTypography.displaySmall.copy(fontWeight = FontWeight.SemiBold, letterSpacing = (-0.5).sp),
    headlineMedium = BaseTypography.headlineMedium.copy(fontWeight = FontWeight.SemiBold, letterSpacing = (-0.25).sp),
    headlineSmall = BaseTypography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
    titleLarge = BaseTypography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
    titleMedium = BaseTypography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
    labelSmall = BaseTypography.labelSmall.copy(fontWeight = FontWeight.SemiBold, letterSpacing = 0.8.sp),
)

@Composable
fun OtpSampleTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = SampleTypography,
        content = content,
    )
}
