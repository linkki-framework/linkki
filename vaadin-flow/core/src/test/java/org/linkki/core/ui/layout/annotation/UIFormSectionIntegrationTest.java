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

package org.linkki.core.ui.layout.annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.vaadin.component.section.BaseSection;

import com.vaadin.flow.component.Component;

class UIFormSectionIntegrationTest {

    @Test
    void testCreation() {
        BindingContext bindingContext = new BindingContext();
        FormSectionPmo pmo = new FormSectionPmo();

        Component component = VaadinUiCreator.createComponent(pmo, bindingContext);
        assertThat("UiCreator should be able to create a FormSection", component,
                   is(instanceOf(BaseSection.class)));

        BaseSection formSection = (BaseSection)component;

        assertThat(formSection.getCaption(), is("Caption"));
        assertThat(formSection.getContentWrapper().getComponentCount(), is(1));
    }


    @UIFormSection(caption = "Caption")
    public static class FormSectionPmo {
        @UITextField(position = 0)
        public String getText() {
            return "";
        }
    }
}
