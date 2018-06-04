/*
 * This file is part of "SnipSnap Radeox Rendering Engine".
 *
 * Copyright (c) 2002 Stephan J. Schmidt, Matthias L. Jugel
 * All Rights Reserved.
 *
 * Please visit http://radeox.org/ for updates and contact.
 *
 * --LICENSE NOTICE--
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
 * --LICENSE NOTICE--
 */

package org.radeox.engine.context;

import java.util.HashMap;
import java.util.Map;

import org.radeox.api.engine.RenderEngine;
import org.radeox.api.engine.context.RenderContext;

/**
 * Base impementation for RenderContext
 * 
 * @author Stephan J. Schmidt
 * @version $Id: BaseRenderContext.java 7707 2006-04-12 17:30:19Z
 *          ian@caret.cam.ac.uk $
 */

public class BaseRenderContext implements RenderContext
{
	private RenderEngine engine;

	private Map params;

	private Map values;

	public BaseRenderContext()
	{
		values = new HashMap();
	}

	public Object get(String key)
	{
		return values.get(key);
	}

	public void set(String key, Object value)
	{
		values.put(key, value);
	}

	public Map getParameters()
	{
		return params;
	}

	public void setParameters(Map parameters)
	{
		this.params = parameters;
	}

	public RenderEngine getRenderEngine()
	{
		return engine;
	}

	public void setRenderEngine(RenderEngine engine)
	{
		this.engine = engine;
	}

}
