# KMP Migration Analysis — TruckTrack

Reference: https://kotlinlang.org/docs/multiplatform/migrate-from-android.html

## Executive Summary

TruckTrack is a well-structured, multi-module Android app built with Compose, Hilt, Coroutines/Flow, Retrofit, and Navigation 3. The architecture is already close to what KMP recommends — clean interfaces, separated data/UI layers, `StateFlow`/`Flow` throughout. However, several hard Android dependencies block a direct lift-and-shift. This document identifies every blocker per module and proposes a concrete migration path.

---

## Target Architecture

```
shared/                     ← New Kotlin Multiplatform module
  commonMain/               ← Pure Kotlin: business logic, models, interfaces
  androidMain/              ← Android-specific expect/actual implementations
  iosMain/ (future)         ← iOS-specific expect/actual implementations

androidApp/                 ← Thin Android shell (Activity, DI wiring, Android UI)
iosApp/ (future)            ← Thin iOS shell

feature/*/impl              ← Compose Multiplatform UI (stays mostly as-is)
core/ui-library             ← Compose Multiplatform UI (stays mostly as-is)
```

---

## Module-by-Module Analysis

### 1. `core:common`

**Current Android dependencies:**
| Code | Android API | Blocker level |
|------|-------------|---------------|
| `Logger` / `Timber` | `timber.log.Timber` — Android-only library | 🔴 High |
| `ResourcesManager` | `android.content.Context`, `@StringRes`, `@ColorRes`, `@Dimension` | 🔴 High |
| `ConnectivityManagerImpl` | `android.net.ConnectivityManager`, `NetworkCallback` | 🔴 High |
| `CurrentActivityHelper` | `android.app.Activity`, `Application.ActivityLifecycleCallbacks`, `@ApplicationContext` | 🔴 High |
| `DispatcherProvider` | Pure Kotlin (`Dispatchers`) | 🟢 None |
| DataStore Preferences | `androidx.datastore:datastore-preferences` | 🟡 Medium — KMP-compatible variant exists (`datastore-preferences-core`) |

**Required changes:**
- Replace `Timber` with a KMP-compatible logging abstraction (e.g., `co.touchlab:kermit` or a custom `expect/actual` wrapper).
- Extract `Logger` interface to `commonMain`; provide platform-specific trees via `expect/actual`.
- Move `ResourcesManager` Android implementation behind `expect/actual`. Common layer should deal with string keys, not resource IDs.
- `ConnectivityManager` interface stays in `commonMain`; `ConnectivityManagerImpl` moves to `androidMain`.
- `CurrentActivityHelper` is inherently Android-only. Move entirely to `androidMain`. The `AuthManager` that depends on it (to launch the OAuth browser) needs redesign (see `core:user`).
- Switch to `androidx.datastore:datastore-preferences-core` (KMP artifact) for DataStore.

---

### 2. `core:network`

**Current Android dependencies:**
| Code | Android API | Blocker level |
|------|-------------|---------------|
| `OkHttp` + Retrofit | `com.squareup.okhttp3`, `com.squareup.retrofit2` | 🔴 High — Android/JVM only |
| `ConnectivityManagerImpl` dependency | (see `core:common`) | 🔴 High |
| Hilt DI | `dagger.hilt` | 🔴 High |

**Required changes:**
- Replace Retrofit/OkHttp with a KMP-compatible HTTP client. The canonical choice is **Ktor Client** (`io.ktor:ktor-client-*`). Ktor has engine implementations for Android (`OkHttp`), iOS (`Darwin`), etc.
- The `RetrofitPlugin` convention plugin must be replaced with a `KtorPlugin` or equivalent.
- All `@Module @InstallIn` Hilt modules in `core/network/di/` need replacement with KMP DI (see DI section below).

---

### 3. `core:user`

This module has the most invasive Android coupling.

