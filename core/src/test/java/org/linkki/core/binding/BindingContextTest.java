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

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.After;
import org.junit.Test;
import org.linkki.core.ButtonPmo;
import org.linkki.core.PresentationModelObject;
import org.linkki.core.binding.ButtonPmoBindingTest.TestButtonPmo;
import org.linkki.core.binding.dispatcher.ExceptionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.ReflectionPropertyDispatcher;
import org.linkki.core.message.MessageList;
import org.linkki.core.ui.components.LabelComponentWrapper;
import org.linkki.core.ui.section.BaseSection;
import org.linkki.core.ui.section.PmoBasedSectionFactory;
import org.linkki.core.ui.section.annotations.BindingDefinition;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.VisibleType;
import org.linkki.core.ui.section.descriptor.ElementDescriptor;
import org.linkki.core.ui.table.PmoBasedTableFactory;
import org.linkki.core.ui.table.TestRowPmo;
import org.linkki.core.ui.table.TestTablePmo;
import org.linkki.core.ui.util.ComponentFactory;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class BindingContextTest {

    private TextField field1 = spy(new TextField());
    private TextField field2 = spy(new TextField());

    @After
    public void cleanUpUi() {
        UI.setCurrent(null);
    }

    private ComponentBinding createBinding(TestBindingContext context) {
        return createBinding(context, new TestPmo(), new TextField());
    }

    private ComponentBinding createBinding(TestBindingContext context, TestPmo pmo, Component component) {
        return new ComponentBinding(new LabelComponentWrapper(component),
                new ReflectionPropertyDispatcher(() -> pmo, "value", new ExceptionPropertyDispatcher("value", pmo)),
                context::modelChanged, new ArrayList<>());
    }

    @Test
    public void testAdd() {
        TestBindingContext context = TestBindingContext.create();
        ComponentBinding binding = createBinding(context);

        assertEquals(0, context.getBindings().size());
        context.add(binding);
        assertEquals(1, context.getBindings().size());
    }

    @Test
    public void testModelChangedBindings() {
        Handler afterUpdateUi = mock(Handler.class);
        TestBindingContext context = TestBindingContext.create(afterUpdateUi);
        ComponentBinding binding = spy(createBinding(context));

        context.add(binding);

        context.uiUpdated();
        verify(binding).updateFromPmo();
        verify(afterUpdateUi, never()).apply();
    }

    @Test
    public void testModelChangedBindings_noBindingInContext() {
        Handler afterUpdateUi = mock(Handler.class);

        TestBindingContext context = TestBindingContext.create(afterUpdateUi);
        ComponentBinding binding = spy(createBinding(context));

        context.uiUpdated();

        verify(binding, never()).updateFromPmo();
        verify(binding, never()).displayMessages(any(MessageList.class));
        verify(afterUpdateUi, never()).apply();
    }

    @Test
    public void testModelChangedBindingsAndValidate_noBindingInContext() {
        Handler afterUpdateUi = mock(Handler.class);

        TestBindingContext context = TestBindingContext.create(afterUpdateUi);
        context.modelChanged();
        verify(afterUpdateUi).apply();
    }

    @Test
    public void testModelChangedBindingsAndValidate() {
        Handler afterUpdateUi = mock(Handler.class);

        TestBindingContext context = TestBindingContext.create(afterUpdateUi);
        ComponentBinding binding = spy(createBinding(context));

        context.add(binding);

        context.modelChanged();
        verify(binding).updateFromPmo();
        verify(afterUpdateUi).apply();
    }

    @Test
    public void testChangeBoundObject() {
        TestBindingContext context = TestBindingContext.create();
        ComponentBinding binding = spy(createBinding(context));

        context.uiUpdated();
        verify(binding, never()).updateFromPmo();

        context.add(binding);

        context.uiUpdated();
        verify(binding).updateFromPmo();
    }

    @Test
    public void testRemoveBindingsForComponent() {
        TestBindingContext context = TestBindingContext.create();
        TestPmo testPmo = new TestPmo();
        ComponentBinding binding1 = createBinding(context, testPmo, field1);
        ComponentBinding binding2 = createBinding(context, testPmo, field2);
        context.add(binding1);
        context.add(binding2);

        assertThat(context.getBindings(), hasSize(2));

        VerticalLayout layout = new VerticalLayout(field1, field2);

        context.removeBindingsForComponent(layout);
        assertThat(context.getBindings(), is(empty()));
    }

    @Test
    public void testRemoveBindingsForComponent_Container() {
        TestBindingContext context = TestBindingContext.create();
        TestTablePmo tablePmo = new TestTablePmo();
        tablePmo.addItem();
        Table table = new PmoBasedTableFactory<>(tablePmo, context).createTable();
        UI ui = MockUi.mockUi();
        table.setParent(ui);
        table.attach();

        assertThat(context.getBindings(), hasSize(1));
        Binding binding = context.getBindings().iterator().next();
        assertThat(binding, is(instanceOf(TableBinding.class)));
        @SuppressWarnings("unchecked")
        TableBinding<TestRowPmo> tableBinding = (TableBinding<TestRowPmo>)binding;
        assertThat(tableBinding.getBindings(), hasSize(3));

        context.removeBindingsForComponent(table);
        assertThat(context.getBindings(), is(empty()));
        assertThat(tableBinding.getBindings(), is(empty()));
    }

    @Test
    public void testRemoveBindingsForComponent_Button() {
        TestBindingContext context = TestBindingContext.create();
        TestPmoWithButton testPmoWithButton = new TestPmoWithButton();
        BaseSection section = new PmoBasedSectionFactory().createBaseSection(testPmoWithButton, context);

        assertThat(context.getBindings(), hasSize(1));

        context.removeBindingsForComponent(section);
        assertThat(context.getBindings(), is(empty()));
    }

    @Test
    public void testRemoveBindingsForPmo() {
        TestBindingContext context = TestBindingContext.create();

        TestPmo pmo = new TestPmo();
        Binding binding1 = createBinding(context, pmo, field1);
        Binding binding2 = createBinding(context, pmo, field2);
        context.add(binding1);
        context.add(binding2);

        assertThat(context.getBindings(), hasSize(2));

        context.removeBindingsForPmo(pmo);
        assertThat(context.getBindings(), is(empty()));
    }

    @Test
    public void testRemoveBindingsForPmo_Container() {
        TestBindingContext context = TestBindingContext.create();
        TestTablePmo tablePmo = new TestTablePmo();
        tablePmo.addItem();
        Table table = new PmoBasedTableFactory<>(tablePmo, context).createTable();
        UI ui = MockUi.mockUi();
        table.setParent(ui);
        table.attach();

        assertThat(context.getBindings(), hasSize(1));
        Binding binding = context.getBindings().iterator().next();
        assertThat(binding, is(instanceOf(TableBinding.class)));
        @SuppressWarnings("unchecked")
        TableBinding<TestRowPmo> tableBinding = (TableBinding<TestRowPmo>)binding;
        assertThat(tableBinding.getBindings(), hasSize(3));

        context.removeBindingsForPmo(tablePmo);
        assertThat(context.getBindings(), is(empty()));
        assertThat(tableBinding.getBindings(), is(empty()));
    }

    @Test
    public void testRemoveBindingsForPmo_Button() {
        TestBindingContext context = TestBindingContext.create();
        TestPmoWithButton testPmoWithButton = new TestPmoWithButton();
        new PmoBasedSectionFactory().createBaseSection(testPmoWithButton, context);

        assertThat(context.getBindings(), hasSize(1));

        context.removeBindingsForPmo(testPmoWithButton);
        assertThat(context.getBindings(), is(empty()));
    }

    @Test
    public void testBind_BoundComponentsAreMadeImmediate() {
        TextField field = new TextField();
        BindingDefinition fieldDefintion = mock(BindingDefinition.class);
        when(fieldDefintion.required()).thenReturn(RequiredType.REQUIRED);
        when(fieldDefintion.enabled()).thenReturn(EnabledType.ENABLED);
        when(fieldDefintion.visible()).thenReturn(VisibleType.VISIBLE);
        ElementDescriptor fieldDescriptor = new ElementDescriptor(fieldDefintion, "value", Void.class,
                new ArrayList<>());

        // Precondition
        assertThat(field.isImmediate(), is(false));

        TestBindingContext context = TestBindingContext.create();
        context.bind(new TestPmo(), fieldDescriptor, new LabelComponentWrapper(field));
        assertThat(field.isImmediate(), is(true));
    }

    @Test
    public void testBind_ButtonPmoBindningToCheckUpdateFromPmo() {
        TestBindingContext context = TestBindingContext.create();
        TestButtonPmo buttonPmo = new TestButtonPmo();
        Button button = ComponentFactory.newButton(buttonPmo.getButtonIcon(), buttonPmo.getStyleNames());
        buttonPmo.enabled = false;

        context.bind(buttonPmo, button);

        assertThat(button.isEnabled(), is(false));
    }

    public static class TestPmoWithButton implements PresentationModelObject {

        private final ButtonPmo buttonPmo = () -> {
            /* nothing to do */
        };

        @Override
        public Optional<ButtonPmo> getEditButtonPmo() {
            return Optional.of(buttonPmo);
        }
    }
}
