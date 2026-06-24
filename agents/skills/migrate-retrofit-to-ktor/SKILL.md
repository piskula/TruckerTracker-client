---
name: migrate-retrofit-to-ktor
description: Use when migrating a core module from Retrofit + OkHttp to Ktor Client. Triggered by phrases like "migrate to Ktor", "replace Retrofit", "convert to Ktor Client", "remove Retrofit from module", "migrate HTTP client".
---

# Skill: Migrate a Core Module from Retrofit to Ktor Client

> Converts a `core/*` module's API layer from Retrofit annotation-based interfaces to Ktor Client function-based API classes.

## Triggers

Load this skill when the task matches **any** of these:
- Converting a module from Retrofit to Ktor
- Task says "migrate to Ktor", "replace Retrofit with Ktor", "convert API to Ktor Client"
- Removing Retrofit dependencies from a `core` module
- Modernizing HTTP client to KMP-compatible Ktor

## Steps

### 1. Add Ktor dependencies to `gradle/libs.versions.toml` (if not present)

Add Ktor version to `[versions]`:
```toml
ktor = "3.5.0" (or newer)
```

Add libraries to `[libraries]`:
```toml
ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }
ktor-client-okhttp = { module = "io.ktor:ktor-client-okhttp", version.ref = "ktor" }
ktor-client-content-negotiation = { module = "io.ktor:ktor-client-content-negotiation", version.ref = "ktor" }
ktor-client-auth = { module = "io.ktor:ktor-client-auth", version.ref = "ktor" }
ktor-client-logging = { module = "io.ktor:ktor-client-logging", version.ref = "ktor" }
ktor-serialization-kotlinx-json = { module = "io.ktor:ktor-serialization-kotlinx-json", version.ref = "ktor" }
```

Add bundle to `[bundles]`:
```toml
ktor-client = [
    "ktor-client-core",
    "ktor-client-okhttp",
    "ktor-client-content-negotiation",
    "ktor-client-auth",
    "ktor-client-logging",
    "ktor-serialization-kotlinx-json"
]
```

### 2. Update `core:network` to provide Ktor `HttpClient`

If `core:network` uses Hilt, replace with Koin (or create parallel Koin setup):

File: `core/network/src/main/kotlin/com/momosi/trucktrack/core/network/di/NetworkModule.kt`

```kotlin
package com.momosi.trucktrack.core.network.di

import com.momosi.trucktrack.core.common.logger.Logger
import com.momosi.trucktrack.user.AuthManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private const val BASE_URL = "https://tt.momosi.org/"

val networkModule = module {

    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }
    }

    single {
        HttpClient(OkHttp) {
            defaultRequest {
                url(BASE_URL)
                contentType(ContentType.Application.Json)
            }

            install(ContentNegotiation) {
                json(get())
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val authManager = get<AuthManager>()
                        val token = authManager.token()
                        token?.let {
                            BearerTokens(accessToken = it, refreshToken = "")
                        }
                    }
                }
            }

            install(Logging) {
                level = LogLevel.BODY
                logger = object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        Logger.d("Ktor", message)
                    }
                }
            }

            engine {
                config {
                    followRedirects(true)
                }
            }
        }
    }
}
```

**Important**: 
- If `core:network` still uses Hilt for Retrofit, keep both providers temporarily. Remove Retrofit provider after all modules are migrated.
- The `contentType(ContentType.Application.Json)` in `defaultRequest` means you don't need to specify it on every POST/PUT/PATCH request — it's automatically applied to all requests.

### 3. Update the module's `build.gradle.kts`

**Remove** the Retrofit plugin and dependencies:
```kotlin
plugins {
    alias(libs.plugins.trucktrack.library)
    alias(libs.plugins.kotlin.serialization)  // Keep this - needed for @Serializable DTOs
    // Remove: alias(libs.plugins.trucktrack.retrofit)
}
```

**Add** Ktor bundle to dependencies:
```kotlin
dependencies {
    // Add Ktor
    implementation(libs.bundles.ktor.client)
    
    // Remove these if present:
    // implementation(libs.bundles.retrofit)
    
    // Keep existing dependencies:
    implementation(projects.core.common)
    implementation(projects.core.network)
    // ... etc
}
```