**Current Android dependencies:**
| Code | Android API | Blocker level |
|------|-------------|---------------|
| `AppAuth` (`net.openid:appauth`) | Android-only OAuth library | 🔴 High |
| `AuthActivity` | `ComponentActivity`, `@AndroidEntryPoint` | 🔴 High |
| `OpenIdManager` | Uses `AppAuth` types (`AuthState`, `AuthorizationRequest`, etc.) | 🔴 High |
| `UserStorage` | `SharedPreferences` / DataStore with Android context | 🔴 High |
| `JwtParser`, `TokenVerifier` | `io.jsonwebtoken:jjwt-*` — JVM-only JWT library | 🟡 Medium |
| `CurrentActivityHelper` | Required to launch browser from `AuthManagerImpl.signIn()` | 🔴 High |
| `androidx.core.net.toUri()` | Android extension | 🟡 Medium |
| Hilt | All DI modules | 🔴 High |

**Required changes:**
- OAuth/OIDC flow is the hardest part. Options:
  - **Android target**: keep `AppAuth` in `androidMain`, expose a platform-agnostic `AuthManager` interface in `commonMain`.
  - **iOS target**: use `AppAuth-iOS` or a custom OIDC implementation in `iosMain`.
  - Consider using a KMP-friendly OIDC library such as `io.github.kalinjul.easyoidc` or roll a custom `expect/actual` wrapper.
- Extract `AuthManager` interface to `commonMain` (already clean as an interface — no change needed there).
- `AuthManagerImpl` splits into:
  - `commonMain`: orchestration logic, token caching, state management.
  - `androidMain`: browser launch via `CustomTabsIntent` or `AuthActivity`.
- Replace `jjwt` with a KMP JWT parser (e.g., `io.github.nefilim.kjwt` or manual Base64+JSON parsing via `kotlinx.serialization`).
- Replace `androidx.core.net.toUri()` with `java.net.URI` (available in `commonMain` via Kotlin stdlib) or a KMP URI abstraction.
- `UserStorage` must use KMP DataStore or `kotlinx-io` for persistence.

---

### 4. `core:issue` and `core:vehicle`

**Current Android dependencies:**
| Code | Android API | Blocker level |
|------|-------------|---------------|
| Retrofit API interfaces | JVM-only | 🔴 High |
| OkHttp for file upload | Android/JVM | 🔴 High |
| Hilt modules | Android-only | 🔴 High |
| Domain models, `IssueRepository`, `VehicleRepository` interfaces | Pure Kotlin | 🟢 None |

**Required changes:**
- Repository interfaces and domain models move to `commonMain` as-is.
- Repository implementations that use Retrofit move to using Ktor in `commonMain` (Ktor is KMP-compatible).
- File attachment upload using `OkHttp.RequestBody` → Ktor `MultiPartFormDataContent`.
- Hilt modules replaced by KMP DI solution.

---

### 5. `core:navigation`

**Current Android dependencies:**
| Code | Android API | Blocker level |
|------|-------------|---------------|
| `androidx.navigation3` | Compose/Android navigation, no KMP support yet | 🔴 High |
| `NavigationState`, `Navigator`, `ResultStore` | Depends on Navigation 3 types | 🔴 High |

**Required changes:**
- `androidx.navigation3` does **not** have a KMP artifact. Two options:
  1. Keep `core:navigation` as Android-only and accept that navigation stays per-platform.
  2. Replace with **Decompose** (`com.arkivanov.decompose`) or **Voyager** (`cafe.adriel.voyager`), which are KMP-compatible navigation libraries.
- Navigation keys (`@Serializable object` in `feature/*/api`) are already pure Kotlin and move to `commonMain` without changes.

---

### 6. `core:ui-library`

**Current Android dependencies:**
| Code | Android API | Blocker level |
|------|-------------|---------------|
| `androidx.compose.material3` | Android Compose | 🟡 Medium — Compose Multiplatform has Material3 |
| `ui-text-google-fonts` | Android-only Google Fonts API | 🔴 High |
| `material-icons-extended` | Android-only icons bundle | 🟡 Medium — available in CMP |
| Coil (`coil-compose`, `coil-core`) | KMP-compatible since Coil 3 | 🟢 None |
| `navigation3-ui` | Android-only | 🔴 High |

**Required changes:**
- Switch to **Compose Multiplatform** (`org.jetbrains.compose`) instead of `androidx.compose`. API is identical; only the plugin and BOM change.
- Replace `ui-text-google-fonts` with platform fonts or a KMP font loading solution.
- Remove direct `navigation3-ui` dependency from `ui-library`; navigation concerns belong in `core:navigation`.
- Coil 3 is already KMP-compatible — no change needed.

