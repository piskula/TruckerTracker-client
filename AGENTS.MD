# TruckTrack — Agent Instructions

## Project Overview

TruckTrack is an Android multi-module application for fleet management. Follow these project-specific standards:

* **Language** — Kotlin 2.x for all development.
* **UI** — Jetpack Compose only. Never use XML layouts.
* **Dependency Injection** — Koin. KMP-compatible, no annotation processing required.
* **Libraries** — Use entries from `gradle/libs.versions.toml`. Do not add new libraries without checking KMP compatibility first.
* **Concurrency** — Kotlin coroutines and Flows exclusively. No RxJava, no callbacks.

---

## Project Structure

Multi-module architecture. Root package: `com.momosi.trucktrack`.

### Modules

* **`app`** — Thin Android shell. `TruckTrackApplication`, `TruckTrackActivity`, Koin wiring (`startKoin`), top-level `NavHost`. Should contain only Android entrypoints.
* **`feature`** (multiple modules) — UI screens and logic for each product feature. Always split into:
  * `api` — navigation keys only (`@Serializable` `NavKey` objects).
  * `impl` — ViewModel, screen Composables, state, actions, events.
  * Current features: `sign-in`, `issues`, `profile`.
* **`core`** (multiple modules) — Shared domain logic, utilities, and infrastructure:
  * `common` — `DispatcherProvider`, `ConnectivityManager`, `CurrentActivityHelper`, `Logger` (Timber wrapper), `Page<T>` model.
  * `network` — `NetworkModule` (Ktor client setup), `PageDto`, `PageDtoMapper`.
  * `user` — `AuthManager`, `UserRepository`, OAuth/OIDC via AppAuth, JWT parsing via jjwt, `UserStorage`.
  * `issue` — `Issue`, `IssueStatus`, `IssuePriority`, `Account` models; `IssueRepository`.
  * `vehicle` — `Vehicle`, `VehicleType` models; `VehicleRepository`.
  * `navigation` — `NavigationState`, `Navigator` wrapping Navigation 3.
  * `ui-library` — `TruckTrackTheme`, `AppTheme`, `TruckTrackIcons`, all reusable Compose components.

### Module Dependency Rules

* `feature/*/api` — depends only on `:core:navigation`. Contains only `NavKey` objects. No business logic.
* `feature/*/impl` — depends on its own `api` module + any `core` modules needed. No cross-feature dependencies.
* `core` modules — may depend on other `core` modules in a directed acyclic graph. Never depend on `feature` modules.
* `app` — depends on all `feature/*/impl` and all `core` modules. The only wiring point.

### Package Structure

* Root: `com.momosi.trucktrack`
* Features: `com.momosi.trucktrack.feature.<name>.api` / `.impl`
* Core: `com.momosi.trucktrack.core.<name>`
* App: `com.momosi.trucktrack` (top-level application, activity, DI)

---

## Coding Guidelines

### Architecture — MVVM

#### Component Responsibilities

* **Composable** — Presenter. Receives immutable `state`, delegates interactions via callbacks named `on<Action>` (present tense — never `on<Something>Clicked`). Never holds or mutates state directly.
* **ViewModel** — Extends `ViewModel()` directly (no base class). Exposes a single `StateFlow<State>`. Handles `Action`s. Talks to `core` layer only.

#### ViewModel Pattern

```kotlin
// Always use a private backing field
private val state = StateFlow<FeatureState> 
    field = MutableStateFlow<FeatureState>(FeatureState.Loading)
```

* Expose state with `.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), initialValue)`.
* Collect state in Composables with `collectAsStateWithLifecycle()`.
* One-shot events: `SharedFlow` or `Channel`.

#### State / Action / Event

* **State** — `sealed interface` + `StateFlow`. All data classes marked `@Immutable`. **No lambdas in State** — use `Action` instead.
* **Action** — `sealed interface` for UI-to-VM intents.
* **Event** — `sealed interface` for one-off VM-to-UI signals (e.g., navigate away).

#### File Structure

One package per feature screen. Keep related files together:
`IssuesScreen.kt`, `IssuesViewModel.kt`, `IssuesState.kt`, `IssuesAction.kt`, `IssuesEvent.kt`

#### Data Layer

* **Repository** — data retrieval, persistence, source abstraction.
* **Manager** — complex business logic, orchestration.
* Always define a public **interface**; place implementation in the same module (e.g., `IssueRepository` + `IssueRepositoryImpl`). Bind with Hilt `@Binds`.
* Use **data classes** for domain models.
* **Never throw across public interfaces.** Express failure with:
  * `Result<T>` — for recoverable operations with error context (use `runCatching` internally).
  * `T?` — for optional values with no useful error detail.
  * `List<T>` — for collections (empty = none found).

### Navigation

* **Navigation 3** (`androidx.navigation3`) for all navigation.
* Navigation keys are `@Serializable` objects implementing `NavKey`, in `feature/*/api`.
* Screen entries registered via `EntryProviderScope<NavKey>` extensions in `feature/*/impl/navigation/`.

