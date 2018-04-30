/*
 * Copyright 2016-2018, EnMasse authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.enmasse.osb;

import io.enmasse.address.model.AddressSpacePlan;
import io.enmasse.address.model.Schema;
import io.enmasse.api.common.SchemaProvider;
import io.enmasse.k8s.api.TestSchemaApi;

public class TestSchemaProvider implements SchemaProvider {
    public TestSchemaApi api = new TestSchemaApi();

    @Override
    public Schema getSchema() {
        return api.getSchema();
    }

    @Override
    public void copyIntoNamespace(AddressSpacePlan plan, String namespace) {
        api.copyIntoNamespace(plan, namespace);
    }
}
