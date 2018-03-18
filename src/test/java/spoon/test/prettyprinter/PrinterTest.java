package spoon.test.prettyprinter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static spoon.testing.utils.ModelUtils.canBeBuilt;

import org.junit.Test;

import spoon.Launcher;
import spoon.SpoonException;
import spoon.compiler.SpoonResourceHelper;
import spoon.reflect.code.CtComment;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.DefaultJavaPrettyPrinter;
import spoon.reflect.visitor.ElementPrinterHelper;
import spoon.reflect.visitor.ListPrinter;
import spoon.reflect.visitor.PrettyPrinter;
import spoon.reflect.visitor.PrinterHelper;
import spoon.reflect.visitor.TokenWriter;
import spoon.reflect.visitor.DefaultTokenWriter;
import spoon.test.prettyprinter.testclasses.MissingVariableDeclaration;
import spoon.testing.utils.ModelUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class PrinterTest {

	@Test
	public void testPrettyPrinter() throws Exception {
		Launcher spoon = new Launcher();
		Factory factory = spoon.createFactory();
		spoon.createCompiler(
				factory,
				SpoonResourceHelper
						.resources(
								"./src/test/java/spoon/test/annotation/testclasses/PersistenceProperty.java",
								"./src/test/java/spoon/test/prettyprinter/Validation.java"))
				.build();
		for (CtType<?> t : factory.Type().getAll()) {
			t.toString();
		}
		assertEquals(0, factory.getEnvironment().getWarningCount());
		assertEquals(0, factory.getEnvironment().getErrorCount());

	}

	@Test
	public void testChangeAutoImportModeWorks() throws Exception {
		Launcher spoon = new Launcher();
		spoon.getEnvironment().setAutoImports(false);
		PrettyPrinter printer = spoon.createPrettyPrinter();
		spoon.addInputResource("./src/test/java/spoon/test/prettyprinter/testclasses/AClass.java");
		spoon.buildModel();

		CtType element = spoon.getFactory().Class().getAll().get(0);
		List<CtType<?>> toPrint = new ArrayList<>();
		toPrint.add(element);
		printer.calculate(element.getPosition().getCompilationUnit(), toPrint);
		String result = printer.getResult();

		assertTrue("The result should not contain imports: "+result, !result.contains("import java.util.List;"));

		// recreating an auto-immport  printer
		spoon.getEnvironment().setAutoImports(true);
		printer = spoon.createPrettyPrinter();

		printer.calculate(element.getPosition().getCompilationUnit(), toPrint);
		result = printer.getResult();
		assertTrue("The result should now contain imports: "+result, result.contains("import java.util.List;"));
	}

	@Test
	public void testFQNModeWriteFQNConstructorInCtVisitor() {
		Launcher spoon = new Launcher();
		PrettyPrinter printer = spoon.createPrettyPrinter();
		spoon.getEnvironment().setAutoImports(false);
		spoon.addInputResource("./src/main/java/spoon/support/visitor/replace/ReplacementVisitor.java");
		spoon.buildModel();

		CtType element = spoon.getFactory().Class().getAll().get(0);
		List<CtType<?>> toPrint = new ArrayList<>();
		toPrint.add(element);
		printer.calculate(element.getPosition().getCompilationUnit(), toPrint);
		String result = printer.getResult();

		assertTrue("The result should contain FQN for constructor: "+result, result.contains("new spoon.support.visitor.replace.ReplacementVisitor("));
		assertTrue("The result should not contain reduced constructors: "+result, !result.contains("new ReplacementVisitor("));
	}

	@Test
	public void testAutoimportModeDontImportUselessStatic() {
		Launcher spoon = new Launcher();
		spoon.getEnvironment().setAutoImports(true);
		PrettyPrinter printer = spoon.createPrettyPrinter();
		spoon.addInputResource("./src/test/java/spoon/test/prettyprinter/testclasses/ImportStatic.java");
		spoon.buildModel();

		CtType element = spoon.getFactory().Class().getAll().get(0);
		List<CtType<?>> toPrint = new ArrayList<>();
		toPrint.add(element);
		printer.calculate(element.getPosition().getCompilationUnit(), toPrint);
		String result = printer.getResult();

		assertTrue("The result should not contain import static: ", !result.contains("import static spoon.test.prettyprinter.testclasses.sub.Constants.READY"));
		assertTrue("The result should contain import type: ", result.contains("import spoon.test.prettyprinter.testclasses.sub.Constants"));
		assertTrue("The result should contain import static assertTrue: ", result.contains("import static org.junit.Assert.assertTrue;"));
		assertTrue("The result should contain assertTrue(...): ", result.contains("assertTrue(\"blabla\".equals(\"toto\"));"));
		assertTrue("The result should use System.out.println(Constants.READY): "+result, result.contains("System.out.println(Constants.READY);"));
	}

	@Test
	public void testRuleCanBeBuild() {
		Launcher spoon = new Launcher();
		PrettyPrinter printer = spoon.createPrettyPrinter();
		spoon.getEnvironment().setAutoImports(true);
		String output = "./target/spoon-rule/";
		spoon.addInputResource("./src/test/java/spoon/test/prettyprinter/testclasses/Rule.java");
		spoon.setSourceOutputDirectory(output);
		spoon.run();

		CtType element = spoon.getFactory().Class().getAll().get(0);
		List<CtType<?>> toPrint = new ArrayList<>();
		toPrint.add(element);
		printer.calculate(element.getPosition().getCompilationUnit(), toPrint);
		String result = printer.getResult();

		assertTrue("The result should contain direct this accessor for field: "+result, !result.contains("Rule.Phoneme.this.phonemeText"));
		canBeBuilt(output, 7);
	}

	@Test
	public void testJDTBatchCompilerCanBeBuild() {
		Launcher spoon = new Launcher();
		PrettyPrinter printer = spoon.createPrettyPrinter();
		spoon.getEnvironment().setAutoImports(false);
		String output = "./target/spoon-jdtbatchcompiler/";
		spoon.addInputResource("./src/main/java/spoon/support/compiler/jdt/JDTBatchCompiler.java");
		spoon.setSourceOutputDirectory(output);
		spoon.run();

		CtType element = spoon.getFactory().Class().getAll().get(0);
		List<CtType<?>> toPrint = new ArrayList<>();
		toPrint.add(element);
		printer.calculate(element.getPosition().getCompilationUnit(), toPrint);
		String result = printer.getResult();

		//assertTrue("The result should contain direct this accessor for field: "+result, !result.contains("Rule.Phoneme.this.phonemeText"));
		canBeBuilt(output, 7);
	}

	@Test
	public void testPrintingOfOrphanFieldReference() throws Exception {
		CtType<?> type = ModelUtils.buildClass(MissingVariableDeclaration.class);
		//delete the field, so the model is broken.
		//It may happen during substitution operations and then it is helpful to display descriptive error message
		type.getField("testedField").delete();
		//contract: printer fails with descriptive exception and not with NPE
		assertEquals("/* ERROR: Missing field \"testedField\", please check your model. The code may not compile. */ testedField = 1", type.getMethodsByName("failingMethod").get(0).getBody().getStatement(0).toString());
	}

	private final Set<String> separators = new HashSet<>(Arrays.asList("->","::","..."));
	{
		"(){}[];,.:@=<>?&|".chars().forEach(c->separators.add(new String(Character.toChars(c))));
	}
	private final Set<String> operators = new HashSet<>(Arrays.asList(
			"=",
			">",
			"<",
			"!",
			"~",
			"?",
			":",
			"==",
			"<=",
			">=",
			"!=",
			"&&",
			"||",
			"++",
			"--",
			"+",
			"-",
			"*",
			"/",
			"&",
			"|",
			"^",
			"%",
			"<<",">>",">>>",

			"+=",
			"-=",
			"*=",
			"/=",
			"&=",
			"|=",
			"^=",
			"%=",
			"<<=",
			">>=",
			">>>=",
			"instanceof"
	));

	private final String[] javaKeywordsJoined = new String[] {
			"abstract continue for new switch",
			"assert default goto package synchronized",
			"boolean do if private this",
			"break double implements protected throw",
			"byte else import public throws",
			"case enum instanceof return transient",
			"catch extends int short try",
			"char final interface static void",
			"class finally long strictfp volatile",
			"const float native super while"};

	private final Set<String> javaKeywords = new HashSet<>();
	{
		for (String str : javaKeywordsJoined) {
			StringTokenizer st = new StringTokenizer(str, " ");
			while (st.hasMoreTokens()) {
				javaKeywords.add(st.nextToken());
			}
		}
	}

	@Test
	public void testPrinterTokenListener() throws Exception {
		Launcher spoon = new Launcher();
		Factory factory = spoon.createFactory();
		spoon.createCompiler(
				factory,
				SpoonResourceHelper
				.resources(
						"./src/test/java/spoon/test/annotation/testclasses/",
						"./src/test/java/spoon/test/prettyprinter/"))
//this case needs longer, but checks contract on all spoon java sources
//				.resources("./src/main/java/"))
				.build();
		
		assertTrue(factory.Type().getAll().size() > 0);
		for (CtType<?> t : factory.Type().getAll()) {
			//create DefaultJavaPrettyPrinter with standard DefaultTokenWriter
			DefaultJavaPrettyPrinter pp = new DefaultJavaPrettyPrinter(factory.getEnvironment());
			pp.calculate(t.getPosition().getCompilationUnit(), Collections.singletonList(t));
			//result of printing using standard DefaultTokenWriter
			String standardPrintedResult = pp.getResult();
			
			StringBuilder allTokens = new StringBuilder();
			//print type with custom listener
			//1) register custom TokenWriter which checks the TokenWriter contract
			pp.setPrinterTokenWriter(new TokenWriter() {
				String lastToken;
				PrinterHelper printerHelper = new PrinterHelper(factory.getEnvironment());

				@Override
				public TokenWriter writeSeparator(String separator) {
					checkRepeatingOfTokens("writeSeparator");
					checkTokenWhitespace(separator, false);					
					//one of the separators
					assertTrue("Unexpected separator: "+separator, separators.contains(separator));
					handleTabs();
					allTokens.append(separator);
					return this;
				}
				
				@Override
				public TokenWriter writeOperator(String operator) {
					checkRepeatingOfTokens("writeOperator");
					checkTokenWhitespace(operator, false);					
					assertTrue("Unexpected operator: "+operator, operators.contains(operator));
					handleTabs();
					allTokens.append(operator);
					return this;
				}
				
				@Override
				public TokenWriter writeLiteral(String literal) {
					checkRepeatingOfTokens("writeLiteral");
					assertTrue(literal.length() > 0);
					handleTabs();
					allTokens.append(literal);
					return this;
				}
				
				@Override
				public TokenWriter writeKeyword(String keyword) {
					checkRepeatingOfTokens("writeKeyword");
					checkTokenWhitespace(keyword, false);					
					assertTrue("Unexpected java keyword: "+keyword, javaKeywords.contains(keyword));
					handleTabs();
					allTokens.append(keyword);
					return this;
				}
				
				@Override
				public TokenWriter writeIdentifier(String identifier) {
					checkRepeatingOfTokens("writeIdentifier");
					checkTokenWhitespace(identifier, false);
					for (int i = 0; i < identifier.length(); i++) {
						char c = identifier.charAt(i);
						if(i==0) {
							assertTrue(Character.isJavaIdentifierStart(c));
						} else {
							assertTrue(Character.isJavaIdentifierPart(c));
						}
					}
					assertTrue("Keyword found in Identifier: "+identifier, javaKeywords.contains(identifier) == false);
					handleTabs();
					allTokens.append(identifier);
					return this;
				}
				
				@Override
				public TokenWriter writeComment(CtComment comment) {
					checkRepeatingOfTokens("writeComment");
					DefaultTokenWriter sptw = new DefaultTokenWriter(new PrinterHelper(factory.getEnvironment()));
					PrinterHelper ph = sptw.getPrinterHelper();
					ph.setLineSeparator(getPrinterHelper().getLineSeparator());
					ph.setTabCount(getPrinterHelper().getTabCount());
					sptw.writeComment(comment);
					handleTabs();
					allTokens.append(sptw.getPrinterHelper().toString());
					return this;
				}
				
				@Override
				public TokenWriter writeln() {
					checkRepeatingOfTokens("writeln");
					allTokens.append(getPrinterHelper().getLineSeparator());
					lastTokenWasEOL = true;
					return this;
				}
				
				private boolean lastTokenWasEOL = true;
				private int tabCount = 0;
				
				public TokenWriter handleTabs() {
					if(lastTokenWasEOL) {
						lastTokenWasEOL = false;
						for (int i = 0; i < tabCount; i++) {
							if (factory.getEnvironment().isUsingTabulations()) {
								allTokens.append('\t');
							} else {
								for (int j = 0; j < factory.getEnvironment().getTabulationSize(); j++) {
									allTokens.append(' ');
								}
							}
						}
						
					}
					return this;
				}

				@Override
				public TokenWriter writeCodeSnippet(String token) {
					checkRepeatingOfTokens("writeCodeSnippet");
					assertTrue(token.length() > 0);
					handleTabs();
					allTokens.append(token);
					return this;
				}

				@Override
				public TokenWriter incTab() {
					tabCount++;
					return this;
				}

				@Override
				public TokenWriter decTab() {
					tabCount--;
					return this;
				}

				@Override
				public PrinterHelper getPrinterHelper() {
					return printerHelper;
				}

				@Override
				public void reset() {
					printerHelper.reset();
				}

				@Override
				public TokenWriter writeSpace() {
					checkRepeatingOfTokens("writeWhitespace");
					allTokens.append(' ');
					return this;
				}

				//checks that token types are changing. There must be no two tokens of the same type in queue
				private void checkRepeatingOfTokens(String tokenType) {
					if("writeln".equals(tokenType)
							|| "writeIdentifier".equals(tokenType)
							|| "writeSeparator".equals(tokenType)
							|| "writeWhitespace".equals(tokenType)) {
						// nothing
					} else {
						//check only other tokens then writeln, which is the only one which can repeat
						assertTrue("Two tokens of same type current:" + tokenType + " " + allTokens.toString(), tokenType.equals(this.lastToken)==false);
					}
					this.lastToken = tokenType;
				}
			});
			
			//2) print type using PrettyPrinter with listener
			pp.calculate(t.getPosition().getCompilationUnit(), Collections.singletonList(t));
			String withEmptyListenerResult = pp.getResult();
			//contract: each printed character is handled by listener. PrinterHelper is not called directly
			//and because PrinterTokenListener above does not use PrinterHelper, the result must be empty
			assertEquals(0, withEmptyListenerResult.length());
			
			//contract: result built manually from tokens is same like the one made by DefaultTokenWriter
			assertEquals(standardPrintedResult, allTokens.toString());
		}
	}

	private void checkTokenWhitespace(String stringToken, boolean isWhitespace) {
		//contract: there is no empty token
		assertTrue(stringToken.length() > 0);
		//contract: only whitespace token contains whitespace
		for (int i = 0; i < stringToken.length(); i++) {
			char c = stringToken.charAt(i);
			if (isWhitespace) {
				//a whitespace
				assertTrue(Character.isWhitespace(c)==true);
			} else {
				//not a whitespace
				assertTrue(Character.isWhitespace(c)==false);
			}
		}
	}

	@Test
	public void testListPrinter() {

		Launcher spoon = new Launcher();
		DefaultJavaPrettyPrinter pp = (DefaultJavaPrettyPrinter) spoon.createPrettyPrinter();

		PrinterHelper ph = new PrinterHelper(spoon.getEnvironment());
		TokenWriter tw = new DefaultTokenWriter(ph);
		pp.setPrinterTokenWriter(tw);

		ElementPrinterHelper elementPrinterHelper = pp.getElementPrinterHelper();

		String[] listString = new String[] {"un", "deux", "trois"};

		try (ListPrinter listPrinter = elementPrinterHelper.createListPrinter(true, "start", true, true, "next", true, true, "end")) {
			for (String s : listString) {
				listPrinter.printSeparatorIfAppropriate();
				tw.writeIdentifier(s);
			}
		}

		String expectedResult = " start un next deux next trois end";
		assertEquals(expectedResult, pp.toString());
	}
}
