/**
 * Copyright (C) 2014-2016 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linkedin.pinot.controller.api.resources;

import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class LandingPageHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(LandingPageHandler.class);

  // We configure this static resource as jersey handler because all our APIs are at "/"
  // So, the framework does not serve base index.html page correctly. See ControllerAdminApiApplication
  // for more details.
  @GET
  @Produces(MediaType.TEXT_HTML)
  public InputStream getIndexPage() {
    return getClass().getClassLoader().getResourceAsStream("static/index.html");
  }
}
