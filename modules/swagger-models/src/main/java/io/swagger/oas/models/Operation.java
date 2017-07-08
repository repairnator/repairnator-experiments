/**
 * Copyright 2017 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.oas.models;

import io.swagger.oas.models.callbacks.Callback;
import io.swagger.oas.models.parameters.Parameter;
import io.swagger.oas.models.parameters.RequestBody;
import io.swagger.oas.models.responses.ApiResponses;
import io.swagger.oas.models.security.SecurityRequirement;
import io.swagger.oas.models.servers.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Operation
 *
 * @see "https://github.com/OAI/OpenAPI-Specification/blob/3.0.0-rc2/versions/3.0.md#operationObject"
 */


public class Operation {
  private List<String> tags = null;
  private String summary = null;
  private String description = null;
  private ExternalDocumentation externalDocs = null;
  private String operationId = null;
  private List<Parameter> parameters = null;
  private RequestBody requestBody = null;
  private ApiResponses responses = null;
  private Map<String, Callback> callbacks = null;
  private Boolean deprecated = null;
  private List<SecurityRequirement> security = null;
  private List<Server> servers = null;
  private java.util.Map<String, Object> extensions = null;

  /**
   * returns the tags property from a Operation instance.
   *
   * @return List&lt;String&gt; tags
   **/

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public Operation tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public Operation addTagsItem(String tagsItem) {
    if(this.tags == null) {
      this.tags = new ArrayList<String>();
    }
    this.tags.add(tagsItem);
    return this;
  }

  /**
   * returns the summary property from a Operation instance.
   *
   * @return String summary
   **/

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public Operation summary(String summary) {
    this.summary = summary;
    return this;
  }

  /**
   * returns the description property from a Operation instance.
   *
   * @return String description
   **/

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Operation description(String description) {
    this.description = description;
    return this;
  }

  /**
   * returns the externalDocs property from a Operation instance.
   *
   * @return ExternalDocumentation externalDocs
   **/

  public ExternalDocumentation getExternalDocs() {
    return externalDocs;
  }

  public void setExternalDocs(ExternalDocumentation externalDocs) {
    this.externalDocs = externalDocs;
  }

  public Operation externalDocs(ExternalDocumentation externalDocs) {
    this.externalDocs = externalDocs;
    return this;
  }

  /**
   * returns the operationId property from a Operation instance.
   *
   * @return String operationId
   **/

  public String getOperationId() {
    return operationId;
  }

  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  public Operation operationId(String operationId) {
    this.operationId = operationId;
    return this;
  }

  /**
   * returns the parameters property from a Operation instance.
   *
   * @return List&lt;Parameter&gt; parameters
   **/

  public List<Parameter> getParameters() {
    return parameters;
  }

  public void setParameters(List<Parameter> parameters) {
    this.parameters = parameters;
  }

  public Operation parameters(List<Parameter> parameters) {
    this.parameters = parameters;
    return this;
  }

  public Operation addParametersItem(Parameter parametersItem) {
    if(this.parameters == null) {
      this.parameters = new ArrayList<Parameter>();
    }
    this.parameters.add(parametersItem);
    return this;
  }

  /**
   * returns the requestBody property from a Operation instance.
   *
   * @return RequestBody requestBody
   **/

  public RequestBody getRequestBody() {
    return requestBody;
  }

  public void setRequestBody(RequestBody requestBody) {
    this.requestBody = requestBody;
  }

  public Operation requestBody(RequestBody requestBody) {
    this.requestBody = requestBody;
    return this;
  }

  /**
   * returns the responses property from a Operation instance.
   *
   * @return ApiResponses responses
   **/

  public ApiResponses getResponses() {
    return responses;
  }

  public void setResponses(ApiResponses responses) {
    this.responses = responses;
  }

  public Operation responses(ApiResponses responses) {
    this.responses = responses;
    return this;
  }

