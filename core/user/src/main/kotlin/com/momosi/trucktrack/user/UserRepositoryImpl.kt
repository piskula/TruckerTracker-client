package com.momosi.trucktrack.user

import com.momosi.trucktrack.user.internal.UserStorage
import com.momosi.trucktrack.user.model.User
import kotlinx.coroutines.flow.StateFlow

class UserRepositoryImpl(private val userStorage: UserStorage) : UserRepository {

    override val user: StateFlow<User?> = userStorage.userFlow
}
