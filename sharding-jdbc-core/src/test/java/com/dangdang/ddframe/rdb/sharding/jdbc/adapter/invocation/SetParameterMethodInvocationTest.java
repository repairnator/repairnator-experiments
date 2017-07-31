/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
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
 * </p>
 */

package com.dangdang.ddframe.rdb.sharding.jdbc.adapter.invocation;

import org.junit.Test;

import java.sql.PreparedStatement;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public final class SetParameterMethodInvocationTest {
    
    @Test
    public void assertGetValue() throws NoSuchMethodException, SecurityException {
        SetParameterMethodInvocation actual = new SetParameterMethodInvocation(PreparedStatement.class.getMethod("setInt", int.class, int.class), new Object[] {1, 100}, 100);
        assertThat(actual.getValue(), is((Object) 100));
    }
    
    @Test
    public void assertChangeValueArgument() throws NoSuchMethodException, SecurityException {
        SetParameterMethodInvocation actual = new SetParameterMethodInvocation(PreparedStatement.class.getMethod("setInt", int.class, int.class), new Object[] {1, 100}, 100);
        actual.changeValueArgument(200);
        assertThat(actual.getArguments()[1], is((Object) 200));
    }
}