---

### 7. `feature/sign-in`, `feature/issues`, `feature/profile`

**Current Android dependencies:**
| Code | Android API | Blocker level |
|------|-------------|---------------|
| `@HiltViewModel`, `@Inject` | Hilt | 🔴 High |
| `ViewModel`, `viewModelScope` | `androidx.lifecycle` | 🟡 Medium — KMP ViewModel exists (`lifecycle-viewmodel` 2.8+) |
| `collectAsStateWithLifecycle()` | Android lifecycle | 🟡 Medium — available in `lifecycle-runtime-compose` KMP |
| Compose UI code | Jetpack Compose | 🟡 Medium — Compose Multiplatform drop-in |
| Navigation entry registration | `EntryProviderScope` / `navigation3` | 🔴 High |

**Required changes:**
- ViewModels: Replace `@HiltViewModel` with a KMP DI approach. The `ViewModel` class itself is available in KMP since `androidx.lifecycle:lifecycle-viewmodel:2.8`.
- Switch Compose from `androidx.compose` to `org.jetbrains.compose` (Compose Multiplatform). Most UI code requires minimal or no changes.
- Replace navigation entry registration with the chosen KMP navigation library's equivalent.
- `SignInAction.Failed.NoActivity` is Android-specific — remove or map to a generic error after `AuthManager` redesign.

---

### 8. `app` module

**Current Android dependencies:**
| Code | Android API | Blocker level |
|------|-------------|---------------|
| `ComponentActivity`, `setContent {}` | Android | 🔴 High — stays in `androidApp` |
| `enableEdgeToEdge()` | Android | 🔴 High — stays in `androidApp` |
| `@AndroidEntryPoint` | Hilt | 🔴 High |
| Hilt application setup | `@HiltAndroidApp` | 🔴 High |

**Required changes:**
- The `app` module becomes a thin Android shell (`androidApp`). All wiring that is not Android-specific moves to the shared module.
- `TruckTrack.kt` (Application class) keeps Hilt init in `androidApp`, or is replaced by KMP DI init.

---

## Cross-Cutting Concerns

### Dependency Injection — Hilt → Kotlin Inject or Koin

Hilt is Android-only and cannot be used in `commonMain`. Options:

| Library | KMP Support | Migration Effort |
|---------|-------------|-----------------|
| **Kotlin Inject** (`me.tatarka.inject`) | Full KMP, KSP-based, compile-time | Medium |
| **Koin** (`io.insert-koin:koin-core`) | Full KMP, runtime DI | Low |
| **Manual DI** | N/A | High |

**Recommendation**: Migrate to **Kotlin Inject** for compile-time safety comparable to Hilt, or **Koin** for the lowest friction migration path. Given the existing clean `@Binds`/`@Provides` structure, Kotlin Inject is the closest conceptual match.

- All `@Module @InstallIn(SingletonComponent::class)` → `@Component` / `@Provides` (kotlin-inject) or `module { }` (Koin).
- All `@HiltViewModel` → `@Inject constructor` on ViewModel + platform-specific ViewModel factory.
- All `@Singleton` → `@Singleton` (kotlin-inject) or `single { }` (Koin).

### Build Configuration — Convention Plugins

All convention plugins in `build-logic` assume Android. They need to be extended or replaced:

| Plugin | Change needed |
|--------|--------------|
| `trucktrack.library` | Add `kotlin("multiplatform")` alongside or instead of `com.android.library` |
| `trucktrack.application` | Keep Android-only; used only by `androidApp` |
| `trucktrack.hilt` | Remove or gate behind Android sourceSet |
| `trucktrack.compose` | Switch to `org.jetbrains.compose` plugin + CMP BOM |
| `trucktrack.retrofit` | Replace with Ktor plugin |
| `trucktrack.feature.api` | Already near-pure Kotlin; add KMP source sets |
| `trucktrack.feature.impl` | Add Compose Multiplatform, remove Hilt |

A new `trucktrack.kmp` convention plugin should be introduced that configures `kotlin("multiplatform")` with `androidTarget()` and future platform targets.

### `libs.versions.toml` Changes

