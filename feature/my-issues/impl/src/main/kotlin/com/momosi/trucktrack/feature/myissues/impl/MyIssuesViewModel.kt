package com.momosi.trucktrack.feature.myissues.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momosi.trucktrack.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyIssuesViewModel @Inject constructor(
    userRepository: UserRepository,
) : ViewModel() {

    val state: StateFlow<MyIssuesState> = userRepository.user
        .map { MyIssuesState(user = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MyIssuesState())
}

