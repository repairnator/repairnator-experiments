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

package com.dangdang.ddframe.rdb.sharding.merger.orderby;

import com.dangdang.ddframe.rdb.sharding.constant.OrderType;
import com.dangdang.ddframe.rdb.sharding.parsing.parser.context.OrderItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class OrderByValueTest {
    
    @Mock
    private ResultSet resultSet1;
    
    @Mock
    private ResultSet resultSet2;
    
    @Before
    public void setUp() throws SQLException {
        when(resultSet1.next()).thenReturn(true, false);
        when(resultSet2.next()).thenReturn(true, false);
        when(resultSet1.getObject(1)).thenReturn("1");
        when(resultSet1.getObject(2)).thenReturn("2");
    }
    
    @Test
    public void assertCompareToForAsc() throws SQLException {
        OrderByValue orderByValue1 = new OrderByValue(resultSet1, Arrays.asList(new OrderItem(1, OrderType.ASC), new OrderItem(2, OrderType.ASC)), OrderType.ASC);
        assertTrue(orderByValue1.next());
        when(resultSet2.getObject(1)).thenReturn("3");
        when(resultSet2.getObject(2)).thenReturn("4");
        OrderByValue orderByValue2 = new OrderByValue(resultSet2, Arrays.asList(new OrderItem(1, OrderType.ASC), new OrderItem(2, OrderType.ASC)), OrderType.ASC);
        assertTrue(orderByValue2.next());
        assertTrue(orderByValue1.compareTo(orderByValue2) < 0);
        assertFalse(orderByValue1.getResultSet().next());
        assertFalse(orderByValue2.getResultSet().next());
    }
    
    @Test
    public void assertCompareToForDesc() throws SQLException {
        OrderByValue orderByValue1 = new OrderByValue(resultSet1, Arrays.asList(new OrderItem(1, OrderType.DESC), new OrderItem(2, OrderType.DESC)), OrderType.ASC);
        assertTrue(orderByValue1.next());
        when(resultSet2.getObject(1)).thenReturn("3");
        when(resultSet2.getObject(2)).thenReturn("4");
        OrderByValue orderByValue2 = new OrderByValue(resultSet2, Arrays.asList(new OrderItem(1, OrderType.DESC), new OrderItem(2, OrderType.DESC)), OrderType.ASC);
        assertTrue(orderByValue2.next());
        assertTrue(orderByValue1.compareTo(orderByValue2) > 0);
        assertFalse(orderByValue1.getResultSet().next());
        assertFalse(orderByValue2.getResultSet().next());
    }
    
    @Test
    public void assertCompareToWhenEqual() throws SQLException {
        OrderByValue orderByValue1 = new OrderByValue(resultSet1, Arrays.asList(new OrderItem(1, OrderType.ASC), new OrderItem(2, OrderType.DESC)), OrderType.ASC);
        assertTrue(orderByValue1.next());
        when(resultSet2.getObject(1)).thenReturn("1");
        when(resultSet2.getObject(2)).thenReturn("2");
        OrderByValue orderByValue2 = new OrderByValue(resultSet2, Arrays.asList(new OrderItem(1, OrderType.ASC), new OrderItem(2, OrderType.DESC)), OrderType.ASC);
        assertTrue(orderByValue2.next());
        assertThat(orderByValue1.compareTo(orderByValue2), is(0));
        assertFalse(orderByValue1.getResultSet().next());
        assertFalse(orderByValue2.getResultSet().next());
    }
}
