/*
 * Copyright 1999-2017 Alibaba Group.
 *
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
 */
package com.alibaba.json.bvt;

import java.util.ArrayList;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JSONParseTest extends TestCase {

    public void test_0() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("scheduleAlarmRules", new ArrayList());
        String jsonString = jsonObject.toJSONString();
        String text = "{\"scheduleAlarmRules\":[]}";
        Object jsonValue = JSON.parse(text);
        System.out.println(jsonValue);
    }

}
