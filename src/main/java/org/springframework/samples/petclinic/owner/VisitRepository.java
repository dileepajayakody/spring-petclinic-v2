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

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VisitRepository extends JpaRepository<Visit, Integer> {

	@Query("SELECT COUNT(visit) FROM Visit visit")
	long countVisits();

	@Query("""
			SELECT v.date AS date, v.description AS description, p.name AS petName,
				o.firstName AS ownerFirstName, o.lastName AS ownerLastName
			FROM Owner o
			JOIN o.pets p
			JOIN p.visits v
			ORDER BY v.date DESC, v.id DESC
			""")
	List<VisitActivity> findRecentVisitActivity(Pageable pageable);

	@Query("""
			SELECT v.date AS date, v.description AS description, p.name AS petName,
				o.firstName AS ownerFirstName, o.lastName AS ownerLastName
			FROM Owner o
			JOIN o.pets p
			JOIN p.visits v
			WHERE v.date >= :startDate
			ORDER BY v.date ASC, v.id ASC
			""")
	List<VisitActivity> findUpcomingVisitActivity(LocalDate startDate, Pageable pageable);

	interface VisitActivity {

		LocalDate getDate();

		String getDescription();

		String getPetName();

		String getOwnerFirstName();

		String getOwnerLastName();

	}

}
