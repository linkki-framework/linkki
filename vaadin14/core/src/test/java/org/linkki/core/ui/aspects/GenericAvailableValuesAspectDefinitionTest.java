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

package org.linkki.core.ui.aspects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.linkki.test.matcher.Matchers.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.ui.bind.TestEnum;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.provider.AbstractListDataView;
import com.vaadin.flow.data.provider.HasListDataView;
import com.vaadin.flow.data.provider.ListDataProvider;

public class GenericAvailableValuesAspectDefinitionTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testSetDataProvider_HasDataProvider() {
        GenericAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new GenericAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);
        ListDataProvider<Object> dataProvider = new ListDataProvider<Object>(Collections.emptyList());
        Component component = spy(new TestHasListDataView());
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(component, WrapperType.FIELD);

        hasItemsAvailableValuesAspectDefinition.setDataProvider(componentWrapper, dataProvider);

        verify((HasListDataView<Object, ?>)component).setItems(dataProvider);
    }


    @Test
    public void testHandleNullItems_ComboBox_WithNullValue() {
        GenericAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new GenericAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);

        ComboBox<TestEnum> comboBox = new ComboBox<>();
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(comboBox, WrapperType.FIELD);

        hasItemsAvailableValuesAspectDefinition
                .handleNullItems(componentWrapper, new LinkedList<>(Arrays.asList(TestEnum.ONE, null)));

        assertThat(comboBox.isAllowCustomValue());
    }

    @Test
    public void testHandleNullItems_ComboBox_NoNullValue() {
        GenericAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new GenericAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);

        ComboBox<TestEnum> comboBox = new ComboBox<>();
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(comboBox, WrapperType.FIELD);

        hasItemsAvailableValuesAspectDefinition
                .handleNullItems(componentWrapper, new LinkedList<>(Arrays.asList(TestEnum.TWO)));

        assertThat(comboBox.isAllowCustomValue(), is(false));
    }

    @Test
    public void testHandleNullItems_NativeSelect_WithNull() {
        GenericAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new GenericAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);

        Select<TestEnum> nativeSelect = new Select<>();
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(nativeSelect, WrapperType.FIELD);
        hasItemsAvailableValuesAspectDefinition
                .handleNullItems(componentWrapper, new LinkedList<>(Arrays.asList(TestEnum.ONE, null)));

        assertThat(nativeSelect.isEmptySelectionAllowed(), is(true));
    }

    @Test
    public void testHandleNullItems_NativeSelect_NoNull() {
        GenericAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new GenericAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);

        Select<TestEnum> nativeSelect = new Select<>();
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(nativeSelect, WrapperType.FIELD);
        hasItemsAvailableValuesAspectDefinition
                .handleNullItems(componentWrapper, new LinkedList<>(Arrays.asList(TestEnum.TWO)));

        assertThat(nativeSelect.isEmptySelectionAllowed(), is(false));
    }


    @Tag("test-has-data-provider")
    private static class TestHasListDataView extends Component
            implements HasListDataView<Object, AbstractListDataView<Object>> {

        private static final long serialVersionUID = 1L;


        @Override
        public AbstractListDataView<Object> setItems(ListDataProvider<Object> dataProvider) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public AbstractListDataView<Object> getListDataView() {
            // TODO Auto-generated method stub
            return null;
        }
    }

}
