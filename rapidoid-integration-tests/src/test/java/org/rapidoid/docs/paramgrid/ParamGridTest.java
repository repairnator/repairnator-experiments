package org.rapidoid.docs.paramgrid;

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

import org.rapidoid.docs.DocTest;
import org.rapidoid.annotation.IntegrationTest;
import org.rapidoid.test.Doc;

@IntegrationTest(main = Main.class)
@Doc(title = "Create key-value data grid from a Map")
public class ParamGridTest extends DocTest {

	@Override
	protected void exercise() {
		GET("/table?name=Rambo&age=50&weapon=gun");
	}

}
