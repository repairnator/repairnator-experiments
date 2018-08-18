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

package tech.tablesaw.plotly;

import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.traces.BarTrace;

import static tech.tablesaw.aggregate.AggregateFunctions.sum;

/**
 *
 */
public class ParetoExample {

    public static void main(String[] args) throws Exception {
        Table table = Table.read().csv("../data/tornadoes_1950-2014.csv");
        table = table.where(table.numberColumn("Fatalities").isGreaterThan(3));
        Table t2 = table.summarize("fatalities", sum).by("State");

        t2 = t2.sortDescendingOn(t2.column(1).name());
        Layout layout = Layout.builder().title("Tornado Fatalities by State").build();
        BarTrace trace = BarTrace.builder(
                t2.categoricalColumn(0),
                t2.numberColumn(1))
                .build();
        Plot.show(new Figure(layout, trace));
    }
}