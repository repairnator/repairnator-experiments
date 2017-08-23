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
package org.sonar.server.source.ws;

import com.google.common.io.Resources;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;
import org.sonar.api.web.UserRole;
import org.sonar.db.DbClient;
import org.sonar.db.DbSession;
import org.sonar.db.component.ComponentDto;
import org.sonar.server.component.ComponentFinder;
import org.sonar.server.source.SourceService;
import org.sonar.server.user.UserSession;

public class RawAction implements SourcesWsAction {

  private final DbClient dbClient;
  private final SourceService sourceService;
  private final UserSession userSession;
  private final ComponentFinder componentFinder;

  public RawAction(DbClient dbClient, SourceService sourceService, UserSession userSession, ComponentFinder componentFinder) {
    this.dbClient = dbClient;
    this.sourceService = sourceService;
    this.userSession = userSession;
    this.componentFinder = componentFinder;
  }

  @Override
  public void define(WebService.NewController controller) {
    WebService.NewAction action = controller.createAction("raw")
      .setDescription("Get source code as raw text. Require 'See Source Code' permission on file")
      .setSince("5.0")
      .setResponseExample(Resources.getResource(getClass(), "example-raw.txt"))
      .setHandler(this);

    action
      .createParam("key")
      .setRequired(true)
      .setDescription("File key")
      .setExampleValue("my_project:src/foo/Bar.php");
  }

  @Override
  public void handle(Request request, Response response) {
    String fileKey = request.mandatoryParam("key");

    try (DbSession dbSession = dbClient.openSession(false)) {
      ComponentDto file = componentFinder.getByKey(dbSession, fileKey);
      userSession.checkComponentPermission(UserRole.CODEVIEWER, file);

      Optional<Iterable<String>> lines = sourceService.getLinesAsRawText(dbSession, file.uuid(), 1, Integer.MAX_VALUE);
      response.stream().setMediaType("text/plain");
      if (lines.isPresent()) {
        OutputStream output = response.stream().output();
        for (String line : lines.get()) {
          output.write(line.getBytes(StandardCharsets.UTF_8));
          output.write("\n".getBytes(StandardCharsets.UTF_8));
        }
      }
    } catch (IOException e) {
      throw new IllegalStateException("Fail to write raw source of file " + fileKey, e);
    }
  }
}
