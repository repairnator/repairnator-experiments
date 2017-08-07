package org.rapidoid.setup;

/*
 * #%L
 * rapidoid-http-server
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

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.commons.Arr;
import org.rapidoid.config.ConfigHelp;
import org.rapidoid.env.Env;
import org.rapidoid.u.U;

import java.util.List;

@Authors("Nikolche Mihajlovski")
@Since("5.3.0")
public class PreApp extends RapidoidThing {

	public static void args(String[] args, String... extraArgs) {
		args = Arr.concat(extraArgs, args);

		List<String> oldArgs = Env.args();

		if (U.isEmpty(oldArgs)) {
			ConfigHelp.processHelp(args);

			Env.setArgs(args);

			AppVerification.selfVerify(args);

		} else {
			List<String> newArgs = U.list(args);
			U.must(newArgs.equals(oldArgs), "Can't change the args! Different args were already assigned!\nOLD: %s\nNEW: %s\n", oldArgs, newArgs);
		}
	}

}
