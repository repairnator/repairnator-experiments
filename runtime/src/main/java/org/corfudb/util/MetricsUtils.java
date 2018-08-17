package org.corfudb.util;

import com.codahale.metrics.Counter;
import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.Slf4jReporter;
import com.codahale.metrics.Timer;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MetricsUtils {

    private MetricsUtils() {
        // Preventing instantiation of this utility class
    }

    private static final FileDescriptorRatioGauge metricsJVMFdGauge =
            new FileDescriptorRatioGauge();
    private static final MetricSet metricsJVMGC = new GarbageCollectorMetricSet();
    private static final MetricSet metricsJVMMem = new MemoryUsageGaugeSet();
    private static final MetricSet metricsJVMThread = new ThreadStatesGaugeSet();

    // Domain prefix for reporting Corfu metrics
    private static final String CORFU_METRICS = "corfu.metrics";

    // JVM flags used for configuration of collection and reporting of metrics
    private static final String PROPERTY_JVM_METRICS_COLLECTION = "corfu.metrics.jvm";
    private static final String PROPERTY_CSV_INTERVAL = "corfu.metrics.csv.interval";
    private static final String PROPERTY_CSV_FOLDER = "corfu.metrics.csv.folder";
    private static final String PROPERTY_JMX_REPORTING = "corfu.metrics.jmxreporting";
    private static final String PROPERTY_LOG_INTERVAL = "corfu.metrics.log.interval";
    private static final String PROPERTY_METRICS_COLLECTION = "corfu.metrics.collection";

    private static final String ADDRESS_SPACE_METRIC_PREFIX = "corfu.runtime.as-view";
    private static final MetricFilter ADDRESS_SPACE_FILTER =
            (name, metric) -> !name.contains(ADDRESS_SPACE_METRIC_PREFIX);

    private static long metricsCsvInterval;
    private static long metricsLogInterval;
    private static String metricsCsvFolder;
    @Getter
    private static boolean metricsCollectionEnabled = false;
    private static boolean metricsCsvReportingEnabled = false;
    private static boolean metricsJmxReportingEnabled = false;
    private static boolean metricsJvmCollectionEnabled = false;
    private static boolean metricsSlf4jReportingEnabled = false;
    private static String mpTrigger = "filter-trigger"; // internal use only


    /**
     * Load metrics properties.
     *
     * <p>The following properties can be set using jvm flags:
     * <ul>
     * <li> metricsCollectionEnabled: Boolean taken from jvm corfu.metrics.collection
     * property to enable the metrics collection.
     * <li> metricsJmxReportingEnabled: Boolean taken from jvm corfu.metrics.jmxreporting
     * property to enable jmx reporting of metrics.
     * <li> metricsJvmCollectionEnabled: Boolean taken from jvm corfu.metrics.jvm
     * property for enabling reporting on jmv metrics such as garbage collection, threads,
     * and memory consumption.
     * <li> metricsLogAnalysisEnabled: Boolean taken from jvm corfu.metrics.log.analysis
     * property for enabling reporting on logger statistics.
     * <li> metricsLogInterval: Integer taken from jvm corfu.metrics.log.interval
     * property for enabling and setting the intervals of reporting to log output (in
     * seconds). A positive value indicates the reporting is enabled at provided
     * intervals.
     * <li> metricsCsvInterval: Integer taken from jvm corfu.metrics.csv.interval
     * property for enabling and setting the intervals of reporting to csv (in seconds).
     * A positive value indicates the reporting is enabled at provided intervals.
     * <li> metricsCsvFolder: String taken from jvm corfu.metrics.csv.folder
     * property for destination path of csv reporting.
     * </ul>
     *
     * <p>This method will be called to set the value of above-mentioned properties
     * for collection and reporting. For example using the following jvm flags will
     * enable collection of corfu, jvm, and log statistics and their reporting through
     * logs, csv, and jmx.
     *
     * {@code -Dcorfu.metrics.collection=True
     * -Dcorfu.metrics.csv.interval=30
     * -Dcorfu.metrics.csv.folder=/tmp/csv5
     * -Dcorfu.metrics.jmxreporting=True
     * -Dcorfu.metrics.log.analysis=True
     * -Dcorfu.metrics.jvm=True
     * -Dcorfu.metrics.log.interval=60}
     */
    private static void loadVmProperties() {
        metricsCollectionEnabled = Boolean.valueOf(System.getProperty(PROPERTY_METRICS_COLLECTION));

        metricsJmxReportingEnabled = Boolean.valueOf(System.getProperty(PROPERTY_JMX_REPORTING));
        metricsJvmCollectionEnabled = Boolean.valueOf(System.getProperty(PROPERTY_JVM_METRICS_COLLECTION));

        metricsLogInterval = Long.valueOf(System.getProperty(PROPERTY_LOG_INTERVAL, "0"));
        metricsSlf4jReportingEnabled = metricsLogInterval > 0 ? true : false;

        metricsCsvInterval = Long.valueOf(System.getProperty(PROPERTY_CSV_INTERVAL, "0"));
        metricsCsvFolder = String.valueOf(System.getProperty(PROPERTY_CSV_FOLDER));
        metricsCsvReportingEnabled = metricsCsvInterval > 0 ? true : false;
    }

    /**
     * Check if the metricsReportingSetup() function has been called
     * on 'metrics' before now.
     *
     * @param metrics Metric Registry
     * @return True if metricsReportingSetup() function has been called earlier
     */
    public static boolean isMetricsReportingSetUp(@NonNull MetricRegistry metrics) {
        return metrics.getNames().contains(mpTrigger);
    }

    /**
     * Start metrics reporting via the Dropwizard 'CsvReporter', 'JmxReporter',
     * and 'Slf4jReporter'. Reporting can be turned on and off via the system
     * properties described in loadVmProperties()'s docs.
     * The report interval and report directory cannot be altered at runtime.
     *
     * @param metrics Metrics registry
     */
    public static void metricsReportingSetup(@NonNull MetricRegistry metrics) {
        if (isMetricsReportingSetUp(metrics)) return;

        metrics.counter(mpTrigger);

        loadVmProperties();

        if (metricsCollectionEnabled) {
            setupCsvReporting(metrics);
            setupJvmMetrics(metrics);
            setupJmxReporting(metrics);
            setupSlf4jReporting(metrics);
            log.info("Corfu metrics collection and all reporting types are enabled");
        } else {
            log.info("Corfu metrics collection and all reporting types are disabled");
        }
    }

    // If enabled, setup jmx reporting
    private static void setupJmxReporting(MetricRegistry metrics) {
        if (!metricsJmxReportingEnabled) return;

        // This filters noisy addressSpace metrics to have a clean JMX reporting
        JmxReporter jmxReporter = JmxReporter.forRegistry(metrics)
                .convertDurationsTo(TimeUnit.MICROSECONDS)
                .convertRatesTo(TimeUnit.SECONDS)
                .inDomain(CORFU_METRICS)
                .filter(ADDRESS_SPACE_FILTER)
                .build();
        jmxReporter.start();
    }

    // If enabled, setup slf4j reporting
    private static void setupSlf4jReporting(MetricRegistry metrics) {
        if (!metricsSlf4jReportingEnabled) return;

        MetricFilter mpTriggerFilter = (name, metric) -> !name.equals(mpTrigger);

        Slf4jReporter slf4jReporter = Slf4jReporter.forRegistry(metrics)
                .convertDurationsTo(TimeUnit.MICROSECONDS)
                .convertRatesTo(TimeUnit.SECONDS)
                .outputTo(LoggerFactory.getLogger("org.corfudb.metricsdata"))
                .filter(mpTriggerFilter)
                .build();
        slf4jReporter.start(metricsLogInterval, TimeUnit.SECONDS);
    }

    // If enabled, setup csv reporting
    private static void setupCsvReporting(MetricRegistry metrics) {
        if (!metricsCsvReportingEnabled) return;

        final File directory = new File(metricsCsvFolder);

        if (!directory.isDirectory() ||
            !directory.exists() ||
            !directory.canWrite()) {
            log.warn("Provided CSV directory : {} doesn't exist, isn't a directory, or " +
                    "not accessible for writes. Disabling CSV Reporting.", directory);
            metricsCsvReportingEnabled = false;
            return;
        }

        CsvReporter csvReporter = CsvReporter.forRegistry(metrics)
                .convertDurationsTo(TimeUnit.MICROSECONDS)
                .convertRatesTo(TimeUnit.SECONDS)
                .filter(ADDRESS_SPACE_FILTER)
                .build(directory);
        csvReporter.start(metricsCsvInterval, TimeUnit.SECONDS);
    }

    public static void addCacheGauges(@NonNull MetricRegistry metrics,
                                      @NonNull String name,
                                      @NonNull Cache cache) {
        try {
            metrics.register(name + "cache-size", (Gauge<Long>) () -> cache.estimatedSize());
            metrics.register(name + "evictions", (Gauge<Long>) () -> cache.stats().evictionCount());
            metrics.register(name + "hit-rate", (Gauge<Double>) () -> cache.stats().hitRate());
            metrics.register(name + "hits", (Gauge<Long>) () -> cache.stats().hitCount());
            metrics.register(name + "misses", (Gauge<Long>) () -> cache.stats().missCount());
        } catch (IllegalArgumentException e) {
            // Re-registering metrics during test runs, not a problem
        }
    }

    // If enabled, setup reporting of JVM metrics including garbage collection,
    // memory, and thread statistics.
    private static void setupJvmMetrics(@NonNull MetricRegistry metrics) {
        if (!metricsJvmCollectionEnabled) return;

        try {
            metrics.register("jvm.gc", metricsJVMGC);
            metrics.register("jvm.memory", metricsJVMMem);
            metrics.register("jvm.thread", metricsJVMThread);
            metrics.register("jvm.file-descriptors-used", metricsJVMFdGauge);
        } catch (IllegalArgumentException e) {
            // Re-registering metrics during test runs, not a problem
        }
    }

    public static Timer.Context getConditionalContext(@NonNull Timer t) {
        return getConditionalContext(metricsCollectionEnabled, t);
    }

    public static Timer.Context getConditionalContext(boolean enabled, @NonNull Timer t) {
        return enabled ? t.time() : null;
    }

    public static void stopConditionalContext(Timer.Context context) {
        if (context != null) {
            context.stop();
        }
    }

    public static void incConditionalCounter(boolean enabled, @NonNull Counter counter, long amount) {
        if (enabled) {
            counter.inc(amount);
        }
    }
}
