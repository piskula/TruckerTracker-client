# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This project is BackEnd for issue-tracking management system called TruckTrack.

## Build Commands

```bash
# Full build
./gradlew clean build

# Run locally (after build)
java -jar module-server/build/libs/module-server-*-SNAPSHOT.jar

# Start local PostgreSQL
docker-compose up -d
```

## Module Architecture

**`module-api`** — API contract layer. See `module-api/CLAUDE.md` for details.

**`module-server`** — Spring Boot 4 backend (Kotlin, Java 21). See `module-server/CLAUDE.md` for details.

## Key Configuration

**OAuth2/Keycloak:** Issuer URI is `https://sso.momosi.org/realms/momosi`. The backend is a resource server, it validates JWT tokens.

**Database:** PostgreSQL with Liquibase migrations. JPA is set to `ddl-auto: validate`.

**Local environment variables needed:**
- `POSTGRES_DB_URL` — defaults to `jdbc:postgresql://localhost:5435/truckTrack?stringtype=unspecified`
- `POSTGRES_DB_PASSWORD` — defaults to `truckTrack-password`

## Runtime
Currently, application, when deployed on PROD, uses Docker on Ubuntu VM. In Docker there is keycloak, postgres and NGINX. Application itself is running as a service in Linux with `java -jar` command. There is 1 shared postgres with 2 schemas, 1 for keycloak and 1 for Spring Boot app.