  /**
   * returns the callbacks property from a Operation instance.
   *
   * @return Callbacks callbacks
   **/

  public Map<String, Callback> getCallbacks() {
    return callbacks;
  }

  public void setCallbacks(Map<String, Callback> callbacks) {
    this.callbacks = callbacks;
  }

  public Operation callbacks(Map<String, Callback> callbacks) {
    this.callbacks = callbacks;
    return this;
  }

  /**
   * returns the deprecated property from a Operation instance.
   *
   * @return Boolean deprecated
   **/

  public Boolean getDeprecated() {
    return deprecated;
  }

  public void setDeprecated(Boolean deprecated) {
    this.deprecated = deprecated;
  }

  public Operation deprecated(Boolean deprecated) {
    this.deprecated = deprecated;
    return this;
  }

  /**
   * returns the security property from a Operation instance.
   *
   * @return List&lt;SecurityRequirement&gt; security
   **/

  public List<SecurityRequirement> getSecurity() {
    return security;
  }

  public void setSecurity(List<SecurityRequirement> security) {
    this.security = security;
  }

  public Operation security(List<SecurityRequirement> security) {
    this.security = security;
    return this;
  }

  public Operation addSecurityItem(SecurityRequirement securityItem) {
    if(this.security == null) {
      this.security = new ArrayList<SecurityRequirement>();
    }
    this.security.add(securityItem);
    return this;
  }

  /**
   * returns the servers property from a Operation instance.
   *
   * @return List&lt;Server&gt; servers
   **/

  public List<Server> getServers() {
    return servers;
  }

  public void setServers(List<Server> servers) {
    this.servers = servers;
  }

  public Operation servers(List<Server> servers) {
    this.servers = servers;
    return this;
  }

  public Operation addServersItem(Server serversItem) {
    if(this.servers == null) {
      this.servers = new ArrayList<Server>();
    }
    this.servers.add(serversItem);
    return this;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Operation operation = (Operation) o;
    return Objects.equals(this.tags, operation.tags) &&
        Objects.equals(this.summary, operation.summary) &&
        Objects.equals(this.description, operation.description) &&
        Objects.equals(this.externalDocs, operation.externalDocs) &&
        Objects.equals(this.operationId, operation.operationId) &&
        Objects.equals(this.parameters, operation.parameters) &&
        Objects.equals(this.requestBody, operation.requestBody) &&
        Objects.equals(this.responses, operation.responses) &&
        Objects.equals(this.callbacks, operation.callbacks) &&
        Objects.equals(this.deprecated, operation.deprecated) &&
        Objects.equals(this.security, operation.security) &&
        Objects.equals(this.servers, operation.servers) &&
        Objects.equals(this.extensions, operation.extensions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tags, summary, description, externalDocs, operationId, parameters, requestBody, responses, callbacks, deprecated, security, servers, extensions);
  }


  public java.util.Map<String, Object> getExtensions() {
    return extensions;
  }

  public void addExtension(String name, Object value) {
    if(this.extensions == null) {
      this.extensions = new java.util.HashMap<>();
    }
    this.extensions.put(name, value);
  }

  public void setExtensions(java.util.Map<String, Object> extensions) {
    this.extensions = extensions;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Operation {\n");
    
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    externalDocs: ").append(toIndentedString(externalDocs)).append("\n");
    sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
    sb.append("    parameters: ").append(toIndentedString(parameters)).append("\n");
    sb.append("    requestBody: ").append(toIndentedString(requestBody)).append("\n");
    sb.append("    responses: ").append(toIndentedString(responses)).append("\n");
    sb.append("    callbacks: ").append(toIndentedString(callbacks)).append("\n");
    sb.append("    deprecated: ").append(toIndentedString(deprecated)).append("\n");
    sb.append("    security: ").append(toIndentedString(security)).append("\n");
    sb.append("    servers: ").append(toIndentedString(servers)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
}