### 4. Convert Retrofit API interface to Ktor API class

**Before (Retrofit):**
```kotlin
package com.momosi.trucktrack.core.<module>.api

import com.momosi.trucktrack.core.<module>.dto.*
import com.momosi.trucktrack.core.network.dto.PageDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface <Thing>Api {

    @GET("api/v1/<things>")
    suspend fun get<Things>(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
    ): PageDto<<Thing>Dto>

    @GET("api/v1/<things>/{id}")
    suspend fun get<Thing>(@Path("id") id: Long): <Thing>Dto

    @POST("api/v1/<things>")
    suspend fun create<Thing>(@Body body: <Thing>CreateDto): <Thing>Dto
}
```

**After (Ktor):**
```kotlin
package com.momosi.trucktrack.core.<module>.api

import com.momosi.trucktrack.core.<module>.dto.*
import com.momosi.trucktrack.core.network.dto.PageDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

internal class <Thing>Api(private val client: HttpClient) {

    suspend fun get<Things>(page: Int?, size: Int?): PageDto<<Thing>Dto> =
        client.get("api/v1/<things>") {
            page?.let { parameter("page", it) }
            size?.let { parameter("size", it) }
        }.body()

    suspend fun get<Thing>(id: Long): <Thing>Dto =
        client.get("api/v1/<things>/$id").body()

    suspend fun create<Thing>(body: <Thing>CreateDto): <Thing>Dto =
        client.post("api/v1/<things>") {
            setBody(body)
            // contentType is already set as default in HttpClient configuration
        }.body()
}
```

**Note**: `contentType(ContentType.Application.Json)` is set as a default in the `HttpClient` configuration, so you don't need to specify it on each request. Only override it if you need a different content type (e.g., multipart form data).

### Retrofit → Ktor Conversion Reference

| Retrofit | Ktor |
|----------|------|
| `@GET("path")` | `client.get("path")` |
| `@POST("path")` | `client.post("path")` |
| `@PUT("path")` | `client.put("path")` |
| `@DELETE("path")` | `client.delete("path")` |
| `@PATCH("path")` | `client.patch("path")` |
| `@Path("id") id: Long` | Use string interpolation: `"path/$id"` |
| `@Query("key") value: String?` | `parameter("key", value)` inside request block |
| `@Body body: T` | `setBody(body)` (contentType defaults to JSON) |
| `@Multipart` + `@Part` | Use `formData { }` or `MultiPartFormDataContent` |
| Return type `T` | Call `.body<T>()` on response |

### 5. Update the DI module

**If using Hilt (old pattern):**
```kotlin
// Remove this:
@Provides
@Singleton
fun provide<Thing>Api(retrofit: Retrofit): <Thing>Api =
    retrofit.create(<Thing>Api::class.java)
```

**If using Koin (new pattern):**

Change from:
```kotlin
single { retrofit.create(<Thing>Api::class.java) }
```

To:
```kotlin
single { <Thing>Api(get()) }  // get() resolves HttpClient from networkModule
```

Full example:
```kotlin
package com.momosi.trucktrack.core.<module>.di

import com.momosi.trucktrack.core.<module>.*
import com.momosi.trucktrack.core.<module>.api.*
import org.koin.dsl.module

val <module>Module = module {
    single { <Thing>Api(get()) }
    single<<Thing>Repository> { <Thing>RepositoryImpl(get()) }
}
```

### 6. Handle special cases

#### Multipart file upload (images, attachments)

**Retrofit (old):**
```kotlin
@Multipart
@POST("api/v1/issue/{id}/photo")
suspend fun uploadPhoto(
    @Path("id") id: Long,
    @Part file: MultipartBody.Part,
): AttachmentDto
```

**Ktor (new):**
```kotlin
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

suspend fun uploadPhoto(issueId: Long, fileName: String, fileBytes: ByteArray): AttachmentDto =
    client.submitFormWithBinaryData(
        url = "api/v1/issue/$issueId/photo",
        formData = formData {
            append("file", fileBytes, Headers.build {
                append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
            })
        }
        // Ktor automatically sets multipart/form-data content type
    ).body()
```

**Note**: `submitFormWithBinaryData` automatically overrides the default JSON content type with `multipart/form-data`.

