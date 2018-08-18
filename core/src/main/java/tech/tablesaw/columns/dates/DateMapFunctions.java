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

package tech.tablesaw.columns.dates;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.TimeColumn;
import tech.tablesaw.columns.Column;
import tech.tablesaw.columns.datetimes.PackedLocalDateTime;
import tech.tablesaw.columns.numbers.DoubleColumnType;
import tech.tablesaw.columns.numbers.NumberColumnFormatter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;

import static tech.tablesaw.api.DateColumn.*;

/**
 * An interface for mapping operations unique to Date columns
 */
public interface DateMapFunctions extends Column<LocalDate> {

    static String dateColumnName(Column<LocalDate> column1, int value, TemporalUnit unit) {
        return column1.name() + ": " + value + " " + unit.toString() + "(s)";
    }

    default DoubleColumn daysUntil(DateColumn column2) {
        return timeUntil(column2, ChronoUnit.DAYS);
    }

    default DoubleColumn weeksUntil(DateColumn column2) {
        return timeUntil(column2, ChronoUnit.WEEKS);
    }

    default DoubleColumn monthsUntil(DateColumn column2) {
        return timeUntil(column2, ChronoUnit.MONTHS);
    }

    default DoubleColumn yearsUntil(DateColumn column2) {
        return timeUntil(column2, ChronoUnit.YEARS);
    }

    default DoubleColumn dayOfMonth() {
        DoubleColumn newColumn = DoubleColumn.create(this.name() + " day of month");
        for (int r = 0; r < this.size(); r++) {
            int c1 = this.getIntInternal(r);
            if (DateColumn.valueIsMissing(c1)) {
                newColumn.append(DoubleColumnType.missingValueIndicator());
            } else {
                newColumn.append(PackedLocalDate.getDayOfMonth(c1));
            }
        }
        return newColumn;
    }

    default DoubleColumn dayOfYear() {
        DoubleColumn newColumn = DoubleColumn.create(this.name() + " day of year");
        for (int r = 0; r < this.size(); r++) {
            int c1 = this.getIntInternal(r);
            if (DateColumn.valueIsMissing(c1)) {
                newColumn.append(DoubleColumnType.missingValueIndicator());
            } else {
                newColumn.append((short) PackedLocalDate.getDayOfYear(c1));
            }
        }
        return newColumn;
    }

    default DoubleColumn monthValue() {
        DoubleColumn newColumn = DoubleColumn.create(this.name() + " month");

        for (int r = 0; r < this.size(); r++) {
            int c1 = this.getIntInternal(r);
            if (DateColumn.valueIsMissing(c1)) {
                newColumn.append(DoubleColumnType.missingValueIndicator());
            } else {
                newColumn.append(PackedLocalDate.getMonthValue(c1));
            }
        }
        return newColumn;
    }

    default StringColumn month() {
        StringColumn newColumn = StringColumn.create(this.name() + " month");

        for (int r = 0; r < this.size(); r++) {
            int c1 = this.getIntInternal(r);
            if (DateColumn.valueIsMissing(c1)) {
                newColumn.append(StringColumn.MISSING_VALUE);
            } else {
                newColumn.append(PackedLocalDate.getMonth(c1).name());
            }
        }
        return newColumn;
    }

    default DoubleColumn year() {
        DoubleColumn newColumn = DoubleColumn.create(this.name() + " year");
        for (int r = 0; r < this.size(); r++) {
            int c1 = this.getIntInternal(r);
            if (DateColumn.valueIsMissing(c1)) {
                newColumn.append(DoubleColumnType.missingValueIndicator());
            } else {
                newColumn.append(PackedLocalDate.getYear(c1));
            }
        }
        return newColumn;
    }

