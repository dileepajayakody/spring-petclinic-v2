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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.owner.VisitRepository;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ClinicOperationsController.class)
@DisabledInNativeImage
@DisabledInAotMode
class ClinicOperationsControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OwnerRepository ownerRepository;

	@MockitoBean
	private PetRepository petRepository;

	@MockitoBean
	private VisitRepository visitRepository;

	@MockitoBean
	private VetRepository vetRepository;

	@BeforeEach
	void setup() {
		given(this.ownerRepository.countOwners()).willReturn(10L);
		given(this.petRepository.countPets()).willReturn(13L);
		given(this.visitRepository.countVisits()).willReturn(4L);
		given(this.vetRepository.countVets()).willReturn(6L);
		given(this.petRepository.findPetTypeCounts())
			.willReturn(List.of(petTypeCount("cat", 4L), petTypeCount("dog", 4L)));
		given(this.vetRepository.findSpecialtyCoverage())
			.willReturn(List.of(specialtyCoverage("radiology", 2L), specialtyCoverage("surgery", 2L)));
		given(this.visitRepository.findRecentVisitActivity(any(Pageable.class)))
			.willReturn(List.of(visitActivity(LocalDate.of(2013, 1, 4), "spayed", "Samantha", "Jean", "Coleman")));
		given(this.visitRepository.findUpcomingVisitActivity(any(LocalDate.class), any(Pageable.class)))
			.willReturn(List.of());
	}

	@Test
	void showDashboard() throws Exception {
		ClinicOperationsView expectedDashboard = new ClinicOperationsView(
				new ClinicOperationsView.Totals(10L, 13L, 4L, 6L),
				List.of(new ClinicOperationsView.PetTypeSummary("cat", 4L),
						new ClinicOperationsView.PetTypeSummary("dog", 4L)),
				List.of(new ClinicOperationsView.SpecialtyCoverageSummary("radiology", 2L),
						new ClinicOperationsView.SpecialtyCoverageSummary("surgery", 2L)),
				List.of(new ClinicOperationsView.VisitActivity(LocalDate.of(2013, 1, 4), "spayed", "Samantha",
						"Jean Coleman")),
				List.of());

		this.mockMvc.perform(get("/dashboard"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("dashboard"))
			.andExpect(model().attribute("dashboard", expectedDashboard))
			.andExpect(view().name("dashboard/clinicOperations"))
			.andExpect(content().string(containsString("Clinic Operations Dashboard")))
			.andExpect(content().string(containsString("spayed")))
			.andExpect(content().string(containsString("No upcoming visits scheduled.")));
	}

	@Test
	void showDashboardWithEmptyActivityLists() throws Exception {
		given(this.visitRepository.findRecentVisitActivity(any(Pageable.class))).willReturn(List.of());
		given(this.visitRepository.findUpcomingVisitActivity(any(LocalDate.class), any(Pageable.class)))
			.willReturn(List.of());

		ClinicOperationsView expectedDashboard = new ClinicOperationsView(
				new ClinicOperationsView.Totals(10L, 13L, 4L, 6L),
				List.of(new ClinicOperationsView.PetTypeSummary("cat", 4L),
						new ClinicOperationsView.PetTypeSummary("dog", 4L)),
				List.of(new ClinicOperationsView.SpecialtyCoverageSummary("radiology", 2L),
						new ClinicOperationsView.SpecialtyCoverageSummary("surgery", 2L)),
				List.of(), List.of());

		this.mockMvc.perform(get("/dashboard"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("dashboard", expectedDashboard))
			.andExpect(content().string(containsString("No recent visits recorded.")))
			.andExpect(content().string(containsString("No upcoming visits scheduled.")));
	}

	private PetRepository.PetTypeCount petTypeCount(String typeName, long petCount) {
		return new PetRepository.PetTypeCount() {
			@Override
			public String getTypeName() {
				return typeName;
			}

			@Override
			public long getPetCount() {
				return petCount;
			}
		};
	}

	private VetRepository.SpecialtyCoverage specialtyCoverage(String specialtyName, long vetCount) {
		return new VetRepository.SpecialtyCoverage() {
			@Override
			public String getSpecialtyName() {
				return specialtyName;
			}

			@Override
			public long getVetCount() {
				return vetCount;
			}
		};
	}

	private VisitRepository.VisitActivity visitActivity(LocalDate date, String description, String petName,
			String ownerFirstName, String ownerLastName) {
		return new VisitRepository.VisitActivity() {
			@Override
			public LocalDate getDate() {
				return date;
			}

			@Override
			public String getDescription() {
				return description;
			}

			@Override
			public String getPetName() {
				return petName;
			}

			@Override
			public String getOwnerFirstName() {
				return ownerFirstName;
			}

			@Override
			public String getOwnerLastName() {
				return ownerLastName;
			}
		};
	}

}
