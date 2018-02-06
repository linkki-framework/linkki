/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.core.ui.section;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.PresentationModelObject;
import org.linkki.core.binding.Binder;
import org.linkki.core.binding.TestBindingContext;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.ui.section.annotations.ToolTipType;
import org.linkki.core.ui.section.annotations.UIToolTip;
import org.mockito.runners.MockitoJUnitRunner;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

@RunWith(MockitoJUnitRunner.class)
public class ManuelBindIntegrationTest {
    private final ManuallyBoundPmo pmo = new ManuallyBoundPmo();
    private final TestBindingContext bindingContext = TestBindingContext.create();
    private final ManuallyBoundSection section = new ManuallyBoundSection();

    @Test
    public void testGetStaticToolTipFromManualyBindComponent() {
        section.createContent();

        new Binder(section, pmo).setupBindings(bindingContext);

        assertThat(section.textField.getDescription(), is("ToolTip"));
    }

    @Test
    public void testGetDynamicToolTipFromManualyBindComponent() {
        section.createContent();

        new Binder(section, pmo).setupBindings(bindingContext);

        assertThat(section.label.getDescription(), is(StringUtils.EMPTY));
        pmo.setText(ManuallyBoundSection.TOOL_TIP);
        bindingContext.updateUI();
        assertThat(section.label.getDescription(), is(ManuallyBoundSection.TOOL_TIP));
    }

    @Test
    public void testSetLabelFromManualBindComponen() {
        section.createContent();

        new Binder(section, pmo).setupBindings(bindingContext);

        pmo.setText("12345");
        bindingContext.updateUI();
        assertThat(section.label.getValue(), is("12345"));

    }

    public static class ManuallyBoundPmo implements PresentationModelObject {
        public static final String PROPERTY_TEXT = "text";
        public static final String STATIC_STRING = "StaticString";

        private String text = StringUtils.EMPTY;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTextToolTip() {
            return text;
        }
    }

    public class ManuallyBoundSection extends FormLayout {

        public static final String TOOL_TIP = "ToolTip";

        private static final long serialVersionUID = 1L;

        @UIToolTip(text = TOOL_TIP)
        @Bind(pmoProperty = ManuallyBoundPmo.PROPERTY_TEXT)
        private final TextField textField = new TextField();

        @UIToolTip(toolTipType = ToolTipType.DYNAMIC)
        @Bind(pmoProperty = ManuallyBoundPmo.PROPERTY_TEXT)
        private final Label label = new Label();

        public void createContent() {
            setCaption("Manuell gebundene Section");
            textField.setCaption("Text Field");
            addComponent(textField);
            addComponent(label);
        }
    }
}
