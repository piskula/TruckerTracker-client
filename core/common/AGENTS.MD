# core:common

Base dependency for all other modules. Contains shared utilities with no business logic. KMP module with `commonMain`, `androidMain`, and `iosMain` source sets.

## Public API

| Symbol | Description |
|--------|-------------|
| `DispatcherProvider` | Coroutine dispatcher abstraction (`main`, `io`, `default`). Inject instead of hardcoding `Dispatchers.*`. |
| `ConnectivityManager` | Interface in `commonMain`. `ConnectivityManagerImpl` — Android impl (`android.net.ConnectivityManager`) in `androidMain`, iOS impl (`NWPathMonitor`) in `iosMain`. Use the interface everywhere. |
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
  di/CommonModule.android.kt       ← Koin bindings needing androidContext()
iosMain/
  network/ConnectivityManagerImpl.kt ← NWPathMonitor-backed
  di/CommonModule.ios.kt
```

## Depends On

Nothing. This is the base module — no project dependencies.

