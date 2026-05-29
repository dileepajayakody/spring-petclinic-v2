## 1. Goal
Build a first-pass clinic operations dashboard that gives staff a single read-only page for clinic KPIs and recent/upcoming activity, accessible from the existing global navigation.

## 2. Approach
The current application is server-rendered and keeps business flow in MVC controllers that call repositories directly, as shown in [OwnerController.java](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/main/java/org/springframework/samples/petclinic/owner/OwnerController.java?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A1450%2C%22second%22%3A5574%7D%2C%22lines%22%3A%7B%22first%22%3A48%2C%22second%22%3A158%7D%7D&root=%252F) and [VetController.java](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/main/java/org/springframework/samples/petclinic/vet/VetController.java?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A1048%2C%22second%22%3A2691%7D%2C%22lines%22%3A%7B%22first%22%3A35%2C%22second%22%3A76%7D%7D&root=%252F). The dashboard should follow that pattern: a dedicated controller builds a view model from repository queries and renders a Thymeleaf template via the shared layout in [layout.html](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/main/resources/templates/fragments/layout.html?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A617%2C%22second%22%3A2343%7D%2C%22lines%22%3A%7B%22first%22%3A20%2C%22second%22%3A84%7D%7D&root=%252F).

Because the existing domain exposes visits only through eager `Owner -> Pet -> Visit` traversal in [Owner.java](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/main/java/org/springframework/samples/petclinic/owner/Owner.java?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A1462%2C%22second%22%3A5209%7D%2C%22lines%22%3A%7B%22first%22%3A49%2C%22second%22%3A176%7D%7D&root=%252F) and [Pet.java](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/main/java/org/springframework/samples/petclinic/owner/Pet.java?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A1263%2C%22second%22%3A2337%7D%2C%22lines%22%3A%7B%22first%22%3A44%2C%22second%22%3A85%7D%7D&root=%252F), the plan adds focused repository queries for metrics and recent activity rather than computing all dashboard data by loading full owner graphs.

Assumption for this plan: the first version is a hybrid dashboard with KPI cards plus recent/upcoming activity, exposed as a new navbar item rather than replacing the welcome page.

## 3. File Changes
- **Create** `src/main/java/org/springframework/samples/petclinic/system/ClinicOperationsController.java`
  Responsibility: expose `GET /dashboard`, collect dashboard view data, and return a new Thymeleaf view using the same package-private controller style as [WelcomeController.java](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/main/java/org/springframework/samples/petclinic/system/WelcomeController.java?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A545%2C%22second%22%3A699%7D%2C%22lines%22%3A%7B%22first%22%3A22%2C%22second%22%3A29%7D%7D&root=%252F).
- **Create** `src/main/java/org/springframework/samples/petclinic/system/ClinicOperationsView.java`
  Responsibility: immutable dashboard view model or DTO that groups KPIs, per-type summaries, recent visits, upcoming visits, and vet/specialty summary rows so the Thymeleaf template stays simple.
- **Create** `src/main/java/org/springframework/samples/petclinic/owner/VisitRepository.java`
  Responsibility: focused visit queries against the `visits` table for recent visits, upcoming visits, and total visit counts; avoids forcing the dashboard to traverse every owner and pet from [VisitController.java](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/main/java/org/springframework/samples/petclinic/owner/VisitController.java?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A1365%2C%22second%22%3A3469%7D%2C%22lines%22%3A%7B%22first%22%3A41%2C%22second%22%3A102%7D%7D&root=%252F).
- **Modify** `src/main/java/org/springframework/samples/petclinic/owner/OwnerRepository.java:36-60`
  Add owner-level aggregate queries needed for totals or owner city breakdowns; keep simple counts in the repository layer rather than adding a service layer.
- **Modify** `src/main/java/org/springframework/samples/petclinic/vet/VetRepository.java:38-56`
  Add query methods or projections for active vet counts and specialty distribution, extending the repository that already provides paginated and cached vet access.
