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
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;
import org.sonar.api.utils.System2;
import org.sonar.core.util.stream.Collectors;
import org.sonar.db.DbClient;
import org.sonar.db.DbSession;
import org.sonar.db.issue.IssueChangeDto;
import org.sonar.db.issue.IssueDto;
import org.sonar.server.exceptions.NotFoundException;
import org.sonar.server.issue.IssueFinder;
import org.sonar.server.user.UserSession;
import org.sonarqube.ws.client.issue.EditCommentRequest;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.String.format;
import static org.sonar.core.util.Uuids.UUID_EXAMPLE_01;
import static org.sonarqube.ws.client.issue.IssuesWsParameters.ACTION_EDIT_COMMENT;
import static org.sonarqube.ws.client.issue.IssuesWsParameters.PARAM_COMMENT;
import static org.sonarqube.ws.client.issue.IssuesWsParameters.PARAM_TEXT;

public class EditCommentAction implements IssuesWsAction {

  private final System2 system2;
  private final UserSession userSession;
  private final DbClient dbClient;
  private final IssueFinder issueFinder;
  private final OperationResponseWriter responseWriter;

  public EditCommentAction(System2 system2, UserSession userSession, DbClient dbClient, IssueFinder issueFinder, OperationResponseWriter responseWriter) {
    this.system2 = system2;
    this.userSession = userSession;
    this.dbClient = dbClient;
    this.issueFinder = issueFinder;
    this.responseWriter = responseWriter;
  }

  @Override
  public void define(WebService.NewController context) {
    WebService.NewAction action = context.createAction(ACTION_EDIT_COMMENT)
      .setDescription("Edit a comment.<br/>" +
        "Requires authentication and the following permission: 'Browse' on the project of the specified issue.<br/>" +
        "Since 6.3, the response contains the issue with all details, not only the edited comment.<br/>" +
        "Since 6.3, 'key' parameter has been renamed %s", PARAM_COMMENT)
      .setSince("3.6")
      .setHandler(this)
      .setResponseExample(Resources.getResource(this.getClass(), "edit_comment-example.json"))
      .setPost(true);

    action.createParam(PARAM_COMMENT)
      .setDescription("Comment key")
      .setDeprecatedKey("key", "6.3")
      .setSince("6.3")
      .setRequired(true)
      .setExampleValue(UUID_EXAMPLE_01);
    action.createParam(PARAM_TEXT)
      .setDescription("Comment text")
      .setRequired(true)
      .setExampleValue("Won't fix because it doesn't apply to the context");
  }

  @Override
  public void handle(Request request, Response response) {
    userSession.checkLoggedIn();
    try (DbSession dbSession = dbClient.openSession(false)) {
      IssueDto issueDto = Stream.of(request)
        .map(toWsRequest())
        .map(loadCommentData(dbSession))
        .peek(updateComment(dbSession))
        .collect(Collectors.toOneElement())
        .getIssueDto();
      responseWriter.write(issueDto.getKey(), request, response);
    }
  }

  private Function<EditCommentRequest, CommentData> loadCommentData(DbSession dbSession) {
    return request -> new CommentData(dbSession, request);
  }

  private Consumer<CommentData> updateComment(DbSession dbSession) {
    return commentData -> {
      commentData.getIssueChangeDto().setUpdatedAt(system2.now());
      commentData.getIssueChangeDto().setChangeData(commentData.getRequest().getText());
      dbClient.issueChangeDao().update(dbSession, commentData.getIssueChangeDto());
      dbSession.commit();
    };
  }

  private static Function<Request, EditCommentRequest> toWsRequest() {
    return request -> {
      EditCommentRequest wsRequest = new EditCommentRequest(request.mandatoryParam(PARAM_COMMENT), request.mandatoryParam(PARAM_TEXT));
      checkArgument(!isNullOrEmpty(wsRequest.getText()), "Cannot set empty comment to an issue");
      return wsRequest;
    };
  }

  private class CommentData {
    private final IssueChangeDto issueChangeDto;
    private final IssueDto issueDto;
    private final EditCommentRequest request;

    CommentData(DbSession dbSession, EditCommentRequest request) {
      this.request = request;
      this.issueChangeDto = dbClient.issueChangeDao().selectCommentByKey(dbSession, request.getComment())
        .orElseThrow(() -> new NotFoundException(format("Comment with key '%s' does not exist", request.getComment())));
      // Load issue now to quickly fail if user hasn't permission to see it
      this.issueDto = issueFinder.getByKey(dbSession, issueChangeDto.getIssueKey());
      checkArgument(Objects.equals(issueChangeDto.getUserLogin(), userSession.getLogin()), "You can only edit your own comments");
    }

    IssueChangeDto getIssueChangeDto() {
      return issueChangeDto;
    }

    IssueDto getIssueDto() {
      return issueDto;
    }

    EditCommentRequest getRequest() {
      return request;
    }
  }

}
