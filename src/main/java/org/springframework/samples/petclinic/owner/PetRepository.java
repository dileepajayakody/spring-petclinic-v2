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
package org.springframework.samples.petclinic.owner;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PetRepository extends JpaRepository<Pet, Integer> {

	@Query("SELECT COUNT(pet) FROM Pet pet")
	long countPets();

	@Query("""
			SELECT pet.type.name AS typeName, COUNT(pet) AS petCount
			FROM Pet pet
			GROUP BY pet.type.name
			ORDER BY COUNT(pet) DESC, pet.type.name ASC
			""")
	List<PetTypeCount> findPetTypeCounts();

	interface PetTypeCount {

		String getTypeName();

		long getPetCount();

	}

}
