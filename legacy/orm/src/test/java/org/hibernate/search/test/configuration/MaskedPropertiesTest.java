/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.test.configuration;

import java.io.IOException;
import java.util.Properties;

import org.hibernate.search.testsupport.serialization.SerializationTestHelper;
import org.hibernate.search.util.configuration.impl.MaskedProperty;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Sanne Grinovero
 */
public class MaskedPropertiesTest {

	@Test
	public void testConfigurationParsingPrecedence() {
		Properties cfg = new Properties();
		cfg.put( "hibernate.search.Animals.transaction.indexwriter.max_merge_docs", "1" );
		cfg.put( "hibernate.search.Animals.2.transaction.indexwriter.max_merge_docs", "2" );
		cfg.put( "hibernate.search.Animals.2.transaction.max_merge_docs", "3" );
		cfg.put( "hibernate.search.Animals.transaction.max_merge_docs", "5" );
		cfg.put( "hibernate.search.default.transaction.max_merge_docs", "6" );
		cfg.put( "hibernate.search.default.transaction.indexwriter.max_field_length", "7" );
		cfg.put( "hibernate.notsearch.tests.default", "7" );

		//this is more a "concept demo" than a test:
		Properties root = new MaskedProperty( cfg, "hibernate.search" );
		//only keys starting as "hibernate.search.default" are exposed:
		Properties common = new MaskedProperty( root, "default" );
		//now as "hibernate.search.Animals" or "hibernate.search.default" if first fails:
		Properties dirProvider = new MaskedProperty( root, "Animals", common );
		//this narrows visibility to "hibernate.search.<providername|default>.transaction":
		Properties transaction = new MaskedProperty( dirProvider, "transaction" );
		Properties shard2 = new MaskedProperty( dirProvider, "2", dirProvider );
		Properties transactionInShard2 = new MaskedProperty( shard2, "transaction", transaction );
		Properties newStyleTransaction = new MaskedProperty( transaction, "indexwriter", transaction );
		Properties newStyleTransactionInShard2 = new MaskedProperty(
				transactionInShard2, "indexwriter", transactionInShard2
		);

		assertEquals( "7", newStyleTransaction.getProperty( "max_field_length" ) );
		assertEquals( "7", newStyleTransactionInShard2.getProperty( "max_field_length" ) );
		assertEquals( "5", transaction.getProperty( "max_merge_docs" ) );
	}

	@Test
	public void testSerializability() throws IOException, ClassNotFoundException {
		Properties properties = new Properties();
		properties.setProperty( "base.key", "value" );
		MaskedProperty originalProps = new MaskedProperty( properties, "base" );
		MaskedProperty theCopy = SerializationTestHelper.duplicateBySerialization( originalProps );
		//this is also testing the logger (transient) has been restored:
		assertEquals( "value", theCopy.getProperty( "key" ) );
	}

	@Test
	public void testListingKeys() {
		Properties defaultProp = new Properties();
		defaultProp.put( "some.inherited.prop", "to test standard Properties fallback behaviour" );
		Properties rootProp = new Properties( defaultProp );
		rootProp.put( "some.long.dotted.prop1", "hello!" );
		rootProp.put( "hidden.long.dotted.prop2", "hello again" );
		Properties fallbackProp = new Properties();
		fallbackProp.put( "default.long.dotted.prop3", "hello!" );
		Properties masked = new MaskedProperty( rootProp, "some", fallbackProp );

		assertTrue( masked.keySet().contains( "long.dotted.prop1" ) );
		assertTrue( masked.keySet().contains( "default.long.dotted.prop3" ) );
		assertTrue( masked.keySet().contains( "inherited.prop" ) );
		assertFalse( masked.keySet().contains( "hidden.long.dotted.prop2" ) );
		assertFalse( masked.keySet().contains( "long.dotted.prop2" ) );

		Properties maskedAgain = new MaskedProperty(
				masked,
				"long.dotted",
				masked
		); //falling back to same instance for **
		assertTrue( maskedAgain.keySet().contains( "prop1" ) );
		assertTrue( maskedAgain.keySet().contains( "long.dotted.prop1" ) ); //**: prop 1 should be visible in both ways
		assertTrue( maskedAgain.keySet().contains( "default.long.dotted.prop3" ) );

		Properties maskingAll = new MaskedProperty( masked, "secured" );
		assertTrue( maskingAll.keySet().isEmpty() );
		assertTrue( maskingAll.isEmpty() );
	}

}
