package com.momosi.trucktrack.user.internal

import com.momosi.trucktrack.user.model.StoredTokens
import com.momosi.trucktrack.user.model.User
import kotlinx.coroutines.flow.StateFlow

const val USER_AUTH_STORAGE = "user_auth_storage"

interface UserStorage {
    var tokens: StoredTokens?
    var serverPublicKey: String?
    var user: User?
    val userFlow: StateFlow<User?>
}
