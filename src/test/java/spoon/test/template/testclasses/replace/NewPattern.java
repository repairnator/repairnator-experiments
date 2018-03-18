package spoon.test.template.testclasses.replace;

import java.util.function.Consumer;

import spoon.pattern.Pattern;
import spoon.pattern.PatternBuilder;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.meta.ContainerKind;
import spoon.reflect.meta.RoleHandler;
import spoon.reflect.meta.impl.RoleHandlerHelper;

public class NewPattern {

	/**
	 * The body of this method contains a model of transformed code 
	 */
	private void patternModel(OldPattern.Parameters params) throws Exception {
		elementPrinterHelper.printList(params.getIterable.S(), 
				params.startPrefixSpace, 
				params.start, 
				params.startSuffixSpace, 
				params.nextPrefixSpace, 
				params.next, 
				params.nextSuffixSpace, 
				params.endPrefixSpace, 
				params.end,
				v -> {
					params.statements.S();
				});
	}
	
	/**
	 * Creates a Pattern for this model
	 */
	public static Pattern createPattern(Factory factory) {
		return PatternBuilder
			.create(factory, NewPattern.class, model->model.setBodyOfMethod("patternModel"))
			.configureAutomaticParameters()
			.configureParameters(pb -> {
				pb.parameter("statements").setContainerKind(ContainerKind.LIST);
			})
			.build();
	}
	
	/**
	 * Looks for all matches of Pattern defined by `OldPattern_ParamsInNestedType.class`
	 * and replaces each match with code generated by Pattern defined by `NewPattern.class`
	 * @param rootElement the root element of AST whose children has to be transformed
	 */
	@SuppressWarnings("unchecked")
	public static void replaceOldByNew(CtElement rootElement) {
		CtType<?> targetType = (rootElement instanceof CtType) ? (CtType) rootElement : rootElement.getParent(CtType.class);
		Factory f = rootElement.getFactory();
		Pattern newPattern = NewPattern.createPattern(f);
		Pattern oldPattern = OldPattern.createPattern(f);
		oldPattern.forEachMatch(rootElement, (match) -> {
			RoleHandler rh = RoleHandlerHelper.getRoleHandlerWrtParent(match.getMatchingElement(CtElement.class, false));
			match.replaceMatchesBy(newPattern.applyToType(targetType, (Class) rh.getValueClass(), match.getParametersMap()));
		});
	}
	
	/*
	 * Helper type members
	 */
	
	private ElementPrinterHelper elementPrinterHelper;
	
	interface Entity {
		Iterable<Item> $getItems$();
	}
	
	interface ElementPrinterHelper {
		void printList(Iterable<Item> $getItems$, 
				boolean startPrefixSpace, String start, boolean startSufficSpace, 
				boolean nextPrefixSpace, String next, boolean nextSuffixSpace, 
				boolean endPrefixSpace, String end, 
				Consumer<Item> consumer);
	}
}
