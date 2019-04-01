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

package org.linkki.framework.ui.dialogs;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.section.PmoBasedSectionFactory;
import org.linkki.framework.state.ApplicationConfig;
import org.linkki.framework.ui.LinkkiApplicationStyles;
import org.linkki.framework.ui.application.ApplicationHeader;
import org.linkki.framework.ui.pmo.ApplicationInfoPmo;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.VerticalLayout;

/**
 * A dialog to present application information to the user.
 * 
 * @implNote Extend {@link ApplicationInfoPmo} and override
 *           {@link ApplicationHeader#createApplicationInfoPmo(ApplicationConfig applicationConfig)} to
 *           customize the dialog.
 */
@SuppressWarnings("javadoc")
public class ApplicationInfoDialog extends ConfirmationDialog {

    private static final long serialVersionUID = 1L;

    public ApplicationInfoDialog(ApplicationInfoPmo applicationInfoPmo) {
        super(applicationInfoPmo.getDialogCaption(), Handler.NOP_HANDLER, createContent(applicationInfoPmo));
    }

    private static VerticalLayout createContent(ApplicationInfoPmo applicationInfoPmo) {
        VerticalLayout content = new PmoBasedSectionFactory().createSection(applicationInfoPmo, new BindingContext());
        content.setWidth(applicationInfoPmo.getDialogWidth());
        content.addStyleName(LinkkiApplicationStyles.APPLICATION_INFO_DIALOG);

        return content;
    }

}
