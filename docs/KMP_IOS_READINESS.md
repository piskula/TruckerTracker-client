# KMP iOS Readiness Analysis

> Generated: 2026-07-01 · Updated: 2026-07-01

This document tracks what must still be resolved before the iOS target is fully functional.
Items that have already been fixed are not listed here.

---

## What Was Already Fixed

- iOS targets (`iosArm64`, `iosSimulatorArm64`) declared in `LibraryPlugin.kt`
- `ktor-client-darwin` added to `libs.versions.toml`, wired per-platform in `KtorPlugin.kt`
- `ktor-client-okhttp` removed from `ktor` bundle (Android-only — now in `androidMain` only)
- `koin` bundle cleaned up — Android-only deps removed
- `koin-compose-viewmodel` moved to `commonMain` (KMP-compatible)
- `actual fun httpClientEngineFactory()` — iOS impl returns `Darwin` engine
- `actual fun createPlatformDateFormatter()` — iOS impl uses `NSDateFormatter`

---

## Remaining Issues

### 1 · `androidx.paging` KMP Artifact Verification

**Severity:** 🔴 Blocker (if not KMP-compatible)

`core:issue` and `feature:issues:impl` both declare `androidx.paging` in `commonMain`:

| Module | Dependency |
|--------|-----------|
| `core:issue` | `libs.androidx.paging.common` in `commonMain` |
| `feature:issues:impl` | `libs.androidx.paging.compose` in `commonMain` |

`paging-common` has published KMP artifacts since 3.3.0 and should be fine at 3.5.0.
`paging-compose` KMP support is less certain — verify it publishes `iosArm64` / `iosSimulatorArm64` variants.

**Action:** Run `./gradlew :core:issue:compileKotlinIosArm64` and `:feature:issues:impl:compileKotlinIosArm64` once an iOS framework target is wired up. If resolution fails, migrate to [`app.cash.paging`](https://github.com/cashapp/multiplatform-paging) which is a drop-in KMP replacement.

---

### 2 · Interface Implementations Missing for iOS (Koin-Wired)

**Severity:** 🟡 High — Koin cannot satisfy these bindings on iOS without iOS implementations.

These interfaces have Android implementations in `androidMain` registered via Koin. No iOS equivalents exist yet. Each needs an `iosMain` implementation and a Koin module to wire it.

| Interface | Android impl | iOS replacement |
|-----------|-------------|-----------------|
| `ConnectivityManager` (`core:common`) | `ConnectivityManagerImpl` — Android `ConnectivityManager` | `NWPathMonitor` (Network.framework) |
| `PhotoReader` (`core:common`) | `PhotoReaderImpl` — `android.content.Context` + `android.net.Uri` | `NSFileManager` / `PHPickerViewController` |
| `CurrentActivityHelper` (`core:common`) | `Application.ActivityLifecycleCallbacks` | Not applicable — hide behind `expect/actual` with iOS no-op or remove from shared interface |
| `AuthManager` (`core:user`) | `AuthManagerImpl` — AppAuth (`net.openid:appauth`) | `ASWebAuthenticationSession` (AuthenticationServices.framework) |
| `UserStorage` (`core:user`) | `UserStorageImpl` — `SharedPreferences` | `NSUserDefaults` or iOS Keychain |
| JWT parsing (`core:user`) | `JwtParser` — `android.util.Base64` | Replace with `kotlin.io.encoding.Base64` (stdlib, KMP-compatible) — quickest fix |
| `OpenIdManager` (`core:user`) | Android `Activity`, `PendingIntent`, `Intent` | Full reimplementation with `ASWebAuthenticationSession` |

**Estimated effort:**
- **Low:** `JwtParser` — swap `android.util.Base64` for `kotlin.io.encoding.Base64`
- **Medium:** `UserStorage` (`NSUserDefaults`), `ConnectivityManager` (`NWPathMonitor`), `CurrentActivityHelper` (no-op)
- **High:** `AuthManager` / `OpenIdManager` — entire OAuth browser flow needs reimplementation

---

### 3 · Koin Initialization Uses `androidContext()`

**Severity:** 🟡 High — `startKoin` will crash on iOS as-is.

`androidApp/.../TruckTrack.kt` calls `androidContext(this)` inside `startKoin { }`.
`core/common/src/androidMain/.../CommonModule.kt` and `core/user/src/androidMain/.../UserModule.kt` use `get<Context>()`.

**Fix:** Introduce `expect/actual` for platform Koin modules:

```kotlin
// commonMain
expect fun platformModules(): List<Module>

// androidMain
actual fun platformModules() = listOf(commonAndroidModule, userAndroidModule)

// iosMain
actual fun platformModules() = listOf(commonIosModule, userIosModule)
```

Call `startKoin { modules(sharedModules + platformModules()) }` from `AppInitializer` in `commonMain`.
Remove `androidContext()` from the shared call site — pass it only inside the Android-specific module.

---

### 4 · iOS App Entry Point Does Not Exist

**Severity:** 🟡 High — nothing can run on iOS without this.

`androidApp/` is the Android shell. There is no `iosApp/` equivalent.

**Fix:** Create an `iosApp/` Xcode project with a SwiftUI `App` entry point that calls into `composeApp` via the Compose Multiplatform iOS integration:

```swift
import ComposeApp

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
```

`ContentView` wraps `TruckTrackApp` exposed as a Kotlin/Native framework from `composeApp`.

---

## Remaining Checklist

- [ ] Verify `androidx.paging:paging-compose:3.5.0` publishes iOS artifacts (or migrate to `app.cash.paging`)
- [ ] Replace `android.util.Base64` in `JwtParser` with `kotlin.io.encoding.Base64`
- [ ] Implement iOS `UserStorage` — `NSUserDefaults` or Keychain
- [ ] Implement iOS `ConnectivityManager` — `NWPathMonitor`
- [ ] Handle `CurrentActivityHelper` on iOS — no-op `expect/actual` or remove from shared interface
- [ ] Implement iOS `AuthManager` / `OpenIdManager` — `ASWebAuthenticationSession`
- [ ] Implement iOS `PhotoReader` — `PHPickerViewController` or abstract file picking
- [ ] Split Koin initialization — `expect/actual platformModules()` pattern
- [ ] Create `iosApp/` Xcode project with SwiftUI entry point
- [ ] Run `./gradlew :composeApp:compileKotlinIosArm64` — verify shared code compiles clean

---

## What Already Works for iOS ✅

- All domain models — pure Kotlin data classes in `commonMain`
- All repository interfaces and implementations — pure Kotlin
- All ViewModels, State, Action, Event — `commonMain`
- All screen Composables — Compose Multiplatform in `commonMain`
- Ktor API clients and DTOs — engine abstracted via `expect/actual`
- Navigation keys — `@Serializable` objects in `commonMain`
- `core:ui-library` — theme, icons, components in `commonMain`
- `core:navigation` — `Navigator`, `NavigationState` in `commonMain`
- Kermit logging via `Logger` — KMP-compatible
- Coil 3 image loading — KMP-compatible
- FileKit — KMP-compatible
- Kotlinx Serialization, Coroutines, Collections Immutable — all KMP-compatible