- **Create** `src/main/resources/templates/dashboard/clinicOperations.html`
  Responsibility: render KPI cards, summary tables, and activity lists via the shared layout, similar to [vetList.html](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/main/resources/templates/vets/vetList.html?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A132%2C%22second%22%3A2424%7D%2C%22lines%22%3A%7B%22first%22%3A7%2C%22second%22%3A54%7D%7D&root=%252F) and [ownerDetails.html](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/main/resources/templates/owners/ownerDetails.html?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A132%2C%22second%22%3A3008%7D%2C%22lines%22%3A%7B%22first%22%3A7%2C%22second%22%3A91%7D%7D&root=%252F).
- **Modify** `src/main/resources/templates/fragments/layout.html:39-62`
  Add a dedicated Dashboard nav item alongside Home, Find Owners, and Veterinarians.
- **Modify** `src/main/resources/messages/messages.properties:15-52`
  Add labels for dashboard title, KPI headings, empty states, and section names.
- **Modify** localized message bundles under `src/main/resources/messages/messages_*.properties`
  Add at least fallback keys matching the new dashboard labels so i18n resolution remains consistent with the existing message setup.
- **Modify** `src/main/scss/petclinic.scss:18-215`
  Add dashboard-specific layout styles for KPI cards, summary grids, and activity tables, following the existing SCSS-first workflow instead of editing generated CSS directly.
- **Modify** `src/test/java/org/springframework/samples/petclinic/system/CrashControllerTests.java` only if package coverage strategy changes
  Likely no functional change required, but keep system-package test layout in mind for new controller tests.
- **Create** `src/test/java/org/springframework/samples/petclinic/system/ClinicOperationsControllerTests.java`
  Responsibility: `@WebMvcTest` coverage for `GET /dashboard`, model attributes, view name, and empty/non-empty states, using the same pattern as [OwnerControllerTests.java](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/test/java/org/springframework/samples/petclinic/owner/OwnerControllerTests.java?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A1910%2C%22second%22%3A9461%7D%2C%22lines%22%3A%7B%22first%22%3A58%2C%22second%22%3A251%7D%7D&root=%252F) and [VetControllerTests.java](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/test/java/org/springframework/samples/petclinic/vet/VetControllerTests.java?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A1313%2C%22second%22%3A3318%7D%2C%22lines%22%3A%7B%22first%22%3A43%2C%22second%22%3A100%7D%7D&root=%252F).
- **Modify** `src/test/java/org/springframework/samples/petclinic/PetClinicIntegrationTests.java:46-67`
  Add an integration test that verifies `/dashboard` returns `200 OK` in the full application context.

## 4. Implementation Steps
### Task 1: Define the dashboard contract
1. Create `src/main/java/org/springframework/samples/petclinic/system/ClinicOperationsView.java` with nested row types or companion DTOs for:
   - KPI totals: owner count, pet count, visit count, vet count.
   - Pet mix summary: pet type name plus count.
   - Visit activity: visit date, description, pet name, owner name.
   - Vet summary: vet name plus specialty count or specialty labels.
2. Keep this model presentation-focused so Thymeleaf in `src/main/resources/templates/dashboard/clinicOperations.html` can iterate over flat collections rather than traversing entity graphs directly.

### Task 2: Add aggregate query support
1. Extend `src/main/java/org/springframework/samples/petclinic/owner/OwnerRepository.java:36-60` with count and lightweight summary queries that cannot be derived efficiently from the existing `findByLastNameStartingWith(...)` and `findById(...)` methods.
2. Create `src/main/java/org/springframework/samples/petclinic/owner/VisitRepository.java` with explicit read-only queries for:
   - Total number of visits.
   - Most recent N visits.
   - Upcoming visits on or after `LocalDate.now()` if future-dated visits are present.
3. Extend `src/main/java/org/springframework/samples/petclinic/vet/VetRepository.java:38-56` with methods that support dashboard summaries without forcing controller logic to page through all vets.
4. If pet-type counts are awkward to compute from existing owner traversal, add one focused pet aggregate query rather than loading every owner with eager pets from [Owner.java](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/main/java/org/springframework/samples/petclinic/owner/Owner.java?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A1918%2C%22second%22%3A2451%7D%2C%22lines%22%3A%7B%22first%22%3A64%2C%22second%22%3A67%7D%7D&root=%252F).