| Entry | Action |
|-------|--------|
| `hilt` | Remove or scope to Android only |
| `retrofit`, `okhttp` | Replace with `ktor` versions |
| `appauth` | Move to Android-only, add platform-specific entries |
| `timber` | Replace with `kermit` or custom expect/actual |
| `jjwt-*` | Replace with KMP JWT solution |
| `compose-bom` | Replace `androidx.compose:compose-bom` with `org.jetbrains.compose:compose-bom` |
| `navigation3` | Remove or scope to Android only; add KMP nav library |
| `datastore` | Change to `datastore-preferences-core` KMP artifact |

---

## What Does NOT Need to Change

These parts of the codebase are already KMP-compatible or require minimal changes:

- **Domain model data classes** — all `model/` directories use pure Kotlin types.
- **Repository and Manager interfaces** — pure Kotlin interfaces with `StateFlow`/`Flow`.
- **`DispatcherProvider`** — pure Kotlin coroutines.
- **`kotlinx.serialization`** — already KMP-compatible; navigation keys stay as-is.
- **`kotlinx-collections-immutable`** — KMP-compatible.
- **State/Action/Event sealed interfaces** in feature modules — pure Kotlin.
- **Coil 3** — KMP-compatible since v3.

---

## Recommended Migration Order

### Phase 1 — Shared Core (Lowest Risk)

1. Add `kotlin("multiplatform")` to `build-logic` and create `trucktrack.kmp` convention plugin.
2. Migrate `core:common` domain models and interfaces to KMP source sets.
3. Migrate `core:issue` and `core:vehicle` domain models and repository interfaces to `commonMain`.
4. Replace Timber with Kermit (or custom `expect/actual` Logger).
5. Switch DataStore to the KMP-compatible `datastore-preferences-core` artifact.

### Phase 2 — Network Layer

6. Replace Retrofit/OkHttp with Ktor Client in `core:network`, `core:issue`, `core:vehicle`.
7. Implement Ktor engines as `expect/actual` or via engine-per-platform configuration.
8. Replace `RetrofitPlugin` convention plugin with `KtorPlugin`.

### Phase 3 — Authentication

9. Extract `AuthManager` orchestration to `commonMain`.
10. Implement OAuth browser flow in `androidMain` (keep `AppAuth`) and `iosMain` (use `AppAuth-iOS` or `ASWebAuthenticationSession`).
11. Replace `jjwt` with a KMP JWT parser.
12. Remove `CurrentActivityHelper` dependency from `AuthManager`; pass the platform browser launcher via `expect/actual`.

### Phase 4 — Dependency Injection

13. Replace Hilt with Kotlin Inject or Koin across all modules.
14. Update all `@HiltViewModel` to plain `@Inject` ViewModels with KMP-compatible factory.
15. Update all `@Module @InstallIn` to the chosen DI equivalent.

### Phase 5 — UI & Navigation

16. Switch Compose BOM from `androidx.compose` to `org.jetbrains.compose` (Compose Multiplatform).
17. Migrate `core:ui-library` to Compose Multiplatform.
18. Replace `androidx.navigation3` with a KMP-compatible navigation library (e.g., Decompose or Voyager).
19. Update feature navigation entry registrations.
20. Replace `ui-text-google-fonts` with platform-native font loading.

### Phase 6 — Android Shell

21. Slim down `app` module to only Android-specific entrypoints (`TruckTrackActivity`, `TruckTrackApplication`, Hilt/DI init).
22. Create `iosApp` with SwiftUI entry point calling the shared Compose Multiplatform `MainViewController`.

---

## Effort Estimate

| Area | Effort |
|------|--------|
| Build system (Gradle, convention plugins) | High |
| `core:common` | Medium |
| `core:network` (Retrofit → Ktor) | Medium |
| `core:user` (AppAuth, JJWT) | High |
| `core:issue`, `core:vehicle` | Low |
| `core:navigation` (navigation3 → KMP nav) | High |
| `core:ui-library` (CMP switch) | Low–Medium |
| Feature modules (Compose, DI) | Medium |
| Hilt → Kotlin Inject/Koin | High |
| `app` slimming + iOS shell | Medium |

**Total estimated complexity: High**. The core logic is already well-separated and the architecture is clean, which significantly reduces risk. The biggest blockers are Hilt, AppAuth, and Navigation 3 — all of which have clear KMP-compatible replacements.

