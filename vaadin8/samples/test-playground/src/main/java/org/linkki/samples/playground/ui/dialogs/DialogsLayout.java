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

package org.linkki.samples.playground.ui.dialogs;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.samples.playground.ui.SidebarSheetDefinition;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class DialogsLayout extends VerticalLayout implements SidebarSheetDefinition {
    private static final long serialVersionUID = 1L;

    public static final String ID = "dialogs-layout";

    public DialogsLayout() {
        super();
        setMargin(true);
        addComponent(new Label(
                "The dialogs are created in a different view to test the behavior of dialogs upon view change."));

        addComponent(VaadinUiCreator.createComponent(new OkCancelDialogPmo(),
                                                     new BindingContext(getClass().getName())));
    }

    @Override
    public String getSidebarSheetName() {
        return ID;
    }

    @Override
    public Resource getSidebarSheetIcon() {
        return VaadinIcons.MODAL;
    }

    @Override
    public String getSidebarSheetId() {
        return ID;
    }
}
