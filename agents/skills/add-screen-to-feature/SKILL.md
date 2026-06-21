---
name: add-screen-to-feature
description: Use when adding a new screen, page, or view to an existing feature module. Triggered by phrases like "add a screen", "create a new screen", "add a page", "new composable screen", "add a view", "new screen in feature".
---

# Skill: Add a Screen to an Existing Feature

> Adds a new screen — ViewModel, Composable, State, Action, and optional Event — to an existing `feature/*/impl` module.

## Triggers

Load this skill when the task matches **any** of these:
- Adding a new screen to an existing feature
- Task says "add a screen", "create a page", "add a new view/composable screen"
- A new nav key is needed inside an existing feature's `impl` module
- A ViewModel needs to be created for a new screen

## Prerequisites

- The target `feature/*/impl` module already exists
- The `EntryProvider` file for the feature already exists in `impl/navigation/`
- The Koin module for the feature already exists in `impl/di/`

## Steps

### 1. Create the screen package

```
feature/<feature>/impl/src/main/kotlin/com/momosi/trucktrack/feature/<feature>/impl/<screen>/
  <Screen>Screen.kt
  <Screen>ViewModel.kt
  <Screen>State.kt
  <Screen>Action.kt
  <Screen>Event.kt      ← omit if no one-shot events needed
```

### 2. Create `<Screen>State.kt`

```kotlin
package com.momosi.trucktrack.feature.<feature>.impl.<screen>

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

sealed interface <Screen>State {
    data object Loading : <Screen>State

    data object Error : <Screen>State

    @Immutable
    data class Content(
        val items: ImmutableList<SomeDomainModel> = persistentListOf(),
    ) : <Screen>State
}
```

### 3. Create `<Screen>Action.kt`

```kotlin
package com.momosi.trucktrack.feature.<feature>.impl.<screen>

sealed interface <Screen>Action {
    data object Retry : <Screen>Action
    data class SelectItem(val id: Long) : <Screen>Action
}
```

### 4. Create `<Screen>Event.kt` (only if one-shot navigation or signals are needed)

```kotlin
package com.momosi.trucktrack.feature.<feature>.impl.<screen>

sealed interface <Screen>Event {
    data class NavigateToDetail(val id: Long) : <Screen>Event
}
```

### 5. Create `<Screen>ViewModel.kt`

```kotlin
package com.momosi.trucktrack.feature.<feature>.impl.<screen>

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momosi.trucktrack.core.common.logger.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class <Screen>ViewModel(
    private val someRepository: SomeRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<<Screen>State>(<Screen>State.Loading)
    val state: StateFlow<<Screen>State> = _state
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), <Screen>State.Loading)

    private val _events = Channel<<Screen>Event>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        load()
    }

    fun onAction(action: <Screen>Action) {
        when (action) {
            is <Screen>Action.Retry -> load()
            is <Screen>Action.SelectItem -> viewModelScope.launch {
                _events.send(<Screen>Event.NavigateToDetail(action.id))
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.value = <Screen>State.Loading
            someRepository.getData()
                .onSuccess { _state.value = <Screen>State.Content(it.toImmutableList()) }
                .onFailure { Logger.e("<Screen>ViewModel", it, "Failed to load") }
        }
    }
}
```

### 6. Create `<Screen>Screen.kt`

```kotlin
package com.momosi.trucktrack.feature.<feature>.impl.<screen>

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momosi.trucktrack.core.uilibrary.theme.TruckTrackTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun <Screen>Screen(
    onNavigateToDetail: (Long) -> Unit,
    viewModel: <Screen>ViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is <Screen>Event.NavigateToDetail -> onNavigateToDetail(event.id)
            }
        }
    }

    <Screen>Content(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun <Screen>Content(
    state: <Screen>State,
    onAction: (<Screen>Action) -> Unit,
) {
    when (state) {
        is <Screen>State.Loading -> LoadingSpinner()
        is <Screen>State.Error -> ErrorContent(onRetry = { onAction(<Screen>Action.Retry) })
        is <Screen>State.Content -> { /* actual content */ }
    }
}

@Preview(showBackground = true)
@Composable
private fun <Screen>ContentPreview() {
    TruckTrackTheme {
        <Screen>Content(
            state = <Screen>State.Content(),
            onAction = {},
        )
    }
}
```

### 7. Add an internal nav key if the screen is navigated to with parameters

Internal nav keys (not the feature entry point) live in `impl/navigation/`, not `api/`:

```kotlin
// feature/<feature>/impl/navigation/<Screen>NavKey.kt
package com.momosi.trucktrack.feature.<feature>.impl.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
internal data class <Screen>NavKey(val id: Long) : NavKey
```

### 8. Register the entry in the EntryProvider

In `feature/<feature>/impl/navigation/<Feature>EntryProvider.kt`, add:

```kotlin
entry<<Screen>NavKey> { key ->
    <Screen>Screen(
        id = key.id,
        onBack = navigator::goBack,
    )
}
```

### 9. Register the ViewModel in the Koin module

In `feature/<feature>/impl/di/<Feature>Module.kt`:

```kotlin
val <feature>Module = module {
    // ...existing declarations...
    viewModel { <Screen>ViewModel(get()) }
}
```

## Verification

- [ ] All five files exist in the screen package (`Screen`, `ViewModel`, `State`, `Action`, and `Event` if needed)
- [ ] `<Screen>State` data classes are annotated `@Immutable`; list fields use `ImmutableList`
- [ ] `<Screen>Screen` does not import `androidx.compose.material3` — only `core:ui-library` components
- [ ] `<Screen>Screen` uses `koinViewModel()`, not `hiltViewModel()`
- [ ] ViewModel is registered in the feature's Koin module
- [ ] Nav entry is registered in the EntryProvider
- [ ] `./gradlew :feature:<feature>:impl:assembleDebug` passes
- [ ] `./gradlew spotlessCheck` passes


