# module-api

API contract layer shared between backend and frontend.

- Defines ALL endpoint contracts: `@PostMapping`, `@Operation`, `@Tag`, produces/consumes, return types
- `module-server` controllers implement these interfaces — they add nothing to routing or docs on their own
- Also used to generate the OpenAPI 3.1.0 spec file (`api-docs.json`), which drives Angular client generation
- Dependencies: `compileOnly` Spring Web + SpringDoc, plus `com.momosi.trucktrack:shared` for DTOs. No Spring Data, no module-server types.
- **DTOs and enums live in the separate `:shared` build** (`shared/src/commonMain/kotlin/com/momosi/trucktrack/shared/`), not here — module-api only defines the Spring MVC contract interfaces that use them. `shared` is a plain Kotlin Multiplatform module with zero Spring/Ktor dependencies, consumed by both this backend and the KMP client.

## Package Structure

```
api/
  <domain>/
    <Name>Api.kt      ← one interface per logical group of endpoints, importing DTOs from com.momosi.trucktrack.shared.<domain>
```

## API Interface Conventions

- No `@RequestMapping` at the interface level.
- Define the base path as a `companion object` constant and reference it in each mapping:

```kotlin
import com.momosi.trucktrack.shared.common.PageDto
import com.momosi.trucktrack.shared.issue.IssueDto

@Tag(name = "Issues")
interface IssueManagementApi {

    companion object {
        private const val ENDPOINT = "/api/v1/issue"
    }

    @GetMapping(ENDPOINT)
    fun getIssueList(...): PageDto<IssueDto>

    @GetMapping("$ENDPOINT/{id}")
    fun getIssue(@PathVariable id: Long): IssueDto
}
```

- URL paths use singular nouns: `/api/v1/issue`, `/api/v1/vehicle`, `/api/v1/issue/{issueId}/photo`.
- Function names: `getIssueList`, `getVehicleList`, `getPhotoList` (not `listIssues`, not `getIssues`).
- Pagination input: `@ParameterObject pageable: PageableDto` (from SpringDoc). Do not use individual `page: Int` and `size: Int` params.
- No DTO wrapper for single-string request bodies — use `@RequestBody text: String` directly.

## Adding a new DTO or enum

New DTOs/enums go in `shared/src/commonMain/kotlin/com/momosi/trucktrack/shared/<domain>/`, not in this module:

- `@Serializable` (kotlinx.serialization) on every DTO/enum — this is what the KMP client's Ktor client uses to deserialize responses. Jackson on the server side ignores the annotation and works via reflection, same as before.
- Naming: `<Name>Dto` (not `<Name>DTO`) and `<EnumName>Dto`.
- Dates: `kotlin.time.Instant`, not `java.time.OffsetDateTime` — `java.time.*` isn't available on non-JVM KMP targets. `module-server`'s `config/JacksonConfig.kt` bridges this to/from JSON as a plain ISO-8601 string, same wire format as before.
- IDs that are UUIDs: `kotlin.uuid.Uuid`, not `java.util.UUID` — same KMP-availability reasoning. Convert with `.toKotlinUuid()`/`.toJavaUuid()` at the mapper boundary in `module-server`. `JacksonConfig.kt` bridges this too.
- No Spring/Swagger/Jackson annotations on shared types — `shared` has zero framework dependencies by design, since it's also compiled for Android/iOS.

## PageDto

```kotlin
@Serializable
data class PageDto<T>(
    val totalElements: Long,
    val totalPages: Int,
    val number: Int,
    val size: Int,
    val numberOfElements: Int,
    val content: List<T>,
)
```

## PageableDto

```kotlin
@Serializable
data class PageableDto(
    val page: Int = 0,
    val size: Int = 20,
    val sort: String? = null,   // format: "property,direction;property2,direction2"
)
```

Sort direction values: `asc` or `desc`. Multiple columns separated by `;`.

## api-docs.json

`api-docs.json` is **hand-written** — it is NOT auto-generated from annotations. Whenever you add or change an endpoint or DTO, you must also manually update `api-docs.json` and rebuild so the Angular client regenerates.

_In the future, building of api-docs.json should also happen as part of the build._

## See Also

- `../AGENTS.MD` — server-wide conventions.
- `../../shared/AGENTS.MD` — the `shared` build these DTOs actually live in.
