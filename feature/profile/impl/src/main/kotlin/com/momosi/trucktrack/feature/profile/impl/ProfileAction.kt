package com.momosi.trucktrack.feature.profile.impl

sealed interface ProfileAction {
    data object SignOut : ProfileAction
}
