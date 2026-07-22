package com.momosi.trucktrack.core.uilibrary.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class TruckTrackColorPalette(
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val surfaceContainerHighest: Color,
    val outlineVariant: Color,

    val onBackground: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,

    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,

    val positive: Color,
    val onPositive: Color,
    val positiveContainer: Color,
    val onPositiveContainer: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,

    val shadow: Color,
)

internal val LightTruckTrackColors = TruckTrackColorPalette(
    background = Color(0xFFF2F3FA),             // azure neutral-96 (surfaceContainerLow)
    surface = Color(0xFFF8F9FF),                 // azure neutral-98
    surfaceVariant = Color(0xFFE0E2EC),          // azure neutral-variant-90
    surfaceContainerHighest = Color(0xFFE1E2E9), // azure neutral-90
    outlineVariant = Color(0xFFC1C7D0),          // azure neutral-variant-80
    onBackground = Color(0xFF1A1B1F),    // azure neutral-10
    onSurface = Color(0xFF1A1B1F),       // azure neutral-10
    onSurfaceVariant = Color(0xFF74777F), // azure neutral-variant-50
    primary = Color(0xFF005CBB),              // azure primary-40
    onPrimary = Color(0xFFFFFFFF),            // azure primary-100
    primaryContainer = Color(0xFFD7E3FF),     // azure primary-90
    onPrimaryContainer = Color(0xFF001B3F),   // azure primary-10
    positive = Color(0xFF2E7D32),
    onPositive = Color(0xFFFFFFFF),
    positiveContainer = Color(0xFFC8E6C9),
    onPositiveContainer = Color(0xFF00210B),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
    warning = Color(0xFF8A4F00),
    onWarning = Color(0xFFFFFFFF),
    warningContainer = Color(0xFFFFE0B2),
    onWarningContainer = Color(0xFF2E1500),
    shadow = Color(0x1A000000),
)

internal val DarkTruckTrackColors = TruckTrackColorPalette(
    background = Color(0xFF121316),             // azure surface (neutral-6) — darkest screen bg
    surface = Color(0xFF1A1C1F),                 // azure surface-container-low — card bg (elevated)
    surfaceVariant = Color(0xFF343537),          // azure neutral-variant
    surfaceContainerHighest = Color(0xFF333538), // azure surface-container-highest
    outlineVariant = Color(0xFF41484D),          // azure neutral-variant-30
    onBackground = Color(0xFFE3E2E6),   // azure neutral-90
    onSurface = Color(0xFFE3E2E6),      // azure neutral-90
    onSurfaceVariant = Color(0xFFC4C6D0), // azure neutral-variant-80
    primary = Color(0xFFABC7FF),              // azure primary-80
    onPrimary = Color(0xFF002F65),            // azure primary-20
    primaryContainer = Color(0xFF00458F),     // azure primary-30
    onPrimaryContainer = Color(0xFFD7E3FF),   // azure primary-90
    positive = Color(0xFF81C784),
    onPositive = Color(0xFF003916),
    positiveContainer = Color(0xFF1B5E20),
    onPositiveContainer = Color(0xFFC8E6C9),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    warning = Color(0xFFFFB95C),
    onWarning = Color(0xFF4A2800),
    warningContainer = Color(0xFF7A4B00),
    onWarningContainer = Color(0xFFFFDDB3),
    shadow = Color(0x00000000),
)

internal fun lightColorScheme(colors: TruckTrackColorPalette) = lightColorScheme(
    primary = colors.primary,
    onPrimary = colors.onPrimary,
    primaryContainer = colors.primaryContainer,
    onPrimaryContainer = colors.onPrimaryContainer,
    background = colors.background,
    onBackground = colors.onBackground,
    surface = colors.surface,
    onSurface = colors.onSurface,
    surfaceVariant = colors.surfaceVariant,
    onSurfaceVariant = colors.onSurfaceVariant,
    surfaceContainerLow = colors.background,
    surfaceContainerHighest = colors.surfaceContainerHighest,
    error = colors.error,
    onError = colors.onError,
    errorContainer = colors.errorContainer,
    onErrorContainer = colors.onErrorContainer,
    outline = colors.onSurfaceVariant,
    outlineVariant = colors.outlineVariant,
)

internal fun darkColorScheme(colors: TruckTrackColorPalette) = darkColorScheme(
    primary = colors.primary,
    onPrimary = colors.onPrimary,
    primaryContainer = colors.primaryContainer,
    onPrimaryContainer = colors.onPrimaryContainer,
    background = colors.background,
    onBackground = colors.onBackground,
    surface = colors.surface,
    onSurface = colors.onSurface,
    surfaceVariant = colors.surfaceVariant,
    onSurfaceVariant = colors.onSurfaceVariant,
    surfaceContainerLow = colors.background,
    surfaceContainerHighest = colors.surfaceContainerHighest,
    error = colors.error,
    onError = colors.onError,
    errorContainer = colors.errorContainer,
    onErrorContainer = colors.onErrorContainer,
    outline = colors.onSurfaceVariant,
    outlineVariant = colors.outlineVariant,
)

internal val LocalTruckTrackColors = staticCompositionLocalOf { LightTruckTrackColors }