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

package tech.tablesaw.columns.datetimes;

import com.google.common.base.Strings;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.TimeColumn;
import tech.tablesaw.columns.Column;
import tech.tablesaw.columns.dates.PackedLocalDate;
import tech.tablesaw.columns.numbers.DoubleColumnType;
import tech.tablesaw.columns.numbers.NumberColumnFormatter;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;

import static tech.tablesaw.api.DateTimeColumn.MISSING_VALUE;
import static tech.tablesaw.columns.datetimes.PackedLocalDateTime.*;

public interface DateTimeMapFunctions extends Column<LocalDateTime> {

    default DoubleColumn differenceInMilliseconds(DateTimeColumn column2) {
        return difference(column2, ChronoUnit.MILLIS);
    }

    default DoubleColumn differenceInSeconds(DateTimeColumn column2) {
        return difference(column2, ChronoUnit.SECONDS);
    }

    default DoubleColumn differenceInMinutes(DateTimeColumn column2) {
        return difference(column2, ChronoUnit.MINUTES);
    }

    default DoubleColumn differenceInHours(DateTimeColumn column2) {
        return difference(column2, ChronoUnit.HOURS);
    }

    default DoubleColumn differenceInDays(DateTimeColumn column2) {
        return difference(column2, ChronoUnit.DAYS);
    }

    default DoubleColumn differenceInYears(DateTimeColumn column2) {
        return difference(column2, ChronoUnit.YEARS);
    }

    default DoubleColumn difference(DateTimeColumn column2, ChronoUnit unit) {
        DoubleColumn newColumn = DoubleColumn.create(name() + " - " + column2.name());

        for (int r = 0; r < size(); r++) {
            long c1 = this.getLongInternal(r);
            long c2 = column2.getLongInternal(r);
            if (c1 == MISSING_VALUE || c2 == MISSING_VALUE) {
                newColumn.append(DoubleColumnType.missingValueIndicator());
            } else {
                LocalDateTime value1 = asLocalDateTime(c1);
                LocalDateTime value2 = asLocalDateTime(c2);
                if (value1 != null && value2 != null) {
                    newColumn.append(unit.between(value1, value2));
                } else {
                    newColumn.appendMissing();
                }
            }
        }
        return newColumn;
    }

    default DoubleColumn hour() {
        DoubleColumn newColumn = DoubleColumn.create(name() + "[" + "hour" + "]");
        for (int r = 0; r < size(); r++) {
            long c1 = getLongInternal(r);
            if (c1 != MISSING_VALUE) {
                newColumn.append(getHour(c1));
            } else {
                newColumn.append(DoubleColumnType.missingValueIndicator());
            }
        }
        return newColumn;
    }

    default DoubleColumn minuteOfDay() {
        DoubleColumn newColumn = DoubleColumn.create(name() + "[" + "minute-of-day" + "]");
        for (int r = 0; r < size(); r++) {
            long c1 = getLongInternal(r);
            if (c1 != MISSING_VALUE) {
                newColumn.append((short) getMinuteOfDay(c1));
            } else {
                newColumn.append(DoubleColumnType.missingValueIndicator());
            }
        }
        return newColumn;
    }

    default DoubleColumn secondOfDay() {
        DoubleColumn newColumn = DoubleColumn.create(name() + "[" + "second-of-day" + "]");
        for (int r = 0; r < size(); r++) {
            long c1 = getLongInternal(r);
            if (c1 != MISSING_VALUE) {
                newColumn.append(getSecondOfDay(c1));
            } else {
                newColumn.append(DoubleColumnType.missingValueIndicator());
            }
        }
        return newColumn;
    }

    @Override
    default DateTimeColumn lead(int n) {
        DateTimeColumn column = lag(-n);
        column.setName(name() + " lead(" + n + ")");
        return column;
    }

    @Override
    DateTimeColumn lag(int n);

    /**
     * Returns a TimeColumn containing the time portion of each dateTime in this DateTimeColumn
     */
    default TimeColumn time() {
        TimeColumn newColumn = TimeColumn.create(this.name() + " time");
        for (int r = 0; r < this.size(); r++) {
            long c1 = getLongInternal(r);
            if (DateTimeColumn.valueIsMissing(c1)) {
                newColumn.appendInternal(TimeColumn.MISSING_VALUE);
            } else {
                newColumn.appendInternal(PackedLocalDateTime.time(c1));
            }
        }
        return newColumn;
    }

