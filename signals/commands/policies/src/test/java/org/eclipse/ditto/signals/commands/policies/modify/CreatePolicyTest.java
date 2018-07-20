/*
 * Copyright (c) 2017 Bosch Software Innovations GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/index.php
 *
 * Contributors:
 *    Bosch Software Innovations GmbH - initial contribution
 */
package org.eclipse.ditto.signals.commands.policies.modify;

import static org.eclipse.ditto.model.base.assertions.DittoBaseAssertions.assertThat;
import static org.mutabilitydetector.unittesting.AllowedReason.provided;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertInstancesOf;
import static org.mutabilitydetector.unittesting.MutabilityMatchers.areImmutable;

import org.eclipse.ditto.json.JsonFactory;
import org.eclipse.ditto.json.JsonObject;
import org.eclipse.ditto.model.base.json.FieldType;
import org.eclipse.ditto.model.policies.PoliciesModelFactory;
import org.eclipse.ditto.model.policies.Policy;
import org.eclipse.ditto.model.policies.PolicyIdInvalidException;
import org.eclipse.ditto.signals.commands.policies.PolicyCommand;
import org.eclipse.ditto.signals.commands.policies.TestConstants;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * Unit test for {@link CreatePolicy}.
 */
public final class CreatePolicyTest {

    private static final JsonObject KNOWN_JSON = JsonFactory.newObjectBuilder()
            .set(PolicyCommand.JsonFields.TYPE, CreatePolicy.TYPE)
            .set(CreatePolicy.JSON_POLICY, TestConstants.Policy.POLICY.toJson(FieldType.regularOrSpecial()))
            .build();


    @Test
    public void assertImmutability() {
        assertInstancesOf(CreatePolicy.class,
                areImmutable(),
                provided(Policy.class, JsonObject.class).isAlsoImmutable());
    }


    @Test
    public void testHashCodeAndEquals() {
        EqualsVerifier.forClass(CreatePolicy.class)
                .withRedefinedSuperclass()
                .verify();
    }


    @Test(expected = NullPointerException.class)
    public void tryToCreateInstanceWithNullPolicy() {
        CreatePolicy.of(null, TestConstants.EMPTY_DITTO_HEADERS);
    }


    @Test(expected = PolicyIdInvalidException.class)
    public void tryToCreateInstanceWithInvalidPolicyId() {
        final Policy policy = PoliciesModelFactory.newPolicyBuilder("test.ns:foo bar")
                .set(TestConstants.Policy.POLICY_ENTRY)
                .build();

        CreatePolicy.of(policy, TestConstants.EMPTY_DITTO_HEADERS);
    }


    @Test
    public void createInstanceWithValidPolicyId() {
        final Policy policy = PoliciesModelFactory.newPolicyBuilder("test.ns:foo-bar")
                .set(TestConstants.Policy.POLICY_ENTRY)
                .build();

        final CreatePolicy createPolicy =
                CreatePolicy.of(policy, TestConstants.EMPTY_DITTO_HEADERS);

        assertThat(createPolicy).isNotNull();
    }


    @Test
    public void toJsonReturnsExpected() {
        final CreatePolicy underTest =
                CreatePolicy.of(TestConstants.Policy.POLICY, TestConstants.EMPTY_DITTO_HEADERS);
        final JsonObject actualJson = underTest.toJson(FieldType.regularOrSpecial());

        assertThat(actualJson).isEqualTo(KNOWN_JSON);
    }


    @Test
    public void createInstanceFromValidJson() {
        final CreatePolicy underTest =
                CreatePolicy.fromJson(KNOWN_JSON.toString(), TestConstants.EMPTY_DITTO_HEADERS);

        assertThat(underTest).isNotNull();
        assertThat(underTest.getPolicy()).isEqualTo(TestConstants.Policy.POLICY);
    }

}
