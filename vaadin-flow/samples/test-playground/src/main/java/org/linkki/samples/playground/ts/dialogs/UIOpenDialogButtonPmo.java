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

package org.linkki.samples.playground.ts.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.faktorips.runtime.ValidationContext;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.aspects.annotation.BindVariantNames;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITableComponent;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.uiframework.UiFramework;
import org.linkki.framework.ui.dialogs.DialogPmo;
import org.linkki.framework.ui.dialogs.PmoBasedDialogFactory;
import org.linkki.framework.ui.dialogs.UIOpenDialogButton;
import org.linkki.ips.binding.dispatcher.IpsPropertyDispatcherFactory;
import org.linkki.ips.messages.MessageConverter;
import org.linkki.samples.playground.ips.model.IpsModelObject;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.icon.VaadinIcon;

@UISection
public class UIOpenDialogButtonPmo {

    private final List<IpsModelObject> ipsModelObjects = new ArrayList<>();

    @SectionHeader
    @BindIcon(VaadinIcon.REFRESH)
    @UIButton(position = 0, caption = "Manually trigger model changed")
    public void refresh() {
        // does nothing, but would trigger modelChanged
    }

    @SectionHeader
    @BindVariantNames("primary")
    // tag::openDialogWithUIButton[]
    @UIButton(position = 10, caption = "Open dialog with UIButton")
    public void add() {
        var contentPmo = new NewModelObjectPmo();
        new PmoBasedDialogFactory(() -> MessageConverter
                .convert(contentPmo.getModelObject().validate(new ValidationContext(UiFramework.getLocale()))))
                        .openOkCancelDialog("New model object",
                                            () -> saveNewModelObject(contentPmo.getModelObject()),
                                            contentPmo);
    }
    // end::openDialogWithUIButton[]

    @SectionHeader
    @BindVariantNames("primary")
    // tag::openDialogPmoWithUIButton[]
    @UIButton(position = 20, caption = "Open dialog with UIButton + DialogPmo")
    public void addWithDialogPmo() {
        var dialogPmo = new AddModelObjectDialogPmo(this::saveNewModelObject);
        new PmoBasedDialogFactory(dialogPmo::validate)
                .openOkCancelDialog(dialogPmo.getCaption(), dialogPmo.getOkHandler(), dialogPmo.getContentPmo());
    }
    // end::openDialogPmoWithUIButton[]

    @SectionHeader
    @BindVariantNames("primary")
    // tag::uiOpenDialogButton[]
    @UIOpenDialogButton(position = 30, caption = "Open dialog with UIOpenDialogButton")
    public DialogPmo getAddModelObjectDialog() {
        return new AddModelObjectDialogPmo(this::saveNewModelObject);
    }
    // end::uiOpenDialogButton[]

    private void saveNewModelObject(IpsModelObject ipsModelObject) {
        this.ipsModelObjects.add(ipsModelObject);
    }

    @UITableComponent(position = 99, rowPmoClass = IpsModelObjectRowPmo.class)
    public List<IpsModelObject> getIpsModelObjects() {
        return ipsModelObjects;
    }

    public static final class IpsModelObjectRowPmo {

        @ModelObject
        private final IpsModelObject modelObject;

        private IpsModelObjectRowPmo(IpsModelObject modelObject) {
            this.modelObject = modelObject;
        }

        @UILabel(position = 0, modelAttribute = IpsModelObject.PROPERTY_DECIMAL, label = "Decimal")
        public void decimal() {
            // model binding
        }
    }

    // tag::dialogPmo[]
    static class AddModelObjectDialogPmo implements DialogPmo {

        private final NewModelObjectPmo contentPmo;
        private final Consumer<IpsModelObject> saveModelObject;

        public AddModelObjectDialogPmo(Consumer<IpsModelObject> saveModelObject) {
            this.saveModelObject = saveModelObject;
            this.contentPmo = new NewModelObjectPmo();
        }

        @Override
        public String getCaption() {
            return "New model object";
        }

        @Override
        public Handler getOkHandler() {
            return () -> saveModelObject.accept(contentPmo.getModelObject());
        }

        @Override
        public PropertyDispatcherFactory getPropertyDispatcherFactory() {
            return new IpsPropertyDispatcherFactory();
        }

        @Override
        public MessageList validate() {
            return MessageConverter
                    .convert(contentPmo.getModelObject().validate(new ValidationContext(UiFramework.getLocale())));
        }

        @Override
        public NewModelObjectPmo getContentPmo() {
            return contentPmo;
        }
    }

    @UISection
    static class NewModelObjectPmo {

        private final IpsModelObject modelObject;

        public NewModelObjectPmo() {
            this.modelObject = new IpsModelObject();
        }

        @ModelObject
        public IpsModelObject getModelObject() {
            return modelObject;
        }

        @UITextField(position = 10, modelAttribute = IpsModelObject.PROPERTY_DECIMAL, required = RequiredType.REQUIRED)
        public void decimal() {
            // Model Binding
        }

    }
    // end::dialogPmo[]
}
