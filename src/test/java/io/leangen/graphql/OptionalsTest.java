package io.leangen.graphql;

import org.junit.Test;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;

import static io.leangen.graphql.support.QueryResultAssertions.assertValueAtPathEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OptionalsTest {

    private static final GraphQLSchema schema = new TestSchemaGenerator()
            .withOperationsFromSingleton(new PrimitiveService()).generate();
    private static final GraphQL exe = GraphQLRuntime.newGraphQL(schema).build();

    @Test
    public void testMapping() {
        GraphQLFieldDefinition intQuery = schema.getQueryType().getFieldDefinition("int");
        GraphQLFieldDefinition longQuery = schema.getQueryType().getFieldDefinition("long");
        GraphQLFieldDefinition doubleQuery = schema.getQueryType().getFieldDefinition("double");
        GraphQLFieldDefinition stringQuery = schema.getQueryType().getFieldDefinition("string");
        GraphQLFieldDefinition nestedQuery = schema.getQueryType().getFieldDefinition("nested");
        assertEquals(Scalars.GraphQLInt, intQuery.getType());
        assertEquals(Scalars.GraphQLInt, intQuery.getArgument("opt").getType());
        assertEquals(Scalars.GraphQLLong, longQuery.getType());
        assertEquals(Scalars.GraphQLLong, longQuery.getArgument("opt").getType());
        assertEquals(Scalars.GraphQLFloat, doubleQuery.getType());
        assertEquals(Scalars.GraphQLFloat, doubleQuery.getArgument("opt").getType());
        assertEquals(Scalars.GraphQLString, stringQuery.getType());
        assertEquals(Scalars.GraphQLString, stringQuery.getArgument("opt").getType());
        assertEquals(Scalars.GraphQLString, nestedQuery.getType());
        assertEquals(Scalars.GraphQLString, nestedQuery.getArgument("opt").getType());
    }

    @Test
    public void testValues() {
        ExecutionResult result;
        result = exe.execute("{int(opt: 77)}");
        assertTrue(result.getErrors().isEmpty());
        assertValueAtPathEquals(77, result, "int");
        result = exe.execute("{long(opt: 77)}");
        assertTrue(result.getErrors().isEmpty());
        assertValueAtPathEquals(77L, result, "long");
        result = exe.execute("{double(opt: 77.77)}");
        assertTrue(result.getErrors().isEmpty());
        assertValueAtPathEquals(77.77, result, "double");
        result = exe.execute("{string(opt: \"xyz\")}");
        assertTrue(result.getErrors().isEmpty());
        assertValueAtPathEquals("xyz", result, "string");
        result = exe.execute("{nested(opt: \"xyz\")}");
        assertTrue(result.getErrors().isEmpty());
        assertValueAtPathEquals("xyz", result, "nested");
    }

    @Test
    public void testNullValues() {
        ExecutionResult result;
        result = exe.execute("{int}");
        assertTrue(result.getErrors().isEmpty());
        assertValueAtPathEquals(null, result, "int");
        result = exe.execute("{long}");
        assertTrue(result.getErrors().isEmpty());
        assertValueAtPathEquals(null, result, "long");
        result = exe.execute("{double}");
        assertTrue(result.getErrors().isEmpty());
        assertValueAtPathEquals(null, result, "double");
        result = exe.execute("{string}");
        assertTrue(result.getErrors().isEmpty());
        assertValueAtPathEquals(null, result, "string");
        result = exe.execute("{nested}");
        assertTrue(result.getErrors().isEmpty());
        assertValueAtPathEquals(null, result, "nested");
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static class PrimitiveService {

        @GraphQLQuery(name = "int")
        public OptionalInt getInt(@GraphQLArgument(name = "opt") OptionalInt optInt) {
            return optInt;
        }

        @GraphQLQuery(name = "long")
        public OptionalLong getLong(@GraphQLArgument(name = "opt") OptionalLong optLong) {
            return optLong;
        }

        @GraphQLQuery(name = "double")
        public OptionalDouble getDouble(@GraphQLArgument(name = "opt") OptionalDouble optDouble) {
            return optDouble;
        }

        @GraphQLQuery(name = "string")
        public Optional<String> getString(@GraphQLArgument(name = "opt") Optional<String> optString) {
            return optString != null ? optString : Optional.empty();
        }

        @GraphQLQuery(name = "nested")
        public Optional<Optional<String>> getNestedString(@GraphQLArgument(name = "opt") Optional<Optional<String>> optString) {
            return optString != null ? optString : Optional.of(Optional.empty());
        }
    }
}
