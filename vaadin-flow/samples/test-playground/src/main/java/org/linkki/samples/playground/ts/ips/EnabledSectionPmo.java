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

package org.linkki.samples.playground.ts.ips;

import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.framework.ui.dialogs.PmoBasedDialogFactory;
import org.linkki.ips.binding.dispatcher.IpsPropertyDispatcherFactory;
import org.linkki.samples.playground.ips.model.IpsModelObject;
import org.linkki.util.handler.Handler;

@UISection
public class EnabledSectionPmo {
    @ModelObject
    private final IpsModelObject modelObject = new IpsModelObject();

    @UITextField(position = 20,
            modelAttribute = IpsModelObject.PROPERTY_UNRESTRICTEDINCLNULL, //
            label = "Non-Empty ValueSet - Must be Enabled")
    public void notEmptyValueSet() {
        // model binding
    }

    @UIComboBox(position = 50,
            modelAttribute = IpsModelObject.PROPERTY_EMPTYVALUESET, //
            visible = VisibleType.DYNAMIC,
            label = "Empty ValueSet - Must be Disabled")
    public void emptyValueSet() {
        // model binding
    }

    /*
     * Needs to be visible to be able to see the field.
     */
    public boolean isEmptyValueSetVisible() {
        return true;
    }

    @UIComboBox(position = 70,
            modelAttribute = IpsModelObject.PROPERTY_EMPTYVALUESET, //
            visible = VisibleType.DYNAMIC,
            enabled = EnabledType.DYNAMIC, //
            label = "Empty ValueSet Dynamically Configured to be Enabled")
    public void dynamicEnabledEmptyValueSet() {
        // model binding
    }

    @UIButton(position = 90, caption = "Open dialog with DialogBindingManager")
    public void showDialogWithBindingManager() {
        new PmoBasedDialogFactory(ValidationService.NOP_VALIDATION_SERVICE,
                PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER, new IpsPropertyDispatcherFactory())
                        .newOkCancelDialog("Dialog with Property Dispatcher Factory", Handler.NOP_HANDLER,
                                           Handler.NOP_HANDLER, new EnabledSectionPmo())
                        .open();
    }

    /*
     * Needs to be visible to be able to see the field.
     */
    public boolean isDynamicEnabledEmptyValueSetVisible() {
        return true;
    }

    public boolean isDynamicEnabledEmptyValueSetEnabled() {
        return true;
    }
}
