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

import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.framework.ui.application.ApplicationHeader;
import org.linkki.framework.ui.application.ApplicationInfo;
import org.linkki.framework.ui.dialogs.ApplicationInfoDialog;
import org.linkki.framework.ui.nls.NlsText;

/**
 * PMO for {@link ApplicationInfoDialog}.
 * 
 * @implSpec Extend this and override {@link ApplicationHeader#createApplicationInfoPmo()} to
 *           customize the default {@link ApplicationInfoDialog}.
 */
@SuppressWarnings("javadoc")
@UIVerticalLayout
public class ApplicationInfoPmo {

    private final ApplicationInfo applicationInfo;

    public ApplicationInfoPmo(ApplicationInfo applicationInfo) {
        this.applicationInfo = applicationInfo;
    }

    @UILabel(position = 10)
    public String getTitle() {
        return getApplicationInfo().getApplicationName();
    }

    @UILabel(position = 20)
    public String getVersion() {
        return NlsText.getString("ApplicationInfoPmo.Version") + ": "
                + getApplicationInfo().getApplicationVersion();
    }

    @UILabel(position = 30)
    public String getDescription() {
        return getApplicationInfo().getApplicationDescription();
    }

    @UILabel(position = 40)
    public String getCopyright() {
        return getApplicationInfo().getCopyright();
    }

    public ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    public String getDialogCaption() {
        return NlsText.getString("ApplicationInfoPmo.About") + " " + getApplicationInfo().getApplicationName();
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
