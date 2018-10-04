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

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linkki.core.PresentationModelObject;
import org.linkki.core.binding.Binder;
import org.linkki.core.binding.TestBindingContext;
import org.linkki.core.binding.TestEnum;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.core.ui.section.annotations.BindTooltip;
import org.linkki.core.ui.section.annotations.BindTooltipType;
import org.mockito.runners.MockitoJUnitRunner;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

@RunWith(MockitoJUnitRunner.class)
public class ManuelBindIntegrationTest {

    private final ManuallyBoundPmo pmo = new ManuallyBoundPmo();
    private final TestBindingContext bindingContext = TestBindingContext.create();
    private final ManuallyBoundSection section = new ManuallyBoundSection();

    @Test
    public void testGetStaticTooltipFromManualyBindComponent() {
        section.createContent();

        new Binder(section, pmo).setupBindings(bindingContext);

        assertThat(section.textField.getDescription(), is("Tooltip"));
    }

    @Test
    public void testGetDynamicTooltipFromManualyBindComponent() {
        section.createContent();

        new Binder(section, pmo).setupBindings(bindingContext);

        assertThat(section.label.getDescription(), is(StringUtils.EMPTY));
        pmo.setText(ManuallyBoundSection.TOOL_TIP);
        bindingContext.modelChanged();
        assertThat(section.label.getDescription(), is(ManuallyBoundSection.TOOL_TIP));
    }

    @Test
    public void testSetLabelFromManualBindComponen() {
        section.createContent();

        new Binder(section, pmo).setupBindings(bindingContext);

        pmo.setText("12345");
        bindingContext.modelChanged();
        assertThat(section.label.getValue(), is("12345"));
    }

    @Test
    public void testComboValueAndStaticAvailableValues() {
        section.createContent();
        new Binder(section, pmo).setupBindings(bindingContext);

        assertThat(section.comboBox.getValue(), is(TestEnum.THREE));
        assertThat(section.comboBox.getItemIds(), contains(TestEnum.ONE, TestEnum.TWO, TestEnum.THREE));

        pmo.combo = TestEnum.TWO;
        bindingContext.modelChanged();

        assertThat(section.comboBox.getValue(), is(TestEnum.TWO));
    }

    @Test
    public void testReadOnly() {
        section.createContent();
        new Binder(section, pmo).setupBindings(bindingContext);

        assertThat(section.textField.isReadOnly(), is(false));
        assertThat(section.comboBox.isReadOnly(), is(true));
    }


    public static class ManuallyBoundPmo implements PresentationModelObject {
        public static final String PROPERTY_TEXT = "text";
        public static final String STATIC_STRING = "StaticString";
        public static final String PROPERTY_COMBO = "combo";

        private String text = StringUtils.EMPTY;

        private TestEnum combo = TestEnum.THREE;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTextTooltip() {
            return text;
        }

        public TestEnum getCombo() {
            return combo;
        }

    }

    public class ManuallyBoundSection extends FormLayout {

        public static final String TOOL_TIP = "Tooltip";

        private static final long serialVersionUID = 1L;

        @BindTooltip(text = TOOL_TIP)
        @Bind(pmoProperty = ManuallyBoundPmo.PROPERTY_TEXT)
        private final TextField textField = new TextField();

        @Bind(pmoProperty = ManuallyBoundPmo.PROPERTY_COMBO, availableValues = AvailableValuesType.ENUM_VALUES_EXCL_NULL)
        private final ComboBox comboBox = new ComboBox();

        @BindTooltip(tooltipType = BindTooltipType.DYNAMIC)
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
