# app

Thin Android shell. Wires together all feature and core modules. Contains no business logic.

## Key Files

```
TruckTrackApplication.kt   ← startKoin { } — registers all Koin modules
TruckTrackActivity.kt      ← setContent { }, enableEdgeToEdge(), top-level NavHost
```

## Responsibilities

- `TruckTrackApplication`: initializes Koin with modules from all `core` and `feature` modules.
- `TruckTrackActivity`: decides the initial navigation key based on `AuthManager.authenticationState`, renders the `NavHost` using `NavigationState` + feature `EntryProvider`s.

## Depends On

All modules:
- `:core:common`, `:core:network`, `:core:user`, `:core:issue`, `:core:vehicle`, `:core:navigation`, `:core:ui-library`
- `:feature:sign-in:impl`, `:feature:issues:impl`, `:feature:profile:impl`

## Rules

- Do **not** add business logic here. If logic is needed across features, it belongs in a `core` module.
- Do **not** add new screens here. Every screen lives in a `feature/*/impl` module.
- Keep Koin module registration flat: one `loadKoinModules(...)` call per core/feature group.

