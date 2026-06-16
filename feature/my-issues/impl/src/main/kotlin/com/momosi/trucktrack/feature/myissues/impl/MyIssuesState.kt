package com.momosi.trucktrack.feature.myissues.impl

import androidx.compose.runtime.Immutable
import com.momosi.trucktrack.user.model.User

@Immutable
data class MyIssuesState(
    val user: User? = null,
)

