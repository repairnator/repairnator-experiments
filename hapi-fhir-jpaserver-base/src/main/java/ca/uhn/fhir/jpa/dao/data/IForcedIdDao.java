package ca.uhn.fhir.jpa.dao.data;

import java.util.List;

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
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.uhn.fhir.jpa.entity.ForcedId;

public interface IForcedIdDao extends JpaRepository<ForcedId, Long> {
	
	@Query("SELECT f FROM ForcedId f WHERE myForcedId = :forced_id")
	public List<ForcedId> findByForcedId(@Param("forced_id") String theForcedId);

	@Query("SELECT f FROM ForcedId f WHERE myResourceType = :resource_type AND myForcedId = :forced_id")
	public List<ForcedId> findByTypeAndForcedId(@Param("resource_type") String theResourceType, @Param("forced_id") String theForcedId);

	@Query("SELECT f FROM ForcedId f WHERE f.myResourcePid = :resource_pid")
	public ForcedId findByResourcePid(@Param("resource_pid") Long theResourcePid);
	
}
