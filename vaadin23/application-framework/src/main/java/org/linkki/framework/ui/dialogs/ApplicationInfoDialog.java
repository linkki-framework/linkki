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
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.framework.ui.LinkkiApplicationTheme;
import org.linkki.framework.ui.application.ApplicationHeader;
import org.linkki.framework.ui.pmo.ApplicationInfoPmo;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;

/**
 * A dialog to present application information to the user.
 * 
 * @implNote Extend {@link ApplicationInfoPmo} and override
 *           {@link ApplicationHeader#createApplicationInfoPmo()} to customize the dialog.
 */
@SuppressWarnings("javadoc")
public class ApplicationInfoDialog extends ConfirmationDialog {

    private static final long serialVersionUID = 1L;

    public ApplicationInfoDialog(ApplicationInfoPmo applicationInfoPmo) {
        super(applicationInfoPmo.getDialogCaption(), Handler.NOP_HANDLER, createContent(applicationInfoPmo));
    }

    private static Component createContent(ApplicationInfoPmo applicationInfoPmo) {
        Component content = VaadinUiCreator.createComponent(applicationInfoPmo, new BindingContext());
        content.getElement().getStyle().set("width", applicationInfoPmo.getDialogWidth());
        content.getElement().getClassList().add(LinkkiApplicationTheme.APPLICATION_INFO_DIALOG);

        return content;
    }

}
