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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.core.ui.bind.TestEnum;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.provider.HasListDataView;

public class AvailableValuesAspectDefinitionTest {

    private static final BiConsumer<HasListDataView<Object, ?>, List<Object>> NOP = (c, p) -> {
        /* NOP */
    };

    @SuppressWarnings("unused")
    public void testConstructorWithoutAvailableValuesType() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new AvailableValuesAspectDefinition<>(null, NOP);
        });
    }

    @Test
    public void testGetAvailableValuesType() {
        for (AvailableValuesType type : AvailableValuesType.values()) {
            assertThat(new AvailableValuesAspectDefinition<>(type, NOP).getAvailableValuesType(), is(type));
        }
    }

    @Test
    public void testGetValuesDerivedFromDatatype_NonEnumDatatype() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            new AvailableValuesAspectDefinition<>(AvailableValuesType.DYNAMIC, NOP)
                    .getValuesDerivedFromDatatype("foo", String.class);
        });
    }

    @Test
    public void testGetValuesDerivedFromDatatype() {
        AvailableValuesAspectDefinition<HasListDataView<Object, ?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
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
        BiConsumer<HasListDataView<Object, ?>, List<Object>> dataProviderSetter = mock(BiConsumer.class);
        AvailableValuesAspectDefinition<HasListDataView<Object, ?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.DYNAMIC, dataProviderSetter);

        List<Object> data = new ArrayList<>();
        ComboBox<Object> component = new ComboBox<>();
        availableValuesAspectDefinition.setDataProvider(new NoLabelComponentWrapper(component), data);

        verify(dataProviderSetter).accept(component, data);
    }

    @Test
    public void testHandleNullItems() {
        AvailableValuesAspectDefinition<HasListDataView<Object, ?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.DYNAMIC, NOP);

        @SuppressWarnings("unchecked")
        List<Object> items = mock(List.class);
        @SuppressWarnings("unchecked")
        ComboBox<Object> component = mock(ComboBox.class);
        availableValuesAspectDefinition
                .handleNullItems(new NoLabelComponentWrapper(component, WrapperType.FIELD), items);

        verifyNoMoreInteractions(component, items);
    }

    @Test
    public void testCreateAspect_Dynamic() {
        AvailableValuesAspectDefinition<HasListDataView<Object, ?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.DYNAMIC, NOP);

        Aspect<Collection<?>> aspect = availableValuesAspectDefinition.createAspect("foo", TestEnum.class);

        assertThat(aspect.getName(), is(AvailableValuesAspectDefinition.NAME));
        assertThat(aspect.isValuePresent(), is(false));
    }

    @Test
    public void testCreateAspect_NoValues() {
        AvailableValuesAspectDefinition<HasListDataView<Object, ?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.NO_VALUES, NOP);

        Aspect<Collection<?>> aspect = availableValuesAspectDefinition.createAspect("foo", TestEnum.class);

        assertThat(aspect.getName(), is(AvailableValuesAspectDefinition.NAME));
        assertThat(aspect.isValuePresent(), is(true));
        assertThat(aspect.getValue(), is(empty()));
    }

    @Test
    public void testCreateAspect_EnumValuesExclNull() {
        AvailableValuesAspectDefinition<HasListDataView<Object, ?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.ENUM_VALUES_EXCL_NULL, NOP);

        Aspect<Collection<?>> aspect = availableValuesAspectDefinition.createAspect("foo", TestEnum.class);

        assertThat(aspect.getName(), is(AvailableValuesAspectDefinition.NAME));
        assertThat(aspect.isValuePresent(), is(true));
        assertThat(aspect.getValue(), contains(TestEnum.ONE, TestEnum.TWO, TestEnum.THREE));
    }

    @Test
    public void testCreateAspect_EnumValuesInclNull() {
        AvailableValuesAspectDefinition<HasListDataView<Object, ?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.ENUM_VALUES_INCL_NULL, NOP);

        Aspect<Collection<?>> aspect = availableValuesAspectDefinition.createAspect("foo", TestEnum.class);

        assertThat(aspect.getName(), is(AvailableValuesAspectDefinition.NAME));
        assertThat(aspect.isValuePresent(), is(true));
        assertThat(aspect.getValue(), contains(null, TestEnum.ONE, TestEnum.TWO, TestEnum.THREE));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUiUpdater() {
        AvailableValuesAspectDefinition<DataComponent<Object>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.DYNAMIC, DataComponent<Object>::setItems);
        DataComponent<TestEnum> component = spy(new DataComponent<>());
        PropertyDispatcher propertyDispatcher = mock(PropertyDispatcher.class);
        when(propertyDispatcher.pull(any())).thenReturn(Arrays.asList(TestEnum.ONE, TestEnum.TWO));

        Handler uiUpdater = availableValuesAspectDefinition.createUiUpdater(propertyDispatcher,
                                                                            new NoLabelComponentWrapper(component));

        // items are set initially during the first update
        verifyNoMoreInteractions(component);
        uiUpdater.apply();
        verify(component).setItems(Arrays.asList(TestEnum.ONE, TestEnum.TWO));
        verifyNoMoreInteractions(component);
        // items are updated when the aspect value changes
        reset(component);
        when(propertyDispatcher.pull(any())).thenReturn(Arrays.asList(TestEnum.ONE));
        uiUpdater.apply();
        verify(component).setItems(Arrays.asList(TestEnum.ONE));
        verifyNoMoreInteractions(component);
        // items are not updated when the aspect value stays the same
        reset(component);
        uiUpdater.apply();
        verifyNoMoreInteractions(component);
    }

    private static class DataComponent<T> extends Div {

        private static final long serialVersionUID = 1L;

        public void setItems(@SuppressWarnings("unused") List<T> items) {
            // method is only used for verify
        }
    }

}
