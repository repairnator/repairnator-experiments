/**
 * This file is part of General Entity Annotator Benchmark.
 *
 * General Entity Annotator Benchmark is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * General Entity Annotator Benchmark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with General Entity Annotator Benchmark.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.gerbil.utils.filter;

import java.util.ArrayList;
import java.util.List;

import org.aksw.gerbil.transfer.nif.Marking;

public abstract class AbstractMarkingFilter<T extends Marking> implements MarkingFilter<T> {

    @Override
    public List<T> filterList(List<T> markings) {
        List<T> filteredMarkings = new ArrayList<T>(markings.size());
        for (T marking : markings) {
            if (isMarkingGood(marking)) {
                filteredMarkings.add(marking);
            }
        }
        return filteredMarkings;
    }

    @Override
    public List<List<T>> filterListOfLists(List<List<T>> markings) {
        List<List<T>> filteredMarkings = new ArrayList<List<T>>(markings.size());
        for (List<T> list : markings) {
            filteredMarkings.add(filterList(list));
        }
        return filteredMarkings;
    }
}