    /**
     * Returns a StringColumn with the year and quarter from this column concatenated into a String that will sort
     * lexicographically in temporal order.
     * <p>
     * This simplifies the production of plots and tables that aggregate values into standard temporal units (e.g.,
     * you want monthly data but your source data is more than a year long and you don't want months from different
     * years aggregated together).
     */
    default StringColumn yearQuarter() {
        StringColumn newColumn = StringColumn.create(this.name() + " year & quarter");
        for (int r = 0; r < this.size(); r++) {
            int c1 = this.getIntInternal(r);
            if (DateColumn.valueIsMissing(c1)) {
                newColumn.append(StringColumn.MISSING_VALUE);
            } else {
                String yq = String.valueOf(PackedLocalDate.getYear(c1));
                yq = yq + "-" + Strings.padStart(
                        String.valueOf(PackedLocalDate.getQuarter(c1)), 2, '0');
                newColumn.append(yq);
            }
        }
        return newColumn;
    }

    /**
     * Returns a StringColumn with the year and month from this column concatenated into a String that will sort
     * lexicographically in temporal order.
     * <p>
     * This simplifies the production of plots and tables that aggregate values into standard temporal units (e.g.,
     * you want monthly data but your source data is more than a year long and you don't want months from different
     * years aggregated together).
     */
    default StringColumn yearMonth() {
        StringColumn newColumn = StringColumn.create(this.name() + " year & month");
        for (int r = 0; r < this.size(); r++) {
            int c1 = this.getIntInternal(r);
            if (DateColumn.valueIsMissing(c1)) {
                newColumn.append(StringColumn.MISSING_VALUE);
            } else {
                String ym = String.valueOf(PackedLocalDate.getYear(c1));
                ym = ym + "-" + Strings.padStart(
                        String.valueOf(PackedLocalDate.getMonthValue(c1)), 2, '0');
                newColumn.append(ym);
            }
        }
        return newColumn;
    }

    /**
     * Returns a StringColumn with the year and day-of-year derived from this column concatenated into a String
     * that will sort lexicographically in temporal order.
     * <p>
     * This simplifies the production of plots and tables that aggregate values into standard temporal units (e.g.,
     * you want monthly data but your source data is more than a year long and you don't want months from different
     * years aggregated together).
     */
    default StringColumn yearDay() {
        StringColumn newColumn = StringColumn.create(this.name() + " year & month");
        for (int r = 0; r < this.size(); r++) {
            int c1 = this.getIntInternal(r);
            if (DateColumn.valueIsMissing(c1)) {
                newColumn.append(StringColumn.MISSING_VALUE);
            } else {
                String ym = String.valueOf(PackedLocalDate.getYear(c1));
                ym = ym + "-" + Strings.padStart(
                        String.valueOf(PackedLocalDate.getDayOfYear(c1)), 3, '0');
                newColumn.append(ym);
            }
        }
        return newColumn;
    }

    /**
     * Returns a StringColumn with the year and week-of-year derived from this column concatenated into a String
     * that will sort lexicographically in temporal order.
     * <p>
     * This simplifies the production of plots and tables that aggregate values into standard temporal units (e.g.,
     * you want monthly data but your source data is more than a year long and you don't want months from different
     * years aggregated together).
     */
    default StringColumn yearWeek() {
        StringColumn newColumn = StringColumn.create(this.name() + " year & month");
        for (int r = 0; r < this.size(); r++) {
            int c1 = this.getIntInternal(r);
            if (DateColumn.valueIsMissing(c1)) {
                newColumn.append(StringColumn.MISSING_VALUE);
            } else {
                String ym = String.valueOf(PackedLocalDate.getYear(c1));
                ym = ym + "-" + Strings.padStart(
                        String.valueOf(PackedLocalDate.getWeekOfYear(c1)), 2, '0');
                newColumn.append(ym);
            }
        }
        return newColumn;
    }

    default DoubleColumn dayOfWeekValue() {
        DoubleColumn newColumn = DoubleColumn.create(this.name() + " day of week", this.size());
        for (int r = 0; r < this.size(); r++) {
            int c1 = this.getIntInternal(r);
            if (DateColumn.valueIsMissing(c1)) {
                newColumn.set(r, DoubleColumnType.missingValueIndicator());
            } else {
                newColumn.append((short) PackedLocalDate.getDayOfWeek(c1).getValue());
            }
        }
        return newColumn;
    }