### Koin Dependency Injection

* **Koin** (`io.insert-koin:koin-core`) is the DI framework — KMP-compatible, runtime-based.
* ViewModels: declare with `viewModel { MyViewModel(get()) }` in a Koin module. Inject in Composables via `koinViewModel()`.
* Modules: use `val myModule = module { }`. Group by feature/core module. Single top-level `startKoin { }` in `TruckTrackApplication`.
* Prefer `single { }` for repositories and managers (singleton scope).
* Prefer `factory { }` for use-cases and other short-lived objects.
* No `@Inject` annotations on constructors — pass dependencies explicitly in the `module { }` DSL.
* Bind interfaces to implementations with `single<IssueRepository> { IssueRepositoryImpl(get()) }`.

### UI Library

* All reusable Compose components must live in `:core:ui-library`.
* Feature and `app` modules must **not** import `androidx.compose.material3` directly. Use only `androidx.compose.foundation`, `androidx.compose.ui`, and components from `:core:ui-library`.
* Theme access via `AppTheme` (colors, typography, shapes) provided exclusively by `:core:ui-library`.

### Compose Stability

* Wrap list state/parameters in `kotlinx.collections.immutable` (`ImmutableList`, `persistentListOf`).
* Annotate State data classes with `@Immutable`.
* Annotate non-data-class Composable parameters with `@Stable` if they are stable but the compiler cannot infer it.

### Compose Previews

* Every file with `@Composable` functions must have `@Preview` functions for key composables.
* Wrap in `TruckTrackTheme { }`.
* Preview functions must be `private`, named with a `Preview` suffix (e.g., `IssueCardDriverPreview`).
* Use realistic sample data. Avoid empty/null states unless explicitly testing that variant.
* Never preview top-level screen composables that require a `ViewModel`. Preview the inner stateless content composable instead.

### Logging

* Use `Logger` from `core:common` — never call `Timber` directly.
* Always provide a tag: `Logger.d("FeatureName", "message")`.
* Levels: `v`, `d`, `i`, `w`, `e`. Throwable variants: `Logger.e("Tag", throwable, "message")`.

### Networking

* HTTP client: **Ktor Client** (`io.ktor:ktor-client-*`). Configured in `core:network`. KMP-compatible.
* Engine: `ktor-client-okhttp` on Android (swap to `ktor-client-darwin` on iOS).
* Serialization: **Kotlinx Serialization JSON** via `ktor-client-content-negotiation` + `ktor-serialization-kotlinx-json`.
* All API interfaces/clients live in the relevant `core` module (e.g., `core:issue`, `core:user`).
* Base URL: `https://tt.momosi.org/`.
* Authentication: inject a bearer token via Ktor's `Auth` plugin, wired to `AuthManager`.
* To use Ktor in a module: add `implementation(libs.bundles.ktor.client)` to `dependencies` in `build.gradle.kts`. No plugin needed.

### Serialization

* **Kotlinx Serialization** — for all DTO serialization and navigation key serialization (`@Serializable`). KMP-compatible.
* Avoid `Parcelable` / `@Parcelize` for new code. Use Kotlinx Serialization instead.

### Convention Plugins (build-logic)

Use these in module `build.gradle.kts` files:

* `trucktrack.library` — base Android library (compileSdk, minSdk, Kotlin, Spotless).
* `trucktrack.application` — `app` module only (Android application setup).
* `trucktrack.feature.api` — feature API module (library + serialization + navigation3 runtime).
* `trucktrack.feature.impl` — feature impl module (library + Koin + Compose + `:core:ui-library` + `:core:navigation`).
* `trucktrack.koin` — Koin core + Koin Android + Koin Compose dependencies.
* `trucktrack.compose` — Compose compiler plugin + BOM + compose/navigation3 bundles.
* `trucktrack.spotless` — ktlint formatting via Spotless.

### Style & Conventions

* Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html).
* Use `data object` instead of plain `object` for singleton data carriers.
* Run `./gradlew spotlessApply` to auto-format before committing.
* No wildcard imports. Always import specific symbols.
* No comments, KDoc, or Javadoc unless explicitly requested. Code must be self-explanatory.
* `public` only when part of the public API. Prefer `private`, then `internal`.

### Naming Conventions

* `camelCase` — functions, variables, properties.
* `PascalCase` — classes, interfaces, objects, enum values, `@Composable` functions.
* `UPPER_SNAKE_CASE` — constants.

---

## Standard Libraries

