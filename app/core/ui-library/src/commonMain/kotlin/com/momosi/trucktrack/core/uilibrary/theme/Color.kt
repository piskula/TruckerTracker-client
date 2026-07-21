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

    val onBackground: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,

    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,

    val positive: Color,
    val positiveContainer: Color,
    val negative: Color,
    val negativeContainer: Color,
    val warning: Color,
    val warningContainer: Color,

    val shadow: Color,
)

internal val LightTruckTrackColors = TruckTrackColorPalette(
    background = Color(0xFFF2F0F4),     // azure neutral-95
    surface = Color(0xFFFFFFFF),         // azure neutral-100
    surfaceVariant = Color(0xFFE0E2EC),  // azure neutral-variant-90
    onBackground = Color(0xFF1A1B1F),    // azure neutral-10
    onSurface = Color(0xFF1A1B1F),       // azure neutral-10
    onSurfaceVariant = Color(0xFF74777F), // azure neutral-variant-50
    primary = Color(0xFF005CBB),              // azure primary-40
    onPrimary = Color(0xFFFFFFFF),            // azure primary-100
    primaryContainer = Color(0xFFD7E3FF),     // azure primary-90
    onPrimaryContainer = Color(0xFF001B3F),   // azure primary-10
    positive = Color(0xFF2E7D32),
    positiveContainer = Color(0xFFE8F5E9),
    negative = Color(0xFFC62828),
    negativeContainer = Color(0xFFFFEBEE),
    warning = Color(0xFFF57C00),
    warningContainer = Color(0xFFFFF3E0),
    shadow = Color(0x1A000000),
)

internal val DarkTruckTrackColors = TruckTrackColorPalette(
    background = Color(0xFF121316),      // azure neutral-6
    surface = Color(0xFF1F2022),         // azure neutral-12
    surfaceVariant = Color(0xFF343537),  // azure neutral-22
    onBackground = Color(0xFFE3E2E6),   // azure neutral-90
    onSurface = Color(0xFFE3E2E6),      // azure neutral-90
    onSurfaceVariant = Color(0xFFC4C6D0), // azure neutral-variant-80
    primary = Color(0xFFABC7FF),              // azure primary-80
    onPrimary = Color(0xFF002F65),            // azure primary-20
    primaryContainer = Color(0xFF00458F),     // azure primary-30
    onPrimaryContainer = Color(0xFFD7E3FF),   // azure primary-90
    positive = Color(0xFF66BB6A),
    positiveContainer = Color(0xFF1B3A1C),
    negative = Color(0xFFEF5350),
    negativeContainer = Color(0xFF3E1414),
    warning = Color(0xFFFFB74D),
    warningContainer = Color(0xFF3E2A0F),
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
    error = colors.negative,
    onError = colors.surface,
    errorContainer = colors.negativeContainer,
    onErrorContainer = colors.negative,
    outline = colors.onSurfaceVariant,
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
    error = colors.negative,
    onError = colors.surface,
    errorContainer = colors.negativeContainer,
    onErrorContainer = colors.negative,
    outline = colors.onSurfaceVariant,
)

internal val LocalTruckTrackColors = staticCompositionLocalOf { LightTruckTrackColors }