    default StringColumn dayOfWeek() {
        StringColumn newColumn = StringColumn.create(this.name() + " day of week");
        for (int r = 0; r < this.size(); r++) {
            int c1 = this.getIntInternal(r);
            if (DateColumn.valueIsMissing(c1)) {
                newColumn.append(StringColumn.MISSING_VALUE);
            } else {
                newColumn.append(PackedLocalDate.getDayOfWeek(c1).toString());
            }
        }
        return newColumn;
    }

    /**
     * Calculates the temporal difference between each element of the receiver and the respective element of the
     * argument
     * <p>
     * Missing values in either result in a Missing Value for the new column
     */
    default DoubleColumn timeUntil(DateColumn end, ChronoUnit unit) {

        DoubleColumn newColumn = DoubleColumn.create(name() + " - " + end.name());
        for (int r = 0; r < size(); r++) {
            int c1 = getIntInternal(r);
            int c2 = end.getIntInternal(r);
            if (valueIsMissing(c1) || valueIsMissing(c2)) {
                newColumn.append(DoubleColumnType.missingValueIndicator());
            } else {
                switch (unit) {
                    case DAYS:
                        newColumn.append(PackedLocalDate.daysUntil(c2, c1));
                        break;
                    case WEEKS:
                        newColumn.append(PackedLocalDate.weeksUntil(c2, c1));
                        break;
                    case MONTHS:
                        newColumn.append(PackedLocalDate.monthsUntil(c2, c1));
                        break;
                    case YEARS:
                        newColumn.append(PackedLocalDate.yearsUntil(c2, c1));
                        break;
                    default:  // handle decades, etc.
                        LocalDate value1 = PackedLocalDate.asLocalDate(c1);
                        LocalDate value2 = PackedLocalDate.asLocalDate(c2);
                        if (value1 == null || value2 == null) {
                            newColumn.appendMissing();
                        } else {
                            newColumn.append(unit.between(value1, value2));
                        }
                        break;
                }
            }
        }
        return newColumn;
    }

    // These functions fill some amount of time to a date, producing a new date column
    default DateColumn plusDays(int days) {
        return plus(days, ChronoUnit.DAYS);
    }

    default DateColumn plusWeeks(int weeks) {
        return plus(weeks, ChronoUnit.WEEKS);
    }

    default DateColumn plusYears(int years) {
        return plus(years, ChronoUnit.YEARS);
    }

    // These functions subtract some amount of time from a date, producing a new date column

    default DateColumn plusMonths(int months) {
        return plus(months, ChronoUnit.MONTHS);
    }

    default DateColumn minusDays(int days) {
        return plusDays(-days);
    }

    default DateColumn minusWeeks(int weeks) {
        return plusWeeks(-weeks);
    }

    default DateColumn minusYears(int years) {
        return plusYears(-years);
    }

    default DateColumn minusMonths(int months) {
        return plusMonths(-months);
    }

