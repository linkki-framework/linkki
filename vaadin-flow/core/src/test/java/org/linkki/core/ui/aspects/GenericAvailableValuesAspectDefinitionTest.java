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

package org.linkki.core.ui.aspects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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

class GenericAvailableValuesAspectDefinitionTest {

    @SuppressWarnings("unchecked")
    @Test
    void testSetDataProvider_HasDataProvider() {
        var hasItemsAvailableValuesAspectDefinition =
                new GenericAvailableValuesAspectDefinition(
                        AvailableValuesType.DYNAMIC);
        Component component = spy(new TestHasListDataView());
        var componentWrapper = new NoLabelComponentWrapper(component, WrapperType.FIELD);
        List<Object> list = new ArrayList<>();

        hasItemsAvailableValuesAspectDefinition.setDataProvider(componentWrapper, list);

        verify((HasListDataView<Object, ?>)component).setItems(list);
    }

    @Test
    void testHandleNullItems_ComboBox_WithNullValue() {
        var hasItemsAvailableValuesAspectDefinition =
                new GenericAvailableValuesAspectDefinition(
                        AvailableValuesType.DYNAMIC);

        var comboBox = new ComboBox<TestEnum>();
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(comboBox, WrapperType.FIELD);

        hasItemsAvailableValuesAspectDefinition
                .handleNullItems(componentWrapper, new LinkedList<>(Arrays.asList(TestEnum.ONE, null)));

        assertThat(comboBox.isAllowCustomValue()).isTrue();
    }

    @Test
    void testHandleNullItems_ComboBox_NoNullValue() {
        var hasItemsAvailableValuesAspectDefinition =
                new GenericAvailableValuesAspectDefinition(
                        AvailableValuesType.DYNAMIC);

        var comboBox = new ComboBox<TestEnum>();
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(comboBox, WrapperType.FIELD);

        hasItemsAvailableValuesAspectDefinition
                .handleNullItems(componentWrapper, new LinkedList<>(Arrays.asList(TestEnum.TWO)));

        assertThat(comboBox.isAllowCustomValue()).isFalse();
    }

    @Test
    void testHandleNullItems_NativeSelect_WithNull() {
        var hasItemsAvailableValuesAspectDefinition =
                new GenericAvailableValuesAspectDefinition(
                        AvailableValuesType.DYNAMIC);

        var nativeSelect = new Select<TestEnum>();
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(nativeSelect, WrapperType.FIELD);
        hasItemsAvailableValuesAspectDefinition
                .handleNullItems(componentWrapper, new LinkedList<>(Arrays.asList(TestEnum.ONE, null)));

        assertThat(nativeSelect.isEmptySelectionAllowed()).isTrue();
    }

    @Test
    void testHandleNullItems_NativeSelect_NoNull() {
        var hasItemsAvailableValuesAspectDefinition =
                new GenericAvailableValuesAspectDefinition(
                        AvailableValuesType.DYNAMIC);

        var nativeSelect = new Select<TestEnum>();
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(nativeSelect, WrapperType.FIELD);
        hasItemsAvailableValuesAspectDefinition
                .handleNullItems(componentWrapper, new LinkedList<>(Arrays.asList(TestEnum.TWO)));

        assertThat(nativeSelect.isEmptySelectionAllowed()).isFalse();
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
