# Agent Guide

## Codebase Overview

This repository is the Spring PetClinic sample application. It is a Spring Boot
4.0.3 web application that uses Java 17, Spring MVC, Thymeleaf, Spring Data JPA,
Bean Validation, JCache/Caffeine, and an SQL database. H2 is used by default for
local development and tests, with optional MySQL and PostgreSQL profiles.

The application models a veterinary clinic. Users can search and edit owners,
manage pets, add visits, and view vets. The UI is rendered server-side with
Thymeleaf templates under `src/main/resources/templates`, while static assets
and generated CSS live under `src/main/resources/static`.

## Main Architecture

- `src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java`
  is the Spring Boot entry point.
- `model` contains shared JPA base classes:
  `BaseEntity`, `NamedEntity`, and `Person`.
- `owner` contains the owner-facing domain and web flow:
  `Owner`, `Pet`, `PetType`, `Visit`, MVC controllers, validators, formatters,
  and Spring Data repositories.
- `vet` contains vet domain objects, the vet repository, a wrapper DTO for JSON
  and XML rendering, and the vet controller.
- `system` contains cross-cutting web concerns such as the welcome page,
  crash endpoint, cache setup, and locale switching.
- `src/main/resources/db/{h2,mysql,postgres}` contains schema and seed data.
- `src/main/resources/messages` contains i18n message bundles.
- `src/main/scss` contains SCSS sources. The checked-in CSS is generated from
  these sources with the Maven `css` profile.
- `src/test/java` contains MVC slice tests, JPA tests, full Spring Boot
  integration tests, and database-specific integration tests.

There is intentionally no dedicated service layer in the core application.
Controllers coordinate form binding and validation, then call repositories
directly. Preserve that style unless a change clearly requires a new layer.

## Build And Run

Prefer the Maven wrapper because the README documents Maven as the complete path
for CSS generation. Gradle is also configured and valid for normal build/test
work.

- Run the app with H2:
  `./mvnw spring-boot:run`
- Run tests:
  `./mvnw test`
- Build the jar:
  `./mvnw package`
- Recompile CSS after editing SCSS or Bootstrap-related styling:
  `./mvnw generate-resources -P css`
- Gradle alternatives:
  `./gradlew bootRun`, `./gradlew test`, `./gradlew build`

The application serves on `http://localhost:8080` by default. The H2 console is
available during local development at `/h2-console`; use the JDBC URL printed in
startup logs.

## Database Profiles

Default configuration uses H2:

- `src/main/resources/application.properties`
- `src/main/resources/db/h2/schema.sql`
- `src/main/resources/db/h2/data.sql`

Persistent database profiles:

- MySQL: `spring.profiles.active=mysql`
- PostgreSQL: `spring.profiles.active=postgres`

Local database containers are defined in `docker-compose.yml`:

- `docker compose up mysql`
- `docker compose up postgres`

The profile-specific properties use environment-variable overrides for database
URLs and credentials. Keep SQL initialization scripts idempotent when changing
database setup.

## Testing Guidance

Use the narrowest test that covers the change:

- Controller/view behavior: update or add `@WebMvcTest` tests beside the
  relevant controller, for example under `src/test/java/.../owner`.
- Repository and JPA behavior: update `ClinicServiceTests` or add a focused
  `@DataJpaTest`.
- Whole application behavior: update `PetClinicIntegrationTests`.
- MySQL/PostgreSQL behavior: use the existing database-specific integration
  tests when a change depends on database dialect behavior.

Before handing off code changes, run at least `./mvnw test` unless the task is
documentation-only or the environment lacks the needed dependencies. For
formatting or style failures, run the Spring Java Format tasks instead of
manually reformatting large unrelated areas.

## Coding Conventions

- Follow `.editorconfig`: Java and XML use tabs with width 4, Gradle uses
  two-space indentation, HTML and SQL use two-space indentation.
- Keep the existing Apache license header on Java files.
- Use constructor injection, as the existing controllers and configuration do.
- Use `jakarta.*` APIs for validation and persistence.
- Keep controllers package-private unless there is a reason to expose them.
- Protect form-bound IDs with `WebDataBinder#setDisallowedFields("id", "*.id")`
  on mutable web forms.
- Prefer Spring Data repository method names for simple queries.
- Preserve validation messages through the i18n bundles when adding
  user-facing validation text.
- Keep Thymeleaf view names aligned with the folder layout under
  `src/main/resources/templates`.
- Do not introduce client-side frameworks for ordinary page work; the app is a
  server-rendered Spring MVC and Thymeleaf application.

## Security And Data-Binding Notes

This codebase already guards common mass-assignment paths by disallowing bound
`id` fields in controllers. Continue that pattern for new form controllers.
When adding endpoints that modify owners, pets, visits, vets, or lookup tables,
validate that path IDs and form/object IDs cannot be mismatched.

Actuator endpoints are exposed broadly in `application.properties` for sample
development and test use. Do not treat that setting as production-ready.

## Working With Agents

- Use semantic code search when locating unfamiliar functionality or tracing
  related implementations.
- Inspect the local code before assuming behavior from the upstream PetClinic
  project; this repository may include local changes.
- Keep edits scoped to the requested feature or fix.
- Do not rewrite generated CSS by hand when the SCSS source should be changed.
- Avoid changing both Maven and Gradle build files unless the dependency or
  plugin change must support both build systems.
- Do not remove existing database fixtures unless tests and sample flows are
  updated accordingly.
