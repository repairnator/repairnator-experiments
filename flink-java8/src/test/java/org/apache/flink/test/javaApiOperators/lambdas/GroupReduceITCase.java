/**
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

package org.apache.flink.test.javaApiOperators.lambdas;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.test.util.JavaProgramTestBase;

public class GroupReduceITCase extends JavaProgramTestBase {

	private static final String EXPECTED_RESULT = "abad\n" +
			"aaac\n";

	private String resultPath;

	@Override
	protected void preSubmit() throws Exception {
		resultPath = getTempDirPath("result");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void testProgram() throws Exception {
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

		DataSet<Tuple2<Integer,String>> stringDs = env.fromElements(
				new Tuple2<Integer,String>(1, "aa"),
				new Tuple2<Integer,String>(2, "ab"),
				new Tuple2<Integer,String>(1, "ac"),
				new Tuple2<Integer,String>(2, "ad")
				);
		DataSet<String> concatDs = stringDs
				.groupBy(0)
				.reduceGroup((values, out) -> {
					String conc = "";
					for (Tuple2<Integer,String> next : values) {
						conc = conc.concat(next.f1);
					}
					out.collect(conc);
				});
		concatDs.writeAsText(resultPath);
		env.execute();
	}

	@Override
	protected void postSubmit() throws Exception {
		compareResultsByLinesInMemory(EXPECTED_RESULT, resultPath);
	}
}