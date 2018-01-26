/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.runtime.resourcemanager.messages.taskexecutor;

import org.apache.flink.runtime.clusterframework.types.AllocationID;
import org.apache.flink.runtime.clusterframework.types.ResourceID;
import org.apache.flink.runtime.instance.InstanceID;

import java.io.Serializable;

/**
 * Acknowledgment by the TaskExecutor for a SlotRequest from the ResourceManager
 */
public abstract class TMSlotRequestReply implements Serializable {

	private static final long serialVersionUID = 42;

	private final InstanceID instanceID;

	private final ResourceID resourceID;

	private final AllocationID allocationID;

	protected TMSlotRequestReply(InstanceID instanceID, ResourceID resourceID, AllocationID allocationID) {
		this.instanceID = instanceID;
		this.resourceID = resourceID;
		this.allocationID = allocationID;
	}

	public InstanceID getInstanceID() {
		return instanceID;
	}

	public ResourceID getResourceID() {
		return resourceID;
	}

	public AllocationID getAllocationID() {
		return allocationID;
	}

}
