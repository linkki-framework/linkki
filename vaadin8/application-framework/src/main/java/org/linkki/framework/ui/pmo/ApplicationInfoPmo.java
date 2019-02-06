/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.linkki.framework.ui.pmo;

import org.linkki.core.ui.section.annotations.UILabel;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.framework.state.ApplicationConfig;
import org.linkki.framework.ui.application.ApplicationHeader;
import org.linkki.framework.ui.dialogs.ApplicationInfoDialog;
import org.linkki.framework.ui.nls.NlsText;

/**
 * PMO for {@link ApplicationInfoDialog}.
 * 
 * @implSpec Extend this and override
 *           {@link ApplicationHeader#createApplicationInfoPmo(ApplicationConfig applicationConfig)}
 *           to customize the default {@link ApplicationInfoDialog}.
 */
@SuppressWarnings("javadoc")
@UISection
public class ApplicationInfoPmo {

    private ApplicationConfig applicationConfig;

    public ApplicationInfoPmo(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @UILabel(position = 10, htmlContent = true)
    public String getTitle() {
        return "<h2>" + getApplicationConfig().getApplicationName() + "</h2>";
    }

    @UILabel(position = 20, htmlContent = true)
    public String getVersion() {
        return NlsText.getString("ApplicationInfoPmo.Version") + ": "
                + getApplicationConfig().getApplicationVersion();
    }

    @UILabel(position = 30, htmlContent = true)
    public String getDescription() {
        return "<p>" + getApplicationConfig().getApplicationDescription() + "</p>";
    }

    @UILabel(position = 40, htmlContent = true)
    public String getCopyright() {
        return "<p>" + getApplicationConfig().getCopyright() + "</p>";
    }

    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    public String getDialogCaption() {
        return NlsText.getString("ApplicationInfoPmo.About") + " " + getApplicationConfig().getApplicationName();
    }

    /**
     * Defines the width of {@link ApplicationInfoDialog}.
     * 
     * @implSpec Override to change the width of {@link ApplicationInfoDialog}.
     */
    public String getDialogWidth() {
        return "600px";
    }
}
