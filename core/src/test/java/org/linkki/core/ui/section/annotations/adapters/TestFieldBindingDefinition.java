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
package org.linkki.core.ui.section.annotations.adapters;

import static java.util.Objects.requireNonNull;

import org.linkki.core.binding.descriptor.bindingdefinition.BindingDefinition;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.core.defaults.section.annotations.TestUIField;
import org.linkki.core.defaults.ui.element.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.element.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.element.aspects.types.VisibleType;

public class TestFieldBindingDefinition implements BindingDefinition {

    private final TestUIField testUIField;

    public TestFieldBindingDefinition(TestUIField testUIField) {
        this.testUIField = requireNonNull(testUIField, "testUIField must not be null");
    }

    @Override
    public TestUiComponent newComponent() {
        return new TestUiComponent();
    }

    @Override
    public int position() {
        return testUIField.position();
    }

    @Override
    public String label() {
        return testUIField.label();
    }

    @Override
    public EnabledType enabled() {
        return testUIField.enabled();
    }

    @Override
    public RequiredType required() {
        return RequiredType.NOT_REQUIRED;
    }

    @Override
    public VisibleType visible() {
        return testUIField.visible();
    }

    @Override
    public String modelAttribute() {
        return testUIField.modelAttribute();
    }

    @Override
    public String modelObject() {
        return testUIField.modelObject();
    }
}