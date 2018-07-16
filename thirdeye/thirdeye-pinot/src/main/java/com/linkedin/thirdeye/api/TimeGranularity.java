package com.linkedin.thirdeye.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;


public class TimeGranularity {
  private final int size;
  private final TimeUnit unit;

  public TimeGranularity() {
    this(1, TimeUnit.HOURS);
  }

  public TimeGranularity(int size, TimeUnit unit) {
    this.size = size;
    this.unit = unit;
  }

  /**
   * Copy constructor
   * @param that to be copied
   */
  public TimeGranularity(TimeGranularity that) {
    this(that.getSize(), that.getUnit());
  }

  @JsonProperty
  public int getSize() {
    return size;
  }

  @JsonProperty
  public TimeUnit getUnit() {
    return unit;
  }

  /**
   * Returns the equivalent milliseconds of this time granularity.
   *
   * @return the equivalent milliseconds of this time granularity.
   */
  public long toMillis() {
    return toMillis(1);
  }

  /**
   * Returns the equivalent milliseconds of the specified number of this time granularity. Highly suggested to use
   * toPeriod instead of this method for handling daylight saving time issue.
   *
   * @param number the specified number of this time granularity.
   *
   * @return the equivalent milliseconds of the specified number of this time granularity.
   */
  public long toMillis(long number) {
    return unit.toMillis(number * size);
  }

  /**
   * Returns an equivalent Period object of this time granularity.
   *
   * @return an equivalent Period object of this time granularity.
   */
  public Period toPeriod() {
    return toPeriod(1);
  }

  /**
   * Returns an equivalent Period object of the specified number of this time granularity.
   *
   * @param number the specified number of this time granularity.
   *
   * @return an equivalent Period object of the specified number of this time granularity.
   */
  public Period toPeriod(int number) {
    int size = this.size * number;
    switch (unit) {
      case DAYS:
        return new Period().withPeriodType(PeriodType.days()).withField(DurationFieldType.days(), size);
      case HOURS:
        return new Period().withPeriodType(PeriodType.hours()).withField(DurationFieldType.hours(), size);
      case MINUTES:
        return new Period().withPeriodType(PeriodType.minutes()).withField(DurationFieldType.minutes(), size);
      case SECONDS:
        return new Period().withPeriodType(PeriodType.seconds()).withField(DurationFieldType.seconds(), size);
      case MILLISECONDS:
        return new Period().withPeriodType(PeriodType.millis()).withField(DurationFieldType.millis(), size);
    }
    throw new IllegalArgumentException(String.format("Unsupported unit type %s", this.unit));
  }

  /**
   * Converts millis to time unit
   * e.g. If TimeGranularity is defined as 1 HOURS,
   * and we invoke convertToUnit(1458284400000) (i.e. 2016-03-18 00:00:00)
   * this method will return HOURS.convert(1458284400000, MILLISECONDS)/1 = 405079 hoursSinceEpoch
   * If TimeGranularity is defined as 10 MINUTES,
   * and we invoke convertToUnit(1458284400000) (i.e. 2016-03-18 00:00:00)
   * this method will return MINUTES.convert(1458284400000, MILLISECONDS)/10 = 2430474
   * tenMinutesSinceEpoch
   * @param millis
   * @return
   */
  public long convertToUnit(long millis) {
    return unit.convert(millis, TimeUnit.MILLISECONDS) / size;
  }

  /**
   * Initialize time granularity from its aggregation string representation, in which duration and unit are separated
   * by "_". For instance, "5_MINUTES" initialize a time granularity with size = 5 and TimeUnit = "MINUTES".
   *
   * @param timeGranularityString the aggregation string representation of the time granularity.
   *
   * @return time granularity that is initialized from the given aggregation string representation.
   */
  public static TimeGranularity fromString(String timeGranularityString) {
    if (timeGranularityString.contains("_")) {
      String[] split = timeGranularityString.split("_");
      return new TimeGranularity(Integer.parseInt(split[0]), TimeUnit.valueOf(split[1]));
    } else {
      return new TimeGranularity(1, TimeUnit.valueOf(timeGranularityString));
    }
  }

  /**
   * Return the string representation of this time granularity, in which duration and unit are separated by "_".
   *
   * @return the string representation of this time granularity, in which duration and unit are separated by "_".
   */
  public String toAggregationGranularityString() {
    return size + "_" + unit;
  }

  /**
   * Return the string representation of this time granularity, in which duration and unit are separated by "-".
   *
   * @return the string representation of this time granularity, in which duration and unit are separated by "-".
   */
  @Override
  public String toString() {
    return size + "-" + unit;
  }

  @Override
  public int hashCode() {
    return Objects.hash(size, unit);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof TimeGranularity)) {
      return false;
    }
    TimeGranularity other = (TimeGranularity) obj;
    return Objects.equals(other.size, this.size) && Objects.equals(other.unit, this.unit);
  }
}
