# module-api

API contract layer shared between backend and frontend.

- Defines ALL endpoint contracts: `@PostMapping`, `@Operation`, `@Tag`, produces/consumes, return types
- `module-server` controllers implement these interfaces — they add nothing to routing or docs on their own
- Also used to generate the OpenAPI 3.1.0 spec file (`api-docs.json`), which drives Angular client generation

## api-docs.json

`api-docs.json` is **hand-written** — it is NOT auto-generated from annotations. Whenever you add or change an endpoint or DTO, you must also manually update `api-docs.json` and rebuild so the Angular client regenerates.

_In the future, building of api-docs.json should also happen as part of the build._

