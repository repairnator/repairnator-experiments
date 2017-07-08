package io.swagger.jaxrs2.ext;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.parameters.Parameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class AbstractOpenAPIExtension implements OpenAPIExtension {

    @Override
    public String extractOperationMethod(Operation operation, Method method, Iterator<OpenAPIExtension> chain) {
        if (chain.hasNext()) {
            return chain.next().extractOperationMethod(operation, method, chain);
        } else {
            return null;
        }
    }

    @Override
    public List<Parameter> extractParameters(List<Annotation> annotations, Type type, Set<Type> typesToSkip,
                                             Iterator<OpenAPIExtension> chain) {
        if (chain.hasNext()) {
            return chain.next().extractParameters(annotations, type, typesToSkip, chain);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void decorateOperation(Operation operation, Method method, Iterator<OpenAPIExtension> chain) {
        if (chain.hasNext()) {
            chain.next().decorateOperation(operation, method, chain);
        }
    }

    protected boolean shouldIgnoreClass(Class<?> cls) {
        return false;
    }

    protected boolean shouldIgnoreType(Type type, Set<Type> typesToSkip) {
        if (typesToSkip.contains(type)) {
            return true;
        }
        if (shouldIgnoreClass(constructType(type).getRawClass())) {
            typesToSkip.add(type);
            return true;
        }
        return false;
    }

    protected JavaType constructType(Type type) {
        return TypeFactory.defaultInstance().constructType(type);
    }
}
