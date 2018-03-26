/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.unifiedpush.rest.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jboss.aerogear.unifiedpush.rest.util.CommonUtils.isAscendingOrder;

public class CommonUtilsTest {

    @Test
    public void verifyAscendingOrderFromNullValue() {
        assertThat(isAscendingOrder(null)).isTrue();
    }

    @Test
    public void verfiyAscendingOrderFromDescValue() {
        assertThat(isAscendingOrder("deSc")).isFalse();
        assertThat(isAscendingOrder("desc")).isFalse();
        assertThat(isAscendingOrder("DESC")).isFalse();
    }

    @Test
    public void verifyAscendingOrderFromAscValue() {
        assertThat(isAscendingOrder("foo")).isTrue();
        assertThat(isAscendingOrder("AsC")).isTrue();
    }

}