    /**
     * Returns a column containing integers representing the nth group (0-based) that a date falls into.
     *
     * Example:     When Unit = ChronoUnit.DAY and n = 5, we form 5 day groups. a Date that is 2 days after the start
     * is assigned to the first ("0") group. A day 7 days after the start is assigned to the second ("1") group.
     *
     * @param unit  A ChronoUnit greater than or equal to a day
     * @param n     The number of units in each group.
     * @param start The starting point of the first group; group boundaries are offsets from this point
     */
    default DoubleColumn timeWindow(ChronoUnit unit, int n, LocalDate start) {
        String newColumnName = "" +  n + " " + unit.toString() + " window [" + name() + "]";
        int packedStartDate = PackedLocalDate.pack(start);
        DoubleColumn numberColumn = DoubleColumn.create(newColumnName, size());
        for (int i = 0; i < size(); i++) {
            int packedDate = getIntInternal(i);
            int result;
            switch (unit) {

                case DAYS:
                    result = PackedLocalDate.daysUntil(packedDate, packedStartDate) / n;
                    numberColumn.append(result); break;
                case WEEKS:
                    result = PackedLocalDate.weeksUntil(packedDate, packedStartDate) / n;
                    numberColumn.append(result); break;
                case MONTHS:
                    result = PackedLocalDate.monthsUntil(packedDate, packedStartDate) / n;
                    numberColumn.append(result); break;
                case YEARS:
                    result = PackedLocalDate.yearsUntil(packedDate, packedStartDate) / n;
                    numberColumn.append(result); break;
                default:
                    throw new UnsupportedTemporalTypeException("The ChronoUnit " + unit + " is not supported for timeWindows on dates");
            }
        }
        numberColumn.setPrintFormatter(NumberColumnFormatter.ints());
        return numberColumn;
    }

    default DoubleColumn timeWindow(ChronoUnit unit, int n) {
        return timeWindow(unit, n, min());
    }

    default DateColumn plus(int value, ChronoUnit unit) {

        DateColumn newColumn = DateColumn.create(dateColumnName(this, value, unit));
        DateColumn column1 = (DateColumn) this;

        for (int r = 0; r < column1.size(); r++) {
            int packedDate = column1.getIntInternal(r);
            if (packedDate == MISSING_VALUE) {
                newColumn.appendInternal(MISSING_VALUE);
            } else {
                newColumn.appendInternal(PackedLocalDate.plus(value, unit, packedDate));
            }
        }
        return newColumn;
    }

    // misc functions
    default DateColumn minus(int value, ChronoUnit unit) {
        return plus(-value, unit);
    }

    default DateTimeColumn atStartOfDay() {
        DateTimeColumn newColumn = DateTimeColumn.create(this.name() + " " + " start");
        for (int r = 0; r < this.size(); r++) {
            LocalDate c1 = this.get(r);
            if (c1 == null) {
                newColumn.appendInternal(DateTimeColumn.MISSING_VALUE);
            } else {
                newColumn.append(c1.atStartOfDay());
            }
        }
        return newColumn;
    }

    /**
     * Returns a DateTime column where each value consists of the dates from this column combined with the corresponding
     * times from the other column
     */
    default DateTimeColumn atTime(LocalTime time) {
        Preconditions.checkNotNull(time);
        DateTimeColumn newColumn = DateTimeColumn.create(this.name() + " " + time.toString());
        for (int r = 0; r < this.size(); r++) {
            int c1 = this.getIntInternal(r);
            if (DateColumn.valueIsMissing(c1)) {
                newColumn.appendInternal(DateTimeColumn.MISSING_VALUE);
            } else {
                LocalDate value1 = PackedLocalDate.asLocalDate(c1);
                newColumn.appendInternal(PackedLocalDateTime.pack(value1, time));
            }
        }
        return newColumn;
    }

    /**
     * Returns a DateTime column where each value consists of the dates from this column combined with the corresponding
     * times from the other column
     */
    default DateTimeColumn atTime(TimeColumn timeColumn) {
        DateTimeColumn newColumn = DateTimeColumn.create(this.name() + " " + timeColumn.name());
        for (int r = 0; r < this.size(); r++) {
            int c1 = this.getIntInternal(r);
            int c2 = timeColumn.getIntInternal(r);
            if (DateColumn.valueIsMissing(c1) || DateColumn.valueIsMissing(c2)) {
                newColumn.appendInternal(DateTimeColumn.MISSING_VALUE);
            } else {
                newColumn.appendInternal(PackedLocalDateTime.create(c1, c2));
            }
        }
        return newColumn;
    }

    int getIntInternal(int r);

    LocalDate get(int index);

    LocalDate min();
    LocalDate max();
}
