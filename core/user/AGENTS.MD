# core:user

Handles authentication (OAuth/OIDC via AppAuth), JWT parsing, user profile, and session state.

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
AuthManager.kt                  ← Public interface
AuthManagerImpl.kt              ← AppAuth + token management
UserRepository.kt               ← Public interface
UserRepositoryImpl.kt
model/User.kt
model/UserRole.kt
model/AuthenticationState.kt
model/AuthActionResult.kt
model/TokenResponse.kt
internal/OpenIdManager.kt       ← AppAuth OIDC flow
internal/AuthActivity.kt        ← Browser-based OAuth redirect handler
internal/JwtParser.kt           ← jjwt-based JWT decoding
internal/TokenVerifier.kt
internal/UserStorage.kt         ← Token persistence
internal/networking/            ← Ktor interceptors for token injection/refresh
di/UserModule.kt                ← Koin bindings
```

## Depends On

- `:core:common` — `CurrentActivityHelper`, `Logger`, `DispatcherProvider`
- `:core:network` — networking infrastructure

## KMP Notes — High Complexity

This is the most Android-coupled module:
- `AppAuth` is Android-only → needs `expect/actual` split (Android: AppAuth, iOS: AppAuth-iOS or `ASWebAuthenticationSession`).
- `jjwt` is JVM-only → replace with manual Base64 + `kotlinx.serialization` parsing.
- `CurrentActivityHelper` dependency → remove by passing a platform browser launcher via `expect/actual`.
- `UserStorage` uses Android DataStore → switch to `datastore-preferences-core` KMP artifact.

