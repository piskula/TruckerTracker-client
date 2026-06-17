package com.momosi.trucktrack.user

import com.momosi.trucktrack.user.internal.UserStorage
import com.momosi.trucktrack.user.model.User
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userStorage: UserStorage,
) : UserRepository {

    override val user: StateFlow<User?> = userStorage.userFlow
}
