/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.test.bridge.builtin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TikaMetadataProcessor;
import org.hibernate.search.bridge.TikaParseContextProvider;
import org.hibernate.search.bridge.TikaParserProvider;
import org.hibernate.search.bridge.builtin.TikaBridge;
import org.hibernate.search.engine.impl.LuceneOptionsImpl;
import org.hibernate.search.engine.metadata.impl.BackReference;
import org.hibernate.search.engine.metadata.impl.DocumentFieldMetadata;
import org.hibernate.search.engine.metadata.impl.DocumentFieldPath;
import org.hibernate.search.exception.SearchException;
import org.hibernate.search.test.util.impl.ClasspathResourceAsFile;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Hardy Ferentschik
 */
public class TikaBridgeTest {

	private static final String TEST_DOCUMENT_PDF = "/org/hibernate/search/test/bridge/builtin/test-document-1.pdf";

	@Rule
	public ClasspathResourceAsFile testDocumentPdf = new ClasspathResourceAsFile( getClass(), TEST_DOCUMENT_PDF );

	private final String testFieldName = "content";
	private TikaBridge bridgeUnderTest;
	private Document testDocument;
	private LuceneOptions options;

	@Before
	public void setUp() {
		bridgeUnderTest = new TikaBridge();
		testDocument = new Document();
		DocumentFieldMetadata fieldMetadata =
				new DocumentFieldMetadata.Builder(
						new BackReference<>(),
						new BackReference<>(), null,
						new DocumentFieldPath( "", "" ), // No field path
						Store.YES, Field.Index.ANALYZED, Field.TermVector.NO
				)
						.boost( 0F )
						.build();
		options = new LuceneOptionsImpl( fieldMetadata, 1f, 1f );

		CustomTikaMetadataProcessor.invocationCount = 0;
		CustomTikaParseContextProvider.invocationCount = 0;
	}

	@Test
	public void testPdfToString() throws Exception {
		bridgeUnderTest.setAppliedOnType( URI.class );
		URI pdfUri = testDocumentPdf.get().toURI();
		bridgeUnderTest.set( testFieldName, pdfUri, testDocument, options );
		assertEquals(
				"Wrong extracted text",
				"Hibernate Search pdf test document",
				testDocument.get( testFieldName ).trim()
		);
	}

	@Test
	public void testUnknownTikaMetadataProcessor() throws Exception {
		try {
			bridgeUnderTest.setMetadataProcessorClass( this.getClass() );
			fail();
		}
		catch (SearchException e) {
			assertEquals(
					"Wrong error message",
					"Wrong configuration of Tika parse context provider: class org.hibernate.search.test.bridge.builtin.TikaBridgeTest does not implement interface org.hibernate.search.bridge.TikaMetadataProcessor",
					e.getMessage()
			);
		}
	}

	@Test
	public void testPrepareMetadata() {
		bridgeUnderTest.setMetadataProcessorClass( CustomTikaMetadataProcessor.class );
		bridgeUnderTest.setAppliedOnType( String.class );
		bridgeUnderTest.set( testFieldName, testDocumentPdf.get().getPath(), testDocument, options );
		assertEquals(
				"The set method of the custom metadata processor should have been called",
				1,
				CustomTikaMetadataProcessor.invocationCount
		);
	}

	@Test
	public void testIndexingMetadata() {
		bridgeUnderTest.setMetadataProcessorClass( CustomTikaMetadataProcessor.class );
		bridgeUnderTest.setAppliedOnType( String.class );
		bridgeUnderTest.set( testFieldName, testDocumentPdf.get().getPath(), testDocument, options );

		assertEquals(
				"The content type should have been indexed",
				"application/pdf",
				testDocument.get( "type" )
		);
	}

	@Test
	public void testUnknownTikaParseContextProvider() throws Exception {
		try {
			bridgeUnderTest.setParseContextProviderClass( this.getClass() );
			fail();
		}
		catch (SearchException e) {
			assertEquals(
					"Wrong error message",
					"Wrong configuration of Tika metadata processor: class org.hibernate.search.test.bridge.builtin.TikaBridgeTest does not implement interface org.hibernate.search.bridge.TikaParseContextProvider",
					e.getMessage()
			);
		}
	}

	@Test
	public void testCustomTikaParseContextProvider() throws Exception {
		bridgeUnderTest.setParseContextProviderClass( CustomTikaParseContextProvider.class );
		bridgeUnderTest.setAppliedOnType( String.class );
		bridgeUnderTest.set( testFieldName, testDocumentPdf.get().getPath(), testDocument, options );

		assertEquals(
				"The getParseContext method of the custom parse context provider should have been called",
				1,
				CustomTikaParseContextProvider.invocationCount
		);

	}

	@Test
	public void testInvalidPath() throws Exception {
		try {
			bridgeUnderTest.setAppliedOnType( String.class );
			bridgeUnderTest.set( testFieldName, "/foo", testDocument, options );
		}
		catch (SearchException e) {
			assertTrue( "Wrong error type", e.getMessage().startsWith( "HSEARCH000152" ) );
		}
	}

	@Test
	public void testCustomTikaParserProvider() throws Exception {
		bridgeUnderTest.setParserProviderClass( CustomTikaParserProvider.class );
		bridgeUnderTest.setAppliedOnType( String.class );
		assertEquals(
				"The createParser method of the custom parser provider should have been called",
				1,
				CustomTikaParserProvider.invocationCount
		);

		bridgeUnderTest.set( testFieldName, testDocumentPdf.get().getPath(), testDocument, options );

		assertEquals(
				"The content should have been set to '" + CustomTikaParserProvider.RESULT + "'",
				CustomTikaParserProvider.RESULT,
				testDocument.get( testFieldName )
		);
	}

	public static class CustomTikaParserProvider implements TikaParserProvider {
		public static int invocationCount = 0;
		public static String RESULT = "foo";

		@Override
		public Parser createParser() {
			invocationCount++;
			return new Parser() {
				@Override
				public Set<MediaType> getSupportedTypes(ParseContext context) {
					return Collections.singleton( MediaType.application( "pdf" ) );
				}

				@Override
				public void parse(InputStream stream, ContentHandler handler, Metadata metadata, ParseContext context)
						throws SAXException {
					handler.startDocument();
					handler.characters( RESULT.toCharArray(), 0, RESULT.length() );
					handler.endDocument();
				}
			};
		}
	}

	public static class CustomTikaMetadataProcessor implements TikaMetadataProcessor {
		public static int invocationCount = 0;

		@Override
		public Metadata prepareMetadata() {
			Metadata meta = new Metadata();
			meta.add( Metadata.RESOURCE_NAME_KEY, "foo" );
			return meta;
		}

		@Override
		public void set(String name, Object value, Document document, LuceneOptions luceneOptions, Metadata metadata) {
			invocationCount++;

			assertEquals(
					"Metadata.RESOURCE_NAME_KEY should be set in the metadata",
					"foo",
					metadata.get( Metadata.RESOURCE_NAME_KEY )
			);

			// indexing the discovered content type
			luceneOptions.addFieldToDocument( "type", metadata.get( Metadata.CONTENT_TYPE ), document );
		}
	}

	public static class CustomTikaParseContextProvider implements TikaParseContextProvider {
		public static int invocationCount = 0;

		@Override
		public ParseContext getParseContext(String name, Object value) {
			invocationCount++;
			return new ParseContext();
		}
	}
}
