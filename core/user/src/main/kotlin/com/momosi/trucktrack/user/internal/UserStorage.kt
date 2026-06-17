package com.momosi.trucktrack.user.internal

import android.content.SharedPreferences
import androidx.core.content.edit
import com.momosi.trucktrack.user.model.User
import com.momosi.trucktrack.user.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.openid.appauth.AuthState
import javax.inject.Inject
import javax.inject.Named

const val USER_AUTH_STORAGE = "user_auth_storage"

private const val AUTH_STATE = "auth_state"
private const val SERVER_PUBLIC_KEY = "server_public_key"
private const val USER_ID = "user_id"
private const val USER_NAME = "user_name"
private const val USER_EMAIL = "user_email"
private const val USER_ROLE = "user_role"

interface UserStorage {
    var authState: AuthState
    var serverPublicKey: String?
    var user: User?
    val userFlow: StateFlow<User?>
}

class UserStorageImpl @Inject constructor(
    @Named(USER_AUTH_STORAGE) private val authStorage: SharedPreferences,
) : UserStorage {

    override var authState: AuthState
        set(value) = authStorage.edit { putString(AUTH_STATE, value.jsonSerializeString()) }
        get() = authStorage.getString(AUTH_STATE, null)?.let { AuthState.jsonDeserialize(it) } ?: AuthState()

    override var serverPublicKey: String?
        set(value) = authStorage.edit { putString(SERVER_PUBLIC_KEY, value) }
        get() = authStorage.getString(SERVER_PUBLIC_KEY, null)

    private val _userFlow = MutableStateFlow(loadUser())
    override val userFlow: StateFlow<User?> = _userFlow

    override var user: User?
        set(value) {
            authStorage.edit {
                if (value != null) {
                    putString(USER_ID, value.id)
                    putString(USER_NAME, value.name)
                    putString(USER_EMAIL, value.email)
                    putString(USER_ROLE, value.role.name)
                } else {
                    remove(USER_ID)
                    remove(USER_NAME)
                    remove(USER_EMAIL)
                    remove(USER_ROLE)
                }
            }
            _userFlow.value = value
        }
        get() = loadUser()

    private fun loadUser(): User? {
        val id = authStorage.getString(USER_ID, null) ?: return null
        val name = authStorage.getString(USER_NAME, null) ?: return null
        val email = authStorage.getString(USER_EMAIL, null) ?: return null
        val role = authStorage.getString(USER_ROLE, null)
            ?.let { runCatching { UserRole.valueOf(it) }.getOrNull() } ?: return null
        return User(id = id, name = name, email = email, role = role)
    }
}