#### Custom headers

**Retrofit:**
```kotlin
@Headers("X-Custom-Header: value")
@GET("path")
```

**Ktor:**
```kotlin
client.get("path") {
    headers {
        append("X-Custom-Header", "value")
    }
}
```

#### Overriding default content type for a specific request

If you need a different content type for a specific request:
```kotlin
client.post("api/v1/special") {
    contentType(ContentType.Application.Xml)  // Override the default JSON
    setBody(xmlData)
}.body()
```

#### Response types other than suspend functions

Ktor only supports `suspend fun`. If Retrofit API used `Call<T>` or `Flow<T>`, convert to `suspend` and wrap call site in `flow { emit(api.getData()) }`.

### 7. Update repository implementation (if needed)

Repository implementations usually don't need changes — they already call `api.something()` and map the result.

**Verify**: If the repository was catching Retrofit-specific exceptions (e.g., `HttpException`), update to catch Ktor exceptions:

```kotlin
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException

override suspend fun getData(): Result<Data> = runCatching {
    api.getData().toDomain()
}.onFailure { error ->
    when (error) {
        is ClientRequestException -> Logger.e("Repo", "Client error: ${error.response.status}")
        is ServerResponseException -> Logger.e("Repo", "Server error: ${error.response.status}")
        else -> Logger.e("Repo", error, "Unknown error")
    }
}
```

### 8. Update imports in affected files

Remove:
```kotlin
import retrofit2.*
import okhttp3.*
```

Add (as needed):
```kotlin
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
```

Run `./gradlew spotlessApply` to auto-organize imports.

### 9. Remove Retrofit from module dependencies (final cleanup)

After **all** API classes in the module are migrated:

In `core/<module>/build.gradle.kts`, remove:
```kotlin
// Remove after migration complete:
implementation(libs.bundles.retrofit)
```

Keep `libs.bundles.ktor.client` and ensure `kotlin-serialization` plugin is applied.

## Verification

Before marking the migration complete, check:

- [ ] All `@GET`, `@POST`, etc. Retrofit annotations are removed from API files
- [ ] All API interfaces are now `internal class` with constructor `(private val client: HttpClient)`
- [ ] All API methods use Ktor `client.get/post/put/delete` and call `.body<T>()`
- [ ] DI module provides `<Thing>Api(get())` instead of `retrofit.create(...)`
- [ ] `build.gradle.kts` has `implementation(libs.bundles.ktor.client)` dependency
- [ ] `build.gradle.kts` has `kotlin-serialization` plugin applied
- [ ] No `import retrofit2.*` or `import okhttp3.*` in API or repository files
- [ ] `./gradlew :core:<module>:assembleDebug` compiles successfully
- [ ] `./gradlew spotlessApply` passes
- [ ] `./gradlew spotlessCheck` passes
- [ ] Manual smoke test: the feature using this module still works correctly

## Common Issues

### "Unresolved reference: HttpClient"
- Ensure `implementation(libs.bundles.ktor.client)` is in `build.gradle.kts` dependencies
- Sync Gradle after adding Ktor to `libs.versions.toml`

### "No such host" or connection errors
- Check base URL is set in `HttpClient { defaultRequest { url("...") } }`
- Verify `core:network` provides the configured `HttpClient` in Koin

### "Serialization not found for class X"
- Ensure `@Serializable` is on all DTOs
- Ensure `plugin { kotlin("serialization") }` is in the module's `build.gradle.kts`
- Ensure `ContentNegotiation` with `json(get())` is installed in `HttpClient`

### Auth token not being sent
- Verify `Auth` plugin with `bearer { ... }` is installed in `HttpClient`
- Verify `AuthManager.token()` returns the current token

### Multipart upload not working
- Use `submitFormWithBinaryData` + `formData { append(...) }`
- Do **not** use `setBody()` for multipart — Ktor handles encoding automatically

## Notes

- **One module at a time** — don't migrate all modules simultaneously. Test each one.
- **Keep Retrofit temporarily** — `core:network` can provide both Retrofit and Ktor clients during the migration. Remove Retrofit only after all modules are migrated.
- **KMP-ready** — Once all modules use Ktor, you can move `HttpClient` creation to `commonMain` when migrating to KMP.


