package com.momosi.trucktrack.user

import com.momosi.trucktrack.user.model.User
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {

    val user: StateFlow<User?>
}

