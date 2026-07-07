package com.momosi.trucktrack.core.uilibrary

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // No-op: iOS has no hardware/OS-level back gesture to intercept at screen level here;
    // the in-screen back button already routes through the same onBack callback.
}
