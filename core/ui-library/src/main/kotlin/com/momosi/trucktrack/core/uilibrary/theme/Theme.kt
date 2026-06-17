package com.momosi.trucktrack.core.uilibrary.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

@Composable
fun TruckTrackTheme(
    isDarkTheme: Boolean = false, // isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val truckTrackColors = if (isDarkTheme) DarkTruckTrackColors else LightTruckTrackColors
    val materialColorScheme = remember(isDarkTheme) {
        if (isDarkTheme) darkColorScheme(truckTrackColors) else lightColorScheme(truckTrackColors)
    }

    CompositionLocalProvider(
        LocalTruckTrackColors provides truckTrackColors,
        LocalTruckTrackTypography provides TruckTrackTypography.toTruckTrackTypographyStyles(),
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = TruckTrackTypography,
            content = content,
        )
    }
}

object AppTheme {
    val colors: TruckTrackColorPalette
        @Composable
        @ReadOnlyComposable
        get() = LocalTruckTrackColors.current

    val typography: TruckTrackTypographyStyles
        @Composable
        @ReadOnlyComposable
        get() = LocalTruckTrackTypography.current
}
