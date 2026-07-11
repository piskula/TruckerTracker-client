# TruckTrack

[![Build Debug Apps](https://github.com/piskula/TruckerTracker-client/actions/workflows/build-app.yml/badge.svg)](https://github.com/piskula/TruckerTracker-client/actions/workflows/build-app.yml)
[![Release Android App](https://github.com/piskula/TruckerTracker-client/actions/workflows/release-app.yml/badge.svg)](https://github.com/piskula/TruckerTracker-client/actions/workflows/release-app.yml)

Fleet management app for drivers and mechanics — report and track issues, manage vehicles, sign in
via OAuth/OIDC. Kotlin Multiplatform, targeting Android and iOS from one shared codebase.

## Tech stack

| Concern | Choice |
|---------|--------|
| UI | Compose Multiplatform (`org.jetbrains.compose`) |
| Navigation | Navigation 3 (`androidx.navigation3` + `org.jetbrains.androidx.navigation3:navigation3-ui`) |
| ViewModel | AndroidX ViewModel |
| DI | Koin |
| HTTP | Ktor Client |
| Logging | Kermit |
| Serialization | Kotlinx Serialization |
| Auth / OIDC | [kotlin-multiplatform-oidc](https://github.com/kalinjul/kotlin-multiplatform-oidc) |

## Project structure

Multi-module KMP project — `app:*` (platform shells + shared app wiring), `core:*` (domain logic,
infra), `feature:*/api` + `feature:*/impl` (product features). See **`AGENTS.MD`** for the full
module map, dependency rules, and coding conventions — that's the canonical reference for
contributing here (including for AI coding agents).

## Building & running

**Android** — `./gradlew :app:android:assembleDebug`, or open the project in Android Studio and
run the `app:android` configuration.

**iOS** — open `app/ios/iosApp.xcodeproj` in Xcode (15+) and run. See
**`docs/KMP_IOS_READINESS.md`** for current known iOS limitations before relying on a build —
notably, sign-in has not yet been verified end-to-end on a device or simulator.

CI builds both on every push to `main` (`.github/workflows/build-app.yml`) and publishes a debug
APK and an unsigned iOS Simulator build to the repo's `latest` pre-release.

## Releasing

A signed Android release (`.github/workflows/release-app.yml`) is cut by pushing a version tag —
there's no separate version bump commit, the tag *is* the version:

```bash
git tag v1.2.3
git push origin v1.2.3
```

- Tag must match `vMAJOR.MINOR.PATCH`, with `MINOR` and `PATCH` each under 100 (so
  `versionCode = MAJOR * 10000 + MINOR * 100 + PATCH` can't collide across versions).
- Produces a signed `truck-track-<version>.apk` and `truck-track-<version>.aab`, both built with
  `versionName`/`versionCode` embedded from the tag, published as a GitHub Release named after the
  tag.
- Requires four repo secrets already configured: `ANDROID_KEYSTORE_BASE64`,
  `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS`, `ANDROID_KEY_PASSWORD`.
- iOS has no equivalent signed release pipeline yet — no distribution certificate/provisioning
  profile is configured (see `docs/KMP_IOS_READINESS.md`).

## Docs

- **`AGENTS.MD`** — module structure, dependency rules, architecture (MVVM), coding conventions.
- **`docs/KMP_IOS_READINESS.md`** — known iOS-specific limitations and how to resolve them.
- **`docs/TODO.md`** — product feature backlog.