| Library | Purpose | KMP-compatible |
|---------|---------|---------------|
| `kotlinx-coroutines` | Concurrency | ✅ |
| `kotlinx-serialization-json` | JSON, nav keys | ✅ |
| `kotlinx-collections-immutable` | Stable Compose state | ✅ |
| Coil 3 (`coil-compose`, `coil-network-okhttp`) | Image loading | ✅ |
| Ktor Client (`ktor-client-core`, `ktor-client-okhttp`) | HTTP client | ✅ |
| Koin (`koin-core`, `koin-android`, `koin-compose`) | Dependency injection | ✅ |
| Timber (via `Logger`) | Logging | ❌ Android only — replace with Kermit |
| AppAuth (`net.openid:appauth`) | OAuth/OIDC | ❌ Android only |
| jjwt | JWT parsing | ❌ JVM only |
| Navigation 3 (`androidx.navigation3`) | Navigation | ❌ Android only |

---

## KMP Migration Readiness

The project is being prepared for **Kotlin Multiplatform** migration. This affects every technical decision. A full migration analysis is at `docs/KMP_MIGRATION_ANALYSIS.md`.

### Core Principle

> When choosing a technology, always check for a KMP-compatible alternative first. Never introduce a new Android-only or JVM-only dependency unless there is no KMP option and Android-only scope is explicitly accepted.

### Agent Skills

Reusable step-by-step workflows are in `agents/skills/`. Use them for recurring tasks:

| Skill | When to use |
|-------|-------------|
| [`create-feature-module.md`](agents/skills/create-feature-module.md) | Scaffolding a new `feature/*/api` + `feature/*/impl` pair |
| [`add-screen-to-feature.md`](agents/skills/add-screen-to-feature.md) | Adding a new screen (ViewModel + Composable + State/Action/Event) |
| [`add-repository.md`](agents/skills/add-repository.md) | Adding a repository/manager to a `core/*` module |
| [`migrate-retrofit-to-ktor.md`](agents/skills/migrate-retrofit-to-ktor/SKILL.md) | Migrating a module from Retrofit to Ktor Client |
| [`di-migration.md`](agents/skills/di-migration/SKILL.md) | Migrating DI frameworks (Hilt→Koin, Dagger→Koin, etc.) |

### KMP-Compatible Replacements for Remaining Blockers

| Current (Android-only) | KMP Replacement | Status |
|------------------------|-----------------|--------|
| Timber (via `Logger`) | **Kermit** (`co.touchlab:kermit`) — done ✅ | ✅ Complete |
| AppAuth | Platform `expect/actual` with AppAuth on Android, AppAuth-iOS on iOS | Planned Phase 3 |
| jjwt | Manual Base64 + Kotlinx Serialization parsing, or `io.github.nefilim.kjwt` | Planned Phase 3 |
| Navigation 3 | **Decompose** or **Voyager** (both KMP-compatible) | Planned Phase 5 |
| `androidx.compose` BOM | **Compose Multiplatform** (`org.jetbrains.compose`) — drop-in replacement | Planned Phase 5 |
| Android DataStore | `androidx.datastore:datastore-preferences-core` (KMP artifact) | Planned Phase 1 |

### Rules for New Code

1. **New `core` domain models** — pure Kotlin only. No Android imports. No `Context`, no `@StringRes`, no Android-specific types.
2. **New `core` repository/manager interfaces** — pure Kotlin. Return types use only KMP-compatible types (`String`, `Long`, `kotlinx.datetime`, data classes, `Flow`, `Result`, etc.).
3. **New libraries** — must be KMP-compatible or explicitly scoped to `androidMain`. Document the reason for any Android-only addition.
4. **Avoid `@Parcelize`** — use Kotlinx Serialization instead.
5. **Avoid `android.net.Uri`** — use `String` or `java.net.URI` for URIs.
6. **Avoid `android.content.Context` in `commonMain` scope** — pass only the data you need, not the Context.

### Planned Target Architecture (for context)

```
shared/
  commonMain/       ← Pure Kotlin: domain models, interfaces, business logic
  androidMain/      ← Android expect/actual implementations
  iosMain/          ← iOS expect/actual implementations (future)

androidApp/         ← Thin Android shell (Activity, Application, Koin init)
iosApp/             ← Thin iOS shell calling shared Compose Multiplatform (future)

feature/*/impl      ← Compose Multiplatform UI (minimal changes needed)
core/ui-library     ← Compose Multiplatform component library
```

---

## Unit Testing

No tests exist yet. When adding tests, follow these conventions:

* **MockK** — for mocking.
* **Turbine** — for `Flow` testing.
* **kotlinx-coroutines-test** (`runTest`) — for coroutine testing.
* Place tests in `src/test/kotlin/` mirroring the main source package.

---

## String Resources & Copywriting

When writing `strings.xml` resources, act as a **technical copywriter** — precise about domain concepts, but writing for a non-developer fleet operator.

* Write for a **non-technical user** — a driver or mechanic who understands their job but not software.
* Be **concise** — one or two short sentences maximum.
* **Plain English**. No jargon unless immediately explained.
* **Active voice**, **present tense**.
* Avoid vague phrases ("some data", "certain features"). Be specific.
* Be technically accurate about truck/fleet domain concepts.
