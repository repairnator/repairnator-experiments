/*
 * Copyright (C) 2012-2016 DuyHai DOAN
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.archinnov.achilles.internals.sample_classes.config;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import com.datastax.driver.core.TupleValue;
import com.datastax.driver.core.UDTValue;

import info.archinnov.achilles.annotations.FunctionRegistry;

@FunctionRegistry
public interface TestFunctionRegistry {

    int min(int val1, int val2);

    int sum(List<Integer> integers);

    List<String> extractTuple(TupleValue tuple);

    Map<String, ByteBuffer> extractUDT(UDTValue udtValue);
}