### Task 3: Implement the dashboard controller
1. Create `src/main/java/org/springframework/samples/petclinic/system/ClinicOperationsController.java` as a package-private `@Controller`, matching [WelcomeController.java](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/main/java/org/springframework/samples/petclinic/system/WelcomeController.java?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A545%2C%22second%22%3A699%7D%2C%22lines%22%3A%7B%22first%22%3A22%2C%22second%22%3A29%7D%7D&root=%252F) and [CrashController.java](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/main/java/org/springframework/samples/petclinic/system/CrashController.java?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A713%2C%22second%22%3A952%7D%2C%22lines%22%3A%7B%22first%22%3A28%2C%22second%22%3A35%7D%7D&root=%252F).
2. Implement `GET /dashboard` to populate a `ClinicOperationsView` using repository queries only; do not introduce owner/pet mutation paths or form binding.
3. Set model attributes that are directly consumable by Thymeleaf, including explicit empty collections so the template has deterministic behavior when there are no future visits or no specialty rows.

### Task 4: Build the Thymeleaf dashboard page
1. Create `src/main/resources/templates/dashboard/clinicOperations.html` using `th:replace="~{fragments/layout :: layout (~{::body},'dashboard')}"`, following [welcome.html](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/main/resources/templates/welcome.html?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A75%2C%22second%22%3A355%7D%2C%22lines%22%3A%7B%22first%22%3A3%2C%22second%22%3A14%7D%7D&root=%252F) and [vetList.html](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/main/resources/templates/vets/vetList.html?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A74%2C%22second%22%3A116%7D%2C%22lines%22%3A%7B%22first%22%3A3%2C%22second%22%3A5%7D%7D&root=%252F).
2. Render four KPI cards at the top using scalar model attributes.
3. Render one compact summary table for pet-type mix and one for vet specialty coverage.
4. Render one recent-activity table and one upcoming-activity table, with empty-state messaging when no rows exist.
5. Keep the page read-only: no forms, no `WebDataBinder`, and no object binding concerns.

### Task 5: Wire navigation and labels
1. Modify `src/main/resources/templates/fragments/layout.html:39-62` to add a `menuItem` entry for `/dashboard` with a suitable icon and `menu` key such as `dashboard`.
2. Add dashboard keys to `src/main/resources/messages/messages.properties:15-52` for menu text, section headings, KPI labels, and empty-state copy.
3. Mirror those keys into the localized bundles in `src/main/resources/messages/messages_de.properties`, `messages_en.properties`, `messages_es.properties`, `messages_fa.properties`, `messages_ko.properties`, `messages_pt.properties`, `messages_ru.properties`, and `messages_tr.properties` so navigation does not break in other locales.

### Task 6: Style the page through SCSS
1. Extend `src/main/scss/petclinic.scss:18-215` with dashboard-specific classes for a responsive KPI grid, compact summary tables, and activity sections.
2. Preserve the existing color palette variables in `src/main/scss/petclinic.scss:18-48`; do not hand-edit generated CSS in `src/main/resources/static/resources/css/petclinic.css`.
3. Rebuild CSS through the documented Maven profile after implementation.

### Task 7: Add focused tests
1. Create `src/test/java/org/springframework/samples/petclinic/system/ClinicOperationsControllerTests.java` with `@WebMvcTest`, mocking the new repositories and asserting:
   - `/dashboard` returns `200 OK`.
   - The model contains dashboard totals and summary collections.
   - The view name is `dashboard/clinicOperations`.
   - Empty-state rendering works when recent/upcoming lists are empty.
2. Update `src/test/java/org/springframework/samples/petclinic/PetClinicIntegrationTests.java:52-63` with a `/dashboard` smoke test similar to the existing `/owners/1` and `/owners?lastName=` integration checks.

