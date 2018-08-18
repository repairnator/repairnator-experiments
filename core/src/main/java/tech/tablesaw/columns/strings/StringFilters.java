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

package tech.tablesaw.columns.strings;

import tech.tablesaw.api.StringColumn;
import tech.tablesaw.columns.Column;
import tech.tablesaw.selection.BitmapBackedSelection;
import tech.tablesaw.selection.Selection;

import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static tech.tablesaw.columns.strings.StringPredicates.*;

public interface StringFilters extends Column<String> {

    default Selection eval(BiPredicate<String, String> predicate, StringColumn otherColumn) {
        Selection selection = new BitmapBackedSelection();
        for (int idx = 0; idx < size(); idx++) {
            if (predicate.test(get(idx), otherColumn.get(idx))) {
                selection.add(idx);
            }
        }
        return selection;
    }

    default Selection eval(BiPredicate<String, String> predicate, String value) {
        Selection selection = new BitmapBackedSelection();
        for (int idx = 0; idx < size(); idx++) {
            if (predicate.test(get(idx), value)) {
                selection.add(idx);
            }
        }
        return selection;
    }

    default Selection eval(BiPredicate<String, Integer> predicate, Integer value) {
        Selection selection = new BitmapBackedSelection();
        for (int idx = 0; idx < size(); idx++) {
            if (predicate.test(get(idx), value)) {
                selection.add(idx);
            }
        }
        return selection;
    }

    default Selection eval(Predicate<String> predicate) {
        Selection selection = new BitmapBackedSelection();
        for (int idx = 0; idx < size(); idx++) {
            if (predicate.test(get(idx))) {
                selection.add(idx);
            }
        }
        return selection;
    }

    default Selection equalsIgnoreCase(String string) {
        return eval(isEqualToIgnoringCase, string);
    }

    default Selection isEmptyString() {
        return eval(isEmpty);
    }

    default Selection startsWith(String string) {
        return eval(startsWith, string);
    }

    default Selection endsWith(String string) {
        return eval(endsWith, string);
    }

    default Selection containsString(String string) {
        return eval(stringContains, string);
    }

    default Selection matchesRegex(String string) {
        return eval(matchesRegex, string);
    }

    default Selection isAlpha() {
        return eval(isAlpha);
    }

    default Selection isNumeric() {
        return eval(isNumeric);
    }

    default Selection isAlphaNumeric() {
        return eval(isAlphaNumeric);
    }

    default Selection isUpperCase() {
        return eval(isUpperCase);
    }

    default Selection isLowerCase() {
        return eval(isLowerCase);
    }

    default Selection lengthEquals(int stringLength) {
        return eval(hasEqualLengthTo, stringLength);
    }

    default Selection isShorterThan(int stringLength) {
        return eval(isShorterThan, stringLength);
    }

    default Selection isLongerThan(int stringLength) {
        return eval(isLongerThan, stringLength);
    }

    Selection isIn(String... strings);

    default Selection isIn(Collection<String> strings) {
        return isIn(strings.toArray(new String[0]));
    }

    Selection isNotIn(String... strings);

    default Selection isNotIn(Collection<String> strings) {
        return isNotIn(strings.toArray(new String[0]));
    }

    // Column Methods
    default Selection isEqualTo(StringColumn other) {
        return eval(isEqualTo, other);
    }

    default Selection isNotEqualTo(StringColumn other) {
        return eval(isNotEqualTo, other);
    }

    default Selection equalsIgnoreCase(StringColumn other) {
        return eval(isEqualToIgnoringCase, other);
    }

    default Selection startsWith(StringColumn other) {
        return eval(startsWith, other);
    }

    @Override
    default Selection isMissing() {
        return eval(isMissing);
    }

    @Override
    default Selection isNotMissing() {
        return eval(isNotMissing);
    }

    Selection isEqualTo(String string);

    String get(int index);
}