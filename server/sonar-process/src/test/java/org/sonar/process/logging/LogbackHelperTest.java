/*
 * SonarQube
 * Copyright (C) 2009-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.process.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import java.io.File;
import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.sonar.process.MessageException;
import org.sonar.process.ProcessId;
import org.sonar.process.ProcessProperties;
import org.sonar.process.Props;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.slf4j.Logger.ROOT_LOGGER_NAME;

@RunWith(DataProviderRunner.class)
public class LogbackHelperTest {

  @Rule
  public TemporaryFolder temp = new TemporaryFolder();
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private Props props = new Props(new Properties());
  private LogbackHelper underTest = new LogbackHelper();

  @Before
  public void setUp() throws Exception {
    File dir = temp.newFolder();
    props.set(ProcessProperties.PATH_LOGS, dir.getAbsolutePath());
  }

  @After
  public void resetLogback() throws Exception {
    new LogbackHelper().resetFromXml("/org/sonar/process/logging/LogbackHelperTest/logback-test.xml");
  }

  @Test
  public void getRootContext() {
    assertThat(underTest.getRootContext()).isNotNull();
  }

  @Test
  public void enableJulChangePropagation() {
    LoggerContext ctx = underTest.getRootContext();
    int countListeners = ctx.getCopyOfListenerList().size();

    LoggerContextListener propagator = underTest.enableJulChangePropagation(ctx);
    assertThat(ctx.getCopyOfListenerList().size()).isEqualTo(countListeners + 1);

    ctx.removeListener(propagator);
  }

  @Test
  public void newConsoleAppender() {
    LoggerContext ctx = underTest.getRootContext();
    ConsoleAppender<?> appender = underTest.newConsoleAppender(ctx, "MY_APPENDER", "%msg%n");

    assertThat(appender.getName()).isEqualTo("MY_APPENDER");
    assertThat(appender.getContext()).isSameAs(ctx);
    assertThat(appender.isStarted()).isTrue();
    assertThat(((PatternLayoutEncoder) appender.getEncoder()).getPattern()).isEqualTo("%msg%n");
    assertThat(appender.getCopyOfAttachedFiltersList()).isEmpty();
  }

  @Test
  public void createRollingPolicy_defaults() {
    LoggerContext ctx = underTest.getRootContext();
    LogbackHelper.RollingPolicy policy = underTest.createRollingPolicy(ctx, props, "sonar");
    FileAppender appender = policy.createAppender("SONAR_FILE");
    assertThat(appender).isInstanceOf(RollingFileAppender.class);

    // max 5 daily files
    RollingFileAppender fileAppender = (RollingFileAppender) appender;
    TimeBasedRollingPolicy triggeringPolicy = (TimeBasedRollingPolicy) fileAppender.getTriggeringPolicy();
    assertThat(triggeringPolicy.getMaxHistory()).isEqualTo(7);
    assertThat(triggeringPolicy.getFileNamePattern()).endsWith("sonar.%d{yyyy-MM-dd}.log");
  }

  @Test
  public void createRollingPolicy_none() {
    props.set("sonar.log.rollingPolicy", "none");
    LoggerContext ctx = underTest.getRootContext();
    LogbackHelper.RollingPolicy policy = underTest.createRollingPolicy(ctx, props, "sonar");

    Appender appender = policy.createAppender("SONAR_FILE");
    assertThat(appender).isNotInstanceOf(RollingFileAppender.class).isInstanceOf(FileAppender.class);
  }

  @Test
  public void createRollingPolicy_size() {
    props.set("sonar.log.rollingPolicy", "size:1MB");
    props.set("sonar.log.maxFiles", "20");
    LoggerContext ctx = underTest.getRootContext();
    LogbackHelper.RollingPolicy policy = underTest.createRollingPolicy(ctx, props, "sonar");

    Appender appender = policy.createAppender("SONAR_FILE");
    assertThat(appender).isInstanceOf(RollingFileAppender.class);

    // max 20 files of 1Mb
    RollingFileAppender fileAppender = (RollingFileAppender) appender;
    FixedWindowRollingPolicy rollingPolicy = (FixedWindowRollingPolicy) fileAppender.getRollingPolicy();
    assertThat(rollingPolicy.getMaxIndex()).isEqualTo(20);
    assertThat(rollingPolicy.getFileNamePattern()).endsWith("sonar.%i.log");
    SizeBasedTriggeringPolicy triggeringPolicy = (SizeBasedTriggeringPolicy) fileAppender.getTriggeringPolicy();
    assertThat(triggeringPolicy.getMaxFileSize()).isEqualTo("1MB");
  }

  @Test
  public void createRollingPolicy_time() {
    props.set("sonar.log.rollingPolicy", "time:yyyy-MM");
    props.set("sonar.log.maxFiles", "20");

    LoggerContext ctx = underTest.getRootContext();
    LogbackHelper.RollingPolicy policy = underTest.createRollingPolicy(ctx, props, "sonar");

    RollingFileAppender appender = (RollingFileAppender) policy.createAppender("SONAR_FILE");

    // max 5 monthly files
    TimeBasedRollingPolicy triggeringPolicy = (TimeBasedRollingPolicy) appender.getTriggeringPolicy();
    assertThat(triggeringPolicy.getMaxHistory()).isEqualTo(20);
    assertThat(triggeringPolicy.getFileNamePattern()).endsWith("sonar.%d{yyyy-MM}.log");
  }

  @Test
  public void createRollingPolicy_fail_if_unknown_policy() {
    props.set("sonar.log.rollingPolicy", "unknown:foo");
    try {
      LoggerContext ctx = underTest.getRootContext();
      underTest.createRollingPolicy(ctx, props, "sonar");
      fail();
    } catch (MessageException e) {
      assertThat(e).hasMessage("Unsupported value for property sonar.log.rollingPolicy: unknown:foo");
    }
  }

  @Test
  public void apply_fails_with_IAE_if_global_property_has_unsupported_level() {
    LogLevelConfig config = LogLevelConfig.newBuilder().rootLevelFor(ProcessId.WEB_SERVER).build();

    props.set("sonar.log.level", "ERROR");

    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("log level ERROR in property sonar.log.level is not a supported value (allowed levels are [TRACE, DEBUG, INFO])");

    underTest.apply(config, props);
  }

  @Test
  public void apply_fails_with_IAE_if_process_property_has_unsupported_level() {
    LogLevelConfig config = LogLevelConfig.newBuilder().rootLevelFor(ProcessId.WEB_SERVER).build();

    props.set("sonar.log.level.web", "ERROR");

    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("log level ERROR in property sonar.log.level.web is not a supported value (allowed levels are [TRACE, DEBUG, INFO])");

    underTest.apply(config, props);
  }

  @Test
  public void apply_sets_logger_to_INFO_if_no_property_is_set() {
    LogLevelConfig config = LogLevelConfig.newBuilder().rootLevelFor(ProcessId.WEB_SERVER).build();

    LoggerContext context = underTest.apply(config, props);

    assertThat(context.getLogger(ROOT_LOGGER_NAME).getLevel()).isEqualTo(Level.INFO);
  }

  @Test
  public void apply_sets_logger_to_globlal_property_if_set() {
    LogLevelConfig config = LogLevelConfig.newBuilder().rootLevelFor(ProcessId.WEB_SERVER).build();

    props.set("sonar.log.level", "TRACE");

    LoggerContext context = underTest.apply(config, props);

    assertThat(context.getLogger(ROOT_LOGGER_NAME).getLevel()).isEqualTo(Level.TRACE);
  }

  @Test
  public void apply_sets_logger_to_process_property_if_set() {
    LogLevelConfig config = LogLevelConfig.newBuilder().rootLevelFor(ProcessId.WEB_SERVER).build();

    props.set("sonar.log.level.web", "DEBUG");

    LoggerContext context = underTest.apply(config, props);

    assertThat(context.getLogger(ROOT_LOGGER_NAME).getLevel()).isEqualTo(Level.DEBUG);
  }

  @Test
  public void apply_sets_logger_to_process_property_over_global_property_if_both_set() {
    LogLevelConfig config = LogLevelConfig.newBuilder().rootLevelFor(ProcessId.WEB_SERVER).build();
    props.set("sonar.log.level", "DEBUG");
    props.set("sonar.log.level.web", "TRACE");

    LoggerContext context = underTest.apply(config, props);

    assertThat(context.getLogger(ROOT_LOGGER_NAME).getLevel()).isEqualTo(Level.TRACE);
  }

  @Test
  public void apply_sets_domain_property_over_process_and_global_property_if_all_set() {
    LogLevelConfig config = LogLevelConfig.newBuilder().levelByDomain("foo", ProcessId.WEB_SERVER, LogDomain.ES).build();
    props.set("sonar.log.level", "DEBUG");
    props.set("sonar.log.level.web", "DEBUG");
    props.set("sonar.log.level.web.es", "TRACE");

    LoggerContext context = underTest.apply(config, props);

    assertThat(context.getLogger("foo").getLevel()).isEqualTo(Level.TRACE);
  }

  @Test
  public void apply_sets_domain_property_over_process_property_if_both_set() {
    LogLevelConfig config = LogLevelConfig.newBuilder().levelByDomain("foo", ProcessId.WEB_SERVER, LogDomain.ES).build();
    props.set("sonar.log.level.web", "DEBUG");
    props.set("sonar.log.level.web.es", "TRACE");

    LoggerContext context = underTest.apply(config, props);

    assertThat(context.getLogger("foo").getLevel()).isEqualTo(Level.TRACE);
  }

  @Test
  public void apply_sets_domain_property_over_global_property_if_both_set() {
    LogLevelConfig config = LogLevelConfig.newBuilder().levelByDomain("foo", ProcessId.WEB_SERVER, LogDomain.ES).build();
    props.set("sonar.log.level", "DEBUG");
    props.set("sonar.log.level.web.es", "TRACE");

    LoggerContext context = underTest.apply(config, props);

    assertThat(context.getLogger("foo").getLevel()).isEqualTo(Level.TRACE);
  }

  @Test
  public void apply_fails_with_IAE_if_domain_property_has_unsupported_level() {
    LogLevelConfig config = LogLevelConfig.newBuilder().levelByDomain("foo", ProcessId.WEB_SERVER, LogDomain.JMX).build();

    props.set("sonar.log.level.web.jmx", "ERROR");

    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage("log level ERROR in property sonar.log.level.web.jmx is not a supported value (allowed levels are [TRACE, DEBUG, INFO])");

    underTest.apply(config, props);
  }

  @Test
  @UseDataProvider("logbackLevels")
  public void apply_accepts_any_level_as_hardcoded_level(Level level) {
    LogLevelConfig config = LogLevelConfig.newBuilder().immutableLevel("bar", level).build();

    LoggerContext context = underTest.apply(config, props);

    assertThat(context.getLogger("bar").getLevel()).isEqualTo(level);
  }

  @Test
  public void changeRoot_sets_level_of_ROOT_and_all_loggers_with_a_config_but_the_hardcoded_one() {
    LogLevelConfig config = LogLevelConfig.newBuilder()
        .rootLevelFor(ProcessId.WEB_SERVER)
        .levelByDomain("foo", ProcessId.WEB_SERVER, LogDomain.JMX)
        .levelByDomain("bar", ProcessId.COMPUTE_ENGINE, LogDomain.ES)
        .immutableLevel("doh", Level.ERROR)
        .immutableLevel("pif", Level.TRACE)
        .build();
    LoggerContext context = underTest.apply(config, props);
    assertThat(context.getLogger(ROOT_LOGGER_NAME).getLevel()).isEqualTo(Level.INFO);
    assertThat(context.getLogger("foo").getLevel()).isEqualTo(Level.INFO);
    assertThat(context.getLogger("bar").getLevel()).isEqualTo(Level.INFO);
    assertThat(context.getLogger("doh").getLevel()).isEqualTo(Level.ERROR);
    assertThat(context.getLogger("pif").getLevel()).isEqualTo(Level.TRACE);

    underTest.changeRoot(config, Level.DEBUG);

    assertThat(context.getLogger(ROOT_LOGGER_NAME).getLevel()).isEqualTo(Level.DEBUG);
    assertThat(context.getLogger("foo").getLevel()).isEqualTo(Level.DEBUG);
    assertThat(context.getLogger("bar").getLevel()).isEqualTo(Level.DEBUG);
    assertThat(context.getLogger("doh").getLevel()).isEqualTo(Level.ERROR);
    assertThat(context.getLogger("pif").getLevel()).isEqualTo(Level.TRACE);
  }

  @Test
  public void apply_set_level_to_OFF_if_sonar_global_level_is_not_set() {
    LoggerContext context = underTest.apply(LogLevelConfig.newBuilder().offUnlessTrace("fii").build(), new Props(new Properties()));

    assertThat(context.getLogger("fii").getLevel()).isEqualTo(Level.OFF);
  }

  @Test
  public void apply_set_level_to_OFF_if_sonar_global_level_is_INFO() {
    setLevelToOff(Level.INFO);
  }

  @Test
  public void apply_set_level_to_OFF_if_sonar_global_level_is_DEBUG() {
    setLevelToOff(Level.DEBUG);
  }

  @Test
  public void apply_does_not_set_level_if_sonar_global_level_is_TRACE() {
    Properties properties = new Properties();
    properties.setProperty("sonar.log.level", Level.TRACE.toString());
    assertThat(underTest.getRootContext().getLogger("fii").getLevel()).isNull();

    LoggerContext context = underTest.apply(LogLevelConfig.newBuilder().offUnlessTrace("fii").build(), new Props(properties));

    assertThat(context.getLogger("fii").getLevel()).isNull();
  }

  private void setLevelToOff(Level globalLogLevel) {
    Properties properties = new Properties();
    properties.setProperty("sonar.log.level", globalLogLevel.toString());

    LoggerContext context = underTest.apply(LogLevelConfig.newBuilder().offUnlessTrace("fii").build(), new Props(properties));

    assertThat(context.getLogger("fii").getLevel()).isEqualTo(Level.OFF);
  }

  @DataProvider
  public static Object[][] logbackLevels() {
    return new Object[][] {
      {Level.OFF},
      {Level.ERROR},
      {Level.WARN},
      {Level.INFO},
      {Level.DEBUG},
      {Level.TRACE},
      {Level.ALL}
    };
  }
}
