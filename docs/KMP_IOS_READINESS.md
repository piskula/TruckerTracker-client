# KMP iOS Readiness Analysis

> Generated: 2026-07-01 · Updated: 2026-07-08 (multiple passes)

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
- `PhotoReader`/`PhotoReaderImpl` (`core:common`) deleted — turned out to be dead code, never bound in Koin and never called anywhere; actual photo reading already goes through FileKit's `PlatformFile.readBytes()` directly. `PhotoData` (the data class, genuinely used) kept and moved to `PhotoData.kt` (2026-07-07)
- Koin `commonModule`/`userModule` split via `expect fun platformCommonModule(): Module` / `expect fun platformUserModule(): Module` — `core:common` and `core:user` each now have a pure `commonMain` module plus a per-platform `actual`. Android's `actual` carries the exact previous bindings (`ConnectivityManager`, `CurrentActivityHelper`, the whole auth stack); iOS's `actual` is an empty module for now, since none of those interfaces have iOS implementations yet (2026-07-07)
- **Result: `app:shared` itself now also compiles clean for `iosArm64`/`iosSimulatorArm64`, along with every feature module and every `core` module. This is compile-clean only — the iOS module list is missing bindings for `ConnectivityManager`, `AuthManager`, `UserRepository`, etc. (empty `platformCommonModule()`/`platformUserModule()`), and there's still no `app/ios/` entry point to actually run it (item 2 below). Getting the app running on iOS still needs items 1 and 2.**

---

## Remaining Issues

### 1 · Interface Implementations Missing for iOS (Koin-Wired)

**Severity:** 🟡 High — Koin cannot satisfy these bindings on iOS without iOS implementations.

These interfaces have Android implementations in `androidMain` registered via Koin. No iOS equivalents exist yet. Each needs an `iosMain` implementation and a Koin module to wire it.

