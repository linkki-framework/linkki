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
package org.linkki.samples.f10.confirm;

import java.util.function.Supplier;

import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.annotation.BindReadOnlyBehavior;
import org.linkki.core.ui.aspects.types.ReadOnlyBehaviorType;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.QueryParameters;

@UIVerticalLayout
public class EditSaveButtonPmo {

    private final Handler save;

    private final Supplier<SampleConfirmObject> currentModelObject;

    public EditSaveButtonPmo(Supplier<SampleConfirmObject> currentModelObject, Handler save) {
        this.currentModelObject = currentModelObject;
        this.save = save;
    }

    @ModelObject
    public SampleConfirmObject getModelObject() {
        return currentModelObject.get();
    }

    @UILabel(position = 0)
    public String getConfirmDialogBtnInfo() {
        return "Press edit to activate Browser Confirmation and save do deactivate it.\n" +
                "The state of the text field value is not really persisted in this example.";
    }

    @UITextField(position = 5, label = "Value")
    public void value() {
        // model binding
    }

    @UIButton(position = 10, captionType = CaptionType.STATIC, caption = "Edit",
            icon = VaadinIcon.EDIT, showIcon = true)
    @BindReadOnlyBehavior(ReadOnlyBehaviorType.INVISIBLE_IF_WRITABLE)
    public void edit() {
        UI.getCurrent().navigate(SampleBrowserConfirmationView.ROUTE_SEGMENT + getModelObject().getId(),
                                 QueryParameters.fromString(SampleBrowserConfirmationView.PARAM_EDIT));
    }

    @UIButton(position = 20, captionType = CaptionType.STATIC, caption = "Save",
            icon = VaadinIcon.CHECK_CIRCLE_O, showIcon = true)
    @BindReadOnlyBehavior(ReadOnlyBehaviorType.INVISIBLE)
    public void save() {
        save.apply();
        UI.getCurrent().navigate(SampleBrowserConfirmationView.ROUTE_SEGMENT + getModelObject().getId());
    }

}
