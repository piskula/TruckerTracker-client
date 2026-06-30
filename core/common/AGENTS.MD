# core:common

Base dependency for all other modules. Contains shared utilities with no business logic. KMP module with `commonMain` and `androidMain` source sets.

## Public API

| Symbol | Description |
|--------|-------------|
| `DispatcherProvider` | Coroutine dispatcher abstraction (`main`, `io`, `default`). Inject instead of hardcoding `Dispatchers.*`. |
| `ConnectivityManager` | Interface in `commonMain`. `ConnectivityManagerImpl` is the Android impl in `androidMain`. Use the interface everywhere. |
| `CurrentActivityHelper` | Provides the current `Activity` — used by `core:user` for OAuth browser launch. Android-only (`androidMain`). |
| `Logger` | Kermit wrapper. Always use this, never call Kermit directly. |
| `Page<T>` | Generic pagination model returned by all paginated repository calls. |

## Key Files

```
commonMain/
  logger/Logger.kt
  coroutines/DispatcherProvider.kt
  coroutines/Flows.kt              ← Flow utility extensions
  network/ConnectivityManager.kt   ← Interface (pure Kotlin)
  model/Page.kt
  di/CommonModule.kt               ← Koin bindings (common part)
androidMain/
  network/ConnectivityManagerImpl.kt
  lifecycle/CurrentActivityHelper.kt
  di/AndroidCommonModule.kt        ← Koin bindings needing androidContext()
```

## Depends On

Nothing. This is the base module — no project dependencies.

