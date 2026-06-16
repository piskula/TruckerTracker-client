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
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,

    val positive: Color,
    val positiveContainer: Color,
    val negative: Color,
    val negativeContainer: Color,
    val warning: Color,
    val warningContainer: Color,

    val shadow: Color,
)

internal val LightTruckTrackColors = TruckTrackColorPalette(
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFE0E0E0),
    onBackground = Color(0xFF212121),
    onSurface = Color(0xFF212121),
    onSurfaceVariant = Color(0xFF757575),
    primary = Color(0xFF1565C0),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE3F2FD),
    secondary = Color(0xFF1976D2),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFBBDEFB),
    onSecondaryContainer = Color(0xFF0D47A1),
    tertiary = Color(0xFF0D47A1),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFBBDEFB),
    onTertiaryContainer = Color(0xFF0D47A1),
    positive = Color(0xFF2E7D32),
    positiveContainer = Color(0xFFE8F5E9),
    negative = Color(0xFFC62828),
    negativeContainer = Color(0xFFFFEBEE),
    warning = Color(0xFFF57C00),
    warningContainer = Color(0xFFFFF3E0),
    shadow = Color(0x1A000000),
)

internal val DarkTruckTrackColors = TruckTrackColorPalette(
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    surfaceVariant = Color(0xFF2C2C2C),
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFF9E9E9E),
    primary = Color(0xFF64B5F6),
    onPrimary = Color(0xFF0D47A1),
    primaryContainer = Color(0xFF1565C0),
    secondary = Color(0xFF90CAF9),
    onSecondary = Color(0xFF0D47A1),
    secondaryContainer = Color(0xFF1565C0),
    onSecondaryContainer = Color(0xFFBBDEFB),
    tertiary = Color(0xFF64B5F6),
    onTertiary = Color(0xFF0D47A1),
    tertiaryContainer = Color(0xFF0D47A1),
    onTertiaryContainer = Color(0xFFBBDEFB),
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
    onPrimaryContainer = colors.primary,
    secondary = colors.secondary,
    onSecondary = colors.onSecondary,
    secondaryContainer = colors.secondaryContainer,
    onSecondaryContainer = colors.onSecondaryContainer,
    tertiary = colors.tertiary,
    onTertiary = colors.onTertiary,
    tertiaryContainer = colors.tertiaryContainer,
    onTertiaryContainer = colors.onTertiaryContainer,
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
    onPrimaryContainer = colors.primary,
    secondary = colors.secondary,
    onSecondary = colors.onSecondary,
    secondaryContainer = colors.secondaryContainer,
    onSecondaryContainer = colors.onSecondaryContainer,
    tertiary = colors.tertiary,
    onTertiary = colors.onTertiary,
    tertiaryContainer = colors.tertiaryContainer,
    onTertiaryContainer = colors.onTertiaryContainer,
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
