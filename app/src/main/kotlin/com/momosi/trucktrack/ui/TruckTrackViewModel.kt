package com.momosi.trucktrack.ui

import androidx.lifecycle.ViewModel
import com.momosi.trucktrack.user.AuthManager
import com.momosi.trucktrack.user.model.AuthenticationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TruckTrackViewModel @Inject constructor(authManager: AuthManager) : ViewModel() {

    val authenticationState: StateFlow<AuthenticationState> = authManager.authenticationState
}
