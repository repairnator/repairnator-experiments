/*
 * #%L
 * ACS AEM Commons Bundle
 * %%
 * Copyright (C) 2013 Adobe
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package com.adobe.acs.commons.workflow.synthetic.impl.testprocesses;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadDataWorkflowProcess implements WorkflowProcess {
    private static final Logger log = LoggerFactory.getLogger(ReadDataWorkflowProcess.class);

    public ReadDataWorkflowProcess() {

    }

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {

        // Test Workflow Data Map
        String actual = workItem.getWorkflowData().getMetaDataMap().get("workflowdata", String.class);
        Assert.assertEquals("set on workflowdata", actual);

        actual = workItem.getWorkflowData().getMetaDataMap().get("workflow", String.class);
        Assert.assertEquals("set on workflow", actual);

        actual = workItem.getWorkflowData().getMetaDataMap().get("workitem", String.class);
        Assert.assertNull(actual);


        // Test Workflow Map
        actual = workItem.getWorkflow().getMetaDataMap().get("workflowdata", String.class);
        Assert.assertEquals("set on workflowdata", actual);

        actual = workItem.getWorkflow().getMetaDataMap().get("workflow", String.class);
        Assert.assertEquals("set on workflow", actual);

        actual = workItem.getWorkflow().getMetaDataMap().get("workitem", String.class);
        Assert.assertNull(actual);


        // Test WorkItem
        actual = workItem.getMetaDataMap().get("workflowdata", String.class);
        Assert.assertNull(actual);

        actual = workItem.getMetaDataMap().get("workflow", String.class);
        Assert.assertNull(actual);

        actual = workItem.getMetaDataMap().get("workitem", String.class);
        Assert.assertNull(actual);
    }
}
