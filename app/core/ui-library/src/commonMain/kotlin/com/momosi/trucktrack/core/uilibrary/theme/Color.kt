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
    val surfaceContainerLowest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,
    val surfaceVariant: Color,
    val outline: Color,
    val outlineVariant: Color,

    val onBackground: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,

    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,

    val open: Color,
    val onOpen: Color,
    val openContainer: Color,
    val onOpenContainer: Color,

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
    background = Color(0xFFECEEF4),                  // azure neutral-94 (surfaceContainer)
    surface = Color(0xFFFFFFFF),                      // azure neutral-100 (surfaceContainerLowest)
    surfaceContainerLowest = Color(0xFFFFFFFF),        // azure neutral-100
    surfaceContainerLow = Color(0xFFF2F3FA),           // azure neutral-96
    surfaceContainer = Color(0xFFECEEF4),              // azure neutral-94
    surfaceContainerHigh = Color(0xFFE6E7EE),          // azure neutral-92
    surfaceContainerHighest = Color(0xFFE1E2E9),       // azure neutral-90
    surfaceVariant = Color(0xFFE0E2EC),                // azure neutral-variant-90
    outline = Color(0xFF74777F),                       // azure neutral-variant-50
    outlineVariant = Color(0xFFC1C7D0),                // azure neutral-variant-80
    onBackground = Color(0xFF1A1B1F),                  // azure neutral-10
    onSurface = Color(0xFF1A1B1F),                     // azure neutral-10
    onSurfaceVariant = Color(0xFF74777F),               // azure neutral-variant-50
    primary = Color(0xFF005CBB),              // azure primary-40
    onPrimary = Color(0xFFFFFFFF),            // azure primary-100
    primaryContainer = Color(0xFFD7E3FF),     // azure primary-90
    onPrimaryContainer = Color(0xFF001B3F),   // azure primary-10
    open = Color(0xFF004D68),
    onOpen = Color(0xFFFFFFFF),
    openContainer = Color(0xFFBDE9FF),
    onOpenContainer = Color(0xFF004D68),
    positive = Color(0xFF2E7D32),
    onPositive = Color(0xFFFFFFFF),
    positiveContainer = Color(0xFFBEEFBB),
    onPositiveContainer = Color(0xFF00522C),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
    warning = Color(0xFFFF8E36),
    onWarning = Color(0xFFFFFFFF),
    warningContainer = Color(0xFFFFDCC3),
    onWarningContainer = Color(0xFF753403),
    shadow = Color(0x1A000000),
)

internal val DarkTruckTrackColors = TruckTrackColorPalette(
    background = Color(0xFF1F2023),                  // azure neutral-12 (surfaceContainer)
    surface = Color(0xFF0D0E11),                      // azure neutral-4 (surfaceContainerLowest)
    surfaceContainerLowest = Color(0xFF0D0E11),        // azure neutral-4
    surfaceContainerLow = Color(0xFF1A1C1F),           // azure neutral-10
    surfaceContainer = Color(0xFF1F2023),              // azure neutral-12
    surfaceContainerHigh = Color(0xFF292A2F),          // azure neutral-17
    surfaceContainerHighest = Color(0xFF333538),       // azure neutral-20
    surfaceVariant = Color(0xFF343537),                // azure neutral-variant-30
    outline = Color(0xFF8E9099),                       // azure neutral-variant-60
    outlineVariant = Color(0xFF41484D),                // azure neutral-variant-30
    onBackground = Color(0xFFE3E2E6),                  // azure neutral-90
    onSurface = Color(0xFFE3E2E6),                     // azure neutral-90
    onSurfaceVariant = Color(0xFFC4C6D0),               // azure neutral-variant-80
    primary = Color(0xFFABC7FF),              // azure primary-80
    onPrimary = Color(0xFF002F65),            // azure primary-20
    primaryContainer = Color(0xFF00458F),     // azure primary-30
    onPrimaryContainer = Color(0xFFD7E3FF),   // azure primary-90
    open = Color(0xFF67D4FF),
    onOpen = Color(0xFF004D68),
    openContainer = Color(0xFF67D4FF),
    onOpenContainer = Color(0xFF004D68),
    positive = Color(0xFF81C784),
    onPositive = Color(0xFF003916),
    positiveContainer = Color(0xFF80DA88),
    onPositiveContainer = Color(0xFF00522C),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    warning = Color(0xFFFFB787),
    onWarning = Color(0xFF4A2800),
    warningContainer = Color(0xFFFFB683),
    onWarningContainer = Color(0xFF753403),
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
    surfaceContainerLowest = colors.surfaceContainerLowest,
    surfaceContainerLow = colors.surfaceContainerLow,
    surfaceContainer = colors.surfaceContainer,
    surfaceContainerHigh = colors.surfaceContainerHigh,
    surfaceContainerHighest = colors.surfaceContainerHighest,
    error = colors.error,
    onError = colors.onError,
    errorContainer = colors.errorContainer,
    onErrorContainer = colors.onErrorContainer,
    outline = colors.outline,
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
    surfaceContainerLowest = colors.surfaceContainerLowest,
    surfaceContainerLow = colors.surfaceContainerLow,
    surfaceContainer = colors.surfaceContainer,
    surfaceContainerHigh = colors.surfaceContainerHigh,
    surfaceContainerHighest = colors.surfaceContainerHighest,
    error = colors.error,
    onError = colors.onError,
    errorContainer = colors.errorContainer,
    onErrorContainer = colors.onErrorContainer,
    outline = colors.outline,
    outlineVariant = colors.outlineVariant,
)

internal val LocalTruckTrackColors = staticCompositionLocalOf { LightTruckTrackColors }