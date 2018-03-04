package io.leangen.graphql.execution.complexity;

import java.util.Collections;
import java.util.Map;

import graphql.language.Field;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLOutputType;
import io.leangen.graphql.generator.types.MappedGraphQLFieldDefinition;
import io.leangen.graphql.metadata.Operation;
import io.leangen.graphql.metadata.Resolver;
import io.leangen.graphql.util.GraphQLUtils;

public class ResolvedField {

    private final String name;
    private final Field field;
    private final GraphQLFieldDefinition fieldDefinition;
    private final GraphQLOutputType fieldType;
    private final Map<String, Object> arguments;
    private final Resolver resolver;

    private Map<String, ResolvedField> children;
    private int complexityScore;

    public ResolvedField(Field field, GraphQLFieldDefinition fieldDefinition, Map<String, Object> arguments) {
        this(field, fieldDefinition, arguments, Collections.emptyMap());
    }
    
    public ResolvedField(Field field, GraphQLFieldDefinition fieldDefinition, Map<String, Object> arguments, Map<String, ResolvedField> children) {
        this.name = field.getAlias() != null ? field.getAlias() : field.getName();
        this.field = field;
        this.fieldDefinition = fieldDefinition;
        this.fieldType = (GraphQLOutputType) GraphQLUtils.unwrap(fieldDefinition.getType());
        this.arguments = arguments;
        this.children = children;
        this.resolver = findResolver(fieldDefinition, arguments);
    }
    
    private Resolver findResolver(GraphQLFieldDefinition fieldDefinition, Map<String, Object> arguments) {
        if (fieldDefinition instanceof MappedGraphQLFieldDefinition) {
            Operation operation = ((MappedGraphQLFieldDefinition) fieldDefinition).getOperation();
            if (operation.getResolvers().size() == 1) {
                return operation.getResolvers().iterator().next();
            } else {
                String[] nonNullArgumentNames = arguments.entrySet().stream()
                        .filter(arg -> arg.getValue() != null)
                        .map(Map.Entry::getKey)
                        .toArray(String[]::new);

                return operation.getResolver(nonNullArgumentNames);
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public Field getField() {
        return field;
    }

    public GraphQLFieldDefinition getFieldDefinition() {
        return fieldDefinition;
    }

    public GraphQLOutputType getFieldType() {
        return fieldType;
    }
    
    public Map<String, Object> getArguments() {
        return arguments;
    }

    public Map<String, ResolvedField> getChildren() {
        return children;
    }

    public int getComplexityScore() {
        return complexityScore;
    }

    public void setComplexityScore(int complexityScore) {
        this.complexityScore = complexityScore;
    }

    public Resolver getResolver() {
        return resolver;
    }

    @Override
    public String toString() {
        return name;
    }
}
