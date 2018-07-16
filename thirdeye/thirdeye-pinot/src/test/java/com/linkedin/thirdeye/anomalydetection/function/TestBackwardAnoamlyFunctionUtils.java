package com.linkedin.thirdeye.anomalydetection.function;

import com.linkedin.thirdeye.anomalydetection.context.TimeSeries;
import com.linkedin.thirdeye.api.MetricSchema;
import com.linkedin.thirdeye.api.MetricTimeSeries;
import com.linkedin.thirdeye.api.MetricType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.joda.time.Interval;
import org.testng.annotations.Test;


public class TestBackwardAnoamlyFunctionUtils {
  private static final String TEST_METRIC = "test";
  @Test
  public void testSplitSetsOfTimeSeries() {
    List<String> metricNames = new ArrayList<>();
    metricNames.add(TEST_METRIC);
    MetricSchema metricSchema =
        new MetricSchema(metricNames, Collections.nCopies(metricNames.size(), MetricType.DOUBLE));
    MetricTimeSeries metricTimeSeries = new MetricTimeSeries(metricSchema);
    metricTimeSeries.set(0, TEST_METRIC, 0);
    metricTimeSeries.set(TimeUnit.DAYS.toMillis(28), TEST_METRIC, 1);
    metricTimeSeries.set(TimeUnit.DAYS.toMillis(56), TEST_METRIC, 2);
    metricTimeSeries.set(TimeUnit.DAYS.toMillis(84), TEST_METRIC, 3);
    metricTimeSeries.set(TimeUnit.DAYS.toMillis(112), TEST_METRIC, 4);
    metricTimeSeries.set(TimeUnit.DAYS.toMillis(140), TEST_METRIC, 5);

    List<Interval> intervalList = new ArrayList<>();
    intervalList.add(new Interval(TimeUnit.DAYS.toMillis(140), TimeUnit.DAYS.toMillis(168)));
    intervalList.add(new Interval(0, TimeUnit.DAYS.toMillis(140)));

    List<TimeSeries> timeSeriesList =
        BackwardAnomalyFunctionUtils.splitSetsOfTimeSeries(metricTimeSeries, TEST_METRIC, intervalList);
    assert(timeSeriesList.size() == 2);
    assert(timeSeriesList.get(0).size() == 1);
    assert(timeSeriesList.get(1).size() == 5);

    // assert current time series
    assert(timeSeriesList.get(0).get(TimeUnit.DAYS.toMillis(140)).equals(5.0));

    for (int i = 0; i < 5; i++) {
      assert (timeSeriesList.get(1).get(TimeUnit.DAYS.toMillis(28 * i)).equals(Double.valueOf(i)));
    }
  }
}
