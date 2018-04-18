/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.artemis.core.journal.impl;

import java.util.Map;

/**
 * This is an interface used only internally.
 *
 * During a TX.commit, the JournalTransaction needs to get a valid list of records from either the JournalImpl or JournalCompactor.
 *
 * when a commit is read, the JournalTransaction will inquire the JournalCompactor about the existent records
 */
public interface JournalRecordProvider {

   JournalCompactor getCompactor();

   Map<Long, JournalRecord> getRecords();
}
