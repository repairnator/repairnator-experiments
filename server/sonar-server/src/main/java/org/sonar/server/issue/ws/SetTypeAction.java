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
package org.sonar.server.issue.ws;

import com.google.common.io.Resources;
import java.util.Date;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;
import org.sonar.core.issue.DefaultIssue;
import org.sonar.core.issue.IssueChangeContext;
import org.sonar.core.util.Uuids;
import org.sonar.db.DbClient;
import org.sonar.db.DbSession;
import org.sonar.server.issue.IssueFieldsSetter;
import org.sonar.server.issue.IssueFinder;
import org.sonar.server.issue.IssueUpdater;
import org.sonar.server.user.UserSession;

import static org.sonar.api.web.UserRole.ISSUE_ADMIN;
import static org.sonarqube.ws.client.issue.IssuesWsParameters.ACTION_SET_TYPE;
import static org.sonarqube.ws.client.issue.IssuesWsParameters.PARAM_ISSUE;
import static org.sonarqube.ws.client.issue.IssuesWsParameters.PARAM_TYPE;

public class SetTypeAction implements IssuesWsAction {

  private final UserSession userSession;
  private final DbClient dbClient;
  private final IssueFinder issueFinder;
  private final IssueFieldsSetter issueFieldsSetter;
  private final IssueUpdater issueUpdater;
  private final OperationResponseWriter responseWriter;

  public SetTypeAction(UserSession userSession, DbClient dbClient, IssueFinder issueFinder, IssueFieldsSetter issueFieldsSetter, IssueUpdater issueUpdater,
    OperationResponseWriter responseWriter) {
    this.userSession = userSession;
    this.dbClient = dbClient;
    this.issueFinder = issueFinder;
    this.issueFieldsSetter = issueFieldsSetter;
    this.issueUpdater = issueUpdater;
    this.responseWriter = responseWriter;
  }

  @Override
  public void define(WebService.NewController controller) {
    WebService.NewAction action = controller.createAction(ACTION_SET_TYPE)
      .setDescription("Change type of issue, for instance from 'code smell' to 'bug'.<br/>" +
        "Requires the following permissions:" +
        "<ul>" +
        "  <li>'Authentication'</li>" +
        "  <li>'Browse' rights on project of the specified issue</li>" +
        "  <li>'Administer Issues' rights on project of the specified issue</li>" +
        "</ul>")
      .setSince("5.5")
      .setHandler(this)
      .setResponseExample(Resources.getResource(this.getClass(), "set_type-example.json"))
      .setPost(true);

    action.createParam(PARAM_ISSUE)
      .setDescription("Issue key")
      .setRequired(true)
      .setExampleValue(Uuids.UUID_EXAMPLE_01);
    action.createParam(PARAM_TYPE)
      .setDescription("New type")
      .setRequired(true)
      .setPossibleValues(RuleType.names());
  }

  @Override
  public void handle(Request request, Response response) throws Exception {
    userSession.checkLoggedIn();
    String issueKey = request.mandatoryParam(PARAM_ISSUE);
    RuleType ruleType = RuleType.valueOf(request.mandatoryParam(PARAM_TYPE));
    try (DbSession session = dbClient.openSession(false)) {
      setType(session, issueKey, ruleType);
    }
    responseWriter.write(issueKey, request, response);
  }

  private void setType(DbSession session, String issueKey, RuleType ruleType) {
    DefaultIssue issue = issueFinder.getByKey(session, issueKey).toDefaultIssue();
    userSession.checkComponentUuidPermission(ISSUE_ADMIN, issue.projectUuid());

    IssueChangeContext context = IssueChangeContext.createUser(new Date(), userSession.getLogin());
    if (issueFieldsSetter.setType(issue, ruleType, context)) {
      issueUpdater.saveIssue(session, issue, context, null);
    }
  }

}
