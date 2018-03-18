package spoon.test.template.testclasses.match;

import spoon.pattern.ParametersBuilder;
import spoon.pattern.Pattern;
import spoon.pattern.PatternBuilder;
import spoon.reflect.factory.Factory;
import spoon.reflect.meta.ContainerKind;
import spoon.template.TemplateParameter;

import static java.lang.System.out;

import java.util.List;
import java.util.function.Consumer;

public class MatchMultiple2 {

	public static Pattern createPattern(Factory factory, Consumer<ParametersBuilder> cfgParams) {
		return PatternBuilder.create(factory, MatchMultiple2.class, tmb -> tmb.setBodyOfMethod("matcher1"))
			.configureTemplateParameters()
			.configureParameters(pb -> {
				pb.parameter("statements1").setContainerKind(ContainerKind.LIST);
				pb.parameter("statements2").setContainerKind(ContainerKind.LIST);
				pb.parameter("printedValue").byVariable("something");
				cfgParams.accept(pb);
			})
			.configureLiveStatements(ls -> {
				ls.byVariableName("something");
			})
			.build();
	}
	
	public void matcher1(List<String> something) {
		statements1.S();
		statements2.S();
		for (String v : something) {
			System.out.println(v);
		}
	}
	
	TemplateParameter<Void> statements1;
	TemplateParameter<Void> statements2;
	
	public void testMatch1() {
		int i = 0;
		i++;
		System.out.println(i);
		out.println("Xxxx");
		System.out.println((String) null);
		System.out.println("last one");
	}

}
