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
package org.linkki.core.binding;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.aspect.definition.AvailableValuesAspectDefinition;
import org.linkki.core.binding.dispatcher.ExceptionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.ReflectionPropertyDispatcher;
import org.linkki.core.ui.components.LinkkiComboBox;
import org.linkki.core.ui.section.annotations.AvailableValuesType;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;

@SuppressWarnings("null")
public class FieldBindingIntergrationTest {

    private TestPmo pmo = new TestPmo();

    private Label label = new Label();
    private ComboBox comboField = new LinkkiComboBox();

    private FieldBinding<Object> selectBinding;

    private List<TestEnum> valueList = Arrays.asList(new TestEnum[] { TestEnum.ONE, TestEnum.THREE });

    private PropertyDispatcher propertyDispatcher;

    private BindingContext context;

    @Before
    public void setUp() {
        ExceptionPropertyDispatcher exceptionDispatcher = new ExceptionPropertyDispatcher("enumValue", pmo);
        propertyDispatcher = new ReflectionPropertyDispatcher(() -> pmo, "enumValue", exceptionDispatcher);
        context = TestBindingContext.create();

        selectBinding = new FieldBinding<Object>(label, comboField, propertyDispatcher, context::updateUI,
                Arrays.asList(new TestAvailableValuesAspectDefinition()));
        context.add(selectBinding);

        context.add(selectBinding);
    }

    /**
     * Tests bug fixed in LIN-76, where combo field would throw a ReadOnlyException when binding was
     * initialized.
     */
    @Test
    public void testFieldBinding_readonlyField() {
        ExceptionPropertyDispatcher exceptionDispatcher = new ExceptionPropertyDispatcher("readonlyEnumValue", pmo);
        propertyDispatcher = new ReflectionPropertyDispatcher(() -> pmo, "readonlyEnumValue", exceptionDispatcher);
        ComboBox comboField2 = new LinkkiComboBox();
        pmo.setEnumValue(TestEnum.THREE);
        @SuppressWarnings("unused")
        FieldBinding<Object> fieldBinding = new FieldBinding<Object>(label, comboField2, propertyDispatcher,
                context::updateUI, new ArrayList<>());
    }

    @Test
    public void testBindAvailableValues_retainSelectedValue() {
        pmo.setEnumValue(null);
        pmo.setEnumValueAvailableValues(valueList);
        context.updateUI();

        comboField.setValue(TestEnum.THREE);
        assertThat(pmo.getEnumValue(), is(TestEnum.THREE));
        assertThat(comboField.getValue(), is(TestEnum.THREE));

        List<TestEnum> otherValues = Arrays.asList(new TestEnum[] { TestEnum.ONE, TestEnum.THREE });
        pmo.setEnumValueAvailableValues(otherValues);
        context.updateUI();

        assertThat(comboField.getValue(), is(TestEnum.THREE));
        assertThat(pmo.getEnumValue(), is(TestEnum.THREE));
    }

    /**
     * Tests whether the current value is retained, even if it is no longer available.
     */
    @Test
    public void testBindAvailableValues_retainSelectedValue2() {
        pmo.setEnumValue(null);
        pmo.setEnumValueAvailableValues(valueList);
        context.updateUI();

        comboField.setValue(TestEnum.THREE);
        assertThat(comboField.getValue(), is(TestEnum.THREE));
        assertThat(pmo.getEnumValue(), is(TestEnum.THREE));
        Collection<?> itemIds = comboField.getItemIds();
        assertThat(itemIds.size(), is(2));
        Iterator<?> iterator = itemIds.iterator();
        assertThat(iterator.next(), is(TestEnum.ONE));
        assertThat(iterator.next(), is(TestEnum.THREE));

        List<TestEnum> otherValues = Arrays.asList(new TestEnum[] { TestEnum.ONE });
        pmo.setEnumValueAvailableValues(otherValues);
        context.updateUI();

        assertThat(comboField.getValue(), is(TestEnum.THREE));
        assertThat(pmo.getEnumValue(), is(TestEnum.THREE));
        Collection<?> itemIds2 = comboField.getItemIds();
        assertThat(itemIds2.size(), is(1));
        Iterator<?> iterator2 = itemIds2.iterator();
        assertThat(iterator2.next(), is(TestEnum.ONE));
    }

    private class TestAvailableValuesAspectDefinition extends AvailableValuesAspectDefinition {

        @Override
        public void initialize(Annotation annotation) {
            // does nothing
        }

        @Override
        protected AvailableValuesType getAvailableValuesType() {
            return AvailableValuesType.DYNAMIC;
        }

    }
}
