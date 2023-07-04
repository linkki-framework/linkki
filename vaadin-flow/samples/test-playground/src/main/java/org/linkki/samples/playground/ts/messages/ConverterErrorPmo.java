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


package org.linkki.samples.playground.ts.messages;

import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;

import com.vaadin.flow.component.Component;

@UIVerticalLayout
public class ConverterErrorPmo {
    @ModelObject
    private RequiredIntegerFieldModelObject modelObject;

    private ConverterErrorPmo() {
        modelObject = new RequiredIntegerFieldModelObject();
    }

    @UIIntegerField(position = 10, label = "IntegerField", modelAttribute = "integerField")
    public void integerField() {
        // model binding
    }

    public static Component createComponent() {
        var pmo = new ConverterErrorPmo();
        var bindingManager = new DefaultBindingManager();
        return VaadinUiCreator.createComponent(pmo, bindingManager.getContext(pmo.getClass()));
    }

    static class RequiredIntegerFieldModelObject {
        private int integerField = 42;

        public int getIntegerField() {
            return integerField;
        }

        public void setIntegerField(int integerField) {
            this.integerField = integerField;
        }
    }

}
