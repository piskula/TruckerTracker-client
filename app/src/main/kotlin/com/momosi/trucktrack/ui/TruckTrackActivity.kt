package com.momosi.trucktrack.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.momosi.trucktrack.core.uilibrary.theme.TruckTrackTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class TruckTrackActivity : ComponentActivity() {

    private val viewModel: TruckTrackViewModel by viewModel()

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
