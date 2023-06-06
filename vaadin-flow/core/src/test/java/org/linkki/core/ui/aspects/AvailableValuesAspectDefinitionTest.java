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
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
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

class AvailableValuesAspectDefinitionTest {

    private static final BiConsumer<HasListDataView<Object, ?>, List<Object>> NOP = (c, p) -> {
        /* NOP */
    };

    @Test
    void testConstructorWithoutAvailableValuesType() {
        Assertions.assertThrows(NullPointerException.class, () -> new AvailableValuesAspectDefinition<>(null, NOP));
    }

    @Test
    void testGetAvailableValuesType() {
        for (AvailableValuesType type : AvailableValuesType.values()) {
            assertThat(new AvailableValuesAspectDefinition<>(type, NOP).getAvailableValuesType(), is(type));
        }
    }

    @Test
    void testGetValuesDerivedFromDatatype_NonEnumDatatype_Null() {

        AvailableValuesAspectDefinition<HasListDataView<Object, ?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.DYNAMIC,
                NOP);

        List<?> availableValues = availableValuesAspectDefinition.getValuesDerivedFromDatatype(String.class);

        assertThat(availableValues, is(nullValue()));
    }

    @Test
    void testGetValuesDerivedFromDatatype() {
        AvailableValuesAspectDefinition<HasListDataView<Object, ?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.DYNAMIC, NOP);

        assertThat(availableValuesAspectDefinition.getValuesDerivedFromDatatype(TestEnum.class),
                   contains(TestEnum.ONE, TestEnum.TWO, TestEnum.THREE, TestEnum.EMPTY));
        assertThat(availableValuesAspectDefinition.getValuesDerivedFromDatatype(Boolean.class),
                   contains(null, true, false));
        assertThat(availableValuesAspectDefinition.getValuesDerivedFromDatatype(boolean.class),
                   contains(true, false));
    }

    @Test
    void testSetDataProvider() {
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
    void testHandleNullItems() {
        AvailableValuesAspectDefinition<HasListDataView<Object, ?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.DYNAMIC, NOP);

        @SuppressWarnings("unchecked")
        List<Object> items = mock(List.class);
        @SuppressWarnings("unchecked")
        ComboBox<Object> component = mock(ComboBox.class);
        availableValuesAspectDefinition
                .handleNullItems(new NoLabelComponentWrapper(component, WrapperType.FIELD), items);

        // is called by VaadinComponentWrapper#workaroundVaadinClientValidation, must be removed when
        // workaround is fixed
        verify(component).addClientValidatedEventListener(any());
        verifyNoMoreInteractions(component, items);
    }

    @Test
    void testCreateAspect_Dynamic() {
        AvailableValuesAspectDefinition<HasListDataView<Object, ?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.DYNAMIC, NOP);

        Aspect<Collection<?>> aspect = availableValuesAspectDefinition.createAspect(TestEnum.class);

        assertThat(aspect.getName(), is(AvailableValuesAspectDefinition.NAME));
        assertThat(aspect.isValuePresent(), is(false));
    }

    @Test
    void testCreateAspect_NoValues() {
        AvailableValuesAspectDefinition<HasListDataView<Object, ?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.NO_VALUES, NOP);

        Aspect<Collection<?>> aspect = availableValuesAspectDefinition.createAspect(TestEnum.class);

        assertThat(aspect.getName(), is(AvailableValuesAspectDefinition.NAME));
        assertThat(aspect.isValuePresent(), is(true));
        assertThat(aspect.getValue(), is(empty()));
    }

    @Test
    void testCreateAspect_EnumValuesExclNull() {
        AvailableValuesAspectDefinition<HasListDataView<Object, ?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.ENUM_VALUES_EXCL_NULL, NOP);

        Aspect<Collection<?>> aspect = availableValuesAspectDefinition.createAspect(TestEnum.class);

        assertThat(aspect.getName(), is(AvailableValuesAspectDefinition.NAME));
        assertThat(aspect.isValuePresent(), is(true));
        assertThat(aspect.getValue(), contains(TestEnum.ONE, TestEnum.TWO, TestEnum.THREE, TestEnum.EMPTY));
    }

    @Test
    void testCreateAspect_EnumValuesInclNull() {
        AvailableValuesAspectDefinition<HasListDataView<Object, ?>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.ENUM_VALUES_INCL_NULL, NOP);

        Aspect<Collection<?>> aspect = availableValuesAspectDefinition.createAspect(TestEnum.class);

        assertThat(aspect.getName(), is(AvailableValuesAspectDefinition.NAME));
        assertThat(aspect.isValuePresent(), is(true));
        assertThat(aspect.getValue(), contains(null, TestEnum.ONE, TestEnum.TWO, TestEnum.THREE, TestEnum.EMPTY));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testUiUpdater() {
        AvailableValuesAspectDefinition<DataComponent<Object>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.DYNAMIC, DataComponent<Object>::setItems);
        DataComponent<TestEnum> component = spy(new DataComponent<>());
        PropertyDispatcher propertyDispatcher = mock(PropertyDispatcher.class);
        when(propertyDispatcher.pull(any())).thenReturn(Arrays.asList(TestEnum.ONE, TestEnum.TWO));

        Handler uiUpdater = availableValuesAspectDefinition.createUiUpdater(propertyDispatcher,
                                                                            new NoLabelComponentWrapper(component));

        // items are set initially during the first update
        reset(component);
        uiUpdater.apply();
        verify(component).setItems(List.of(TestEnum.ONE, TestEnum.TWO));
        verifyNoMoreInteractions(component);
        // items are updated when the aspect value changes
        reset(component);
        when(propertyDispatcher.pull(any())).thenReturn(List.of(TestEnum.ONE));
        uiUpdater.apply();
        verify(component).setItems(List.of(TestEnum.ONE));
        verifyNoMoreInteractions(component);
        // items are not updated when the aspect value stays the same
        reset(component);
        uiUpdater.apply();
        verifyNoMoreInteractions(component);
    }

    @Test
    void testUiUpdater_NullOnPull_WhenDynamic_NullPointerException() {
        AvailableValuesAspectDefinition<DataComponent<Object>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.DYNAMIC, DataComponent<Object>::setItems);
        DataComponent<?> component = spy(new DataComponent<>());
        PropertyDispatcher propertyDispatcher = mock(PropertyDispatcher.class);
        when(propertyDispatcher.pull(any())).thenReturn(null);

        Handler uiUpdater = availableValuesAspectDefinition.createUiUpdater(propertyDispatcher,
                                                                            new NoLabelComponentWrapper(component));

        assertThrows(NullPointerException.class, () -> uiUpdater.apply());
    }

    @Test
    void testUiUpdater_NullOnPull_WhenNotDynamic_IllegalStateException() {
        AvailableValuesAspectDefinition<DataComponent<Object>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.ENUM_VALUES_INCL_NULL, DataComponent<Object>::setItems);
        DataComponent<?> component = spy(new DataComponent<>());
        PropertyDispatcher propertyDispatcher = mock(PropertyDispatcher.class);
        when(propertyDispatcher.pull(any())).thenReturn(null);
        doReturn(DataComponent.class).when(propertyDispatcher).getValueClass();

        Handler uiUpdater = availableValuesAspectDefinition.createUiUpdater(propertyDispatcher,
                                                                            new NoLabelComponentWrapper(component));

        assertThrows(IllegalStateException.class, () -> uiUpdater.apply());
    }

    @Test
    void testUiUpdater_SetComboBoxValueBeforeUiUpdate() {
        AvailableValuesAspectDefinition<ComboBox<Object>> availableValuesAspectDefinition = new AvailableValuesAspectDefinition<>(
                AvailableValuesType.DYNAMIC, ComboBox::setItems);
        ComboBox<TestEnum> component = new ComboBox<>();

        availableValuesAspectDefinition.createUiUpdater(mock(PropertyDispatcher.class),
                                                        new NoLabelComponentWrapper(component));

        // linkki sets values before the UI updater has been called for the first time
        component.setValue(TestEnum.ONE);
    }

    private static class DataComponent<T> extends Div {

        private static final long serialVersionUID = 1L;

        public void setItems(@SuppressWarnings("unused") List<T> items) {
            // method is only used for verify
        }
    }

}
