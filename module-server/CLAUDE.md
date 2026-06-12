# module-server

Spring Boot 4 backend (Kotlin, Java 21). Base package: `sk.momosilabs.truckTrack`.

## Layering Rules

```
Controller → UseCase → Persistence (or shared Service)
```

- **Controllers** implement the `module-api` interface and inject use cases only. No persistence calls, no business logic.
- **Use cases** contain business logic. They call persistence or shared services. **Use cases never call other use cases.** If two use cases need the same query, both call persistence independently.
- **Persistence** handles all data access.

Use case package and class names must not use plural nouns — use descriptive action + singular noun: `getIssueList`, `getVehicleList`, `createIssue`, `startIssue`, etc.

Each use case lives in its own package under `.service.<useCaseName>/` with two files:
- `<UseCaseName>UseCase.kt` — interface
- `<UseCaseName>.kt` — `@Service class` implementing the interface, with `@IsUser`/`@IsAdmin` and `@Transactional` on the override method

The single method on the interface must not be named `execute`. Name it after the action: `get`, `create`, `start`, `resolve`, `delete`, etc.

## Entity & Persistence Patterns

**Entity class:**
- Regular Kotlin class (NOT a data class). Fields as constructor properties.
- `@Entity(name = "table_name")` with explicit lowercase snake_case name. No `@Table` annotation.
- ID: `val id: Long = 0L` with `@Id @GeneratedValue(strategy = GenerationType.IDENTITY)`. Default `0L` allows construction without a DB round-trip.
- For externally-assigned PKs (e.g. Keycloak UUID): `val id: UUID` with no `@GeneratedValue` — caller provides the value.
- Non-nullable fields: Kotlin non-nullable type + `@field:NotNull` (note the `@field:` target prefix).
- Nullable fields: Kotlin `?` suffix only.
- `val` for immutable fields (id, FK refs); `var` for fields that get updated after creation.
- **Relationships: always `@ManyToOne` only — never `@OneToMany`, never bidirectional.** This is a deliberate rule.
- Non-nullable `@ManyToOne`: use `@ManyToOne(optional = false)` + `@field:NotNull`. Omit `@JoinColumn` unless a custom column name is required.
- Annotation order on a parameter: JPA/domain annotation first (`@Enumerated`, `@ManyToOne`), then `@field:NotNull` on the next line.
- Blank line between every constructor parameter block for readability.
- No shared base entity, no `@MappedSuperclass`, no `@Embeddable`.
- `LocalDateTime` for timestamps in entities; field names suffixed with `Utc` (e.g. `startedAtUtc`).
- `AccountEntity` (package `account`) stores only `id` (Keycloak UUID), `username`, `fullName` — no `role` column. Role is read from the JWT at runtime.

**Model class:**
- `data class` in `model/` package. `id = 0L` or `UUID(0,0)` signals a new (unsaved) record.
- Timestamps use `OffsetDateTime` — no `Utc` suffix (e.g. `createdAt: OffsetDateTime`).
- Mapper converts: entity → model via `.atOffset(ZoneOffset.UTC)`; model → entity via `.toLocalDateTime()`.

**Package layout per domain:**
```
<domain>/
  entity/
    <Name>Entity.kt
  model/
    <Name>Model.kt                    ← data class; id = 0L / UUID(0,0) for new records
  service/
    <Name>Persistence.kt              ← interface owned by the service layer (DIP); returns models, never entities
    <useCaseName>/
      <UseCaseName>UseCase.kt
      <UseCaseName>.kt
  persistence/
    <Name>PersistenceProvider.kt      ← @Repository class implementing the interface
    repository/
      <Name>Repository.kt             ← JpaRepository<Entity, Id> interface
    mapper/
      <Name>EntityMapper.kt           ← extension fns: fun Entity.toModel() = Model(...)
```

**Filter data classes:**
- When a persistence `findPage` (or similar) has multiple optional filter params, wrap them in a `data class <Domain>ListFilter(...)` with nullable fields defaulting to `null`.
- Place this class in the `service/` package (not a use-case subpackage) so the persistence interface can import it.

**Persistence provider:**
- `@Repository class` implementing the `<Name>Persistence` interface.
- Constructor-injects repositories (and `EntityManager` if QueryDSL is needed).
- Read methods: `@Transactional(readOnly = true)`. Write methods: `@Transactional`.
- Throws `GlobalNotFoundException("fieldName=value not found")` for missing records.
- Never combine create and update in a single `save()` method. Expose `create(model)` and `update(model)` as separate functions on both the interface and the provider.
- Prefer `getReferenceById` over `findById.orElseThrow` — it avoids a SELECT and lets JPA throw `EntityNotFoundException` on access. Exception: `update()` must use `findById` to load the full entity before mutating fields.
- In `create()` and `saveHistory()`: name the local variable `entityToSave`; return `repository.save(entityToSave).toModel()` directly — do not do `val saved = ...; model.copy(id = saved.id)`.
- In `update()`: extract mutation into a private extension function `private fun EntityClass.updateWith(toUpdate: ModelClass)` on the entity. Within a `@Transactional` method the entity is already managed — call `entity.updateWith(model)` then return `entity.toModel()` directly; no explicit `repository.save()` needed (Hibernate dirty checking flushes on commit).
- When building a `Specification`, start with `var spec = Specification<EntityType> { _, _, cb -> cb.conjunction() }` (always-true base). `Specification.where(null)` does not compile — Spring Data JPA 3.x exposes only non-nullable overloads in its Kotlin API. Add conditions with `spec.and(Specification { root, _, cb -> ... })` using explicit `Specification<T> { }` SAM syntax to avoid ambiguity with the `PredicateSpecification` overload.

**Mapper:**
- File-level extension functions only — no mapper class.
- `fun EntityClass.toModel() = ModelClass(...)` for entity → model.
- `fun ModelToCreate.asNewEntity(resolver: (id) -> RelatedEntity) = Entity(...)` for model → entity when FK resolution is needed.
