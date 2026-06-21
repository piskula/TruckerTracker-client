---
name: create-feature-module
description: Use when creating a new product feature from scratch. Triggered by phrases like "create a new feature", "add a feature module", "scaffold a feature", "new feature module", "create feature/*/api", "create feature/*/impl".
---

# Skill: Create a New Feature Module

> Scaffolds a complete `feature/<name>/api` + `feature/<name>/impl` module pair from scratch, wired into the app.

## Triggers

Load this skill when the task matches **any** of these:
- Adding a brand-new product feature that doesn't exist yet
- Creating a new `feature/*/api` or `feature/*/impl` module
- Task says "create a new feature", "add a feature module", or "scaffold a feature"

## Prerequisites

- `settings.gradle.kts` is accessible — the new module paths must be added there
- `app/build.gradle.kts` and `TruckTrackApplication.kt` are accessible for wiring

## Steps

### 1. Create directory structure

```
feature/<name>/
  api/
    build.gradle.kts
    src/main/kotlin/com/momosi/trucktrack/feature/<name>/api/
    src/main/res/values/strings.xml
  impl/
    build.gradle.kts
    src/main/kotlin/com/momosi/trucktrack/feature/<name>/impl/
    src/main/res/values/strings.xml
```

### 2. Create `feature/<name>/api/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.trucktrack.feature.api)
}

android {
    namespace = "com.momosi.trucktrack.feature.<name>.api"
}
```

### 3. Create the entry-point nav key in `api/`

File: `...feature/<name>/api/<Name>NavKey.kt`

```kotlin
package com.momosi.trucktrack.feature.<name>.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object <Name>NavKey : NavKey
```

For nav keys with parameters, use `data class` instead of `data object`:
```kotlin
@Serializable
data class <Name>NavKey(val id: Long) : NavKey
```

### 4. Create `feature/<name>/impl/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.trucktrack.feature.impl)
}

android {
    namespace = "com.momosi.trucktrack.feature.<name>.impl"
}

dependencies {
    api(projects.feature.<name>.api)
    implementation(libs.kotlinx.collections.immutable)
    // Add as needed: projects.core.issue, projects.core.user, projects.core.vehicle
}
```

### 5. Create screen files — follow `add-screen-to-feature/SKILL.md` for each screen

Package: `...feature/<name>/impl/<screen>/`
Required files: `<Screen>Screen.kt`, `<Screen>ViewModel.kt`, `<Screen>State.kt`, `<Screen>Action.kt`
Optional: `<Screen>Event.kt` (only when one-shot VM→UI signals are needed)

### 6. Create the entry provider in `impl/navigation/`

File: `...feature/<name>/impl/navigation/<Name>EntryProvider.kt`

```kotlin
package com.momosi.trucktrack.feature.<name>.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.momosi.trucktrack.core.navigation.Navigator
import com.momosi.trucktrack.feature.<name>.api.<Name>NavKey
import com.momosi.trucktrack.feature.<name>.impl.<screen>.<Screen>Screen

fun EntryProviderScope<NavKey>.<name>Entries(navigator: Navigator) {
    entry<<Name>NavKey> {
        <Screen>Screen(
            onBack = navigator::goBack,
        )
    }
}
```

### 7. Create the Koin module in `impl/di/`

File: `...feature/<name>/impl/di/<Name>Module.kt`

```kotlin
package com.momosi.trucktrack.feature.<name>.impl.di

import com.momosi.trucktrack.feature.<name>.impl.<screen>.<Screen>ViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val <name>Module = module {
    viewModel { <Screen>ViewModel(get()) }
}
```

### 8. Register modules in `settings.gradle.kts`

Add to the `include(...)` block:
```kotlin
":feature:<name>:api",
":feature:<name>:impl",
```

### 9. Wire into `app`

In `app/build.gradle.kts`:
```kotlin
implementation(projects.feature.<name>.impl)
```

In `TruckTrackApplication.kt`, add `<name>Module` to `startKoin { modules(...) }`.

In `TruckTrackActivity.kt`, call `<name>Entries(navigator)` inside the `entryProvider` block.

### 10. Create module AGENTS.MD files

Create `feature/<name>/api/AGENTS.MD` and `feature/<name>/impl/AGENTS.MD`.
Document the nav keys, screens, and dependencies. See existing modules for examples.

## Verification

- [ ] `feature/<name>/api/` contains `build.gradle.kts` and one `*NavKey.kt`
- [ ] `feature/<name>/impl/` contains `build.gradle.kts`, at least one screen package, an `EntryProvider`, and a Koin module
- [ ] Both module paths appear in `settings.gradle.kts`
- [ ] `app/build.gradle.kts` references `projects.feature.<name>.impl`
- [ ] `TruckTrackApplication.kt` includes the new Koin module
- [ ] `TruckTrackActivity.kt` calls the new `<name>Entries(navigator)`
- [ ] `./gradlew :feature:<name>:impl:assembleDebug` passes
- [ ] `./gradlew spotlessCheck` passes


