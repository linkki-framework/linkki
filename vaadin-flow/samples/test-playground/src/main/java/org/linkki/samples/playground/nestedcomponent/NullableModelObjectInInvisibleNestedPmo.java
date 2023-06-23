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
package org.linkki.samples.playground.nestedcomponent;

import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.annotation.BindVisible;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.core.ui.nested.annotation.UINestedComponent;

import java.util.Optional;
import java.util.function.Supplier;


@UIVerticalLayout
public class NullableModelObjectInInvisibleNestedPmo {

    private boolean nestedComponentVisible;
    private TestModelObject modelObject;

    public NullableModelObjectInInvisibleNestedPmo() {
        this.modelObject = null;
        nestedComponentVisible = false;
    }

    @UIButton(position = 10, captionType = CaptionType.DYNAMIC)
    public void toggleModelObject() {
        if (modelObject != null) {
            modelObject = null;
        } else {
            modelObject = new TestModelObject();
        }
    }

    public String getToggleModelObjectCaption() {
        return Optional.ofNullable(modelObject).map(o -> "Set model object to null").orElse("Create new model object");
    }

    @UICheckBox(position = 20, caption = "Show nested component")
    public boolean isNestedComponentVisible() {
        return nestedComponentVisible;
    }

    public void setNestedComponentVisible(boolean nestedComponentVisible) {
        this.nestedComponentVisible = nestedComponentVisible;
    }

    @BindVisible
    @UINestedComponent(position = 50)
    public PmoWithNullableModelObject getNestedPmoWithNullableModelObject() {
        return new PmoWithNullableModelObject(() -> modelObject);
    }

    public boolean isNestedPmoWithNullableModelObjectVisible() {
        return isNestedComponentVisible();
    }

    @UIVerticalLayout
    public static class PmoWithNullableModelObject {

        private final Supplier<TestModelObject> modelObjectSupplier;

        public PmoWithNullableModelObject(Supplier<TestModelObject> modelObjectSupplier) {
            this.modelObjectSupplier = modelObjectSupplier;
        }

        @ModelObject
        public TestModelObject getModelObject() {
            return modelObjectSupplier.get();
        }

        @UITextField(position = 10, label = "Model attribute", modelAttribute = TestModelObject.PROPERTY_ATTRIBUTE)
        public void textField() {
            // delegates to model object
        }
    }


    public static class TestModelObject {

        public static final String PROPERTY_ATTRIBUTE = "attribute";
        private String attribute;

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
        }
    }
}
