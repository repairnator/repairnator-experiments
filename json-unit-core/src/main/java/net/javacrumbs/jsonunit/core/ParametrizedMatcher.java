package net.javacrumbs.jsonunit.core;

/**
 * Implement this interface to use with parametrized matchers. Sample usage
 *
 * <pre>
 *     {@code
 *
 *     Matcher<?> divisionMatcher = new DivisionMatcher();
 *     assertJsonEquals("{\"test\": \"${json-unit.matches:isDivisibleBy}3\"}", "{\"test\":5}", JsonAssert.withMatcher("isDivisibleBy", divisionMatcher));
 *
 *     private static class DivisionMatcher extends BaseMatcher<Object> implements ParametrizedMatcher {
 *       private BigDecimal param;
 *
 *       public boolean matches(Object item) {
 *           return ((BigDecimal)item).remainder(param).compareTo(ZERO) == 0;
 *       }
 *
 *       public void describeTo(Description description) {
 *           description.appendValue(param);
 *       }
 *
 *       @Override
 *       public void describeMismatch(Object item, Description description) {
 *           description.appendText("It is not divisible by ").appendValue(param);
 *       }
 *
 *       public void setParameter(String parameter) {
 *           this.param = new BigDecimal(parameter);
 *       }
 *   }
 *
 *
 *     }
 * </pre>
 */
public interface ParametrizedMatcher {
    void setParameter(String parameter);
}
