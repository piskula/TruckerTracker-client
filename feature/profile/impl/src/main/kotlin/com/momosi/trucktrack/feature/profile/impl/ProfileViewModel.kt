package com.momosi.trucktrack.feature.profile.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momosi.trucktrack.user.AuthManager
import com.momosi.trucktrack.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    userRepository: UserRepository,
    private val authManager: AuthManager,
) : ViewModel() {

    private val _event = Channel<ProfileEvent>(Channel.BUFFERED)
    val event: Flow<ProfileEvent> = _event.receiveAsFlow()

    private val isSigningOut = MutableStateFlow(false)

    val state: StateFlow<ProfileState> = combine(
        userRepository.user,
        isSigningOut,
    ) { user, signingOut ->
        ProfileState(
            user = user,
            isSigningOut = signingOut,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileState(),
    )

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.SignOut -> signOut()
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            isSigningOut.value = true
            authManager.signOut()
            isSigningOut.value = false
            _event.send(ProfileEvent.NavigateToSignIn)
        }
    }
}

