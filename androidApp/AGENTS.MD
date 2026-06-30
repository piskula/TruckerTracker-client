# androidApp

Thin Android shell. Contains only Android platform entrypoints. No business logic, no UI screens.

## Key Files

```
TruckTrackApplication.kt   ← Calls initializeApp() from composeApp to start Koin + Logger
TruckTrackActivity.kt      ← setContent { }, enableEdgeToEdge(), renders TruckTrackApp() from composeApp
```

## Responsibilities

- `TruckTrackApplication`: initializes Koin via `initializeApp()` from `composeApp`, sets up Coil image loader.
- `TruckTrackActivity`: calls `setContent { TruckTrackApp() }` — the shared root Composable from `composeApp`.

## Depends On

- `:composeApp` — shared KMP app module (root Composable, Koin wiring, all feature/core transitives)
- `:core:common` — Logger initialization
- `:core:network` — Coil/Ktor image loader setup

## Rules

- Do **not** add business logic here. If logic is needed across features, it belongs in a `core` module.
- Do **not** add new screens here. Every screen lives in a `feature/*/impl` module.
- Do **not** add Koin module registration here. All Koin modules are registered in `composeApp`'s `AppModule`.
- This module uses standard Android `com.android.application` plugin, not KMP.
