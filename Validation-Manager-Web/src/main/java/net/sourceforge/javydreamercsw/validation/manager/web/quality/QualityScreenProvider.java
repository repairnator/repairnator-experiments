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
package net.sourceforge.javydreamercsw.validation.manager.web.quality;

import com.vaadin.ui.Notification;
import com.validation.manager.core.DataBaseManager;
import com.validation.manager.core.IMainContentProvider;
import com.validation.manager.core.api.internationalization.InternationalizationProvider;
import com.validation.manager.core.db.ExecutionStep;
import com.validation.manager.core.db.VmSetting;
import com.validation.manager.core.db.controller.ExecutionStepJpaController;
import com.validation.manager.core.server.core.VMSettingServer;
import net.sourceforge.javydreamercsw.validation.manager.web.ValidationManagerUI;
import net.sourceforge.javydreamercsw.validation.manager.web.tester.ExecutionScreen;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
@ServiceProvider(service = IMainContentProvider.class, position = 2)
public class QualityScreenProvider extends ExecutionScreen {

    @Override
    public String getComponentCaption() {
        return "quality.tab.name";
    }

    @Override
    public boolean shouldDisplay() {
        VmSetting setting = VMSettingServer.getSetting("quality.review");
        return ValidationManagerUI.getInstance().getUser() != null
                && ValidationManagerUI.getInstance()
                        .checkRight("quality.assurance")
                && setting != null
                && setting.getBoolVal();
    }

    @Override
    public void processNotification() {
        if (shouldDisplay()) {
            ExecutionStepJpaController c
                    = new ExecutionStepJpaController(DataBaseManager
                            .getEntityManagerFactory());
            for (ExecutionStep es : c.findExecutionStepEntities()) {
                if (es.getLocked() && !es.getReviewed()) {
                    //It has been assigned but not started
                    Notification.show(Lookup.getDefault().lookup(InternationalizationProvider.class)
                            .translate("quality.review.pending.title"),
                            Lookup.getDefault().lookup(InternationalizationProvider.class)
                                    .translate("quality.review.pending.message"),
                            Notification.Type.TRAY_NOTIFICATION);
                    break;
                }
            }
        }
    }
}
