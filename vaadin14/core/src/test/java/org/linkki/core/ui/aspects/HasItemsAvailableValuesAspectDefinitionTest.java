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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Arrays;
import java.util.Collection;
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
import com.vaadin.flow.data.binder.HasDataProvider;
import com.vaadin.flow.data.binder.HasFilterableDataProvider;
import com.vaadin.flow.data.binder.HasItems;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializablePredicate;

public class HasItemsAvailableValuesAspectDefinitionTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testSetDataProvider_HasDataProvider() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);
        ListDataProvider<Object> dataProvider = new ListDataProvider<Object>(Collections.emptyList());
        Component component = spy(new TestHasDataProvider());
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(component, WrapperType.FIELD);

        hasItemsAvailableValuesAspectDefinition.setDataProvider(componentWrapper, dataProvider);

        verify((HasDataProvider<Object>)component).setDataProvider(dataProvider);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetDataProvider_HasFilterableDataProvider() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);
        ListDataProvider<Object> dataProvider = new ListDataProvider<Object>(Collections.emptyList());
        Component component = spy(new TestHasFilterableDataProvider());
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(component, WrapperType.FIELD);

        hasItemsAvailableValuesAspectDefinition.setDataProvider(componentWrapper, dataProvider);

        verify((HasFilterableDataProvider<Object, SerializablePredicate<Object>>)component)
                .setDataProvider(dataProvider);
    }

    @Test
    public void testSetDataProvider_Other() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);
        @SuppressWarnings("unchecked")
        ListDataProvider<Object> dataProvider = mock(ListDataProvider.class);
        Component component = spy(new TestHasItems());
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(component, WrapperType.FIELD);

        hasItemsAvailableValuesAspectDefinition.setDataProvider(componentWrapper, dataProvider);

        verifyNoMoreInteractions(component);
    }

    @Test
    public void testHandleNullItems_ComboBox_WithNullValue() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);

        ComboBox<TestEnum> comboBox = new ComboBox<>();
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(comboBox, WrapperType.FIELD);

        hasItemsAvailableValuesAspectDefinition
                .handleNullItems(componentWrapper, new LinkedList<>(Arrays.asList(TestEnum.ONE, null)));

        assertThat(comboBox.isAllowCustomValue());
    }

    @Test
    public void testHandleNullItems_ComboBox_NoNullValue() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);

        ComboBox<TestEnum> comboBox = new ComboBox<>();
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(comboBox, WrapperType.FIELD);

        hasItemsAvailableValuesAspectDefinition
                .handleNullItems(componentWrapper, new LinkedList<>(Arrays.asList(TestEnum.TWO)));

        assertThat(comboBox.isAllowCustomValue(), is(false));
    }

    @Test
    public void testHandleNullItems_NativeSelect_WithNull() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);

        Select<TestEnum> nativeSelect = new Select<>();
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(nativeSelect, WrapperType.FIELD);
        hasItemsAvailableValuesAspectDefinition
                .handleNullItems(componentWrapper, new LinkedList<>(Arrays.asList(TestEnum.ONE, null)));

        assertThat(nativeSelect.isEmptySelectionAllowed(), is(true));
    }

    @Test
    public void testHandleNullItems_NativeSelect_NoNull() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);

        Select<TestEnum> nativeSelect = new Select<>();
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(nativeSelect, WrapperType.FIELD);
        hasItemsAvailableValuesAspectDefinition
                .handleNullItems(componentWrapper, new LinkedList<>(Arrays.asList(TestEnum.TWO)));

        assertThat(nativeSelect.isEmptySelectionAllowed(), is(false));
    }

    @Test
    public void testHandleNullItems_Other_NullItem() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);
        Component component = spy(new TestHasItems());
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(component, WrapperType.FIELD);

        hasItemsAvailableValuesAspectDefinition.handleNullItems(componentWrapper,
                                                                new LinkedList<>(Arrays.asList(TestEnum.ONE, null)));

        verifyNoMoreInteractions(component);
    }

    @Test
    public void testHandleNullItems_Other_NoNullItem() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);
        Component component = spy(new TestHasItems());
        ComponentWrapper componentWrapper = new NoLabelComponentWrapper(component, WrapperType.FIELD);

        hasItemsAvailableValuesAspectDefinition
                .handleNullItems(componentWrapper,
                                 new LinkedList<>(Arrays.asList(TestEnum.TWO)));

        verifyNoMoreInteractions(component);
    }

    @Tag("test-has-items")
    private static class TestHasItems extends Component implements HasItems<Object> {

        private static final long serialVersionUID = 1L;

        @Override
        public void setItems(Collection<Object> items) {
            // nothing to do
        }
    }

    @Tag("test-has-data-provider")
    private static class TestHasDataProvider extends Component implements HasDataProvider<Object> {

        private static final long serialVersionUID = 1L;

        @Override
        public void setDataProvider(DataProvider<Object, ?> dataProvider) {
            // nothing to do
        }
    }

    @Tag("test-has-filterable-data-provider")
    private static class TestHasFilterableDataProvider extends Component
            implements HasFilterableDataProvider<Object, Object> {

        private static final long serialVersionUID = 1L;

        @Override
        public void setItems(Collection<Object> items) {
            // nothing to do
        }

        @Override
        public <C> void setDataProvider(DataProvider<Object, C> dataProvider,
                SerializableFunction<Object, C> filterConverter) {
            // nothing to do
        }

    }
}
