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

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.owner.VisitRepository;
import org.springframework.samples.petclinic.vet.VetRepository;

@Controller
class ClinicOperationsController {

	private static final int ACTIVITY_LIMIT = 5;

	private final OwnerRepository ownerRepository;

	private final PetRepository petRepository;

	private final VisitRepository visitRepository;

	private final VetRepository vetRepository;

	ClinicOperationsController(OwnerRepository ownerRepository, PetRepository petRepository,
			VisitRepository visitRepository, VetRepository vetRepository) {
		this.ownerRepository = ownerRepository;
		this.petRepository = petRepository;
		this.visitRepository = visitRepository;
		this.vetRepository = vetRepository;
	}

	@GetMapping("/dashboard")
	String showDashboard(Model model) {
		ClinicOperationsView dashboard = new ClinicOperationsView(
				new ClinicOperationsView.Totals(this.ownerRepository.countOwners(), this.petRepository.countPets(),
						this.visitRepository.countVisits(), this.vetRepository.countVets()),
				this.petRepository.findPetTypeCounts()
					.stream()
					.map(row -> new ClinicOperationsView.PetTypeSummary(row.getTypeName(), row.getPetCount()))
					.toList(),
				this.vetRepository.findSpecialtyCoverage()
					.stream()
					.map(row -> new ClinicOperationsView.SpecialtyCoverageSummary(row.getSpecialtyName(),
							row.getVetCount()))
					.toList(),
				mapVisitActivity(this.visitRepository.findRecentVisitActivity(PageRequest.of(0, ACTIVITY_LIMIT))),
				mapVisitActivity(this.visitRepository.findUpcomingVisitActivity(LocalDate.now(),
						PageRequest.of(0, ACTIVITY_LIMIT))));
		model.addAttribute("dashboard", dashboard);
		return "dashboard/clinicOperations";
	}

	private List<ClinicOperationsView.VisitActivity> mapVisitActivity(List<VisitRepository.VisitActivity> visits) {
		return visits.stream()
			.map(visit -> new ClinicOperationsView.VisitActivity(visit.getDate(), visit.getDescription(),
					visit.getPetName(), visit.getOwnerFirstName() + " " + visit.getOwnerLastName()))
			.toList();
	}

}
