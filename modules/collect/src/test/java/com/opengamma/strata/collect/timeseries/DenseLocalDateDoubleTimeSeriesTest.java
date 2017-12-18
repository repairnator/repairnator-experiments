/*
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.collect.timeseries;

import static com.opengamma.strata.collect.TestHelper.assertThrows;
import static com.opengamma.strata.collect.TestHelper.assertThrowsIllegalArg;
import static com.opengamma.strata.collect.timeseries.DenseLocalDateDoubleTimeSeries.DenseTimeSeriesCalculation.INCLUDE_WEEKENDS;
import static com.opengamma.strata.collect.timeseries.DenseLocalDateDoubleTimeSeries.DenseTimeSeriesCalculation.SKIP_WEEKENDS;
import static com.opengamma.strata.collect.timeseries.LocalDateDoubleTimeSeries.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.OptionalDouble;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.ImmutableBean;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Doubles;
import com.opengamma.strata.collect.Guavate;
import com.opengamma.strata.collect.TestHelper;
import com.opengamma.strata.collect.tuple.Pair;

@Test
public class DenseLocalDateDoubleTimeSeriesTest {

  private static final double TOLERANCE = 1e-5;

  private static final LocalDate DATE_2015_06_01 = date(2015, 6, 1);
  private static final LocalDate DATE_2014_06_01 = date(2014, 6, 1);
  private static final LocalDate DATE_2012_06_01 = date(2012, 6, 1);
  private static final LocalDate DATE_2010_06_01 = date(2010, 6, 1);
  private static final LocalDate DATE_2011_06_01 = date(2011, 6, 1);
  private static final LocalDate DATE_2013_06_01 = date(2013, 6, 1);
  private static final LocalDate DATE_2010_01_01 = date(2010, 1, 1);
  private static final LocalDate DATE_2010_01_02 = date(2010, 1, 2);
  private static final LocalDate DATE_2010_01_03 = date(2010, 1, 3);
  // Avoid weekends for these days
  private static final LocalDate DATE_2011_01_01 = date(2011, 1, 1);
  private static final LocalDate DATE_2012_01_01 = date(2012, 1, 1);
  private static final LocalDate DATE_2013_01_01 = date(2013, 1, 1);

  private static final LocalDate DATE_2014_01_01 = date(2014, 1, 1);

  private static final LocalDate DATE_2015_01_02 = date(2015, 1, 2);
  private static final LocalDate DATE_2015_01_03 = date(2015, 1, 3);
  private static final LocalDate DATE_2015_01_04 = date(2015, 1, 4);
  private static final LocalDate DATE_2015_01_05 = date(2015, 1, 5);
  private static final LocalDate DATE_2015_01_06 = date(2015, 1, 6);
  private static final LocalDate DATE_2015_01_07 = date(2015, 1, 7);
  private static final LocalDate DATE_2015_01_08 = date(2015, 1, 8);
  private static final LocalDate DATE_2015_01_09 = date(2015, 1, 9);
  private static final LocalDate DATE_2015_01_11 = date(2015, 1, 11);

  private static final LocalDate DATE_2015_01_12 = date(2015, 1, 12);

  private static final ImmutableList<LocalDate> DATES_2015_1_WEEK = dates(
      DATE_2015_01_05, DATE_2015_01_06, DATE_2015_01_07, DATE_2015_01_08, DATE_2015_01_09);

  private static final ImmutableList<Double> VALUES_1_WEEK = values(10, 11, 12, 13, 14);
  private static final ImmutableList<LocalDate> DATES_2010_12 = dates(
      DATE_2010_01_01, DATE_2011_01_01, DATE_2012_01_01);
  private static final ImmutableList<Double> VALUES_10_12 = values(10, 11, 12);
  private static final ImmutableList<Double> VALUES_1_3 = values(1, 2, 3);
  private static final ImmutableList<Double> VALUES_4_7 = values(4, 5, 6, 7);

  //-------------------------------------------------------------------------
  public void test_of_singleton() {
    LocalDateDoubleTimeSeries test = LocalDateDoubleTimeSeries.of(DATE_2011_01_01, 2d);
    assertEquals(test.isEmpty(), false);
    assertEquals(test.size(), 1);

    // Check start is not weekend

    assertEquals(test.containsDate(DATE_2010_01_01), false);
    assertEquals(test.containsDate(DATE_2011_01_01), true);
    assertEquals(test.containsDate(DATE_2012_01_01), false);
    assertEquals(test.get(DATE_2010_01_01), OptionalDouble.empty());
    assertEquals(test.get(DATE_2011_01_01), OptionalDouble.of(2d));
    assertEquals(test.get(DATE_2012_01_01), OptionalDouble.empty());
    assertEquals(test.dates().toArray(), new Object[] {DATE_2011_01_01});
    assertEquals(test.values().toArray(), new double[] {2d});
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_of_singleton_nullDateDisallowed() {
    LocalDateDoubleTimeSeries.of(null, 1d);
  }

  //-------------------------------------------------------------------------
  public void test_of_collectionCollection() {
    Collection<LocalDate> dates = dates(DATE_2011_01_01, DATE_2012_01_01);
    Collection<Double> values = values(2d, 3d);

    LocalDateDoubleTimeSeries test = LocalDateDoubleTimeSeries.builder().putAll(dates, values).build();
    assertEquals(test.isEmpty(), false);
    assertEquals(test.size(), 2);
    assertEquals(test.containsDate(DATE_2010_01_01), false);
    assertEquals(test.containsDate(DATE_2011_01_01), true);
    assertEquals(test.containsDate(DATE_2012_01_01), true);
    assertEquals(test.get(DATE_2010_01_01), OptionalDouble.empty());
    assertEquals(test.get(DATE_2011_01_01), OptionalDouble.of(2d));
    assertEquals(test.get(DATE_2012_01_01), OptionalDouble.of(3d));
    assertEquals(test.dates().toArray(), new Object[] {DATE_2011_01_01, DATE_2012_01_01});
    assertEquals(test.values().toArray(), new double[] {2d, 3d});
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_of_collectionCollection_dateCollectionNull() {
    Collection<Double> values = values(2d, 3d);

    LocalDateDoubleTimeSeries.builder().putAll(null, values).build();
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_of_collectionCollection_valueCollectionNull() {
    Collection<LocalDate> dates = dates(DATE_2011_01_01, DATE_2012_01_01);

    LocalDateDoubleTimeSeries.builder().putAll(dates, (double[]) null).build();
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_of_collectionCollection_dateCollectionWithNull() {
    Collection<LocalDate> dates = Arrays.asList(DATE_2011_01_01, null);
    Collection<Double> values = values(2d, 3d);

    LocalDateDoubleTimeSeries.builder().putAll(dates, values).build();
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_of_collectionCollection_valueCollectionWithNull() {
    Collection<LocalDate> dates = dates(DATE_2011_01_01, DATE_2012_01_01);
    Collection<Double> values = Arrays.asList(2d, null);

    LocalDateDoubleTimeSeries.builder().putAll(dates, values).build();
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_of_collectionCollection_collectionsOfDifferentSize() {
    Collection<LocalDate> dates = dates(DATE_2011_01_01);
    Collection<Double> values = values(2d, 3d);

    LocalDateDoubleTimeSeries.builder().putAll(dates, values).build();
  }

  public void test_of_collectionCollection_datesUnordered() {
    Collection<LocalDate> dates = dates(DATE_2012_01_01, DATE_2011_01_01);
    Collection<Double> values = values(2d, 1d);

    LocalDateDoubleTimeSeries series = LocalDateDoubleTimeSeries.builder().putAll(dates, values).build();
    assertEquals(series.get(DATE_2011_01_01), OptionalDouble.of(1d));
    assertEquals(series.get(DATE_2012_01_01), OptionalDouble.of(2d));
  }

  public void test_NaN_is_not_allowed() {
    assertThrowsIllegalArg(() -> LocalDateDoubleTimeSeries.of(DATE_2015_01_02, Double.NaN));
    assertThrowsIllegalArg(() -> LocalDateDoubleTimeSeries.builder().put(DATE_2015_01_02, Double.NaN));
    assertThrowsIllegalArg(() -> LocalDateDoubleTimeSeries.builder().putAll(
        ImmutableMap.of(DATE_2015_01_02, Double.NaN)));
    assertThrowsIllegalArg(() -> LocalDateDoubleTimeSeries.builder().put(
        LocalDateDoublePoint.of(DATE_2015_01_02, Double.NaN)));
    assertThrowsIllegalArg(() -> LocalDateDoubleTimeSeries.builder().putAll(
        ImmutableList.of(DATE_2015_01_02), ImmutableList.of(Double.NaN)));
    assertThrowsIllegalArg(() -> LocalDateDoubleTimeSeries.builder().putAll(
        ImmutableList.of(LocalDateDoublePoint.of(DATE_2015_01_02, Double.NaN))));

    LocalDateDoubleTimeSeries s1 = LocalDateDoubleTimeSeries.of(DATE_2015_01_02, 1d);
    LocalDateDoubleTimeSeries s2 = LocalDateDoubleTimeSeries.of(DATE_2015_01_02, 2d);

    assertThrowsIllegalArg(() -> s1.intersection(s2, (d1, d2) -> Double.NaN));

    assertThrowsIllegalArg(() -> s1.mapValues(d -> Double.NaN));
  }

  //-------------------------------------------------------------------------
  public void test_of_map() {
    Map<LocalDate, Double> map = new HashMap<>();
    map.put(DATE_2011_01_01, 2d);
    map.put(DATE_2012_01_01, 3d);

    LocalDateDoubleTimeSeries test = LocalDateDoubleTimeSeries.builder().putAll(map).build();
    assertEquals(test.isEmpty(), false);
    assertEquals(test.size(), 2);
    assertEquals(test.containsDate(DATE_2010_01_01), false);
    assertEquals(test.containsDate(DATE_2011_01_01), true);
    assertEquals(test.containsDate(DATE_2012_01_01), true);
    assertEquals(test.get(DATE_2010_01_01), OptionalDouble.empty());
    assertEquals(test.get(DATE_2011_01_01), OptionalDouble.of(2d));
    assertEquals(test.get(DATE_2012_01_01), OptionalDouble.of(3d));
    assertEquals(test.dates().toArray(), new Object[] {DATE_2011_01_01, DATE_2012_01_01});
    assertEquals(test.values().toArray(), new double[] {2d, 3d});
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_of_map_null() {

    LocalDateDoubleTimeSeries.builder().putAll((Map<LocalDate, Double>) null).build();
  }

  public void test_of_map_empty() {

    LocalDateDoubleTimeSeries series = LocalDateDoubleTimeSeries.builder().putAll(ImmutableMap.of()).build();
    assertEquals(series, empty());
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_of_map_dateNull() {
    Map<LocalDate, Double> map = new HashMap<>();
    map.put(DATE_2011_01_01, 2d);
    map.put(null, 3d);

    LocalDateDoubleTimeSeries.builder().putAll(map).build();
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_of_map_valueNull() {
    Map<LocalDate, Double> map = new HashMap<>();
    map.put(DATE_2011_01_01, 2d);
    map.put(DATE_2012_01_01, null);

    LocalDateDoubleTimeSeries.builder().putAll(map).build();
  }

  //-------------------------------------------------------------------------
  public void test_of_collection() {
    Collection<LocalDateDoublePoint> points = Arrays.asList(
        LocalDateDoublePoint.of(DATE_2011_01_01, 2d),
        LocalDateDoublePoint.of(DATE_2012_01_01, 3d));

    LocalDateDoubleTimeSeries test = LocalDateDoubleTimeSeries.builder().putAll(points.stream()).build();
    assertEquals(test.isEmpty(), false);
    assertEquals(test.size(), 2);
    assertEquals(test.containsDate(DATE_2010_01_01), false);
    assertEquals(test.containsDate(DATE_2011_01_01), true);
    assertEquals(test.containsDate(DATE_2012_01_01), true);
    assertEquals(test.get(DATE_2010_01_01), OptionalDouble.empty());
    assertEquals(test.get(DATE_2011_01_01), OptionalDouble.of(2d));
    assertEquals(test.get(DATE_2012_01_01), OptionalDouble.of(3d));
    assertEquals(test.dates().toArray(), new Object[] {DATE_2011_01_01, DATE_2012_01_01});
    assertEquals(test.values().toArray(), new double[] {2d, 3d});
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_of_collection_collectionNull() {

    LocalDateDoubleTimeSeries.builder().putAll((List<LocalDateDoublePoint>) null).build();
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_of_collection_collectionWithNull() {
    Collection<LocalDateDoublePoint> points = Arrays.asList(
        LocalDateDoublePoint.of(DATE_2011_01_01, 2d), null);

    LocalDateDoubleTimeSeries.builder().putAll(points.stream()).build();
  }

  public void test_of_collection_empty() {
    Collection<LocalDateDoublePoint> points = ImmutableList.of();

    assertEquals(LocalDateDoubleTimeSeries.builder().putAll(points.stream()).build(), empty());
  }

  //-------------------------------------------------------------------------
  public void test_immutableViaBeanBuilder() {
    LocalDate startDate = DATE_2010_01_01;
    double[] values = {6, 5, 4};
    BeanBuilder<? extends DenseLocalDateDoubleTimeSeries> builder = DenseLocalDateDoubleTimeSeries.meta().builder();
    builder.set("startDate", startDate);
    builder.set("points", values);
    builder.set("dateCalculation", INCLUDE_WEEKENDS);
    DenseLocalDateDoubleTimeSeries test = builder.build();
    values[0] = -1;

    LocalDateDoublePoint[] points = test.stream().toArray(LocalDateDoublePoint[]::new);
    assertEquals(points[0], LocalDateDoublePoint.of(DATE_2010_01_01, 6d));
    assertEquals(points[1], LocalDateDoublePoint.of(DATE_2010_01_02, 5d));
    assertEquals(points[2], LocalDateDoublePoint.of(DATE_2010_01_03, 4d));
  }

  public void test_immutableValuesViaBeanGet() {

    LocalDateDoubleTimeSeries test =
        LocalDateDoubleTimeSeries.builder().putAll(DATES_2015_1_WEEK, VALUES_1_WEEK).build();
    double[] array = (double[]) ((Bean) test).property("points").get();
    array[0] = -1;
    LocalDateDoublePoint[] points = test.stream().toArray(LocalDateDoublePoint[]::new);
    assertEquals(points[0], LocalDateDoublePoint.of(DATE_2015_01_05, 10d));
    assertEquals(points[1], LocalDateDoublePoint.of(DATE_2015_01_06, 11d));
    assertEquals(points[2], LocalDateDoublePoint.of(DATE_2015_01_07, 12d));
  }

  //-------------------------------------------------------------------------
  public void test_earliestLatest() {

    LocalDateDoubleTimeSeries test = LocalDateDoubleTimeSeries.builder().putAll(DATES_2010_12, VALUES_10_12).build();
    assertEquals(test.getEarliestDate(), DATE_2010_01_01);
    assertEquals(test.getEarliestValue(), 10d, TOLERANCE);
    assertEquals(test.getLatestDate(), DATE_2012_01_01);
    assertEquals(test.getLatestValue(), 12d, TOLERANCE);
  }

  public void test_earliestLatest_whenEmpty() {
    LocalDateDoubleTimeSeries test = empty();
    assertThrows(test::getEarliestDate, NoSuchElementException.class);
    assertThrows(test::getEarliestValue, NoSuchElementException.class);
    assertThrows(test::getLatestDate, NoSuchElementException.class);
    assertThrows(test::getLatestValue, NoSuchElementException.class);
  }

  public void test_earliest_with_subseries() {

    LocalDateDoubleTimeSeries series = LocalDateDoubleTimeSeries.builder()
        .put(DATE_2015_01_03, 3d) // Saturday, so include weekends
        .put(DATE_2015_01_05, 5d)
        .put(DATE_2015_01_06, 6d)
        .put(DATE_2015_01_07, 7d)
        .put(DATE_2015_01_08, 8d)
        .put(DATE_2015_01_09, 9d)
        .put(DATE_2015_01_11, 11d)
        .build();

    LocalDateDoubleTimeSeries subSeries = series.subSeries(DATE_2015_01_04, DATE_2015_01_11);
    assertEquals(subSeries.getEarliestDate(), DATE_2015_01_05);
    assertEquals(subSeries.getEarliestValue(), 5d);
    assertEquals(subSeries.getLatestDate(), DATE_2015_01_09);
    assertEquals(subSeries.getLatestValue(), 9d);
  }

  //-------------------------------------------------------------------------
  @DataProvider(name = "subSeries")
  Object[][] data_subSeries() {
    return new Object[][] {
        // start = end -> empty
        {DATE_2011_01_01, DATE_2011_01_01, new int[] {}},
        // no overlap
        {date(2006, 1, 1), date(2009, 1, 1), new int[] {}},
        // single point
        {DATE_2015_01_06, DATE_2015_01_07, new int[] {1}},
        // include when start matches base, exclude when end matches base
        {DATE_2015_01_06, DATE_2015_01_08, new int[] {1, 2}},
        // include when start matches base
        {DATE_2015_01_05, DATE_2015_01_09, new int[] {0, 1, 2, 3}},
        // neither start nor end match
        {date(2014, 12, 31), date(2015, 2, 1), new int[] {0, 1, 2, 3, 4}},
    };
  }

  @Test(dataProvider = "subSeries")
  public void test_subSeries(LocalDate start, LocalDate end, int[] expected) {

    LocalDateDoubleTimeSeries base =
        LocalDateDoubleTimeSeries.builder()
            .putAll(DATES_2015_1_WEEK, VALUES_1_WEEK)
            .build();

    LocalDateDoubleTimeSeries test = base.subSeries(start, end);

    assertEquals(test.size(), expected.length);
    for (int i = 0; i < DATES_2015_1_WEEK.size(); i++) {
      if (Arrays.binarySearch(expected, i) >= 0) {
        assertEquals(test.get(DATES_2015_1_WEEK.get(i)), OptionalDouble.of(VALUES_1_WEEK.get(i)));
      } else {
        assertEquals(test.get(DATES_2015_1_WEEK.get(i)), OptionalDouble.empty());
      }
    }
  }

  @Test(dataProvider = "subSeries")
  public void test_subSeries_emptySeries(LocalDate start, LocalDate end, int[] expected) {
    LocalDateDoubleTimeSeries test = empty().subSeries(start, end);
    assertEquals(test.size(), 0);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_subSeries_startAfterEnd() {

    LocalDateDoubleTimeSeries base =
        LocalDateDoubleTimeSeries.builder().putAll(DATES_2015_1_WEEK, VALUES_1_WEEK).build();
    base.subSeries(date(2011, 1, 2), DATE_2011_01_01);
  }

  public void test_subSeries_picks_valid_dates() {

    LocalDateDoubleTimeSeries series = LocalDateDoubleTimeSeries.builder()
        .put(DATE_2015_01_02, 10)  // Friday
        .put(DATE_2015_01_05, 11)  // Mon
        .put(DATE_2015_01_06, 12)
        .put(DATE_2015_01_07, 13)
        .put(DATE_2015_01_08, 14)
        .put(DATE_2015_01_09, 15)  // Fri
        .put(DATE_2015_01_12, 16)  // Mon
        .build();

    // Pick using weekend dates
    LocalDateDoubleTimeSeries subSeries = series.subSeries(DATE_2015_01_04, date(2015, 1, 10));

    assertEquals(subSeries.size(), 5);
    assertEquals(subSeries.get(DATE_2015_01_02), OptionalDouble.empty());
    assertEquals(subSeries.get(DATE_2015_01_04), OptionalDouble.empty());
    assertEquals(subSeries.get(DATE_2015_01_05), OptionalDouble.of(11));
    assertEquals(subSeries.get(DATE_2015_01_06), OptionalDouble.of(12));
    assertEquals(subSeries.get(DATE_2015_01_07), OptionalDouble.of(13));
    assertEquals(subSeries.get(DATE_2015_01_08), OptionalDouble.of(14));
    assertEquals(subSeries.get(DATE_2015_01_09), OptionalDouble.of(15));
    assertEquals(subSeries.get(DATE_2015_01_12), OptionalDouble.empty());
  }

  //-------------------------------------------------------------------------
  @DataProvider(name = "headSeries")
  Object[][] data_headSeries() {
    return new Object[][] {
        {0, new int[] {}},
        {1, new int[] {0}},
        {2, new int[] {0, 1}},
        {3, new int[] {0, 1, 2}},
        {4, new int[] {0, 1, 2, 3}},
        {5, new int[] {0, 1, 2, 3, 4}},
        {6, new int[] {0, 1, 2, 3, 4}},
    };
  }

  @Test(dataProvider = "headSeries")
  public void test_headSeries(int count, int[] expected) {

    LocalDateDoubleTimeSeries base =
        LocalDateDoubleTimeSeries.builder().putAll(DATES_2015_1_WEEK, VALUES_1_WEEK).build();
    LocalDateDoubleTimeSeries test = base.headSeries(count);
    assertEquals(test.size(), expected.length);
    for (int i = 0; i < DATES_2015_1_WEEK.size(); i++) {
      if (Arrays.binarySearch(expected, i) >= 0) {
        assertEquals(test.get(DATES_2015_1_WEEK.get(i)), OptionalDouble.of(VALUES_1_WEEK.get(i)));
      } else {
        assertEquals(test.get(DATES_2015_1_WEEK.get(i)), OptionalDouble.empty());
      }
    }
  }

  @Test(dataProvider = "headSeries")
  public void test_headSeries_emptySeries(int count, int[] expected) {
    LocalDateDoubleTimeSeries test = empty().headSeries(count);
    assertEquals(test.size(), 0);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_headSeries_negative() {

    LocalDateDoubleTimeSeries base = LocalDateDoubleTimeSeries.builder().putAll(DATES_2015_1_WEEK, VALUES_1_WEEK).build();
    base.headSeries(-1);
  }

  //-------------------------------------------------------------------------
  @DataProvider(name = "tailSeries")
  Object[][] data_tailSeries() {
    return new Object[][] {
        {0, new int[] {}},
        {1, new int[] {4}},
        {2, new int[] {3, 4}},
        {3, new int[] {2, 3, 4}},
        {4, new int[] {1, 2, 3, 4}},
        {5, new int[] {0, 1, 2, 3, 4}},
        {6, new int[] {0, 1, 2, 3, 4}},
    };
  }

  @Test(dataProvider = "tailSeries")
  public void test_tailSeries(int count, int[] expected) {

    LocalDateDoubleTimeSeries base =
        LocalDateDoubleTimeSeries.builder().putAll(DATES_2015_1_WEEK, VALUES_1_WEEK).build();
    LocalDateDoubleTimeSeries test = base.tailSeries(count);
    assertEquals(test.size(), expected.length);
    for (int i = 0; i < DATES_2015_1_WEEK.size(); i++) {
      if (Arrays.binarySearch(expected, i) >= 0) {
        assertEquals(test.get(DATES_2015_1_WEEK.get(i)), OptionalDouble.of(VALUES_1_WEEK.get(i)));
      } else {
        assertEquals(test.get(DATES_2015_1_WEEK.get(i)), OptionalDouble.empty());
      }
    }
  }

  @Test(dataProvider = "tailSeries")
  public void test_tailSeries_emptySeries(int count, int[] expected) {
    LocalDateDoubleTimeSeries test = empty().tailSeries(count);
    assertEquals(test.size(), 0);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void test_tailSeries_negative() {

    LocalDateDoubleTimeSeries base = LocalDateDoubleTimeSeries.builder().putAll(DATES_2015_1_WEEK, VALUES_1_WEEK).build();
    base.tailSeries(-1);
  }

  //-------------------------------------------------------------------------
  public void test_stream() {

    LocalDateDoubleTimeSeries base =
        LocalDateDoubleTimeSeries.builder().putAll(DATES_2010_12, VALUES_10_12).build();
    Object[] test = base.stream().toArray();
    assertEquals(test[0], LocalDateDoublePoint.of(DATE_2010_01_01, 10));
    assertEquals(test[1], LocalDateDoublePoint.of(DATE_2011_01_01, 11));
    assertEquals(test[2], LocalDateDoublePoint.of(DATE_2012_01_01, 12));
  }

  public void test_stream_withCollector() {

    LocalDateDoubleTimeSeries base =
        LocalDateDoubleTimeSeries.builder().putAll(DATES_2010_12, VALUES_10_12).build();
    LocalDateDoubleTimeSeries test = base.stream()
        .map(point -> point.withValue(1.5d))
        .collect(LocalDateDoubleTimeSeries.collector());
    assertEquals(test.size(), 3);
    assertEquals(test.get(DATE_2010_01_01), OptionalDouble.of(1.5));
    assertEquals(test.get(DATE_2011_01_01), OptionalDouble.of(1.5));
    assertEquals(test.get(DATE_2012_01_01), OptionalDouble.of(1.5));
  }

  //-------------------------------------------------------------------------
  public void test_dateStream() {

    LocalDateDoubleTimeSeries base = LocalDateDoubleTimeSeries.builder().putAll(DATES_2010_12, VALUES_10_12).build();
    LocalDate[] test = base.dates().toArray(LocalDate[]::new);
    assertEquals(test[0], DATE_2010_01_01);
    assertEquals(test[1], DATE_2011_01_01);
    assertEquals(test[2], DATE_2012_01_01);
  }

  public void test_valueStream() {

    LocalDateDoubleTimeSeries base =
        LocalDateDoubleTimeSeries.builder().putAll(DATES_2010_12, VALUES_10_12).build();
    double[] test = base.values().toArray();
    assertEquals(test[0], 10, TOLERANCE);
    assertEquals(test[1], 11, TOLERANCE);
    assertEquals(test[2], 12, TOLERANCE);
  }

  //-------------------------------------------------------------------------
  public void test_forEach() {

    LocalDateDoubleTimeSeries base = LocalDateDoubleTimeSeries.builder().putAll(DATES_2015_1_WEEK, VALUES_1_WEEK).build();
    AtomicInteger counter = new AtomicInteger();
    base.forEach((date, value) -> counter.addAndGet((int) value));
    assertEquals(counter.get(), 10 + 11 + 12 + 13 + 14);
  }

  //-------------------------------------------------------------------------
  public void test_intersection_withNoMatchingElements() {

    LocalDateDoubleTimeSeries series1 =
        LocalDateDoubleTimeSeries.builder().putAll(DATES_2015_1_WEEK, VALUES_1_WEEK).build();

    List<LocalDate> dates2 = dates(DATE_2010_06_01, DATE_2011_06_01, DATE_2012_06_01, DATE_2013_06_01, DATE_2014_06_01);

    LocalDateDoubleTimeSeries series2 =
        LocalDateDoubleTimeSeries.builder().putAll(dates2, VALUES_1_WEEK).build();

    LocalDateDoubleTimeSeries test = series1.intersection(series2, Double::sum);
    assertEquals(test, LocalDateDoubleTimeSeries.empty());
  }

  public void test_intersection_withSomeMatchingElements() {

    LocalDateDoubleTimeSeries series1 =
        LocalDateDoubleTimeSeries.builder().putAll(DATES_2015_1_WEEK, VALUES_1_WEEK).build();

    Map<LocalDate, Double> updates = ImmutableMap.of(
        DATE_2015_01_02, 1.0,
        DATE_2015_01_05, 1.1,
        DATE_2015_01_08, 1.2,
        DATE_2015_01_09, 1.3,
        DATE_2015_01_12, 1.4);

    LocalDateDoubleTimeSeries series2 =
        LocalDateDoubleTimeSeries.builder()
            .putAll(updates)
            .build();

    LocalDateDoubleTimeSeries test = series1.intersection(series2, Double::sum);
    assertEquals(test.size(), 3);
    assertEquals(test.get(DATE_2015_01_05), OptionalDouble.of(11.1));
    assertEquals(test.get(DATE_2015_01_08), OptionalDouble.of(14.2));
    assertEquals(test.get(DATE_2015_01_09), OptionalDouble.of(15.3));
  }

  public void test_intersection_withSomeMatchingElements2() {
    List<LocalDate> dates1 = dates(DATE_2010_01_01, DATE_2011_01_01, DATE_2012_01_01, DATE_2014_01_01, DATE_2015_06_01);
    List<Double> values1 = values(10, 11, 12, 13, 14);

    LocalDateDoubleTimeSeries series1 = LocalDateDoubleTimeSeries.builder().putAll(dates1, values1).build();

    List<LocalDate> dates2 = dates(DATE_2010_01_01, DATE_2011_06_01, DATE_2012_01_01, DATE_2013_01_01, DATE_2014_01_01);
    List<Double> values2 = values(1.0, 1.1, 1.2, 1.3, 1.4);

    LocalDateDoubleTimeSeries series2 = LocalDateDoubleTimeSeries.builder().putAll(dates2, values2).build();

    LocalDateDoubleTimeSeries test = series1.intersection(series2, Double::sum);
    assertEquals(test.size(), 3);
    assertEquals(test.get(DATE_2010_01_01), OptionalDouble.of(11.0));
    assertEquals(test.get(DATE_2012_01_01), OptionalDouble.of(13.2));
    assertEquals(test.get(DATE_2014_01_01), OptionalDouble.of(14.4));
  }

  public void test_intersection_withAllMatchingElements() {
    List<LocalDate> dates1 = DATES_2015_1_WEEK;
    List<Double> values1 = values(10, 11, 12, 13, 14);

    LocalDateDoubleTimeSeries series1 =
        LocalDateDoubleTimeSeries.builder().putAll(dates1, values1).build();
    List<LocalDate> dates2 = DATES_2015_1_WEEK;
    List<Double> values2 = values(1.0, 1.1, 1.2, 1.3, 1.4);

    LocalDateDoubleTimeSeries series2 =
        LocalDateDoubleTimeSeries.builder().putAll(dates2, values2).build();

    LocalDateDoubleTimeSeries combined = series1.intersection(series2, Double::sum);
    assertEquals(combined.size(), 5);
    assertEquals(combined.getEarliestDate(), DATE_2015_01_05);
    assertEquals(combined.getLatestDate(), DATE_2015_01_09);
    assertEquals(combined.get(DATE_2015_01_05), OptionalDouble.of(11.0));
    assertEquals(combined.get(DATE_2015_01_06), OptionalDouble.of(12.1));
    assertEquals(combined.get(DATE_2015_01_07), OptionalDouble.of(13.2));
    assertEquals(combined.get(DATE_2015_01_08), OptionalDouble.of(14.3));
    assertEquals(combined.get(DATE_2015_01_09), OptionalDouble.of(15.4));
  }

  //-------------------------------------------------------------------------
  public void test_union_withMatchingElements() {

    List<LocalDate> dates1 = dates(DATE_2015_01_03, DATE_2015_01_05, DATE_2015_01_06);
    List<LocalDate> dates2 = dates(DATE_2015_01_02, DATE_2015_01_03, DATE_2015_01_05, DATE_2015_01_08);
    LocalDateDoubleTimeSeries series1 =
        LocalDateDoubleTimeSeries.builder().putAll(dates1, VALUES_10_12).build();
    LocalDateDoubleTimeSeries series2 =
        LocalDateDoubleTimeSeries.builder().putAll(dates2, VALUES_4_7).build();

    LocalDateDoubleTimeSeries test = series1.union(series2, Double::sum);
    assertEquals(test.size(), 5);
    assertEquals(test.getEarliestDate(), DATE_2015_01_02);
    assertEquals(test.getLatestDate(), DATE_2015_01_08);
    assertEquals(test.get(DATE_2015_01_02), OptionalDouble.of(4d));
    assertEquals(test.get(DATE_2015_01_03), OptionalDouble.of(10d + 5d));
    assertEquals(test.get(DATE_2015_01_05), OptionalDouble.of(11d + 6d));
    assertEquals(test.get(DATE_2015_01_06), OptionalDouble.of(12d));
    assertEquals(test.get(DATE_2015_01_08), OptionalDouble.of(7d));
  }

  public void test_union_withNoMatchingElements() {

    List<LocalDate> dates1 = dates(DATE_2015_01_03, DATE_2015_01_05, DATE_2015_01_06);
    List<LocalDate> dates2 = dates(DATE_2015_01_02, DATE_2015_01_04, DATE_2015_01_08);
    LocalDateDoubleTimeSeries series1 =
        LocalDateDoubleTimeSeries.builder().putAll(dates1, VALUES_10_12).build();
    LocalDateDoubleTimeSeries series2 =
        LocalDateDoubleTimeSeries.builder().putAll(dates2, VALUES_1_3).build();

    LocalDateDoubleTimeSeries test = series1.union(series2, Double::sum);
    assertEquals(test.size(), 6);
    assertEquals(test.getEarliestDate(), DATE_2015_01_02);
    assertEquals(test.getLatestDate(), DATE_2015_01_08);
    assertEquals(test.get(DATE_2015_01_02), OptionalDouble.of(1d));
    assertEquals(test.get(DATE_2015_01_03), OptionalDouble.of(10d));
    assertEquals(test.get(DATE_2015_01_04), OptionalDouble.of(2d));
    assertEquals(test.get(DATE_2015_01_05), OptionalDouble.of(11d));
    assertEquals(test.get(DATE_2015_01_06), OptionalDouble.of(12d));
    assertEquals(test.get(DATE_2015_01_08), OptionalDouble.of(3d));
  }

  //-------------------------------------------------------------------------
  public void test_mapValues_addConstantToSeries() {

    LocalDateDoubleTimeSeries base = LocalDateDoubleTimeSeries.builder().putAll(DATES_2015_1_WEEK, VALUES_1_WEEK).build();
    LocalDateDoubleTimeSeries test = base.mapValues(d -> d + 5);
    List<Double> expectedValues = values(15, 16, 17, 18, 19);

    assertEquals(test, LocalDateDoubleTimeSeries.builder().putAll(DATES_2015_1_WEEK, expectedValues).build());
  }

  public void test_mapValues_multiplySeries() {

    LocalDateDoubleTimeSeries base =
        LocalDateDoubleTimeSeries.builder().putAll(DATES_2015_1_WEEK, VALUES_1_WEEK).build();

    LocalDateDoubleTimeSeries test = base.mapValues(d -> d * 5);
    List<Double> expectedValues = values(50, 55, 60, 65, 70);

    assertEquals(test, LocalDateDoubleTimeSeries.builder().putAll(DATES_2015_1_WEEK, expectedValues).build());
  }

  public void test_mapValues_invertSeries() {
    List<Double> values = values(1, 2, 4, 5, 8);

    LocalDateDoubleTimeSeries base =
        LocalDateDoubleTimeSeries.builder().putAll(DATES_2015_1_WEEK, values).build();
    LocalDateDoubleTimeSeries test = base.mapValues(d -> 1 / d);
    List<Double> expectedValues = values(1, 0.5, 0.25, 0.2, 0.125);

    assertEquals(test, LocalDateDoubleTimeSeries.builder().putAll(DATES_2015_1_WEEK, expectedValues).build());
  }

  public void test_mapDates() {
    List<Double> values = values(1, 2, 4, 5, 8);
    LocalDateDoubleTimeSeries base = LocalDateDoubleTimeSeries.builder().putAll(DATES_2015_1_WEEK, values).build();
    LocalDateDoubleTimeSeries test = base.mapDates(date -> date.plusYears(1));
    ImmutableList<LocalDate> expectedDates =
        ImmutableList.of(date(2016, 1, 5), date(2016, 1, 6), date(2016, 1, 7), date(2016, 1, 8), date(2016, 1, 9));
    LocalDateDoubleTimeSeries expected = LocalDateDoubleTimeSeries.builder().putAll(expectedDates, values).build();
    assertEquals(test, expected);
  }

  public void test_mapDates_notAscending() {
    List<Double> values = values(1, 2, 4, 5, 8);
    LocalDateDoubleTimeSeries base = LocalDateDoubleTimeSeries.builder().putAll(DATES_2015_1_WEEK, values).build();
    assertThrowsIllegalArg(() -> base.mapDates(date -> date(2016, 1, 6)));
  }

  //-------------------------------------------------------------------------
  public void test_filter_byDate() {
    List<LocalDate> dates = dates(DATE_2010_01_01, DATE_2011_06_01, DATE_2012_01_01, DATE_2013_06_01, DATE_2014_01_01);

    LocalDateDoubleTimeSeries base = LocalDateDoubleTimeSeries.builder().putAll(dates, VALUES_1_WEEK).build();
    LocalDateDoubleTimeSeries test = base.filter((ld, v) -> ld.getMonthValue() != 6);
    assertEquals(test.size(), 3);
    assertEquals(test.get(DATE_2010_01_01), OptionalDouble.of(10d));
    assertEquals(test.get(DATE_2012_01_01), OptionalDouble.of(12d));
    assertEquals(test.get(DATE_2014_01_01), OptionalDouble.of(14d));
  }

  public void test_filter_byValue() {

    LocalDateDoubleTimeSeries base = LocalDateDoubleTimeSeries.builder().putAll(DATES_2015_1_WEEK, VALUES_1_WEEK).build();
    LocalDateDoubleTimeSeries test = base.filter((ld, v) -> v % 2 == 1);
    assertEquals(test.size(), 2);
    assertEquals(test.get(DATE_2015_01_06), OptionalDouble.of(11d));
    assertEquals(test.get(DATE_2015_01_08), OptionalDouble.of(13d));
  }

  public void test_filter_byDateAndValue() {
    List<LocalDate> dates = dates(DATE_2010_01_01, DATE_2011_06_01, DATE_2012_01_01, DATE_2013_06_01, DATE_2014_01_01);

    LocalDateDoubleTimeSeries series = LocalDateDoubleTimeSeries.builder().putAll(dates, VALUES_1_WEEK).build();

    LocalDateDoubleTimeSeries test = series.filter((ld, v) -> ld.getYear() >= 2012 && v % 2 == 0);
    assertEquals(test.size(), 2);
    assertEquals(test.get(DATE_2012_01_01), OptionalDouble.of(12d));
    assertEquals(test.get(DATE_2014_01_01), OptionalDouble.of(14d));
  }

  //-------------------------------------------------------------------------
  public void test_equals_similarSeriesAreEqual() {
    LocalDateDoubleTimeSeries series1 = LocalDateDoubleTimeSeries.of(DATE_2014_01_01, 1d);

    LocalDateDoubleTimeSeries series2 = LocalDateDoubleTimeSeries.builder().putAll(dates(DATE_2014_01_01), values(1d)).build();
    assertEquals(series1.size(), 1);
    assertEquals(series1, series2);
    assertEquals(series1, series1);
    assertEquals(series1.hashCode(), series1.hashCode());
  }

  public void test_equals_notEqual() {
    LocalDateDoubleTimeSeries series1 = LocalDateDoubleTimeSeries.of(DATE_2014_01_01, 1d);
    LocalDateDoubleTimeSeries series2 = LocalDateDoubleTimeSeries.of(DATE_2013_06_01, 1d);
    LocalDateDoubleTimeSeries series3 = LocalDateDoubleTimeSeries.of(DATE_2014_01_01, 3d);
    assertNotEquals(series1, series2);
    assertNotEquals(series1, series3);
  }

  public void test_equals_bad() {
    LocalDateDoubleTimeSeries test = LocalDateDoubleTimeSeries.of(DATE_2014_01_01, 1d);
    assertEquals(test.equals(""), false);
    assertEquals(test.equals(null), false);
  }

  public void checkOffsetsIncludeWeekends() {

    Map<LocalDate, Double> map = ImmutableMap.<LocalDate, Double>builder()
        .put(dt(2014, 12, 26), 14d)
        // Weekend
        .put(dt(2014, 12, 29), 13d)
        .put(dt(2014, 12, 30), 12d)
        .put(dt(2014, 12, 31), 11d)
        // 1st is bank hol so no data
        .put(dt(2015, 1, 2), 11d)
        // Weekend, so no data
        .put(dt(2015, 1, 5), 12d)
        .put(dt(2015, 1, 6), 13d)
        .put(dt(2015, 1, 7), 14d)
        .build();

    LocalDateDoubleTimeSeries ts = LocalDateDoubleTimeSeries.builder().putAll(map).build();
    assertThat(ts.get(dt(2014, 12, 26))).hasValue(14d);
    assertThat(ts.get(dt(2014, 12, 27))).isEmpty();
    assertThat(ts.get(dt(2014, 12, 28))).isEmpty();
    assertThat(ts.get(dt(2014, 12, 29))).hasValue(13d);
    assertThat(ts.get(dt(2014, 12, 30))).hasValue(12d);
    assertThat(ts.get(dt(2014, 12, 31))).hasValue(11d);
    assertThat(ts.get(dt(2015, 1, 1))).isEmpty();
    assertThat(ts.get(dt(2015, 1, 2))).hasValue(11d);
    assertThat(ts.get(dt(2015, 1, 3))).isEmpty();
    assertThat(ts.get(dt(2015, 1, 4))).isEmpty();
    assertThat(ts.get(dt(2015, 1, 5))).hasValue(12d);
    assertThat(ts.get(dt(2015, 1, 6))).hasValue(13d);
    assertThat(ts.get(dt(2015, 1, 7))).hasValue(14d);
  }

  //-------------------------------------------------------------------------
  public void test_coverage() {
    TestHelper.coverImmutableBean(
        (ImmutableBean) DenseLocalDateDoubleTimeSeries.of(DATE_2015_01_05, DATE_2015_01_05,
            Stream.of(LocalDateDoublePoint.of(DATE_2015_01_05, 1d)), SKIP_WEEKENDS));
  }

  //-------------------------------------------------------------------------
  private static LocalDate date(int year, int month, int day) {
    return LocalDate.of(year, month, day);
  }

  private static ImmutableList<LocalDate> dates(LocalDate... dates) {
    return ImmutableList.copyOf(dates);
  }

  private static ImmutableList<Double> values(double... values) {
    return ImmutableList.copyOf(Doubles.asList(values));
  }

  public void checkOffsetsSkipWeekends() {

    Map<LocalDate, Double> map = ImmutableMap.<LocalDate, Double>builder()
        .put(dt(2014, 12, 26), 14d)
        // Weekend
        .put(dt(2014, 12, 29), 13d)
        .put(dt(2014, 12, 30), 12d)
        .put(dt(2014, 12, 31), 11d)
        // bank hol, so no data
        .put(dt(2015, 1, 2), 11d)
        // Weekend, so no data
        .put(dt(2015, 1, 5), 12d)
        .put(dt(2015, 1, 6), 13d)
        .put(dt(2015, 1, 7), 14d)
        .build();

    LocalDateDoubleTimeSeries ts = LocalDateDoubleTimeSeries.builder().putAll(map).build();
    assertThat(ts.get(dt(2014, 12, 26))).hasValue(14d);
    assertThat(ts.get(dt(2014, 12, 29))).hasValue(13d);
    assertThat(ts.get(dt(2014, 12, 30))).hasValue(12d);
    assertThat(ts.get(dt(2014, 12, 31))).hasValue(11d);
    assertThat(ts.get(dt(2015, 1, 1))).isEmpty();
    assertThat(ts.get(dt(2015, 1, 2))).hasValue(11d);
    assertThat(ts.get(dt(2015, 1, 3))).isEmpty();
    assertThat(ts.get(dt(2015, 1, 4))).isEmpty();
    assertThat(ts.get(dt(2015, 1, 5))).hasValue(12d);
    assertThat(ts.get(dt(2015, 1, 6))).hasValue(13d);
    assertThat(ts.get(dt(2015, 1, 7))).hasValue(14d);
  }

  public void underOneWeekNoWeekend() {

    Map<LocalDate, Double> map = ImmutableMap.<LocalDate, Double>builder()
        .put(dt(2015, 1, 5), 12d) // Monday
        .put(dt(2015, 1, 6), 13d)
        .put(dt(2015, 1, 7), 14d)
        .put(dt(2015, 1, 8), 15d)
        .put(dt(2015, 1, 9), 16d) // Friday
        .build();

    LocalDateDoubleTimeSeries ts = LocalDateDoubleTimeSeries.builder().putAll(map).build();
    assertThat(ts.get(dt(2015, 1, 5))).hasValue(12d);
    assertThat(ts.get(dt(2015, 1, 9))).hasValue(16d);
  }

  public void underOneWeekWithWeekend() {

    Map<LocalDate, Double> map = ImmutableMap.<LocalDate, Double>builder()
        .put(dt(2015, 1, 1), 10d) // Thursday
        .put(dt(2015, 1, 2), 11d) // Friday
        .put(dt(2015, 1, 5), 12d)
        .put(dt(2015, 1, 6), 13d)
        .put(dt(2015, 1, 7), 14d)
        .put(dt(2015, 1, 8), 15d) // Thursday
        .put(dt(2015, 1, 9), 16d) // Friday
        .build();

    LocalDateDoubleTimeSeries ts = LocalDateDoubleTimeSeries.builder().putAll(map).build();
    assertThat(ts.get(dt(2015, 1, 1))).hasValue(10d);
    assertThat(ts.get(dt(2015, 1, 2))).hasValue(11d);
    assertThat(ts.get(dt(2015, 1, 5))).hasValue(12d);
    assertThat(ts.get(dt(2015, 1, 6))).hasValue(13d);
    assertThat(ts.get(dt(2015, 1, 7))).hasValue(14d);
    assertThat(ts.get(dt(2015, 1, 8))).hasValue(15d);
    assertThat(ts.get(dt(2015, 1, 9))).hasValue(16d);
  }

  public void roundTrip() {
    Map<LocalDate, Double> in = ImmutableMap.<LocalDate, Double>builder()
        .put(dt(2015, 1, 1), 10d) // Thursday
        .put(dt(2015, 1, 2), 11d) // Friday
        .put(dt(2015, 1, 5), 12d)
        .put(dt(2015, 1, 6), 13d)
        .put(dt(2015, 1, 7), 14d)
        .put(dt(2015, 1, 8), 15d) // Thursday
        .put(dt(2015, 1, 9), 16d) // Friday
        .build();

    LocalDateDoubleTimeSeries ts = LocalDateDoubleTimeSeries.builder().putAll(in).build();

    Map<LocalDate, Double> out = ts.stream()
        .collect(Guavate.toImmutableMap(LocalDateDoublePoint::getDate, LocalDateDoublePoint::getValue));
    assertThat(out).isEqualTo(in);
  }

  public void partitionEmptySeries() {

    Pair<LocalDateDoubleTimeSeries, LocalDateDoubleTimeSeries> partitioned =
        LocalDateDoubleTimeSeries.empty().partition((ld, d) -> ld.getYear() == 2015);

    assertThat(partitioned.getFirst()).isEqualTo(LocalDateDoubleTimeSeries.empty());
    assertThat(partitioned.getSecond()).isEqualTo(LocalDateDoubleTimeSeries.empty());
  }

  public void partitionSeries() {

    Map<LocalDate, Double> in = ImmutableMap.<LocalDate, Double>builder()
        .put(dt(2015, 1, 1), 10d) // Thursday
        .put(dt(2015, 1, 2), 11d) // Friday
        .put(dt(2015, 1, 5), 12d)
        .put(dt(2015, 1, 6), 13d)
        .put(dt(2015, 1, 7), 14d)
        .put(dt(2015, 1, 8), 15d) // Thursday
        .put(dt(2015, 1, 9), 16d) // Friday
        .build();

    LocalDateDoubleTimeSeries ts = LocalDateDoubleTimeSeries.builder().putAll(in).build();

    Pair<LocalDateDoubleTimeSeries, LocalDateDoubleTimeSeries> partitioned =
        ts.partition((ld, d) -> ld.getDayOfMonth() % 2 == 0);

    LocalDateDoubleTimeSeries even = partitioned.getFirst();
    LocalDateDoubleTimeSeries odd = partitioned.getSecond();

    assertThat(even.size()).isEqualTo(3);
    assertThat(even.get(dt(2015, 1, 2))).hasValue(11d);
    assertThat(even.get(dt(2015, 1, 6))).hasValue(13d);
    assertThat(even.get(dt(2015, 1, 8))).hasValue(15d);

    assertThat(odd.size()).isEqualTo(4);
    assertThat(odd.get(dt(2015, 1, 1))).hasValue(10d);
    assertThat(odd.get(dt(2015, 1, 5))).hasValue(12d);
    assertThat(odd.get(dt(2015, 1, 7))).hasValue(14d);
    assertThat(odd.get(dt(2015, 1, 9))).hasValue(16d);
  }

  public void partitionByValueEmptySeries() {

    Pair<LocalDateDoubleTimeSeries, LocalDateDoubleTimeSeries> partitioned =
        LocalDateDoubleTimeSeries.empty().partitionByValue(d -> d > 10);

    assertThat(partitioned.getFirst()).isEqualTo(LocalDateDoubleTimeSeries.empty());
    assertThat(partitioned.getSecond()).isEqualTo(LocalDateDoubleTimeSeries.empty());
  }

  public void partitionByValueSeries() {

    Map<LocalDate, Double> in = ImmutableMap.<LocalDate, Double>builder()
        .put(dt(2015, 1, 1), 10d) // Thursday
        .put(dt(2015, 1, 2), 11d) // Friday
        .put(dt(2015, 1, 5), 12d)
        .put(dt(2015, 1, 6), 13d)
        .put(dt(2015, 1, 7), 14d)
        .put(dt(2015, 1, 8), 15d) // Thursday
        .put(dt(2015, 1, 9), 16d) // Friday
        .build();

    LocalDateDoubleTimeSeries ts = LocalDateDoubleTimeSeries.builder().putAll(in).build();

    Pair<LocalDateDoubleTimeSeries, LocalDateDoubleTimeSeries> partitioned =
        ts.partitionByValue(d -> d < 12 || d > 15);

    LocalDateDoubleTimeSeries extreme = partitioned.getFirst();
    LocalDateDoubleTimeSeries mid = partitioned.getSecond();

    assertThat(extreme.size()).isEqualTo(3);
    assertThat(extreme.get(dt(2015, 1, 1))).hasValue(10d);
    assertThat(extreme.get(dt(2015, 1, 2))).hasValue(11d);
    assertThat(extreme.get(dt(2015, 1, 9))).hasValue(16d);

    assertThat(mid.size()).isEqualTo(4);
    assertThat(mid.get(dt(2015, 1, 5))).hasValue(12d);
    assertThat(mid.get(dt(2015, 1, 6))).hasValue(13d);
    assertThat(mid.get(dt(2015, 1, 7))).hasValue(14d);
    assertThat(mid.get(dt(2015, 1, 8))).hasValue(15d);
  }

  private LocalDate dt(int yr, int mth, int day) {
    return LocalDate.of(yr, mth, day);
  }
}
