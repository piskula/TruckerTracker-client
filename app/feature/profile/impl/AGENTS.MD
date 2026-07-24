# feature:profile:impl

Profile screen showing the current user's info, a sign-out action, and an app/server version info dialog.

## Files

| File | Description |
|------|-------------|
| `ProfileScreen.kt` | Displays user name, username, roles, Sign Out button, and a toolbar info icon opening a version `InfoDialog` (app version + server version). |
| `ProfileViewModel.kt` | Loads user from `UserRepository`, calls `AuthManager.signOut()`, reads the app's own version from `AppVersionProvider`, fetches the server's build info from `VersionRepository`. |
| `ProfileState.kt` | `user`, `isSigningOut`, `appVersion`, `isVersionDialogVisible`, `serverVersion` (`ServerVersionContent`: `Loading`/`Loaded`/`Error`) |
| `ProfileAction.kt` | `SignOut`, `ShowVersionInfo`, `DismissVersionInfo` |
| `ProfileEvent.kt` | `NavigateToSignIn` after sign-out |
| `navigation/ProfileEntryProvider.kt` | Registers screen entry for `ProfileNavKey` |

## Package

`com.momosi.trucktrack.feature.profile.impl`

## Depends On

- `:feature:profile:api`
- `:core:user` — `AuthManager`, `UserRepository`, `User`, `UserRole`
- `:core:navigation` — `Navigator`
- `:core:ui-library`
- `:core:common` — `AppVersionProvider`, `DateFormatter`, `VersionRepository`

