# module-api

API contract layer shared between backend and frontend.

- Defines ALL endpoint contracts: `@PostMapping`, `@Operation`, `@Tag`, produces/consumes, return types
- `module-server` controllers implement these interfaces — they add nothing to routing or docs on their own
- Also used to generate the OpenAPI 3.1.0 spec file (`api-docs.json`), which drives Angular client generation
- Dependencies: `compileOnly` Spring Web + SpringDoc only. No Spring Data, no module-server types.

## Package Structure

```
api/
  common/
    PageDTO.kt        ← generic paginated response
    PageableDTO.kt    ← generic pagination + sort request
  <domain>/
    <Name>Api.kt      ← one interface per logical group of endpoints
    dto/
      <Name>DTO.kt
      <EnumName>DTO.kt
```

## API Interface Conventions

- No `@RequestMapping` at the interface level.
- Define the base path as a `companion object` constant and reference it in each mapping:

```kotlin
@Tag(name = "Issues")
interface IssueManagementApi {

    companion object {
        private const val ENDPOINT = "/api/v1/issue"
    }

    @GetMapping(ENDPOINT)
    fun getIssueList(...): PageDTO<IssueDTO>

    @GetMapping("$ENDPOINT/{id}")
    fun getIssue(@PathVariable id: Long): IssueDTO
}
```

- URL paths use singular nouns: `/api/v1/issue`, `/api/v1/vehicle`, `/api/v1/issue/{issueId}/photo`.
- Function names: `getIssueList`, `getVehicleList`, `getPhotoList` (not `listIssues`, not `getIssues`).
- Pagination input: `@ParameterObject pageable: PageableDTO` (from SpringDoc). Do not use individual `page: Int` and `size: Int` params.
- No DTO wrapper for single-string request bodies — use `@RequestBody text: String` directly.

## Enum DTOs

Server-side enums are not accessible in module-api. Mirror them as `enum class` in the relevant `dto/` package:

```kotlin
enum class IssueStatusDTO { OPEN, IN_PROGRESS, DONE }
```

Values must match the server enum names exactly — controllers convert via `ServerEnum.valueOf(dto.name)` and `EnumDTO.valueOf(serverEnum.name)`.

## PageDTO

```kotlin
data class PageDTO<T>(
    val totalElements: Long,
    val totalPages: Int,
    val number: Int,
    val size: Int,
    val numberOfElements: Int,
    val content: List<T>,
)
```

## PageableDTO

```kotlin
data class PageableDTO(
    val page: Int = 0,
    val size: Int = 20,
    val sort: String? = null,   // format: "property,direction;property2,direction2"
)
```

Sort direction values: `asc` or `desc`. Multiple columns separated by `;`.

## api-docs.json

`api-docs.json` is **hand-written** — it is NOT auto-generated from annotations. Whenever you add or change an endpoint or DTO, you must also manually update `api-docs.json` and rebuild so the Angular client regenerates.

_In the future, building of api-docs.json should also happen as part of the build._
