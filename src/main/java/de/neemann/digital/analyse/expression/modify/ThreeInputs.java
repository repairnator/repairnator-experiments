package de.neemann.digital.analyse.expression.modify;

import de.neemann.digital.analyse.expression.Expression;
import de.neemann.digital.analyse.expression.Operation;

import java.util.ArrayList;

/**
 * @author hneemann
 */
public class ThreeInputs implements ExpressionModifier {
    @Override
    public Expression modify(Expression expression) {
        if (expression instanceof Operation) {
            Operation op = (Operation) expression;
            if (op.getExpressions().size() > 3) {
                if (expression instanceof Operation.And)
                    return new Three(op.getExpressions(), Operation::andNoMerge).generate();
                else if (expression instanceof Operation.Or)
                    return new Three(op.getExpressions(), Operation::orNoMerge).generate();
                else
                    throw new RuntimeException("operation not supported: " + expression.getClass().getSimpleName());
            } else
                return expression;
        } else
            return expression;
    }

    private interface OpGen {
        Expression op(Expression... ex);
    }

    private static final class Three {
        private final ArrayList<Expression> expressions;
        private final OpGen opGen;

        Three(ArrayList<Expression> expressions, OpGen opGen) {
            this.expressions = expressions;
            this.opGen = opGen;
        }

        Expression generate() {
            return gen(0, expressions.size() - 1);
        }

        private Expression gen(int a, int b) {
            int num = b - a + 1;
            System.out.println(num);
            if (num == 1)
                return expressions.get(a);
            else if (num == 2)
                return opGen.op(expressions.get(a), expressions.get(b));
            else {
                int d = 1;
                while (d * 3 * 2 < num)
                    d *= 3;

                if (d * 3 * 2 == num) {
                    return opGen.op(
                            gen(a, a + d * 3 - 1),
                            gen(a + d * 3, b));
                } else {
                    return opGen.op(
                            gen(a, a + d - 1),
                            gen(a + d, a + d * 2 - 1),
                            gen(a + d * 2, b));
                }
            }
        }
    }

}
