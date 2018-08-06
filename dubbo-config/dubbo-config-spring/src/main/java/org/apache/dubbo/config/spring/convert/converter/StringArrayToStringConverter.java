/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.config.spring.convert.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;


/**
 * String[] to String {@ConditionalGenericConverter}
 *
 * @see StringUtils#arrayToCommaDelimitedString(Object[])
 * @since 2.5.11
 */
public class StringArrayToStringConverter implements Converter<String[], String> {

    @Override
    public String convert(String[] source) {
        return ObjectUtils.isEmpty(source) ? null : StringUtils.arrayToCommaDelimitedString(source);
    }

}
