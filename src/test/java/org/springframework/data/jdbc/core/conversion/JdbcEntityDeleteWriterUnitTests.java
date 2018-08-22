/*
 * Copyright 2017-2018 the original author or authors.
 *
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
 */
package org.springframework.data.jdbc.core.conversion;

import lombok.Data;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.conversion.AggregateChange.Kind;
import org.springframework.data.jdbc.core.conversion.DbAction.Delete;
import org.springframework.data.jdbc.core.conversion.DbAction.DeleteAll;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;

/**
 * Unit tests for the {@link JdbcEntityDeleteWriter}
 *
 * @author Jens Schauder
 */
@RunWith(MockitoJUnitRunner.class)
public class JdbcEntityDeleteWriterUnitTests {

	JdbcEntityDeleteWriter converter = new JdbcEntityDeleteWriter(new JdbcMappingContext());

	private static Object dotPath(DbAction dba) {

		JdbcPropertyPath propertyPath = dba.getPropertyPath();
		return propertyPath == null ? null : propertyPath.toDotPath();
	}

	@Test // DATAJDBC-112
	public void deleteDeletesTheEntityAndReferencedEntities() {

		SomeEntity entity = new SomeEntity(23L);

		AggregateChange<SomeEntity> aggregateChange = new AggregateChange<>(Kind.DELETE, SomeEntity.class, entity);

		converter.write(entity.id, aggregateChange);

		Assertions.assertThat(aggregateChange.getActions())
				.extracting(DbAction::getClass, DbAction::getEntityType, JdbcEntityDeleteWriterUnitTests::dotPath) //
				.containsExactly( //
						Tuple.tuple(Delete.class, YetAnother.class, "other.yetAnother"), //
						Tuple.tuple(Delete.class, OtherEntity.class, "other"), //
						Tuple.tuple(Delete.class, SomeEntity.class, null) //
		);
	}

	@Test // DATAJDBC-188
	public void deleteAllDeletesAllEntitiesAndReferencedEntities() {

		SomeEntity entity = new SomeEntity(23L);

		AggregateChange<SomeEntity> aggregateChange = new AggregateChange(Kind.DELETE, SomeEntity.class, null);

		converter.write(null, aggregateChange);

		Assertions.assertThat(aggregateChange.getActions())
				.extracting(DbAction::getClass, DbAction::getEntityType, JdbcEntityDeleteWriterUnitTests::dotPath) //
				.containsExactly( //
						Tuple.tuple(DeleteAll.class, YetAnother.class, "other.yetAnother"), //
						Tuple.tuple(DeleteAll.class, OtherEntity.class, "other"), //
						Tuple.tuple(DeleteAll.class, SomeEntity.class, null) //
		);
	}

	@Data
	private static class SomeEntity {

		@Id final Long id;
		OtherEntity other;
		// should not trigger own Dbaction
		String name;
	}

	@Data
	private class OtherEntity {

		@Id final Long id;
		YetAnother yetAnother;
	}

	@Data
	private class YetAnother {
		@Id final Long id;
	}
}
