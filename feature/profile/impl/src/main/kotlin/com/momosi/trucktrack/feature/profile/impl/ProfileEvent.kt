package com.momosi.trucktrack.feature.profile.impl

sealed interface ProfileEvent {
    data object NavigateToSignIn : ProfileEvent
}
