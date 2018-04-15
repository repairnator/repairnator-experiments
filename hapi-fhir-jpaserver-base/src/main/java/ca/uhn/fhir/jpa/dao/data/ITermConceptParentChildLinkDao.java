package ca.uhn.fhir.jpa.dao.data;

import java.util.Collection;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;

public interface ITermConceptParentChildLinkDao extends JpaRepository<TermConceptParentChildLink, Long> {

	@Query("DELETE FROM TermConceptParentChildLink t WHERE t.myCodeSystem.myId = :cs_pid")
	@Modifying
	void deleteByCodeSystemVersion(@Param("cs_pid") Long thePid);

	@Query("SELECT t.myParentPid FROM TermConceptParentChildLink t WHERE t.myChildPid = :child_pid")
	Collection<Long> findAllWithChild(@Param("child_pid") Long theConceptPid);
	
}
