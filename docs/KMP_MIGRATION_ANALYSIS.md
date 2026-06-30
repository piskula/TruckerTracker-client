# KMP Migration Plan — TruckTrack

## Decisions

| Concern | Decision |
|---------|----------|
| UI framework | **Compose Multiplatform** (`org.jetbrains.compose`) — shared UI across platforms |
| Navigation | **Navigation 3** (`androidx.navigation3`) — `navigation3-runtime` (KMP) + `navigation3-ui` (KMP Compose integration) |
| ViewModel | **AndroidX ViewModel** — KMP-compatible since `lifecycle-viewmodel 2.8+` |
| DI | **Koin** — already migrated ✅ |
| HTTP | **Ktor Client** — already migrated ✅ |
| Logging | **Kermit** — already migrated ✅ |
| Serialization | **Kotlinx Serialization** — already KMP ✅ |
| Project layout | **Recommended KMP structure** (Kotlin docs) |

### Shared UI + domain

Both domain/data (`core`) and UI (`feature/*/impl`, `core:ui-library`) become multiplatform. The app shell (`app/` on Android, `iosApp/` on iOS) remains platform-specific.

---

## KMP Project Layout

Reference: https://kotlinlang.org/docs/multiplatform/multiplatform-project-recommended-structure.html#optimal-module-structure

```
core/common/
  src/
    commonMain/kotlin/          ← common source set (pure Kotlin)
      com/momosi/trucktrack/core/common/
    androidMain/kotlin/         ← Android-specific implementations
      com/momosi/trucktrack/core/common/
    iosMain/kotlin/             ← iOS-specific implementations (future)
      com/momosi/trucktrack/core/common/
  build.gradle.kts              ← kotlin("multiplatform") + androidTarget()
```

Convention: `commonMain` is shared code. Platform source sets (`androidMain`, `iosMain`) hold `actual` declarations and platform-specific bindings.

---

## Current State — What's Already Done

| Migration step | Status |
|----------------|--------|
| Hilt → Koin | ✅ Complete |
| Retrofit/OkHttp → Ktor Client | ✅ Complete |
| Timber → Kermit | ✅ Complete |
| Step 1 — KMP convention plugins | ✅ Complete |
| Step 2 — Version catalog (Compose Multiplatform) | ✅ Complete |
| Step 3 — `core:network` → KMP | ✅ Complete |
| Step 4 — `core:common` → KMP | ✅ Complete |
| Step 5 — `core:vehicle` → KMP | ✅ Complete |
| Step 6 — `core:issue` → KMP | ✅ Complete |
| Step 7 — `core:user` → KMP (partial) | ✅ Complete |
| Step 8 — `core:ui-library` → CMP | ✅ Complete |
| Step 9 — `core:navigation` → KMP | ✅ Complete |
| Step 10 — `feature/*/api` → KMP | ✅ Complete |
| Step 11 — `feature/*/impl` → CMP | ✅ Complete |
| Step 12 — `app` wiring update | ✅ Complete |
| Convention plugin unification | ✅ Complete — no more `kmp.*` variants |

---

## Module Readiness Assessment

| Module | KMP status |
|--------|------------|
| `core:issue` | 🟢 Complete — fully in `commonMain` |
| `core:vehicle` | 🟢 Complete — fully in `commonMain` |
| `core:network` | 🟢 Complete — fully in `commonMain` |
| `core:common` | 🟢 Complete — interfaces in `commonMain`, Android impls in `androidMain` |
| `core:user` | 🟢 Complete — interfaces in `commonMain`, AppAuth/storage in `androidMain` |
| `core:navigation` | 🟢 Complete — Navigation 3 is KMP-compatible |
| `core:ui-library` | 🟢 Complete — Compose Multiplatform in `commonMain` |
| `feature/*/api` | 🟢 Complete — `@Serializable` nav keys in `commonMain` |
| `feature/*/impl` | 🟢 Complete — Compose Multiplatform screens in `commonMain` |
| `composeApp` | 🟢 Complete — shared KMP app module |
| `androidApp` | ⬜ Android-only shell (by design) |

---

## Migration Steps (ordered)

### Phase A — Library replacements (prepare dependencies)

#### Step 1 — Build system: KMP convention plugins ✅

Convention plugins were unified — no separate `kmp.*` variants needed. The existing `trucktrack.library`, `trucktrack.compose`, `trucktrack.feature.api`, `trucktrack.feature.impl` plugins now all configure `kotlin("multiplatform")` + `com.android.kotlin.multiplatform.library` directly.

