package com.momosi.trucktrack.core.uilibrary

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandler(enabled: Boolean, onBack: () -> Unit)
