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
package org.sonar.server.computation.task.projectanalysis.step;

import com.google.common.base.Optional;
import javax.annotation.Nullable;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.notifications.Notification;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.server.computation.task.projectanalysis.component.Component;
import org.sonar.server.computation.task.projectanalysis.component.ComponentVisitor;
import org.sonar.server.computation.task.projectanalysis.component.CrawlerDepthLimit;
import org.sonar.server.computation.task.projectanalysis.component.DepthTraversalTypeAwareCrawler;
import org.sonar.server.computation.task.projectanalysis.component.TreeRootHolder;
import org.sonar.server.computation.task.projectanalysis.component.TypeAwareVisitorAdapter;
import org.sonar.server.computation.task.projectanalysis.event.Event;
import org.sonar.server.computation.task.projectanalysis.event.EventRepository;
import org.sonar.server.computation.task.projectanalysis.measure.Measure;
import org.sonar.server.computation.task.projectanalysis.measure.MeasureRepository;
import org.sonar.server.computation.task.projectanalysis.measure.QualityGateStatus;
import org.sonar.server.computation.task.projectanalysis.metric.Metric;
import org.sonar.server.computation.task.projectanalysis.metric.MetricRepository;
import org.sonar.server.computation.task.step.ComputationStep;
import org.sonar.server.notification.NotificationService;

/**
 * This step must be executed after computation of quality gate measure {@link QualityGateMeasuresStep}
 */
public class QualityGateEventsStep implements ComputationStep {
  private static final Logger LOGGER = Loggers.get(QualityGateEventsStep.class);

  private final TreeRootHolder treeRootHolder;
  private final MetricRepository metricRepository;
  private final MeasureRepository measureRepository;
  private final EventRepository eventRepository;
  private final NotificationService notificationService;

  public QualityGateEventsStep(TreeRootHolder treeRootHolder,
    MetricRepository metricRepository, MeasureRepository measureRepository, EventRepository eventRepository,
    NotificationService notificationService) {
    this.treeRootHolder = treeRootHolder;
    this.metricRepository = metricRepository;
    this.measureRepository = measureRepository;
    this.eventRepository = eventRepository;
    this.notificationService = notificationService;
  }

  @Override
  public void execute() {
    new DepthTraversalTypeAwareCrawler(
      new TypeAwareVisitorAdapter(CrawlerDepthLimit.PROJECT, ComponentVisitor.Order.PRE_ORDER) {
        @Override
        public void visitProject(Component project) {
          executeForProject(project);
        }
      }).visit(treeRootHolder.getRoot());
  }

  private void executeForProject(Component project) {
    Metric metric = metricRepository.getByKey(CoreMetrics.ALERT_STATUS_KEY);
    Optional<Measure> rawStatus = measureRepository.getRawMeasure(project, metric);
    if (!rawStatus.isPresent() || !rawStatus.get().hasQualityGateStatus()) {
      return;
    }

    checkQualityGateStatusChange(project, metric, rawStatus.get().getQualityGateStatus());
  }

  private void checkQualityGateStatusChange(Component project, Metric metric, QualityGateStatus rawStatus) {
    Optional<Measure> baseMeasure = measureRepository.getBaseMeasure(project, metric);
    if (!baseMeasure.isPresent()) {
      checkNewQualityGate(project, rawStatus);
      return;
    }

    if (!baseMeasure.get().hasQualityGateStatus()) {
      LOGGER.warn(String.format("Previous alterStatus for project %s is not a supported value. Can not compute Quality Gate event", project.getKey()));
      checkNewQualityGate(project, rawStatus);
      return;
    }
    QualityGateStatus baseStatus = baseMeasure.get().getQualityGateStatus();

    if (baseStatus.getStatus() != rawStatus.getStatus()) {
      // The QualityGate status has changed
      String label = String.format("%s (was %s)", rawStatus.getStatus().getColorName(), baseStatus.getStatus().getColorName());
      createEvent(project, label, rawStatus.getText());
      boolean isNewKo = rawStatus.getStatus() == Measure.Level.OK;
      notifyUsers(project, label, rawStatus, isNewKo);
    }
  }

  private void checkNewQualityGate(Component project, QualityGateStatus rawStatus) {
    if (rawStatus.getStatus() != Measure.Level.OK) {
      // There were no defined alerts before, so this one is a new one
      createEvent(project, rawStatus.getStatus().getColorName(), rawStatus.getText());
      notifyUsers(project, rawStatus.getStatus().getColorName(), rawStatus, true);
    }
  }

  /**
   * @param label "Red (was Orange)"
   * @param rawStatus OK, WARN or ERROR + optional text
   */
  private void notifyUsers(Component project, String label, QualityGateStatus rawStatus, boolean isNewAlert) {
    Notification notification = new Notification("alerts")
      .setDefaultMessage(String.format("Alert on %s: %s", project.getName(), label))
      .setFieldValue("projectName", project.getName())
      .setFieldValue("projectKey", project.getKey())
      .setFieldValue("projectUuid", project.getUuid())
      .setFieldValue("alertName", label)
      .setFieldValue("alertText", rawStatus.getText())
      .setFieldValue("alertLevel", rawStatus.getStatus().toString())
      .setFieldValue("isNewAlert", Boolean.toString(isNewAlert));
    notificationService.deliver(notification);
  }

  private void createEvent(Component project, String name, @Nullable String description) {
    eventRepository.add(project, Event.createAlert(name, null, description));
  }

  @Override
  public String getDescription() {
    return "Generate Quality gate events";
  }
}
