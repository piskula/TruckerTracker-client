package com.momosi.trucktrack.feature.signin.impl

sealed interface SignInEvent {
    data object NavigateToMyIssues : SignInEvent
}
