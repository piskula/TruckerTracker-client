# KMP iOS Readiness Analysis

> Generated: 2026-07-01 · Updated: 2026-07-07

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
- `androidx.paging` (3.5.0) verified iOS-compatible — no library swap needed (2026-07-07)
- `androidx.navigation3:navigation3-ui` swapped for the JetBrains multiplatform build; `core:navigation` now compiles clean for iOS (2026-07-07)
- Android-only `androidx.compose.ui.tooling.preview.Preview` swapped for the multiplatform `org.jetbrains.compose.ui.tooling.preview.Preview` across 20 files in `core:ui-library` and 3 feature `impl` modules (2026-07-07)
- `androidx.activity.compose.BackHandler` (Android-only) replaced with an `expect`/`actual` `BackHandler` in `core:ui-library`, Android delegating to the real one, iOS a no-op (2026-07-07)
- `org.jetbrains.kotlin.plugin.serialization` now applied in `FeatureImplPlugin.kt` — `@Serializable` `NavKey`s defined in `impl` modules (not just `api`) now actually generate a `.serializer()` (2026-07-07)
- **Result: every feature module (`feature:issues:impl`, `feature:sign-in:impl`, `feature:profile:impl`) and every `core` module now compiles clean for `iosArm64`/`iosSimulatorArm64`. Only `composeApp` itself remains blocked, by the two pre-existing items below (Koin `androidContext()` and missing iOS interface implementations) — unrelated to navigation, paging, or Preview.**

---

## Remaining Issues

### 1 · Interface Implementations Missing for iOS (Koin-Wired)

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

### 2 · Koin Initialization Uses `androidContext()`

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

Note: `AppModule.kt` in `composeApp` already fails to compile for iOS on exactly this — `commonModule`/`userModule` currently live entirely in `androidMain` (`core/common/.../di/CommonModule.kt`, `core/user/.../di/UserModule.kt`), not split as this doc originally assumed.

---

### 3 · iOS App Entry Point Does Not Exist

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

- [x] Verify `androidx.paging` publishes iOS artifacts — confirmed 2026-07-07, see "What Already Works" below
- [x] Replace `androidx.navigation3:navigation3-ui` with `org.jetbrains.androidx.navigation3:navigation3-ui` — done 2026-07-07, `core:navigation` compiles clean for iOS
- [x] Fix Android-only `@Preview` import across `core:ui-library` and feature `impl` modules — done 2026-07-07
- [x] Fix Android-only `BackHandler` in `IssueDetailScreen.kt` — done 2026-07-07, `core:ui-library.BackHandler` `expect`/`actual`
- [ ] Replace `android.util.Base64` in `JwtParser` with `kotlin.io.encoding.Base64`
- [ ] Implement iOS `UserStorage` — `NSUserDefaults` or Keychain
- [ ] Implement iOS `ConnectivityManager` — `NWPathMonitor`
- [ ] Handle `CurrentActivityHelper` on iOS — no-op `expect/actual` or remove from shared interface
- [ ] Implement iOS `AuthManager` / `OpenIdManager` — `ASWebAuthenticationSession`
- [ ] Implement iOS `PhotoReader` — `PHPickerViewController` or abstract file picking
- [ ] Split Koin initialization — `expect/actual platformModules()` pattern
- [ ] Create `iosApp/` Xcode project with SwiftUI entry point
- [ ] Run `./gradlew :composeApp:compileKotlinIosArm64` — currently fails only on items 1 and 2 above (Koin `androidContext()` + missing iOS interface impls)

---

## What Already Works for iOS ✅

- All domain models — pure Kotlin data classes in `commonMain`
- All repository interfaces and implementations — pure Kotlin
- All ViewModels, State, Action, Event — `commonMain`
- All screen Composables — Compose Multiplatform in `commonMain`
- Ktor API clients and DTOs — engine abstracted via `expect/actual`
- Navigation keys — `@Serializable` objects in `commonMain`
- `core:ui-library` — theme, icons, components in `commonMain`; compiles clean for iOS
- `core:navigation` (`Navigator`, `NavigationState`) — compiles clean for iOS after the `navigation3-ui` fix
- Kermit logging via `Logger` — KMP-compatible
- Coil 3 image loading — KMP-compatible
- FileKit — KMP-compatible
- Kotlinx Serialization, Coroutines, Collections Immutable — all KMP-compatible
- **`androidx.paging` (3.5.0) — verified iOS-compatible (2026-07-07):** both `paging-common` and `paging-compose` publish real iOS klibs; `core:issue` and `feature:issues:impl` compile clean for iOS. No library swap needed — `app.cash.paging` (this doc's original fallback suggestion) is unnecessary and itself deprecated upstream in favor of the now-multiplatform `androidx.paging`.
- **`androidx.navigation3:navigation3-ui` — fixed (2026-07-07):** Google's own artifact publishes no iOS variant at all (only `-android`); swapped to `org.jetbrains.androidx.navigation3:navigation3-ui:1.1.1` (a JetBrains multiplatform build depending on the same Google `navigation3-runtime`). This changed `core:navigation/NavigationState.kt`: the multiplatform build only exposes `rememberNavBackStack(SavedStateConfiguration, vararg keys)` — the reflection-based single-arg overload is JVM/Android-only. Added `composeApp/AppNavKeySerializers.kt` registering all `NavKey` subclasses via `polymorphic(NavKey::class)`.
- **Every feature `impl` module (`feature:issues:impl`, `feature:sign-in:impl`, `feature:profile:impl`) compiles clean for iOS (2026-07-07)** after 3 fixes: the `navigation3-ui` swap above, the `@Preview` import fix, and the `BackHandler` `expect`/`actual`. `composeApp` itself is the only module still failing, and only on the two pre-existing items above.
