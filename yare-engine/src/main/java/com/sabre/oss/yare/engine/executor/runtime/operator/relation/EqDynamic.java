/*
 * MIT License
 *
 * Copyright 2018 Sabre GLBL Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.sabre.oss.yare.engine.executor.runtime.operator.relation;

import com.sabre.oss.yare.core.model.Expression;
import com.sabre.oss.yare.engine.executor.runtime.operator.BiArgsChainedPredicate;
import com.sabre.oss.yare.engine.executor.runtime.operator.OperatorFactory;
import com.sabre.oss.yare.engine.executor.runtime.predicate.Predicate;
import com.sabre.oss.yare.engine.executor.runtime.predicate.PredicateFactoryContext;
import com.sabre.oss.yare.engine.executor.runtime.value.ValueProvider;

import java.util.Arrays;

public class EqDynamic extends BiArgsChainedPredicate {

    public EqDynamic(ValueProvider lOperandProvider, ValueProvider rOperandProvider) {
        super(lOperandProvider, rOperandProvider, Arrays.asList(
                new EqObjectArray(lOperandProvider, rOperandProvider),
                new EqZonedDateTime(lOperandProvider, rOperandProvider),
                new EqObject(lOperandProvider, rOperandProvider)));
    }

    public static class Factory extends OperatorFactory {

        private boolean isApplicable(Expression.Operator operator, ValueProvider... valueProviders) {
            return Eq.OPERATOR_NAME.equals(operator.getCall()) &&
                    operator.getArguments().size() == 2;
        }

        @Override
        public Predicate create(PredicateFactoryContext context, Expression.Operator operator, ValueProvider[] valueProviders) {
            if (!isApplicable(operator, valueProviders)) {
                return null;
            }
            return new EqDynamic(valueProviders[0], valueProviders[1]);
        }
    }
}
