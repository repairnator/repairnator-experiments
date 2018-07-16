/**
 * Copyright (C) 2014-2015 LinkedIn Corp. (pinot-core@linkedin.com)
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
package com.linkedin.pinot.common.response;

import com.linkedin.pinot.common.response.broker.QueryProcessingException;
import java.util.List;
import org.json.JSONObject;


/**
 * Interface for broker response.
 */
public interface BrokerResponse {

  /**
   * Set exceptions caught during request handling, into the broker response.
   */
  void setExceptions(List<ProcessingException> exceptions);

  /**
   * Set the number of servers got queried by the broker.
   *
   * @param numServersQueried number of servers got queried.
   */
  void setNumServersQueried(int numServersQueried);

  /**
   * Set the number of servers responded to the broker.
   *
   * @param numServersResponded number of servers responded.
   */
  void setNumServersResponded(int numServersResponded);

  /**
   * Set the total time used in request handling, into the broker response.
   */
  void setTimeUsedMs(long timeUsedMs);

  /**
   * Convert the broker response to JSONObject.
   */
  JSONObject toJson()
      throws Exception;

  /**
   * Convert the broker response to JSON String.
   */
  String toJsonString()
      throws Exception;

  /**
   * Get number of documents scanned while processing the query.
   */
  long getNumDocsScanned();

  /**
   * Get number of entries scanned in filter phase while processing the query.
   */
  long getNumEntriesScannedInFilter();

  /**
   * Get number of entries scanned post filter phase while processing the query.
   */
  long getNumEntriesScannedPostFilter();

  /**
   * Get total number of documents within the table hit.
   */
  long getTotalDocs();

  /**
   * Get number of exceptions recorded in the response.
   */
  int getExceptionsSize();

  /**
   * Get the list of exceptions
   */
  List<QueryProcessingException> getProcessingExceptions();
}
