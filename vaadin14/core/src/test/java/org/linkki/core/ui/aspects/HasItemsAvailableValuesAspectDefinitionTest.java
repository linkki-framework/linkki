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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.withSettings;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.ui.bind.TestEnum;
import org.linkki.core.ui.wrapper.LabelComponentWrapper;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.HasDataProvider;
import com.vaadin.flow.data.binder.HasFilterableDataProvider;
import com.vaadin.flow.data.binder.HasItems;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.SerializablePredicate;

public class HasItemsAvailableValuesAspectDefinitionTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testSetDataProvider_HasDataProvider() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);

        ListDataProvider<Object> dataProvider = new ListDataProvider<Object>(Collections.emptyList());
        Component component = mock(Component.class,
                                   withSettings().extraInterfaces(HasDataProvider.class));
        hasItemsAvailableValuesAspectDefinition.setDataProvider(new LabelComponentWrapper(component),
                                                                dataProvider);

        verify((HasDataProvider<Object>)component).setDataProvider(dataProvider);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetDataProvider_HasFilterableDataProvider() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);

        ListDataProvider<Object> dataProvider = new ListDataProvider<Object>(Collections.emptyList());
        Component component = mock(Component.class,
                                   withSettings()
                                           .extraInterfaces(HasFilterableDataProvider.class));
        hasItemsAvailableValuesAspectDefinition.setDataProvider(new LabelComponentWrapper(component),
                                                                dataProvider);

        verify((HasFilterableDataProvider<Object, SerializablePredicate<Object>>)component)
                .setDataProvider(dataProvider);
    }

    @Test
    public void testSetDataProvider_Other() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);

        @SuppressWarnings("unchecked")
        ListDataProvider<Object> dataProvider = mock(ListDataProvider.class);
        Component component = mock(Component.class, withSettings().extraInterfaces(HasItems.class));
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

        assertThat(comboBox.isAllowCustomValue());

        hasItemsAvailableValuesAspectDefinition.handleNullItems(new LabelComponentWrapper(comboBox),
                                                                new LinkedList<>(Arrays.asList(TestEnum.TWO)));

        assertThat(comboBox.isAllowCustomValue(), is(false));
    }

    @Test
    public void testHandleNullItems_NativeSelect() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);

        Select<TestEnum> nativeSelect = new Select<>();
        hasItemsAvailableValuesAspectDefinition.handleNullItems(new LabelComponentWrapper(nativeSelect),
                                                                new LinkedList<>(Arrays.asList(TestEnum.ONE, null)));

        assertThat(nativeSelect.isEmptySelectionAllowed(), is(true));

        hasItemsAvailableValuesAspectDefinition.handleNullItems(new LabelComponentWrapper(nativeSelect),
                                                                new LinkedList<>(Arrays.asList(TestEnum.TWO)));

        assertThat(nativeSelect.isEmptySelectionAllowed(), is(false));
    }

    @Test
    public void testHandleNullItems_Other() {
        HasItemsAvailableValuesAspectDefinition hasItemsAvailableValuesAspectDefinition = new HasItemsAvailableValuesAspectDefinition(
                AvailableValuesType.DYNAMIC);

        Component component = mock(Component.class, withSettings().extraInterfaces(HasItems.class));
        hasItemsAvailableValuesAspectDefinition.handleNullItems(new LabelComponentWrapper(component),
                                                                new LinkedList<>(Arrays.asList(TestEnum.ONE, null)));

        verifyNoMoreInteractions(component);

        hasItemsAvailableValuesAspectDefinition.handleNullItems(new LabelComponentWrapper(component),
                                                                new LinkedList<>(Arrays.asList(TestEnum.TWO)));

        verifyNoMoreInteractions(component);
    }
}
