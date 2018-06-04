/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006, 2008 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.opensource.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/




package org.sakaiproject.tool.assessment.services;


/**
 * <p>Isolates exceptions in the common services.</p>
 * <p> </p>
 */
public class CommonServiceException extends RuntimeException
{

  public CommonServiceException()
  {
    super();
  }

  public CommonServiceException(String message)
  {
    super(message);
  }

  public CommonServiceException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public CommonServiceException(Throwable cause)
  {
    super(cause);
  }
}
