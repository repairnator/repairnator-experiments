/* 
 * Copyright 2017 Javier A. Ortiz Bultron javier.ortiz.78@gmail.com.
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
package com.validation.manager.core.db;

import static com.validation.manager.core.server.core.VMSettingServer.getSetting;
import static com.validation.manager.core.server.core.VMSettingServer.getSettings;
import com.validation.manager.test.AbstractVMTestCase;
import java.util.ArrayList;
import org.junit.Test;

/**
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public class SettingTest extends AbstractVMTestCase {

    @Test
    @SuppressWarnings({"unchecked"})
    public void testSettings() {
        ArrayList<VmSetting> settings = getSettings();
        assertFalse(settings.isEmpty());
        settings.forEach((setting) -> {
            assertTrue(getSetting(setting.getSetting()) != null);
        });
    }
}
