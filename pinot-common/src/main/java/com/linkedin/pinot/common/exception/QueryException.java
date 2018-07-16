/**
 * Copyright (C) 2014-2018 LinkedIn Corp. (pinot-core@linkedin.com)
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
package com.linkedin.pinot.common.exception;

import com.linkedin.pinot.common.response.ProcessingException;
import java.io.PrintWriter;
import java.io.StringWriter;


public class QueryException {
  private QueryException() {
  }

  private static int _maxLinesOfStackTrace = 15;

  // TODO: config max lines of stack trace if necessary. The config should be on instance level.
  public static void setMaxLinesOfStackTrace(int maxLinesOfStackTrace) {
    _maxLinesOfStackTrace = maxLinesOfStackTrace;
  }

  // TODO: several ProcessingExceptions are never used, clean them up.
  public static final int JSON_PARSING_ERROR_CODE = 100;
  public static final int JSON_COMPILATION_ERROR_CODE = 101;
  public static final int PQL_PARSING_ERROR_CODE = 150;
  public static final int SEGMENT_PLAN_EXECUTION_ERROR_CODE = 160;
  public static final int COMBINE_SEGMENT_PLAN_TIMEOUT_ERROR_CODE = 170;
  public static final int ACCESS_DENIED_ERROR_CODE = 180;
  public static final int QUERY_EXECUTION_ERROR_CODE = 200;
  // TODO: Handle these errors in broker
  public static final int SERVER_SHUTTING_DOWN_ERROR_CODE = 210;
  public static final int SERVER_OUT_OF_CAPACITY_ERROR_CODE = 211;
  public static final int QUERY_SCHEDULING_TIMEOUT_ERROR_CODE = 240;
  public static final int EXECUTION_TIMEOUT_ERROR_CODE = 250;
  public static final int BROKER_GATHER_ERROR_CODE = 300;
  public static final int DATA_TABLE_DESERIALIZATION_ERROR_CODE = 310;
  public static final int FUTURE_CALL_ERROR_CODE = 350;
  public static final int BROKER_TIMEOUT_ERROR_CODE = 400;
  public static final int BROKER_RESOURCE_MISSING_ERROR_CODE = 410;
  public static final int BROKER_INSTANCE_MISSING_ERROR_CODE = 420;
  public static final int TOO_MANY_REQUESTS_ERROR_CODE = 429;
  public static final int INTERNAL_ERROR_CODE = 450;
  public static final int MERGE_RESPONSE_ERROR_CODE = 500;
  public static final int FEDERATED_BROKER_UNAVAILABLE_ERROR_CODE = 550;
  public static final int COMBINE_GROUP_BY_EXCEPTION_ERROR_CODE = 600;
  public static final int QUERY_VALIDATION_ERROR_CODE = 700;
  public static final int UNKNOWN_ERROR_CODE = 1000;

  public static final ProcessingException JSON_PARSING_ERROR = new ProcessingException(JSON_PARSING_ERROR_CODE);
  public static final ProcessingException JSON_COMPILATION_ERROR = new ProcessingException(JSON_COMPILATION_ERROR_CODE);
  public static final ProcessingException PQL_PARSING_ERROR = new ProcessingException(PQL_PARSING_ERROR_CODE);
  public static final ProcessingException ACCESS_DENIED_ERROR = new ProcessingException(ACCESS_DENIED_ERROR_CODE);
  public static final ProcessingException SEGMENT_PLAN_EXECUTION_ERROR =
      new ProcessingException(SEGMENT_PLAN_EXECUTION_ERROR_CODE);
  public static final ProcessingException COMBINE_SEGMENT_PLAN_TIMEOUT_ERROR =
      new ProcessingException(COMBINE_SEGMENT_PLAN_TIMEOUT_ERROR_CODE);
  public static final ProcessingException QUERY_EXECUTION_ERROR = new ProcessingException(QUERY_EXECUTION_ERROR_CODE);
  public static final ProcessingException SERVER_SCHEDULER_DOWN_ERROR =
      new ProcessingException(SERVER_SHUTTING_DOWN_ERROR_CODE);
  public static final ProcessingException SERVER_OUT_OF_CAPACITY_ERROR =
      new ProcessingException(SERVER_OUT_OF_CAPACITY_ERROR_CODE);
  public static final ProcessingException QUERY_SCHEDULING_TIMEOUT_ERROR =
      new ProcessingException(QUERY_SCHEDULING_TIMEOUT_ERROR_CODE);
  public static final ProcessingException EXECUTION_TIMEOUT_ERROR =
      new ProcessingException(EXECUTION_TIMEOUT_ERROR_CODE);
  public static final ProcessingException BROKER_GATHER_ERROR = new ProcessingException(BROKER_GATHER_ERROR_CODE);
  public static final ProcessingException DATA_TABLE_DESERIALIZATION_ERROR =
      new ProcessingException(DATA_TABLE_DESERIALIZATION_ERROR_CODE);
  public static final ProcessingException FUTURE_CALL_ERROR = new ProcessingException(FUTURE_CALL_ERROR_CODE);
  public static final ProcessingException BROKER_TIMEOUT_ERROR = new ProcessingException(BROKER_TIMEOUT_ERROR_CODE);
  public static final ProcessingException BROKER_RESOURCE_MISSING_ERROR =
      new ProcessingException(BROKER_RESOURCE_MISSING_ERROR_CODE);
  public static final ProcessingException BROKER_INSTANCE_MISSING_ERROR =
      new ProcessingException(BROKER_INSTANCE_MISSING_ERROR_CODE);
  public static final ProcessingException INTERNAL_ERROR = new ProcessingException(INTERNAL_ERROR_CODE);
  public static final ProcessingException MERGE_RESPONSE_ERROR = new ProcessingException(MERGE_RESPONSE_ERROR_CODE);
  public static final ProcessingException FEDERATED_BROKER_UNAVAILABLE_ERROR =
      new ProcessingException(FEDERATED_BROKER_UNAVAILABLE_ERROR_CODE);
  public static final ProcessingException COMBINE_GROUP_BY_EXCEPTION_ERROR =
      new ProcessingException(COMBINE_GROUP_BY_EXCEPTION_ERROR_CODE);
  public static final ProcessingException QUERY_VALIDATION_ERROR = new ProcessingException(QUERY_VALIDATION_ERROR_CODE);
  public static final ProcessingException UNKNOWN_ERROR = new ProcessingException(UNKNOWN_ERROR_CODE);
  public static final ProcessingException QUOTA_EXCEEDED_ERROR = new ProcessingException(TOO_MANY_REQUESTS_ERROR_CODE);

  static {
    JSON_PARSING_ERROR.setMessage("JsonParsingError");
    JSON_COMPILATION_ERROR.setMessage("JsonCompilationError");
    PQL_PARSING_ERROR.setMessage("PQLParsingError");
    SEGMENT_PLAN_EXECUTION_ERROR.setMessage("SegmentPlanExecutionError");
    COMBINE_SEGMENT_PLAN_TIMEOUT_ERROR.setMessage("CombineSegmentPlanTimeoutError");
    QUERY_EXECUTION_ERROR.setMessage("QueryExecutionError");
    SERVER_SCHEDULER_DOWN_ERROR.setMessage("ServerShuttingDown");
    SERVER_OUT_OF_CAPACITY_ERROR.setMessage("ServerOutOfCapacity");
    QUERY_SCHEDULING_TIMEOUT_ERROR.setMessage("QuerySchedulingTimeoutError");
    EXECUTION_TIMEOUT_ERROR.setMessage("ExecutionTimeoutError");
    BROKER_GATHER_ERROR.setMessage("BrokerGatherError");
    DATA_TABLE_DESERIALIZATION_ERROR.setMessage("DataTableDeserializationError");
    FUTURE_CALL_ERROR.setMessage("FutureCallError");
    BROKER_TIMEOUT_ERROR.setMessage("BrokerTimeoutError");
    BROKER_RESOURCE_MISSING_ERROR.setMessage("BrokerResourceMissingError");
    BROKER_INSTANCE_MISSING_ERROR.setMessage("BrokerInstanceMissingError");
    INTERNAL_ERROR.setMessage("InternalError");
    MERGE_RESPONSE_ERROR.setMessage("MergeResponseError");
    FEDERATED_BROKER_UNAVAILABLE_ERROR.setMessage("FederatedBrokerUnavailableError");
    COMBINE_GROUP_BY_EXCEPTION_ERROR.setMessage("CombineGroupByExceptionError");
    QUERY_VALIDATION_ERROR.setMessage("QueryValidationError");
    UNKNOWN_ERROR.setMessage("UnknownError");
    QUOTA_EXCEEDED_ERROR.setMessage("QuotaExceededError");
  }

  public static ProcessingException getException(ProcessingException processingException, Exception exception) {
    String errorType = processingException.getMessage();
    ProcessingException copiedProcessingException = processingException.deepCopy();
    StringWriter stringWriter = new StringWriter();
    exception.printStackTrace(new PrintWriter(stringWriter));
    String fullStackTrace = stringWriter.toString();
    String[] lines = fullStackTrace.split("\n");
    int numLinesOfStackTrace = Math.min(lines.length, _maxLinesOfStackTrace);
    int lengthOfStackTrace = numLinesOfStackTrace - 1;
    for (int i = 0; i < numLinesOfStackTrace; i++) {
      lengthOfStackTrace += lines[i].length();
    }
    copiedProcessingException.setMessage(errorType + ":\n" + fullStackTrace.substring(0, lengthOfStackTrace));
    return copiedProcessingException;
  }

  public static ProcessingException getException(ProcessingException processingException, String errorMessage) {
    String errorType = processingException.getMessage();
    ProcessingException copiedProcessingException = processingException.deepCopy();
    copiedProcessingException.setMessage(errorType + ":\n" + errorMessage);
    return copiedProcessingException;
  }
}
