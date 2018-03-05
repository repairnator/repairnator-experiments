package org.arquillian.cube.kubernetes.impl.visitor;

import io.fabric8.kubernetes.api.builder.v3_1.TypedVisitor;
import io.fabric8.kubernetes.api.builder.v3_1.VisitableBuilder;
import io.fabric8.kubernetes.api.builder.v3_1.Visitor;
import io.fabric8.kubernetes.api.model.v3_1.HasMetadata;
import io.fabric8.kubernetes.api.model.v3_1.ObjectMetaBuilder;
import io.fabric8.kubernetes.clnt.v3_1.HasMetadataVisitiableBuilder;
import org.arquillian.cube.kubernetes.api.Configuration;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NamespaceVisitor extends TypedVisitor<ObjectMetaBuilder> {

    @Inject
    Instance<Configuration> configuration;


    @Override
    public void visit(ObjectMetaBuilder builder) {
        new ImmutableNamespaceVisitor(configuration.get()).visit(builder);
    }

    public static class ImmutableNamespaceVisitor extends TypedVisitor<ObjectMetaBuilder> {

        private final Configuration configuration;

        public ImmutableNamespaceVisitor(Configuration configuration) {
            this.configuration = configuration;
        }

        @Override
        public void visit(ObjectMetaBuilder builder) {
                builder.withNamespace(configuration.getNamespace());
        }
    }
}
