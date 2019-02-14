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

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Test;
import org.linkki.core.binding.TestEnum;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.LabelComponentWrapper;
import org.linkki.core.ui.section.annotations.AvailableValuesType;
import org.linkki.util.handler.Handler;
import org.mockito.ArgumentCaptor;

import com.vaadin.data.HasItems;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.ComboBox;

public class AvailableValuesAspectDefinitionTest {

    private static final BiConsumer<HasItems<?>, ListDataProvider<Object>> NOP = (c, p) -> {
        /* NOP */
    };

    @SuppressWarnings({ "null", "unused" })
    @Test(expected = NullPointerException.class)
    public void testConstructorWithoutAvailableValuesType() {
        new AvailableValuesAspectDefinition<>(null, NOP);
    }

    @Test
    public void testGetAvailableValuesType() {
        for (AvailableValuesType type : AvailableValuesType.values()) {
            assertThat(new AvailableValuesAspectDefinition<>(type, NOP).getAvailableValuesType(), is(type));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValuesDerivedFromDatatype_NonEnumDatatype() {
        new AvailableValuesAspectDefinition<>(AvailableValuesType.DYNAMIC, NOP)
                .getValuesDerivedFromDatatype("foo", String.class);
    }

    @Test
    public void testGetValuesDerivedFromDatatype() {
        AvailableValuesAspectDefinition<HasItems<?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.DYNAMIC, NOP);

        assertThat(availableValuesAspectDefinition.getValuesDerivedFromDatatype("foo", TestEnum.class),
                   contains(TestEnum.ONE, TestEnum.TWO, TestEnum.THREE));
        assertThat(availableValuesAspectDefinition.getValuesDerivedFromDatatype("foo", Boolean.class),
                   contains(null, true, false));
        assertThat(availableValuesAspectDefinition.getValuesDerivedFromDatatype("foo", boolean.class),
                   contains(true, false));
    }

    @Test
    public void testSetDataProvider() {
        @SuppressWarnings("unchecked")
        BiConsumer<HasItems<?>, ListDataProvider<Object>> dataProviderSetter = mock(BiConsumer.class);
        AvailableValuesAspectDefinition<HasItems<?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.DYNAMIC, dataProviderSetter);

        @SuppressWarnings("unchecked")
        ListDataProvider<Object> dataProvider = mock(ListDataProvider.class);
        ComboBox<Object> component = new ComboBox<>();
        availableValuesAspectDefinition.setDataProvider(new LabelComponentWrapper(component), dataProvider);

        verify(dataProviderSetter).accept(component, dataProvider);
    }

    @Test
    public void testHandleNullItems() {
        AvailableValuesAspectDefinition<HasItems<?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.DYNAMIC, NOP);

        @SuppressWarnings("unchecked")
        List<Object> items = mock(List.class);
        @SuppressWarnings("unchecked")
        ComboBox<Object> component = mock(ComboBox.class);
        availableValuesAspectDefinition.handleNullItems(new LabelComponentWrapper(component), items);

        verifyNoMoreInteractions(component, items);
    }

    @Test
    public void testCreateAspect_Dynamic() {
        AvailableValuesAspectDefinition<HasItems<?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.DYNAMIC, NOP);

        Aspect<List<?>> aspect = availableValuesAspectDefinition.createAspect("foo", TestEnum.class);

        assertThat(aspect.getName(), is(AvailableValuesAspectDefinition.NAME));
        assertThat(aspect.isValuePresent(), is(false));
    }

    @Test
    public void testCreateAspect_NoValues() {
        AvailableValuesAspectDefinition<HasItems<?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.NO_VALUES, NOP);

        Aspect<List<?>> aspect = availableValuesAspectDefinition.createAspect("foo", TestEnum.class);

        assertThat(aspect.getName(), is(AvailableValuesAspectDefinition.NAME));
        assertThat(aspect.isValuePresent(), is(true));
        assertThat(aspect.getValue(), is(empty()));
    }

    @Test
    public void testCreateAspect_EnumValuesExclNull() {
        AvailableValuesAspectDefinition<HasItems<?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.ENUM_VALUES_EXCL_NULL, NOP);

        Aspect<List<?>> aspect = availableValuesAspectDefinition.createAspect("foo", TestEnum.class);

        assertThat(aspect.getName(), is(AvailableValuesAspectDefinition.NAME));
        assertThat(aspect.isValuePresent(), is(true));
        assertThat(aspect.getValue(), contains(TestEnum.ONE, TestEnum.TWO, TestEnum.THREE));
    }

    @Test
    public void testCreateAspect_EnumValuesInclNull() {
        AvailableValuesAspectDefinition<HasItems<?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.ENUM_VALUES_INCL_NULL, NOP);

        Aspect<List<?>> aspect = availableValuesAspectDefinition.createAspect("foo", TestEnum.class);

        assertThat(aspect.getName(), is(AvailableValuesAspectDefinition.NAME));
        assertThat(aspect.isValuePresent(), is(true));
        assertThat(aspect.getValue(), contains(null, TestEnum.ONE, TestEnum.TWO, TestEnum.THREE));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateUiUpdater() {
        BiConsumer<HasItems<?>, ListDataProvider<Object>> dataProviderSetter = mock(BiConsumer.class);
        AvailableValuesAspectDefinition<HasItems<?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.DYNAMIC, dataProviderSetter);

        PropertyDispatcher propertyDispatcher = mock(PropertyDispatcher.class);
        when(propertyDispatcher.pull(any(Aspect.class))).thenReturn(Arrays.asList(TestEnum.ONE, TestEnum.THREE));
        ComboBox<Object> component = mock(ComboBox.class);
        Handler uiUpdater = availableValuesAspectDefinition.createUiUpdater(propertyDispatcher,
                                                                            new LabelComponentWrapper(component));

        ArgumentCaptor<ListDataProvider<?>> dataProviderCaptor = ArgumentCaptor.forClass(ListDataProvider.class);
        verify(dataProviderSetter).accept(eq(component), (ListDataProvider<Object>)dataProviderCaptor.capture());
        @SuppressWarnings("null")
        @NonNull
        ListDataProvider<?> dataProvider = dataProviderCaptor.getValue();

        uiUpdater.apply();
        assertThat(dataProvider.getItems(), contains(TestEnum.ONE, TestEnum.THREE));
    }
}
