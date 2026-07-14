# core:vehicle

Domain models and repository for the vehicle domain. Fully KMP — all code in `commonMain`.

## Public API

| Symbol | Description |
|--------|-------------|
| `VehicleRepository` | `suspend fun getVehicles(): Result<List<Vehicle>>` |
| `Vehicle` | Domain model: id, licensePlate, make, model, type |
| `VehicleType` | `Truck`, `Trailer` |

## Key Files

```
commonMain/
  VehicleRepository.kt
  VehicleRepositoryImpl.kt
  model/Vehicle.kt
  model/VehicleType.kt
  dto/VehicleDto.kt
  dto/VehicleDtoMapper.kt
  api/VehicleApi.kt          ← Ktor API client
  di/VehicleModule.kt        ← Koin bindings
```

## Depends On

- `:core:common` — `Logger`, `DispatcherProvider`
- `:core:network` — Ktor `HttpClient`
