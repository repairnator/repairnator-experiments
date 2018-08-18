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

package tech.tablesaw.columns.numbers;

import tech.tablesaw.columns.Column;
import tech.tablesaw.filtering.predicates.DoubleBiPredicate;
import tech.tablesaw.filtering.predicates.DoubleRangePredicate;

import java.util.function.DoublePredicate;

/**
 * Support for built-in predicates on double column
 * <p>
 * TODO(lwhite): Ensure each returns false when handling missing values
 */
public interface NumberPredicates extends Column<Double> {

    DoublePredicate isZero = i -> i == 0.0;

    DoublePredicate isNegative = i -> i < 0;

    DoublePredicate isPositive = i -> i > 0;

    DoublePredicate isNonNegative = i -> i >= 0;

    DoubleBiPredicate isGreaterThan = (valueToTest, valueToCompareAgainst) -> valueToTest > valueToCompareAgainst;

    DoubleRangePredicate isBetweenExclusive = new DoubleRangePredicate() {
        @Override
        public boolean test(double valueToTest, double rangeStart, double rangeEnd) {
            return valueToTest > rangeStart && valueToTest < rangeEnd;
        }
    };

    DoubleRangePredicate isBetweenInclusive = new DoubleRangePredicate() {
        @Override
        public boolean test(double valueToTest, double rangeStart, double rangeEnd) {
            return valueToTest >= rangeStart && valueToTest <= rangeEnd;
        }
    };

    DoubleBiPredicate isGreaterThanOrEqualTo = (valueToTest, valueToCompareAgainst) -> valueToTest >=
            valueToCompareAgainst;

    DoubleBiPredicate isLessThan = (valueToTest, valueToCompareAgainst) -> valueToTest < valueToCompareAgainst;

    DoubleBiPredicate isLessThanOrEqualTo = (valueToTest, valueToCompareAgainst) -> valueToTest <=
            valueToCompareAgainst;

    DoubleBiPredicate isEqualTo = (valueToTest, valueToCompareAgainst) -> valueToTest == valueToCompareAgainst;

    DoubleBiPredicate isNotEqualTo = (valueToTest, valueToCompareAgainst) -> valueToTest != valueToCompareAgainst;

    DoublePredicate isMissing = i -> i != i;

    DoublePredicate isNotMissing = i -> i == i;
}
