package com.momosi.trucktrack.user

import com.momosi.trucktrack.core.common.logger.Logger
import com.momosi.trucktrack.user.internal.TokenVerifier
import com.momosi.trucktrack.user.internal.UserStorage
import com.momosi.trucktrack.user.model.AuthenticationState
import com.momosi.trucktrack.user.model.User
import com.momosi.trucktrack.user.model.UserRole
import io.jsonwebtoken.Claims
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "UserRepository"

private val SYSTEM_ROLES = setOf("offline_access", "uma_authorization")

@Singleton
class UserRepositoryImpl @Inject constructor(
    authManager: AuthManager,
    private val userStorage: UserStorage,
    private val tokenVerifier: TokenVerifier,
    appCoroutineScope: CoroutineScope,
) : UserRepository {

    override val user: StateFlow<User?> = authManager.authenticationState
        .map { state ->
            when (state) {
                AuthenticationState.Authorized -> resolveUserData()
                AuthenticationState.Guest -> null
            }
        }
        .stateIn(appCoroutineScope, SharingStarted.Eagerly, initialValue = null)

    private suspend fun resolveUserData(): User? {
        val accessToken = userStorage.authState.accessToken ?: return null
        return tokenVerifier.verifyToken(accessToken)
            .mapCatching { jws -> jws.payload.toUser() }
            .onFailure { Logger.e(TAG, it, "Failed to parse user data from token") }
            .getOrNull()
    }
}

private fun Claims.toUser(): User {
    val roles = (get("realm_access", Map::class.java)?.get("roles") as? List<*>)
        ?.filterIsInstance<String>()
        ?.filterNot { it.startsWith("default-roles") || it in SYSTEM_ROLES }
        .orEmpty()

    return User(
        name = get("name", String::class.java) ?: "",
        email = get("email", String::class.java) ?: "",
        role = roles.firstNotNullOfOrNull { it.toUserRole() } ?: UserRole.Driver,
    )
}

private fun String.toUserRole(): UserRole? = when (this) {
    "ROLE_DRIVER" -> UserRole.Driver
    "ROLE_MECHANIC" -> UserRole.Mechanic
    else -> null
}