| Interface | Android impl | iOS replacement |
|-----------|-------------|-----------------|
| `ConnectivityManager` (`core:common`) | `ConnectivityManagerImpl` — Android `ConnectivityManager` | ✅ Done (2026-07-08) — `NWPathMonitor` (Network.framework) |
| `CurrentActivityHelper` (`core:common`) | `Application.ActivityLifecycleCallbacks` | Not applicable as-is — `AuthManagerImpl` uses it purely to get a presentation anchor for the OAuth browser intent; the iOS OAuth rewrite (below) needs a UIWindow-based equivalent instead, not a direct port |
| `AuthManager` (`core:user`) | `AuthManagerImpl` — AppAuth (`net.openid:appauth`) | `ASWebAuthenticationSession` (AuthenticationServices.framework) |
| `UserStorage` (`core:user`) | `UserStorageImpl` — `SharedPreferences` | Interface currently exposes `net.openid.appauth.AuthState` directly (`var authState: AuthState`) — Android-only AppAuth type. Needs a platform-neutral token model before an iOS impl (`NSUserDefaults`/Keychain) is even possible |
| JWT verification (`core:user`) | `JwtParser` — RSA-SHA256 signature verification via `io.jsonwebtoken` (jjwt) | jjwt is JVM-only, no KMP port. Not just a `Base64` swap (that part's a one-liner) — needs a real `expect`/`actual` crypto implementation |
| `OpenIdManager` (`core:user`) | Android `Activity`, `PendingIntent`, `Intent` | Full reimplementation with `ASWebAuthenticationSession` |

**Estimated effort:**
- **Medium:** `ConnectivityManager` (`NWPathMonitor`)
- **High:** `UserStorage` interface redesign (drop the `AuthState` leak) + iOS impl; `JwtParser` crypto (`expect`/`actual` RSA-SHA256 verify); `AuthManager`/`OpenIdManager`/`CurrentActivityHelper` — entire OAuth browser flow needs reimplementation on `ASWebAuthenticationSession`, untestable here without an iOS simulator

`PhotoReader` removed from this table — see "What Was Already Fixed" above (it was dead code, not a real gap).

---

### 2 · iOS App Entry Point Does Not Exist

**Severity:** 🟡 High — nothing can run on iOS without this.

`app/android/` is the Android shell. There is no `app/ios/` equivalent.

**Fix:** Create an `app/ios/` Xcode project with a SwiftUI `App` entry point that calls into `app:shared` via the Compose Multiplatform iOS integration:

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

`ContentView` wraps `TruckTrackApp` exposed as a Kotlin/Native framework from `app:shared`.

---

## Remaining Checklist

- [x] Verify `androidx.paging` publishes iOS artifacts — confirmed 2026-07-07, see "What Already Works" below
- [x] Replace `androidx.navigation3:navigation3-ui` with `org.jetbrains.androidx.navigation3:navigation3-ui` — done 2026-07-07, `core:navigation` compiles clean for iOS
- [x] Fix Android-only `@Preview` import across `core:ui-library` and feature `impl` modules — done 2026-07-07
- [x] Fix Android-only `BackHandler` in `IssueDetailScreen.kt` — done 2026-07-07, `core:ui-library.BackHandler` `expect`/`actual`
- [x] Split Koin initialization — `expect fun platformCommonModule()`/`platformUserModule(): Module` pattern — done 2026-07-07 (iOS actuals are empty modules for now, see item 1)
- [x] Remove dead `PhotoReader`/`PhotoReaderImpl` — done 2026-07-07 (was unused; not a real iOS gap)
- [x] Run `./gradlew :app:shared:compileKotlinIosArm64` — passes as of 2026-07-07 (compile-clean only, see note above — not yet functional on iOS)
- [ ] Redesign `UserStorage` interface to drop the `AuthState` (AppAuth) leak, then implement iOS `UserStorage` — `NSUserDefaults` or Keychain
- [x] Implement iOS `ConnectivityManager` — `NWPathMonitor` — done 2026-07-08
- [ ] Implement `JwtParser` RSA-SHA256 verification via `expect`/`actual` (jjwt is JVM-only)
- [ ] Implement iOS `AuthManager` / `OpenIdManager` — `ASWebAuthenticationSession`; replace `CurrentActivityHelper` with a UIWindow-based presentation anchor for iOS
- [ ] Create `app/ios/` Xcode project with SwiftUI entry point

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
- **`androidx.navigation3:navigation3-ui` — fixed (2026-07-07):** Google's own artifact publishes no iOS variant at all (only `-android`); swapped to `org.jetbrains.androidx.navigation3:navigation3-ui:1.1.1` (a JetBrains multiplatform build depending on the same Google `navigation3-runtime`). This changed `core:navigation/NavigationState.kt`: the multiplatform build only exposes `rememberNavBackStack(SavedStateConfiguration, vararg keys)` — the reflection-based single-arg overload is JVM/Android-only. Added `app/shared/AppNavKeySerializers.kt` registering all `NavKey` subclasses via `polymorphic(NavKey::class)`.
- **Every feature `impl` module (`feature:issues:impl`, `feature:sign-in:impl`, `feature:profile:impl`) compiles clean for iOS (2026-07-07)** after 3 fixes: the `navigation3-ui` swap above, the `@Preview` import fix, and the `BackHandler` `expect`/`actual`.
- **`app:shared` compiles clean for iOS too (2026-07-07)**, via `expect fun platformCommonModule()`/`platformUserModule(): Module` in `core:common`/`core:user` (Android `actual` = full existing bindings, iOS `actual` = empty module for now). `initKoin()` in `app/shared/AppInitializer.kt` was already correctly designed — `androidContext()` is passed in via a caller-supplied `platformConfig` lambda from `app:android`, not hardcoded in shared code, so nothing needed to change there.
- Full Android build (`:app:android:assembleDebug`) verified unaffected by all of the above changes.
- **iOS `ConnectivityManager` — implemented (2026-07-08):** `core/common/src/iosMain/.../network/ConnectivityManagerImpl.kt` uses `NWPathMonitor` (`platform.Network` cinterop, ships with the Kotlin/Native distribution — no Xcode invocation needed at compile time). Wifi/cellular detection via `nw_path_uses_interface_type`; `isNetworkConnectionMetered` maps to `nw_path_is_expensive` (the standard iOS proxy for "metered", covering cellular + Personal Hotspot). Wired into `platformCommonModule()` in `CommonModule.ios.kt`. Verified with `./gradlew :core:common:compileKotlinIosArm64 :core:common:compileKotlinIosSimulatorArm64 :core:common:compileAndroidMain` — all three clean, no warnings.
