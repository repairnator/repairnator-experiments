/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.test.analyzer.definition;

import org.hibernate.search.analyzer.impl.LuceneAnalyzerReference;
import org.hibernate.search.engine.impl.NormalizerRegistry;
import org.hibernate.search.engine.integration.impl.ExtendedSearchIntegrator;
import org.hibernate.search.indexes.spi.LuceneEmbeddedIndexManagerType;
import org.hibernate.search.testsupport.junit.SearchFactoryHolder;
import org.hibernate.search.testsupport.junit.SkipOnElasticsearch;
import org.hibernate.search.util.AnalyzerUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;

import static org.hibernate.search.test.analyzer.common.AnalyzerTest.assertTokensEqual;

/**
 * Tests the analyzer creation framework.
 * This test might be affected by the version of the Analyzers being used.
 *
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
@Category(SkipOnElasticsearch.class) // Analyzers cannot be retrieved directly when using Elasticsearch
public class AnalyzerBuilderTest {

	@Rule
	public final SearchFactoryHolder sfHolder = new SearchFactoryHolder( Team.class )
			.withProperty( "hibernate.search.lucene_version", org.apache.lucene.util.Version.LATEST.toString() );

	/**
	 * Tests the analyzers defined on {@link Team}.
	 *
	 * @throws Exception in case the test fails.
	 */
	@Test
	public void testAnalyzers() throws Exception {
		ExtendedSearchIntegrator searchFactory = sfHolder.getSearchFactory();
		Analyzer analyzer = searchFactory.getAnalyzer( "standard_analyzer" );
		String text = "This is just FOOBAR's";
		Token[] tokens = AnalyzerUtils.tokensFromAnalysis( analyzer, "name", text );
		assertTokensEqual( tokens, new String[] { "This", "is", "just", "FOOBAR's" } );

		analyzer = searchFactory.getAnalyzer( "html_standard_analyzer" );
		text = "This is <b>foo</b><i>bar's</i>";
		tokens = AnalyzerUtils.tokensFromAnalysis( analyzer, "name", text );
		assertTokensEqual( tokens, new String[] { "This", "is", "foobar's" } );

		analyzer = searchFactory.getAnalyzer( "html_whitespace_analyzer" );
		text = "This is <b>foo</b><i>bar's</i>";
		tokens = AnalyzerUtils.tokensFromAnalysis( analyzer, "name", text );
		assertTokensEqual( tokens, new String[] { "This", "is", "foobar's" } );

		analyzer = searchFactory.getAnalyzer( "trim_analyzer" );
		text = " Kittens!   ";
		tokens = AnalyzerUtils.tokensFromAnalysis( analyzer, "name", text );
		assertTokensEqual( tokens, new String[] { "Kittens!" } );

		analyzer = searchFactory.getAnalyzer( "length_analyzer" );
		text = "ab abc abcd abcde abcdef";
		tokens = AnalyzerUtils.tokensFromAnalysis( analyzer, "name", text );
		assertTokensEqual( tokens, new String[] { "abc", "abcd", "abcde" } );

		analyzer = searchFactory.getAnalyzer( "length_analyzer" );
		text = "ab abc abcd abcde abcdef";
		tokens = AnalyzerUtils.tokensFromAnalysis( analyzer, "name", text );
		assertTokensEqual( tokens, new String[] { "abc", "abcd", "abcde" } );

		analyzer = searchFactory.getAnalyzer( "porter_analyzer" );
		text = "bikes bikes biking";
		tokens = AnalyzerUtils.tokensFromAnalysis( analyzer, "name", text );
		assertTokensEqual( tokens, new String[] { "bike", "bike", "bike" } );

		analyzer = searchFactory.getAnalyzer( "word_analyzer" );
		text = "CamelCase";
		tokens = AnalyzerUtils.tokensFromAnalysis( analyzer, "name", text );
		assertTokensEqual( tokens, new String[] { "Camel", "Case" } );

		analyzer = searchFactory.getAnalyzer( "synonym_analyzer" );
		text = "ipod universe cosmos";
		tokens = AnalyzerUtils.tokensFromAnalysis( analyzer, "name", text );
		assertTokensEqual( tokens, new String[] { "ipod", "universe", "universe" } );

		analyzer = searchFactory.getAnalyzer( "shingle_analyzer" );
		text = "please divide this sentence into shingles";
		tokens = AnalyzerUtils.tokensFromAnalysis( analyzer, "name", text );
		assertTokensEqual(
				tokens,
				new String[] {
						"please",
						"please divide",
						"divide",
						"divide this",
						"this",
						"this sentence",
						"sentence",
						"sentence into",
						"into",
						"into shingles",
						"shingles"
				}
		);

		analyzer = searchFactory.getAnalyzer( "phonetic_analyzer" );
		text = "The quick brown fox jumped over the lazy dogs";
		tokens = AnalyzerUtils.tokensFromAnalysis( analyzer, "name", text );
		AnalyzerUtils.displayTokens( analyzer, "name", text );
		assertTokensEqual(
				tokens, new String[] { "0", "KK", "BRN", "FKS", "JMPT", "OFR", "0", "LS", "TKS" }
		);

		analyzer = searchFactory.getAnalyzer( "pattern_analyzer" );
		text = "foo,bar";
		tokens = AnalyzerUtils.tokensFromAnalysis( analyzer, "name", text );
		assertTokensEqual( tokens, new String[] { "foo", "bar" } );

		// CharStreamFactories test
		analyzer = searchFactory.getAnalyzer( "mapping_char_analyzer" );
		text = "CORA\u00C7\u00C3O DE MEL\u00C3O";
		tokens = AnalyzerUtils.tokensFromAnalysis( analyzer, "name", text );
		assertTokensEqual( tokens, new String[] { "CORACAO", "DE", "MELAO" } );

		NormalizerRegistry normalizerRegistry =
				searchFactory.getIntegration( LuceneEmbeddedIndexManagerType.INSTANCE )
				.getNormalizerRegistry();

		analyzer = normalizerRegistry.getNamedNormalizerReference( "custom_normalizer" )
				.unwrap( LuceneAnalyzerReference.class ).getAnalyzer();
		text = "This is a D\u00E0scription";
		tokens = AnalyzerUtils.tokensFromAnalysis( analyzer, "name", text );
		assertTokensEqual( tokens, new String[] { "this is a dascription" } );
	}

}
