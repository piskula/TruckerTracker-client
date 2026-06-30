# core:user

Handles authentication (OAuth/OIDC via AppAuth), JWT parsing, user profile, and session state. KMP module with interfaces in `commonMain` and Android-specific implementations in `androidMain`.

## Public API

| Symbol | Description |
|--------|-------------|
| `AuthManager` | Interface. Exposes `authenticationState: StateFlow<AuthenticationState>`, `authenticationInProgress: StateFlow<Boolean>`, `signIn()`, `signOut()`, `token(): TokenResponse`. |
| `UserRepository` | Interface. Exposes `user: StateFlow<User?>`. |
| `AuthenticationState` | Sealed class: `Authenticated`, `Unauthenticated`. |
| `AuthActionResult` | Sealed class: `Success`, `Cancelled`, `Failed`. |
| `User` | Domain model with id, username, name, roles. |
| `UserRole` | Enum: `Driver`, `Mechanic`. |
| `TokenResponse` | Holds `accessToken` and `refreshToken` strings. |

## Key Files

```
commonMain/
  AuthManager.kt                  ← Public interface
  UserRepository.kt               ← Public interface
  model/User.kt
  model/UserRole.kt
  model/AuthenticationState.kt
  model/AuthActionResult.kt
  model/TokenResponse.kt
  di/UserModule.kt                ← Koin bindings (common part)
androidMain/
  AuthManagerImpl.kt              ← AppAuth + token management
  UserRepositoryImpl.kt
  internal/OpenIdManager.kt       ← AppAuth OIDC flow
  internal/AuthActivity.kt        ← Browser-based OAuth redirect handler
  internal/JwtParser.kt           ← JWT decoding
  internal/TokenVerifier.kt
  internal/UserStorage.kt         ← Token persistence
  internal/networking/            ← Ktor interceptors for token injection/refresh
  di/AndroidUserModule.kt        ← Koin Android-specific bindings
```

## Depends On

- `:core:common` — `CurrentActivityHelper`, `Logger`, `DispatcherProvider`
- `:core:network` — networking infrastructure

