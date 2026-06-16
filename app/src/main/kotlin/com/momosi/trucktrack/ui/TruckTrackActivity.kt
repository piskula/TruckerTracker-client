package com.momosi.trucktrack.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.momosi.trucktrack.core.uilibrary.theme.TruckTrackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TruckTrackActivity : ComponentActivity() {

    private val viewModel: TruckTrackViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TruckTrackTheme {
                TruckTrackApp(viewModel)
            }
        }
    }
}
