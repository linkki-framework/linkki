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

import org.linkki.framework.ui.component.sidebar.SidebarLayout;
import org.linkki.framework.ui.dialogs.ConfirmationDialog;
import org.linkki.samples.playground.ui.SidebarSheetDefinition;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class DialogView extends SidebarLayout implements View {

    public static final String NAME = "dialog";

    private static final long serialVersionUID = 1L;

    public DialogView() {
        addSidebarSheet(new DialogsLayout());
    }

    private void addSidebarSheet(SidebarSheetDefinition sidebarSheetDef) {
        addSheet(sidebarSheetDef.createSheet());
    }

    @Override
    public void enter(ViewChangeEvent event) {
        ConfirmationDialog.open("Entering dialog view", "Welcome!").setSize("300px", "250px");
    }

}