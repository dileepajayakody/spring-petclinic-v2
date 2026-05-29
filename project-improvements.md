# Project Improvements

A curated list of refactoring, feature, and test-coverage tasks for the Spring PetClinic application. Each task is scoped to be completable by a coding agent in a short session, touches 1–4 files, and has a clear, verifiable outcome — ideal for a live demo.

---

## 🔧 Refactoring Tasks

### 1. Extract hardcoded page size to a configurable constant
**Files:** `OwnerController.java`, `VetController.java`, `application.properties`

Both controllers hardcode `int pageSize = 5`. Extract this to a `@Value`-injected property so it can be configured per environment.

**Why it's a good demo:** Shows Spring `@Value` injection, touches two controllers and one properties file, and is immediately testable.

---

### 2. Replace `IllegalArgumentException` with a proper `@ControllerAdvice` error handler
**Files:** `OwnerController.java`, new `ExceptionHandlerController.java`

Currently, `findOwner()` and `showOwner()` throw raw `IllegalArgumentException` when an owner is not found. This results in a 500 error page. A proper `@ControllerAdvice` with `@ExceptionHandler` would return a user-friendly 404 page.

**Why it's a good demo:** Introduces a new class, demonstrates Spring MVC exception handling, and improves UX.

---

### 3. Relax the telephone number validation regex
**Files:** `Owner.java`, `messages/messages.properties`

The current regex `\d{10}` only accepts exactly 10 digits — no spaces, dashes, or international formats. Relax it to something like `[0-9 +\-]{7,15}` and update the validation message.

**Why it's a good demo:** Single-file domain change with a message bundle update, easy to verify with a form submission.

---

### 4. Remove the unused `Vets` wrapper object from `showVetList`
**Files:** `VetController.java`

In `showVetList()`, a `Vets` object is created and populated but never added to the model — only `listVets` is used. The `Vets` instantiation is dead code in that method.

**Why it's a good demo:** Clean, safe dead-code removal with a clear before/after.

---

## ✨ Small Feature Tasks

### 5. Add an email field to `Owner`
**Files:** `Owner.java`, `db/h2/schema.sql`, `db/mysql/schema.sql`, `db/postgres/schema.sql`, `owners/createOrUpdateOwnerForm.html`, `owners/ownerDetails.html`

Add an optional email field with `@Email` validation. This is a realistic, end-to-end feature that touches domain, persistence, and UI layers.

**Why it's a good demo:** Full-stack feature addition — entity, schema, form, and view — all in one short task.

---

### 6. Add a "pet count" badge to the owner list page
**Files:** `owners/ownersList.html`

The owner list table currently shows name, address, city, telephone, and pets (names). Add a numeric badge showing the count of pets per owner using Thymeleaf expressions.

**Why it's a good demo:** Pure Thymeleaf/UI change, zero Java changes, immediately visible result.

---

### 7. Add visit count to the owner details page
**Files:** `owners/ownerDetails.html`

Show the total number of visits across all pets for an owner. Computable in Thymeleaf using `th:with` and iteration.

**Why it's a good demo:** Thymeleaf expression logic, no backend changes needed.

---

### 8. Add a "search by city" filter to the owner search
**Files:** `OwnerRepository.java`, `OwnerController.java`, `owners/findOwners.html`

Extend the find-owners form with an optional city filter. Add a derived query method `findByLastNameStartingWithAndCityContaining(...)` to the repository.

**Why it's a good demo:** Shows Spring Data derived query method generation, touches repository + controller + view.

---

### 9. Add pagination to the vet list page (it already exists but isn't linked in the UI)
**Files:** `vets/vetList.html`

The `VetController` already supports pagination via `?page=N`, but the vet list HTML template may not render pagination controls. Add next/previous page navigation matching the owner list style.

**Why it's a good demo:** UI-only change that leverages already-existing backend pagination support.

---

### 10. Add a "last visit date" column to the pet table on owner details
**Files:** `owners/ownerDetails.html`

For each pet, show the date of the most recent visit (or "No visits yet") using Thymeleaf. The data is already loaded via `FETCH = EAGER`.

**Why it's a good demo:** Pure template change, no Java needed, immediately visible and useful.

---

## 🧪 Test Coverage Tasks

### 11. Add a test for the owner-not-found 404 path
**Files:** `owner/OwnerControllerTests.java`

Currently there is no test verifying what happens when `GET /owners/9999` is called with a non-existent ID. Add a `@WebMvcTest` case that asserts a 4xx/error response.

**Why it's a good demo:** Shows `@WebMvcTest` slice testing, `MockMvc` assertions, and error path coverage.

---

### 12. Add a `@DataJpaTest` for `OwnerRepository.findByLastNameStartingWith`
**Files:** `service/ClinicServiceTests.java` or new `OwnerRepositoryTests.java`

Verify pagination behavior: that page 1 returns the right slice, that an empty last name returns all owners, and that a non-matching name returns an empty page.

**Why it's a good demo:** Shows `@DataJpaTest`, `Pageable`, and Spring Data repository testing patterns.

---

## 📋 Summary Table

| # | Task | Category | Files Touched | Difficulty |
|---|------|----------|---------------|------------|
| 1 | Configurable page size | Refactor | 3 | Easy |
| 2 | `@ControllerAdvice` for 404 | Refactor | 2 | Medium |
| 3 | Relax telephone regex | Refactor | 2 | Easy |
| 4 | Remove dead `Vets` code in controller | Refactor | 1 | Easy |
| 5 | Add email field to `Owner` | Feature | 6 | Medium |
| 6 | Pet count badge on owner list | Feature | 1 | Easy |
| 7 | Visit count on owner details | Feature | 1 | Easy |
| 8 | Search by city filter | Feature | 3 | Medium |
| 9 | Vet list pagination UI | Feature | 1 | Easy |
| 10 | Last visit date on owner details | Feature | 1 | Easy |
| 11 | Test for owner-not-found path | Tests | 1 | Easy |
| 12 | `@DataJpaTest` for pagination | Tests | 1 | Medium |

---

## Best picks for a live demo

For maximum audience impact in a short session, the top picks are:

- **Task 5 (Add email field)** — full-stack, visually obvious, realistic
- **Task 2 (`@ControllerAdvice`)** — introduces a new class, clear before/after behavior
- **Task 1 (Configurable page size)** — clean refactor, shows Spring config injection
- **Task 6 (Pet count badge)** — instant visual result, zero risk
