# module-server

Spring Boot 4 backend (Kotlin, Java 21). Base package: `sk.momosilabs.truckTrack`.

## Layering Rules

```
Controller → UseCase → Persistence (or shared Service)
```

- **Controllers** implement the `module-api` interface and inject use cases only. No persistence calls, no business logic.
- **Use cases** contain business logic. They call persistence or shared services. **Use cases never call other use cases.** If two use cases need the same query, both call persistence independently.
- **Persistence** handles all data access.

Each use case lives in its own package under `.service.<useCaseName>/` with two files:
- `<UseCaseName>UseCase.kt` — interface
- `<UseCaseName>.kt` — `@Service open class` implementing the interface, with `@IsUser`/`@IsAdmin` and `@Transactional` on the override method

## Entity & Persistence Patterns

**Entity class:**
- Regular Kotlin class (NOT a data class). Fields as constructor properties.
- `@Entity(name = "table_name")` with explicit lowercase snake_case name. No `@Table` annotation.
- ID: `val id: Long = 0L` with `@Id @GeneratedValue(strategy = GenerationType.IDENTITY)`. Default `0L` allows construction without a DB round-trip.
- For manually assigned PKs (e.g. config tables), omit `@GeneratedValue`.
- Non-nullable fields: Kotlin non-nullable type + `@field:NotNull` (note the `@field:` target prefix).
- Nullable fields: Kotlin `?` suffix only.
- `val` for immutable fields (id, FK refs); `var` for fields that get updated after creation.
- **Relationships: always `@ManyToOne` only — never `@OneToMany`, never bidirectional.** This is a deliberate rule.
- No shared base entity, no `@MappedSuperclass`, no `@Embeddable`.
- `LocalDateTime` for timestamps; field names suffixed with `Utc` (e.g. `startedAtUtc`).

**Package layout per domain:**
```
<domain>/
  entity/
    <Name>Entity.kt
  persistence/
    <Name>Persistence.kt              ← interface (business-level operations on model/DTO objects)
    <Name>PersistenceProvider.kt      ← @Repository open class implementing the interface
    repository/
      <Name>Repository.kt             ← JpaRepository<Entity, Id> interface
    mapper/
      <Name>EntityMapper.kt           ← extension fns: fun Entity.toModel() = Model(...)
```

**Persistence provider:**
- `@Repository open class` implementing the `<Name>Persistence` interface.
- Constructor-injects repositories (and `EntityManager` if QueryDSL is needed).
- Read methods: `@Transactional(readOnly = true)`. Write methods: `@Transactional`.
- Throws `GlobalNotFoundException("fieldName=value not found")` for missing records.

**Mapper:**
- File-level extension functions only — no mapper class.
- `fun EntityClass.toModel() = ModelClass(...)` for entity → model.
- `fun ModelToCreate.asNewEntity(resolver: (id) -> RelatedEntity) = Entity(...)` for model → entity when FK resolution is needed.