**Completed:**
- [x] `LibraryPlugin.kt` — Applies `kotlin("multiplatform")` + `com.android.kotlin.multiplatform.library`, configures Android target, JDK 21 toolchain, Spotless
- [x] `ComposePlugin.kt` — Applies `org.jetbrains.compose` + `org.jetbrains.kotlin.plugin.compose`, adds Compose Multiplatform deps to `commonMain`
- [x] `FeatureApiPlugin.kt` — KMP + serialization + Navigation 3 runtime
- [x] `FeatureImplPlugin.kt` — KMP + Compose + Koin + `:core:ui-library` + `:core:navigation`
- [x] `KoinPlugin.kt` — Koin core in `commonMain`, Koin Android/Compose in `androidMain`
- [x] `KtorPlugin.kt` — Ktor Client core + serialization for KMP
- [x] All registered in `build-logic/convention/build.gradle.kts` and `libs.versions.toml`

---

#### Step 2 — Update `libs.versions.toml` for Compose Multiplatform ✅

**Completed:**
- [x] Added `compose-multiplatform` version + plugin entry (`org.jetbrains.compose`)
- [x] Replaced `androidx-compose-bom` usages with Compose Multiplatform dependencies
- [x] Compose Material3 provided via `org.jetbrains.compose` (API-identical)
- [x] Replaced `material-icons-extended` with Compose Multiplatform equivalent

---

### Phase B — Migrate core domain modules (no UI)

#### Step 3 — Migrate `core:network` to KMP ✅

**Completed:**
- [x] Switched to `trucktrack.library` (KMP)
- [x] Moved source to `src/commonMain/kotlin/`
- [x] Ktor engine configured for Android (`OkHttp`), swappable for iOS
- [x] Koin module in `commonMain`

---

#### Step 4 — Migrate `core:common` to KMP ✅

**Completed:**
- [x] `Logger`, `DispatcherProvider`, `Page<T>`, `ConnectivityManager` interface in `commonMain`
- [x] `ConnectivityManagerImpl`, `CurrentActivityHelper`, Android Koin bindings in `androidMain`

---

#### Step 5 — Migrate `core:vehicle` to KMP ✅

**Completed:**
- [x] All source in `commonMain` (zero Android imports)
- [x] Ktor API client + repository impl fully common

---

#### Step 6 — Migrate `core:issue` to KMP ✅

**Completed:**
- [x] All source in `commonMain` (zero Android imports)
- [x] Ktor API client + repository impl fully common

---

#### Step 7 — Migrate `core:user` to KMP ✅

**Completed:**
- [x] Interfaces, models, common types in `commonMain`
- [x] AppAuth, UserStorage, AuthActivity, JwtParser in `androidMain`
- [x] Koin wiring split between `commonMain` and `androidMain`

---

### Phase C — Migrate UI modules to Compose Multiplatform

#### Step 8 — Migrate `core:ui-library` to Compose Multiplatform ✅

**Completed:**
- [x] Components, theme, icons in `commonMain` using Compose Multiplatform
- [x] `@Preview` functions in `androidMain`
- [x] Coil 3 stays (KMP-compatible)

---

#### Step 9 — Migrate `core:navigation` to KMP ✅

**Completed:**
- [x] `NavigationState`, `Navigator` in `commonMain`
- [x] Navigation 3 runtime + UI are KMP-compatible

---

#### Step 10 — Migrate `feature/*/api` to KMP ✅

**Completed:**
- [x] All nav keys in `commonMain` — `NavKey` + `@Serializable` are both KMP

---

#### Step 11 — Migrate `feature/*/impl` to Compose Multiplatform ✅

**Completed:**
- [x] All screens, ViewModels, State/Action/Event in `commonMain`
- [x] `ViewModel` stays (KMP since `lifecycle-viewmodel 2.8+`)
- [x] `koinViewModel()` stays (Koin Compose is KMP)
- [x] `@Preview` functions in `androidMain`

---

### Phase D — App shell

#### Step 12 — Update app wiring ✅

**Completed:**
- [x] Created `composeApp` KMP module with shared root Composable and Koin wiring
- [x] `androidApp` is thin shell: `TruckTrackApplication` + `TruckTrackActivity`
- [x] `androidApp` depends on `composeApp` and calls shared `TruckTrackApp()`

