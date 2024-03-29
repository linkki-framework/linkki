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

package org.linkki.samples.playground.dialogs;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.framework.ui.dialogs.ConfirmationDialog;
import org.linkki.samples.playground.dialogs.VerticalLayoutContentDialog.VerticalLayoutContentDialogPmo;
import org.linkki.samples.playground.ts.dialogs.OkCancelDialogMessagePmo;
import org.linkki.samples.playground.ui.PlaygroundAppLayout;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = DialogsView.NAME, layout = PlaygroundAppLayout.class)
@PageTitle("linkki Sample :: Dialogs")
public class DialogsView extends VerticalLayout {

    public static final String NAME = "dialogs";

    private static final long serialVersionUID = 1L;

    public DialogsView() {
        setPadding(true);

        add(new Span(
                "The dialogs are created in a different view to test the behavior of dialogs upon view change."));

        add(VaadinUiCreator.createComponent(new SimpleDialogPmo(), new BindingContext(getClass().getName())));
        add(VaadinUiCreator.createComponent(new ValidationConfirmationDialog.ButtonSectionPmo(),
                                            new BindingContext(OkCancelDialogMessagePmo.class.getName())));
        add(VaadinUiCreator.createComponent(new VerticalLayoutContentDialog.VerticalLayoutContentDialogPmo(),
                                            new BindingContext(VerticalLayoutContentDialogPmo.class.getName())));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        ConfirmationDialog.open("Entering dialog view", "Welcome!");
    }

}
