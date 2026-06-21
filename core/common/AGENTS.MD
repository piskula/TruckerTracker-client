# core:common

Base dependency for all other modules. Contains shared utilities with no business logic.

## Public API

| Symbol | Description |
|--------|-------------|
| `DispatcherProvider` | Coroutine dispatcher abstraction (`main`, `io`, `default`). Inject instead of hardcoding `Dispatchers.*`. |
| `ConnectivityManager` | Interface. `ConnectivityManagerImpl` is the Android impl. Use the interface everywhere. |
| `CurrentActivityHelper` | Provides the current `Activity` — used by `core:user` for OAuth browser launch. Android-only. |
| `Logger` | Timber wrapper. Always use this, never call `Timber` directly. |
| `Page<T>` | Generic pagination model returned by all paginated repository calls. |

## Key Files

```
logger/Logger.kt
coroutines/DispatcherProvider.kt
coroutines/Flows.kt          ← Flow utility extensions
network/ConnectivityManager.kt
network/ConnectivityManagerImpl.kt
lifecycle/CurrentActivityHelper.kt
model/Page.kt
di/ConnectivityModule.kt     ← Hilt bindings for ConnectivityManager
```

## Depends On

Nothing. This is the base module — no project dependencies.

## KMP Notes

- `DispatcherProvider` and `Page<T>` are already pure Kotlin — safe to move to `commonMain` as-is.
- `Logger` wraps Timber (Android-only). Target replacement: **Kermit** (`co.touchlab:kermit`).
- `ConnectivityManager` interface is pure Kotlin; `ConnectivityManagerImpl` uses Android APIs — keep impl in `androidMain`.
- `CurrentActivityHelper` is inherently Android-only; stays in `androidMain`.

