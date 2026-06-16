package com.momosi.trucktrack.user.internal

import android.content.SharedPreferences
import androidx.core.content.edit
import net.openid.appauth.AuthState
import javax.inject.Inject
import javax.inject.Named

const val USER_AUTH_STORAGE = "user_auth_storage"

private const val AUTH_STATE = "auth_state"
private const val SERVER_PUBLIC_KEY = "server_public_key"

interface UserStorage {
    var authState: AuthState
    var serverPublicKey: String?
}

class UserStorageImpl @Inject constructor(
    @Named(USER_AUTH_STORAGE) private val authStorage: SharedPreferences,
) : UserStorage {

    override var authState: AuthState
        set(value) {
            authStorage.edit {
                putString(AUTH_STATE, value.jsonSerializeString())
            }
        }
        get() = authStorage.getString(AUTH_STATE, null)?.let { AuthState.jsonDeserialize(it) } ?: AuthState()

    override var serverPublicKey: String?
        set(value) = authStorage.edit { putString(SERVER_PUBLIC_KEY, value) }
        get() = authStorage.getString(SERVER_PUBLIC_KEY, null)
}