## 5. Acceptance Criteria
- `GET /dashboard` returns HTTP `200` and renders a Thymeleaf view named `dashboard/clinicOperations`.
- The global navbar in `src/main/resources/templates/fragments/layout.html` contains a visible Dashboard link that routes to `/dashboard`.
- The dashboard shows numeric totals for owners, pets, visits, and vets based on repository-backed data, not hardcoded values.
- The dashboard shows at least one aggregate breakdown section, such as pet counts by type, derived from persisted data.
- The dashboard shows at least one activity section for recent visits, ordered newest-first.
- If there are visits dated on or after the current date, the dashboard shows them in an upcoming-activity section ordered earliest-first.
- If there are no upcoming visits, the page renders an explicit empty-state message instead of failing or showing a blank broken table.
- The dashboard remains read-only: no POST endpoint, no form submission, and no mutation of owners, pets, visits, or vets.
- All new user-facing labels resolve through message bundles; the page must not rely on hardcoded English-only section headers in the template.
- `ClinicOperationsControllerTests` passes and verifies both populated and empty dashboard states.
- `PetClinicIntegrationTests` passes with a smoke test proving `/dashboard` loads in the full Spring Boot application context.

## 6. Verification Steps
1. Run `./mvnw test` to validate MVC tests and the full application integration suite documented for this repository.
2. Run `./mvnw generate-resources -P css` if SCSS was changed, then verify the generated CSS is in sync with `src/main/scss/petclinic.scss`.
3. Start the app with `./mvnw spring-boot:run` and manually verify:
   - `/dashboard` loads successfully.
   - The Dashboard navbar item is visible and active on the dashboard page.
   - KPI totals match seed expectations from [data.sql](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/main/resources/db/h2/data.sql?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A0%2C%22second%22%3A2317%7D%2C%22lines%22%3A%7B%22first%22%3A0%2C%22second%22%3A53%7D%7D&root=%252F): 10 owners, 13 pets, 4 visits, 6 vets for the default H2 dataset.
   - Recent visits display the seeded visit descriptions and dates from `data.sql:50-53` in newest-first order.
4. Manually test an empty-upcoming scenario using the default seed data, because all seeded visit dates are historical (`2013-01-01` through `2013-01-04` in `data.sql:50-53`), so the upcoming section should render its empty state.
5. Optionally switch locale if the app exposes locale selection and confirm the Dashboard menu label resolves rather than showing missing message codes.

## 7. Risks & Mitigations
- **Risk:** Computing dashboard metrics by traversing eager `Owner -> Pet -> Visit` graphs will be inefficient and may duplicate business logic already embedded in entities like [Owner.java](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/main/java/org/springframework/samples/petclinic/owner/Owner.java?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A1918%2C%22second%22%3A2451%7D%2C%22lines%22%3A%7B%22first%22%3A64%2C%22second%22%3A67%7D%7D&root=%252F) and [Pet.java](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/main/java/org/springframework/samples/petclinic/owner/Pet.java?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A1553%2C%22second%22%3A1828%7D%2C%22lines%22%3A%7B%22first%22%3A56%2C%22second%22%3A59%7D%7D&root=%252F).
  **Mitigation:** Use focused aggregate queries in new or extended repositories for counts and activity slices.
- **Risk:** Visit data currently has no standalone repository, which can make recent/upcoming activity awkward to query.
  **Mitigation:** Introduce a dedicated read-only `VisitRepository` tied to the existing `visits` entity instead of overloading `OwnerRepository`.
- **Risk:** Adding dashboard labels only to `messages.properties` may leave localized bundles unresolved.
  **Mitigation:** Add matching keys to every `messages_*.properties` file modified by the app today.
- **Risk:** Styling the dashboard directly in generated CSS would diverge from the documented SCSS workflow in the repository guide.
  **Mitigation:** Limit styling changes to `src/main/scss/petclinic.scss` and regenerate CSS through Maven.
- **Risk:** Replacing the welcome page at `/` would change current app behavior and expand scope beyond a new feature page, given [WelcomeController.java](air-file://p3i8oqofm9ei5u3rm9hj/Users/dileepa.jayakody/Documents/PetClinic_2/spring-petclinic-v2/src/main/java/org/springframework/samples/petclinic/system/WelcomeController.java?type=file&linesData=%7B%22range%22%3A%7B%22first%22%3A594%2C%22second%22%3A695%7D%2C%22lines%22%3A%7B%22first%22%3A25%2C%22second%22%3A27%7D%7D&root=%252F) currently owns `/`.
  **Mitigation:** Keep the first version at `/dashboard` with a new navbar item; revisit making it the landing page only after the feature proves useful.