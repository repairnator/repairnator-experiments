/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.sql.planner.iterative.rule;

import com.facebook.presto.sql.planner.iterative.rule.test.BaseRuleTest;
import org.testng.annotations.Test;

import java.util.Optional;

import static com.facebook.presto.sql.planner.assertions.PlanMatchPattern.join;
import static com.facebook.presto.sql.planner.assertions.PlanMatchPattern.values;
import static com.facebook.presto.sql.planner.plan.JoinNode.Type.INNER;
import static com.facebook.presto.sql.tree.BooleanLiteral.FALSE_LITERAL;
import static java.util.Collections.emptyList;

public class TestCanonicalizeJoinExpressions
        extends BaseRuleTest
{
    @Test
    public void testDoesNotFireForUnfilteredJoin()
    {
        tester().assertThat(new CanonicalizeJoinExpressions())
                .on(p -> p.join(INNER, p.values(), p.values()))
                .doesNotFire();
    }

    @Test
    public void testDoesNotFireForCanonicalExpressions()
    {
        tester().assertThat(new CanonicalizeJoinExpressions())
                .on(p -> p.join(INNER, p.values(), p.values(), FALSE_LITERAL))
                .doesNotFire();
    }

    @Test
    public void testCanonicalizesExpressions()
    {
        tester().assertThat(new CanonicalizeJoinExpressions())
                .on(p -> p.join(
                        INNER,
                        p.values(p.symbol("x")),
                        p.values(),
                        p.expression("x IS NOT NULL")))
                .matches(join(
                        INNER,
                        emptyList(),
                        Optional.of("NOT (x IS NULL)"),
                        values("x"),
                        values()));
    }
}
