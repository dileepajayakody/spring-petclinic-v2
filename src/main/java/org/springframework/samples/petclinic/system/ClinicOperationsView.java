/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.system;

import java.time.LocalDate;
import java.util.List;

public record ClinicOperationsView(Totals totals, List<PetTypeSummary> petTypeSummary,
		List<SpecialtyCoverageSummary> specialtyCoverage, List<VisitActivity> recentVisits,
		List<VisitActivity> upcomingVisits) {

	public record Totals(long ownerCount, long petCount, long visitCount, long vetCount) {
	}

	public record PetTypeSummary(String typeName, long petCount) {
	}

	public record SpecialtyCoverageSummary(String specialtyName, long vetCount) {
	}

	public record VisitActivity(LocalDate date, String description, String petName, String ownerName) {
	}

}
