package org.rapidoid.docs.mytemplatesloader;

/*
 * #%L
 * rapidoid-integration-tests
 * %%
 * Copyright (C) 2014 - 2017 Nikolche Mihajlovski and contributors
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

import org.rapidoid.setup.My;
import org.rapidoid.setup.On;

public class Main {

	public static void main(String[] args) {

		/* Dummy template loader - constructs templates on-the-fly */

		My.templateLoader(filename -> {
			String tmpl = "In " + filename + ": x = <b>${x}</b>";
			return tmpl.getBytes();
		});

		// The URL parameters will be the MVC model

		On.get("/showx").mvc((req) -> req.params());
	}

}