---

## Library Replacements Summary

| Current | Replacement | Status |
|---------|-------------|--------|
| ~~Timber~~ | Kermit | ✅ Done |
| ~~Hilt~~ | Koin | ✅ Done |
| ~~Retrofit/OkHttp~~ | Ktor Client | ✅ Done |
| ~~`androidx.compose:compose-bom`~~ | `org.jetbrains.compose` (Compose Multiplatform plugin) | ✅ Done |
| ~~`ui-text-google-fonts`~~ | Compose Multiplatform font resources | ✅ Done |
| ~~`android.util.Base64`~~ | `kotlin.io.encoding.Base64` (stdlib) | ✅ Done |
| ~~`SharedPreferences` (UserStorage)~~ | `expect/actual` split | ✅ Done |
| ~~`jjwt`~~ | Kept in `androidMain` | ✅ Done |
| ~~`ktor-client-okhttp` (hardcoded)~~ | Engine in `androidMain`, swappable | ✅ Done |
| ~~`collectAsStateWithLifecycle()`~~ | `lifecycle-runtime-compose` KMP artifact | ✅ Done |

---

## What Stays Platform-Specific

| Component | Platform | Reason |
|-----------|----------|--------|
| `androidApp` module | Android | Android shell (`Activity`, `Application`) |
| `iosApp` (future) | iOS | iOS shell (`@main App`, `ComposeUIViewController`) |
| `ConnectivityManagerImpl` | Android (`androidMain`) | Uses `android.net.ConnectivityManager` |
| `CurrentActivityHelper` | Android (`androidMain`) | Android Activity lifecycle |
| AppAuth / `OpenIdManager` | Android (`androidMain`) | OAuth browser flow |
| `UserStorageImpl` | Android (`androidMain`) | SharedPreferences / DataStore |
| Compose `@Preview` functions | Android (`androidMain`) | Android Studio tooling only |
| Ktor `OkHttp` engine | Android (`androidMain`) | Platform HTTP engine |

---

## Current Architecture

```
androidApp/                       ← Android-only shell (Activity, Application)
composeApp/
  src/commonMain/kotlin/          ← Shared KMP app: root Composable, Koin wiring, AppInitializer
iosApp/                           ← iOS-only shell (future: ComposeUIViewController)
core/
  common/
    src/commonMain/kotlin/        ← KMP: Logger, DispatcherProvider, Page, interfaces
    src/androidMain/kotlin/       ← Android: ConnectivityManagerImpl, CurrentActivityHelper
  network/
    src/commonMain/kotlin/        ← KMP: HttpClient factory, Koin module
  issue/
    src/commonMain/kotlin/        ← KMP: models, API, repository (all pure Kotlin)
  vehicle/
    src/commonMain/kotlin/        ← KMP: models, API, repository (all pure Kotlin)
  user/
    src/commonMain/kotlin/        ← KMP: interfaces, models
    src/androidMain/kotlin/       ← Android: AppAuth, UserStorage, AuthActivity, JwtParser
  navigation/
    src/commonMain/kotlin/        ← KMP: NavigationState, Navigator (Compose Multiplatform)
  ui-library/
    src/commonMain/kotlin/        ← KMP: TruckTrackTheme, components (Compose Multiplatform)
    src/androidMain/kotlin/       ← Android: @Preview functions
feature/
  */api/
    src/commonMain/kotlin/        ← KMP: @Serializable NavKey objects
  */impl/
    src/commonMain/kotlin/        ← KMP: ViewModel, Screen, State/Action/Event (Compose Multiplatform)
    src/androidMain/kotlin/       ← Android: @Preview functions
```

---

## Verification (per step)

After each step:
1. `./gradlew assembleDebug` passes
2. `./gradlew spotlessApply` passes
3. No Android imports in `src/commonMain/kotlin/` directories of migrated modules
4. App runs on device and features work as before

---

## Completion Summary

All 12 migration steps have been completed. The project is now fully Kotlin Multiplatform with Compose Multiplatform UI.

| Phase | Steps | Status |
|-------|-------|--------|
| **Phase A** — Build system + version catalog | Steps 1–2 | ✅ Complete |
| **Phase B** — Core domain modules → KMP | Steps 3–7 | ✅ Complete |
| **Phase C** — UI modules → Compose Multiplatform | Steps 8–11 | ✅ Complete |
| **Phase D** — App shell | Step 12 | ✅ Complete |
