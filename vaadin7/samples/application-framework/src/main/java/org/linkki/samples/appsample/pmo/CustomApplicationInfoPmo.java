/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.samples.appsample.pmo;

import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.uiframework.UiFramework;
import org.linkki.framework.ui.pmo.ApplicationInfoPmo;
import org.linkki.samples.appsample.state.CustomApplicationConfig;

import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;

@UISection
public class CustomApplicationInfoPmo extends ApplicationInfoPmo {

    public CustomApplicationInfoPmo(CustomApplicationConfig applicationConfig) {
        super(applicationConfig);
    }

    @UIButton(position = 100, caption = "License", styleNames = { ValoTheme.BUTTON_LINK })
    public void license() {
        Notification
                .show(((CustomApplicationConfig)getApplicationConfig())
                        .getLicense(UiFramework.getLocale()));
    }

    @Override
    public String getDialogWidth() {
        return "768px";
    }
}
