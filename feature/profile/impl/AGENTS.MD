# feature:profile:impl

Profile screen showing the current user's info and a sign-out action.

## Files

| File | Description |
|------|-------------|
| `ProfileScreen.kt` | Displays user name, username, roles. Sign Out button. |
| `ProfileViewModel.kt` | Loads user from `UserRepository`, calls `AuthManager.signOut()`. |
| `ProfileState.kt` | `Loading`, `Content(user)` |
| `ProfileAction.kt` | `SignOut` |
| `ProfileEvent.kt` | `NavigateToSignIn` after sign-out |
| `navigation/ProfileEntryProvider.kt` | Registers screen entry for `ProfileNavKey` |

## Package

`com.momosi.trucktrack.feature.profile.impl`

## Depends On

- `:feature:profile:api`
- `:core:user` — `AuthManager`, `UserRepository`, `User`, `UserRole`
- `:core:navigation` — `Navigator`
- `:core:ui-library`
- `:core:common`

