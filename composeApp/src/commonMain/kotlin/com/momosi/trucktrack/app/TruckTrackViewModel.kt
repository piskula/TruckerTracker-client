package com.momosi.trucktrack.app

import androidx.lifecycle.ViewModel
import com.momosi.trucktrack.user.AuthManager
import com.momosi.trucktrack.user.model.AuthenticationState
import kotlinx.coroutines.flow.StateFlow

class TruckTrackViewModel(authManager: AuthManager) : ViewModel() {

    val authenticationState: StateFlow<AuthenticationState> = authManager.authenticationState
}
