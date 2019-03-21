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

package org.linkki.core.ui.section.annotations.aspect;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.linkki.test.matcher.Matchers.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.withSettings;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Test;
import org.linkki.core.binding.TestEnum;
import org.linkki.core.defaults.uielement.aspects.types.AvailableValuesType;
import org.linkki.core.ui.components.LabelComponentWrapper;

import com.vaadin.data.HasDataProvider;
import com.vaadin.data.HasFilterableDataProvider;
import com.vaadin.data.HasItems;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeSelect;

public class HasItemsAvailableValuesAspectDefinitionTest {

    @Test
    public void testSetDataProvider_HasDataProvider() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);

        @SuppressWarnings("unchecked")
        ListDataProvider<Object> dataProvider = mock(ListDataProvider.class);
        @SuppressWarnings("unchecked")
        HasDataProvider<Object> component = mock(HasDataProvider.class,
                                                 withSettings().extraInterfaces(Component.class));
        hasItemsAvailableValuesAspectDefinition.setDataProvider(new LabelComponentWrapper(component), dataProvider);

        verify(component).setDataProvider(dataProvider);
    }

    @Test
    public void testSetDataProvider_HasFilterableDataProvider() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);

        @SuppressWarnings("unchecked")
        ListDataProvider<Object> dataProvider = mock(ListDataProvider.class);
        @SuppressWarnings("unchecked")
        HasFilterableDataProvider<Object, SerializablePredicate<Object>> component = mock(HasFilterableDataProvider.class,
                                                                                          withSettings()
                                                                                                  .extraInterfaces(Component.class));
        hasItemsAvailableValuesAspectDefinition.setDataProvider(new LabelComponentWrapper(component), dataProvider);

        verify(component).setDataProvider(dataProvider);
    }

    @Test
    public void testSetDataProvider_Other() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);

        @SuppressWarnings("unchecked")
        ListDataProvider<Object> dataProvider = mock(ListDataProvider.class);
        Component component = mock(HasItems.class, withSettings().extraInterfaces(Component.class));
        hasItemsAvailableValuesAspectDefinition.setDataProvider(new LabelComponentWrapper(component), dataProvider);

        verifyNoMoreInteractions(component);
    }

    @Test
    public void testHandleNullItems_ComboBox() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);

        ComboBox<TestEnum> comboBox = new ComboBox<>();
        hasItemsAvailableValuesAspectDefinition.handleNullItems(new LabelComponentWrapper(comboBox),
                                                                new LinkedList<>(Arrays.asList(TestEnum.ONE, null)));

        assertThat(comboBox.isEmptySelectionAllowed());

        hasItemsAvailableValuesAspectDefinition.handleNullItems(new LabelComponentWrapper(comboBox),
                                                                new LinkedList<>(Arrays.asList(TestEnum.TWO)));

        assertThat(comboBox.isEmptySelectionAllowed(), is(false));
    }

    @Test
    public void testHandleNullItems_NativeSelect() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);

        NativeSelect<TestEnum> nativeSelect = new NativeSelect<>();
        hasItemsAvailableValuesAspectDefinition.handleNullItems(new LabelComponentWrapper(nativeSelect),
                                                                new LinkedList<>(Arrays.asList(TestEnum.ONE, null)));

        assertThat(nativeSelect.isEmptySelectionAllowed());

        hasItemsAvailableValuesAspectDefinition.handleNullItems(new LabelComponentWrapper(nativeSelect),
                                                                new LinkedList<>(Arrays.asList(TestEnum.TWO)));

        assertThat(nativeSelect.isEmptySelectionAllowed(), is(false));
    }

    @Test
    public void testHandleNullItems_Other() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);

        Component component = mock(HasItems.class, withSettings().extraInterfaces(Component.class));
        hasItemsAvailableValuesAspectDefinition.handleNullItems(new LabelComponentWrapper(component),
                                                                new LinkedList<>(Arrays.asList(TestEnum.ONE, null)));

        verifyNoMoreInteractions(component);

        hasItemsAvailableValuesAspectDefinition.handleNullItems(new LabelComponentWrapper(component),
                                                                new LinkedList<>(Arrays.asList(TestEnum.TWO)));

        verifyNoMoreInteractions(component);
    }
}