    default DoubleColumn monthValue() {
        DoubleColumn newColumn = DoubleColumn.create(this.name() + " month");
        for (int r = 0; r < this.size(); r++) {
            long c1 = getLongInternal(r);
            if (DateTimeColumn.valueIsMissing(c1)) {
                newColumn.append(DoubleColumnType.missingValueIndicator());
            } else {
                newColumn.append((short) getMonthValue(c1));
            }
        }
        return newColumn;
    }

    /**
     * Returns a StringColumn containing the name of the month for each date/time in this column
     */
    default StringColumn month() {
        StringColumn newColumn = StringColumn.create(this.name() + " month");
        for (int r = 0; r < this.size(); r++) {
            long c1 = this.getLongInternal(r);
            if (DateTimeColumn.valueIsMissing(c1)) {
                newColumn.append(StringColumn.MISSING_VALUE);
            } else {
                newColumn.append(Month.of(getMonthValue(c1)).name());
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
            long c1 = this.getLongInternal(r);
            if (DateTimeColumn.valueIsMissing(c1)) {
                newColumn.append(StringColumn.MISSING_VALUE);
            } else {
                String yq = String.valueOf(getYear(c1)) + "-" + getQuarter(c1);
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
            long c1 = this.getLongInternal(r);
            if (DateTimeColumn.valueIsMissing(c1)) {
                newColumn.append(StringColumn.MISSING_VALUE);
            } else {
                String ym = String.valueOf(getYear(c1));
                ym = ym + "-" + Strings.padStart(
                        String.valueOf(getMonthValue(c1)), 2, '0');
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
            long c1 = this.getLongInternal(r);
            if (DateTimeColumn.valueIsMissing(c1)) {
                newColumn.append(StringColumn.MISSING_VALUE);
            } else {
                String ym = String.valueOf(getYear(c1));
                ym = ym + "-" + Strings.padStart(
                        String.valueOf(getDayOfYear(c1)), 3, '0');
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
    default StringColumn hourMinute() {
        StringColumn newColumn = StringColumn.create(this.name() + " hour & minute");
        for (int r = 0; r < this.size(); r++) {
            long c1 = this.getLongInternal(r);
            if (DateTimeColumn.valueIsMissing(c1)) {
                newColumn.append(StringColumn.MISSING_VALUE);
            } else {
                String hm = Strings.padStart(String.valueOf(getHour(c1)), 2, '0');
                hm = hm + ":" + Strings.padStart(
                        String.valueOf(getMinute(c1)), 2, '0');
                newColumn.append(hm);
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
            long c1 = this.getLongInternal(r);
            if (DateTimeColumn.valueIsMissing(c1)) {
                newColumn.append(StringColumn.MISSING_VALUE);
            } else {
                String ym = String.valueOf(getYear(c1));
                ym = ym + "-" + Strings.padStart(
                        String.valueOf(getWeekOfYear(c1)), 2, '0');
                newColumn.append(ym);
            }
        }
        return newColumn;
    }


    /**
     * Returns a DateColumn containing the date portion of each dateTime in this DateTimeColumn
     */
    default DateColumn date() {
        DateColumn newColumn = DateColumn.create(this.name() + " date");
        for (int r = 0; r < this.size(); r++) {
            long c1 = getLongInternal(r);
            if (DateTimeColumn.valueIsMissing(c1)) {
                newColumn.appendInternal(DateColumn.MISSING_VALUE);
            } else {
                newColumn.appendInternal(PackedLocalDateTime.date(c1));
            }
        }
        return newColumn;
    }

    default DoubleColumn year() {
        DoubleColumn newColumn = DoubleColumn.create(this.name() + " year");
        for (int r = 0; r < this.size(); r++) {
            long c1 = getLongInternal(r);
            if (DateTimeColumn.valueIsMissing(c1)) {
                newColumn.append(DoubleColumnType.missingValueIndicator());
            } else {
                newColumn.append(PackedLocalDate.getYear(PackedLocalDateTime.date(c1)));
            }
        }
        return newColumn;
    }

    default StringColumn dayOfWeek() {
        StringColumn newColumn = StringColumn.create(this.name() + " day of week", this.size());
        for (int r = 0; r < this.size(); r++) {
            long c1 = this.getLongInternal(r);
            if (DateTimeColumn.valueIsMissing(c1)) {
                newColumn.append(StringColumn.MISSING_VALUE);
            } else {
                newColumn.append(getDayOfWeek(c1).toString());
            }
        }
        return newColumn;
    }

    default DoubleColumn dayOfWeekValue() {
        DoubleColumn newColumn = DoubleColumn.create(this.name() + " day of week", this.size());
        for (int r = 0; r < this.size(); r++) {
            long c1 = this.getLongInternal(r);
            if (DateTimeColumn.valueIsMissing(c1)) {
                newColumn.append(DoubleColumnType.missingValueIndicator());
            } else {
                newColumn.append((short) getDayOfWeek(c1).getValue());
            }
        }
        return newColumn;
    }

    default DoubleColumn dayOfYear() {
        DoubleColumn newColumn = DoubleColumn.create(this.name() + " day of year", this.size());
        for (int r = 0; r < this.size(); r++) {
            long c1 = this.getLongInternal(r);
            if (DateTimeColumn.valueIsMissing(c1)) {
                newColumn.append(DoubleColumnType.missingValueIndicator());
            } else {
                newColumn.append((short) getDayOfYear(c1));
            }
        }
        return newColumn;
    }

    default DoubleColumn dayOfMonth() {
        DoubleColumn newColumn = DoubleColumn.create(this.name() + " day of month");
        for (int r = 0; r < this.size(); r++) {
            long c1 = this.getLongInternal(r);
            if (DateTimeColumn.valueIsMissing(c1)) {
                newColumn.append(DoubleColumnType.missingValueIndicator());
            } else {
                newColumn.append(getDayOfMonth(c1));
            }
        }
        return newColumn;
    }

    /**
     * Returns a column containing integers representing the nth group (0-based) that a date falls into.
     *
     * Example:     When Unit = ChronoUnit.DAY and n = 5, we form 5 day groups. a Date that is 2 days after the start
     * is assigned to the first ("0") group. A day 7 days after the start is assigned to the second ("1") group.
     *
     * @param unit  A ChronoUnit greater than or equal to a minute
     * @param n     The number of units in each group.
     * @param start The starting point of the first group; group boundaries are offsets from this point
     */
    default DoubleColumn timeWindow(ChronoUnit unit, int n, LocalDateTime start) {
        String newColumnName = "" +  n + " " + unit.toString() + " window [" + name() + "]";
        long packedStartDate = pack(start);
        DoubleColumn numberColumn = DoubleColumn.create(newColumnName, size());
        for (int i = 0; i < size(); i++) {
            long packedDate = getLongInternal(i);
            long result;
            switch (unit) {

                case MINUTES:
                    result = minutesUntil(packedDate, packedStartDate) / n;
                    numberColumn.append(result); break;
                case HOURS:
                    result = hoursUntil(packedDate, packedStartDate) / n;
                    numberColumn.append(result); break;
                case DAYS:
                    result = daysUntil(packedDate, packedStartDate) / n;
                    numberColumn.append(result); break;
                case WEEKS:
                    result = weeksUntil(packedDate, packedStartDate) / n;
                    numberColumn.append(result); break;
                case MONTHS:
                    result = monthsUntil(packedDate, packedStartDate) / n;
                    numberColumn.append(result); break;
                case YEARS:
                    result = yearsUntil(packedDate, packedStartDate) / n;
                    numberColumn.append(result); break;
                default:
                    throw new UnsupportedTemporalTypeException("The ChronoUnit " + unit + " is not supported for timeWindows on dates");
            }
        }
        numberColumn.setPrintFormatter(NumberColumnFormatter.ints());
        return numberColumn;
    }

    default DoubleColumn minute() {
        DoubleColumn newColumn = DoubleColumn.create(name() + "[" + "minute" + "]");
        for (int r = 0; r < size(); r++) {
            long c1 = getLongInternal(r);
            if (!DateTimeColumn.valueIsMissing(c1)) {
                newColumn.append(getMinute(c1));
            } else {
                newColumn.append(DoubleColumnType.missingValueIndicator());
            }
        }
        return newColumn;
    }

    default DoubleColumn timeWindow(ChronoUnit unit, int n) {
        return timeWindow(unit, n, min());
    }

    LocalDateTime get(int r);

    long getLongInternal(int r);

    LocalDateTime min();
}
