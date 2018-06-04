/**********************************************************************************
 * $URL: $
 * $Id: $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2008 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/
package org.sakaiproject.api.app.messageforums;

public interface MessageMoveHistory extends MutableEntity{

    public Long getId();

    public void setId(Long id);

    public Integer getVersion();

    public void setVersion(Integer version);

    public Long getMessageId();

    public void setMessageId(Long messageId);

    public Long getFromTopicId();

    public void setFromTopicId(Long topicId);

    public Long getToTopicId();

    public void setToTopicId(Long topicId);

    public Boolean getReminder();

    public void setReminder(Boolean remind); 
}