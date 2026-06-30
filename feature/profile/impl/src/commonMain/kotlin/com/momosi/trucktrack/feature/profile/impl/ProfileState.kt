package com.momosi.trucktrack.feature.profile.impl

import androidx.compose.runtime.Immutable
import com.momosi.trucktrack.user.model.User

@Immutable
data class ProfileState(val user: User? = null, val isSigningOut: Boolean = false)
