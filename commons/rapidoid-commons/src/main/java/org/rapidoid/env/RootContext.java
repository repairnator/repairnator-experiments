package org.rapidoid.env;

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.log.Log;
import org.rapidoid.u.U;
import org.rapidoid.util.Msc;

import java.io.File;
import java.util.List;

/*
 * #%L
 * rapidoid-commons
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

@Authors("Nikolche Mihajlovski")
@Since("5.4.6")
public class RootContext extends RapidoidThing {

	private static final String APP_ROOT = "/app";
	private static final String PLATFORM_ROOT = "/platform";

	private final String root;

	private RootContext(String root) {
		this.root = root;
	}

	public String root() {
		return root;
	}

	public static RootContext from(String root) {
		String rootDir = null;
		String rootDesc = Msc.isSingleApp() ? "application root" : "platform root";

		if (U.isEmpty(root)) {
			if (Msc.hasAppFolder()) {
				root = APP_ROOT;
			} else if (Msc.isPlatform()) {
				root = PLATFORM_ROOT;
			}
		}

		if (root != null) {
			File dir = new File(root);

			if (dir.exists()) {
				if (dir.isDirectory()) {
					File[] files = dir.listFiles();

					if (files != null) {
						List<File> content = U.list();

						for (File file : files) {
							if (Msc.isAppResource(file.getName())) content.add(file);
						}

						Log.info("Setting " + rootDesc, "!root", root, "items", content.size());
						rootDir = root;

					} else {
						Log.error(U.frmt("Couldn't access the %s!", rootDesc), "!root", root);
					}

				} else {
					Log.error(U.frmt("The configured %s must be a folder!", rootDesc), "!root", root);
				}

			} else {
				Log.error(U.frmt("The configured %s folder doesn't exist!", rootDesc), "!root", root);
			}
		}

		return new RootContext(rootDir);
	}

}
