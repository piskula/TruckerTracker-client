# TruckTrack — Copilot Agent Instructions

**Trust these instructions. Only search the codebase if something here appears incomplete or incorrect.**

---

## Mandatory Skill Loading

Before writing any code, scan this table. If the task matches a trigger, **read the full SKILL.md first**.

| Task type | Skill file |
|-----------|-----------|
| Creating a new feature (new `feature/*/api` + `feature/*/impl` pair) | [`agents/skills/create-feature-module/SKILL.md`](../agents/skills/create-feature-module/SKILL.md) |
| Adding a new screen to an existing feature | [`agents/skills/add-screen-to-feature/SKILL.md`](../agents/skills/add-screen-to-feature/SKILL.md) |
| Adding a repository or manager to a `core/*` module | [`agents/skills/add-repository/SKILL.md`](../agents/skills/add-repository/SKILL.md) |
| Migrating a module from Retrofit+Hilt to Ktor+Koin | [`agents/skills/migrate-module-to-ktor-koin/SKILL.md`](../agents/skills/migrate-module-to-ktor-koin/SKILL.md) |

> Skill files follow the convention in [`agents/skills/SKILL_CONVENTION.md`](../agents/skills/SKILL_CONVENTION.md).
> Each skill contains **Triggers** (when to use it), **Prerequisites**, **Steps**, and **Verification** checklists.
> Always complete the Verification checklist before considering a skill done.

---

## Project Summary

TruckTrack is an Android fleet-management app that lets **drivers** report vehicle maintenance issues and **mechanics** manage and resolve them. It communicates with a REST backend at `https://tt.momosi.org/` (OpenAPI at `/v3/api-docs`) secured via OAuth2/OIDC (`https://sso.momosi.org/realms/trucktrack`).

**Stack:** Kotlin 2.3.21 · Android (minSdk 28, targetSdk/compileSdk 36) · Jetpack Compose · Hilt · Retrofit + OkHttp · Navigation 3 · Kotlinx Serialization · Coroutines/Flow · Coil 3

**Build tooling:** Gradle 8 (wrapper at `./gradlew`) · JDK 21 · AGP 9.x · Convention plugins in `build-logic/`

---

## Build & Validate

### Always run before committing
```bash
./gradlew spotlessApply    # auto-format all Kotlin/KTS files (ktlint + Compose rules)
./gradlew assembleDebug    # full compile — this is what CI checks
```
If `spotlessApply` fails with a configuration cache error, retry with `--no-configuration-cache`.

### Check formatting without modifying
```bash
./gradlew spotlessCheck    # fails with a diff if any file needs formatting
```

### Build a specific module only
```bash
./gradlew :core:issue:assembleDebug
./gradlew :feature:issues:impl:assembleDebug
```

### No tests exist yet — skip test tasks.

### CI pipeline (`.github/workflows/build-app.yml`)
Runs on push to `main`. Steps: checkout → JDK 21 (JetBrains) → `./gradlew assembleDebug` → publishes `truck-track.apk` as a pre-release. **CI does not run spotlessCheck** — but always apply spotless locally to avoid ktlint violations that break the compile.

---

## Project Layout

```
build-logic/convention/   Convention plugins (trucktrack.*)
gradle/libs.versions.toml All dependency versions and bundles — always add libs here
settings.gradle.kts       Module include list — add new modules here
gradle.properties         JVM heap (4g), parallel, config-cache, caching enabled

app/                      Thin shell: Application, Activity, Hilt wiring, NavHost
core/
  common/                 Logger, DispatcherProvider, ConnectivityManager, Page<T>
  network/                OkHttp+Retrofit client, PageDto, PageDtoMapper
  user/                   AuthManager, UserRepository, AppAuth, jjwt, UserStorage
  issue/                  IssueRepository, IssueAttachmentRepository, all issue models
  vehicle/                VehicleRepository, Vehicle, VehicleType
  navigation/             Navigator, NavigationState (wraps Navigation 3)
  ui-library/             TruckTrackTheme, AppTheme, TruckTrackIcons, all components
feature/
  sign-in/{api,impl}/
  issues/{api,impl}/      3 screens: list, detail, create
  profile/{api,impl}/
```

**Detailed instructions:** `AGENTS.MD` (root), per-module `AGENTS.MD` in each module directory, skill workflows in `agents/skills/<skill-name>/SKILL.md`.

---

## Architecture Rules — violations cause compile or runtime failures

### Module graph
- `feature/*/api` → only `:core:navigation`
- `feature/*/impl` → own `api` + any `core/*`
- `core/*` → other `core/*` (DAG only, never `feature`)
- `app` → everything

### Adding a new module — required steps
1. Create `build.gradle.kts` with a `trucktrack.*` convention plugin
2. Add `":module:path"` to `settings.gradle.kts` include block
3. Wire Hilt module in `app` if needed

### Convention plugins (in `build.gradle.kts`)
| Plugin | Use for |
|--------|---------|
| `trucktrack.library` | Any `core/*` module |
| `trucktrack.feature.api` | `feature/*/api` |
| `trucktrack.feature.impl` | `feature/*/impl` |
| `trucktrack.hilt` | Modules needing DI |
| `trucktrack.compose` | Modules with Compose UI |
| `trucktrack.retrofit` | Modules calling the API |
| `trucktrack.spotless` | All modules (auto-included by `trucktrack.library`) |

### Code rules enforced by ktlint (will fail `spotlessCheck`)
- No wildcard imports — always import specific symbols
- Functions/constructors with ≥ 3 parameters must use multiline format
- Compose `@Preview` functions must not use past-tense names (`onXxxClicked` → `onXxx`)

### Critical patterns
- **Never import `androidx.compose.material3` in feature or app modules** — use `:core:ui-library` components only
- **Never throw across public interfaces** — return `Result<T>`, `T?`, or `List<T>`
- **State data classes must be `@Immutable`** — use `ImmutableList` / `persistentListOf` for list fields
- **ViewModels use `@HiltViewModel` + `@Inject constructor`** — injected via `hiltViewModel()` in Composables
- **All new `@Serializable` navigation keys** in `feature/*/api` only
- **New string resources** go in the feature module's `src/main/res/values/strings.xml`
- **New icons** must be added to `TruckTrackIcons` in `core/ui-library` — never import Material icon classes in feature modules

### Dependency declarations
- `api(projects.feature.xxx.api)` in impl modules for their own api module
- `implementation(libs.xxx)` for all other dependencies (never hardcode versions — use `libs.*`)

