/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2006, 2007, 2008 The Sakai Foundation
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

package org.sakaibrary.osid.repository.xserver;

/**
 * @author Massachusetts Institute of Techbology, Sakai Software Development Team
 * @version
 */
public class Type
extends org.osid.shared.Type
{

    public Type(String authority
                 , String domain
                 , String keyword
                 , String description)
    {
        super(authority,domain,keyword,description);        
    }

    protected Type(String authority
                 , String domain
                 , String keyword)
    {
        super(authority,domain,keyword);        
    }
}
