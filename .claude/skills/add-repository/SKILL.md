---
name: add-repository
description: Use when adding a new repository, manager, or API data source to a core module. Triggered by phrases like "add a repository", "add a manager", "fetch from API", "expose data to features", "add data source", "new repository", "call a new endpoint".
---

# Skill: Add a Repository to a Core Module

> Adds a repository (or manager) to an existing `core/*` module: domain model, interface, DTO, mapper, Ktor API client, implementation, and Koin binding. All files go in `src/commonMain/kotlin/`.

## Triggers

Load this skill when the task matches **any** of these:
- Adding a new data source or backend resource to a `core/*` module
- Task says "add a repository", "add a manager", "fetch X from the API", "expose X to feature modules"
- A new API endpoint needs to be called from the app

## Prerequisites

- The target `core/<module>` already exists with a `build.gradle.kts`
- `core:network` is available and provides a Koin-managed `HttpClient` with base URL and auth pre-configured
- The API endpoint is known (see OpenAPI at `https://tt.momosi.org/v3/api-docs`)

## Steps

### 1. Create the domain model in `model/`

Place in `core/<module>/src/commonMain/kotlin/.../core/<module>/model/<Thing>.kt`.
Pure Kotlin only — no Android imports, no `Context`, no `@StringRes`.

```kotlin
package com.momosi.trucktrack.core.<module>.model

data class <Thing>(
    val id: Long,
    val name: String,
)
```

### 2. Create the repository interface

```kotlin
package com.momosi.trucktrack.core.<module>

import com.momosi.trucktrack.core.<module>.model.<Thing>
import com.momosi.trucktrack.core.common.model.Page

interface <Thing>Repository {

    suspend fun get<Things>(page: Int? = null, size: Int? = null): Result<Page<<Thing>>>

    suspend fun get<Thing>(id: Long): Result<<Thing>>
}
```

Rules: all methods `suspend`; never throw — return `Result<T>`, `T?`, or `List<T>`; no Android types.

### 3. Create the DTO in `dto/`

```kotlin
package com.momosi.trucktrack.core.<module>.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class <Thing>Dto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
)
```

### 4. Create the DTO mapper in `dto/`

```kotlin
package com.momosi.trucktrack.core.<module>.dto

import com.momosi.trucktrack.core.<module>.model.<Thing>

internal fun <Thing>Dto.to<Thing>(): <Thing> = <Thing>(
    id = id,
    name = name,
)
```

### 5. Create the Ktor API client in `api/`

Mark `internal` — it is an implementation detail, not part of the module's public API.

```kotlin
package com.momosi.trucktrack.core.<module>.api

import com.momosi.trucktrack.core.<module>.dto.<Thing>Dto
import com.momosi.trucktrack.core.network.dto.PageDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class <Thing>Api(private val client: HttpClient) {

    suspend fun get<Things>(page: Int?, size: Int?): PageDto<<Thing>Dto> =
        client.get("api/v1/<things>") {
            page?.let { parameter("page", it) }
            size?.let { parameter("size", it) }
        }.body()

    suspend fun get<Thing>(id: Long): <Thing>Dto =
        client.get("api/v1/<things>/$id").body()
}
```

For POST with JSON body:
```kotlin
suspend fun create<Thing>(body: <Thing>CreateDto): <Thing>Dto =
    client.post("api/v1/<things>") {
        contentType(ContentType.Application.Json)
        setBody(body)
    }.body()
```

### 6. Create the repository implementation

Mark `internal` — bound via Koin, never constructed directly by feature modules.

```kotlin
package com.momosi.trucktrack.core.<module>

import com.momosi.trucktrack.core.<module>.api.<Thing>Api
import com.momosi.trucktrack.core.<module>.dto.to<Thing>
import com.momosi.trucktrack.core.<module>.model.<Thing>
import com.momosi.trucktrack.core.common.model.Page
import com.momosi.trucktrack.core.network.dto.toDomain

internal class <Thing>RepositoryImpl(
    private val api: <Thing>Api,
) : <Thing>Repository {

    override suspend fun get<Things>(page: Int?, size: Int?): Result<Page<<Thing>>> = runCatching {
        api.get<Things>(page, size).toDomain { it.to<Thing>() }
    }

    override suspend fun get<Thing>(id: Long): Result<<Thing>> = runCatching {
        api.get<Thing>(id).to<Thing>()
    }
}
```

### 7. Add Koin bindings in `di/`

Add to the module's existing `di/<Module>Module.kt`, or create it if it doesn't exist.
`get()` for `HttpClient` is resolved from `networkModule` in `core:network`.

```kotlin
package com.momosi.trucktrack.core.<module>.di

import com.momosi.trucktrack.core.<module>.<Thing>Repository
import com.momosi.trucktrack.core.<module>.<Thing>RepositoryImpl
import com.momosi.trucktrack.core.<module>.api.<Thing>Api
import org.koin.dsl.module

val <module>Module = module {
    single { <Thing>Api(get()) }
    single<<Thing>Repository> { <Thing>RepositoryImpl(get()) }
}
```

### 8. Register in `AppModule` (new Koin module files only)

If this is a new `val xxxModule`, add it to `app/shared/src/commonMain/kotlin/.../app/di/AppModule.kt`.
If adding to an existing module file, no app-level change is needed.

### 9. Update `core/<module>/AGENTS.MD`

Add the new repository interface and domain model to the Public API table.
Add the new files to the Key Files section.

## Verification

- [ ] Domain model in `model/` contains no Android imports — placed in `src/commonMain/kotlin/`
- [ ] Repository interface returns only `Result<T>`, `T?`, or `List<T>` — never throws
- [ ] `<Thing>Api` and `<Thing>RepositoryImpl` are marked `internal`
- [ ] DTO uses `@Serializable` and `@SerialName` on all fields
- [ ] Koin module has `single { <Thing>Api(get()) }` and `single<<Thing>Repository> { ... }`
- [ ] `./gradlew :core:<module>:assembleDebug` passes
- [ ] `./gradlew spotlessCheck` passes
