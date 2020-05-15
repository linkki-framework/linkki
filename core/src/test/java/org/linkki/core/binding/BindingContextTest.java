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
package org.linkki.core.binding;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.descriptor.aspect.base.TestComponentClickAspectDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.fallback.ExceptionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.reflection.ReflectionPropertyDispatcher;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.columnbased.ColumnBasedComponentFactory;
import org.linkki.core.defaults.columnbased.TestColumnBasedComponentCreator;
import org.linkki.core.defaults.columnbased.pmo.TestContainerPmo;
import org.linkki.core.defaults.columnbased.pmo.TestRowPmo;
import org.linkki.core.defaults.nls.TestComponentWrapper;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.core.defaults.nls.TestUiLayoutComponent;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.matcher.MessageMatchers;
import org.linkki.core.pmo.ButtonPmo;
import org.linkki.core.pmo.PresentationModelObject;
import org.linkki.util.handler.Handler;

public class BindingContextTest {

    private static final String MSG_CODE = "TEST";
    private TestUiComponent field1 = spy(new TestUiComponent());
    private TestUiComponent field2 = spy(new TestUiComponent());

    private WeakReference<TestUiComponent> weakRefComponent;

    private ElementBinding createBinding(BindingContext context) {
        return createBinding(context, new TestPmo(), new TestUiComponent());
    }

    private ElementBinding createBinding(BindingContext context, TestPmo pmo, TestUiComponent component) {
        return new ElementBinding(new TestComponentWrapper(component),
                new ReflectionPropertyDispatcher(() -> pmo, "value",
                        new ExceptionPropertyDispatcher("value", pmo)),
                context::modelChanged, new ArrayList<>());
    }

    @Test
    public void testAdd() {
        BindingContext context = new BindingContext();
        ElementBinding binding = createBinding(context);

        assertEquals(0, context.getBindings().size());
        context.add(binding, TestComponentWrapper.with(binding));
        assertEquals(1, context.getBindings().size());
    }

    @Test
    public void testAdd_DisplayCurrentMessages() {
        BindingContext context = new BindingContext();
        ElementBinding binding = createBinding(context);
        Message message = Message.builder("test", Severity.ERROR)
                .invalidObjectWithProperties(binding.getPmo(), binding.getPropertyDispatcher().getProperty())
                .code(MSG_CODE)
                .create();
        context.displayMessages(new MessageList(message));

        context.add(binding, TestComponentWrapper.with(binding));

        assertThat(((TestUiComponent)binding.getBoundComponent()).getValidationMessages(),
                   MessageMatchers.hasMessage(MSG_CODE));
    }

    @Test
    public void testModelChangedBindings() {
        Handler afterUpdateUi = mock(Handler.class);
        BindingContext context = new BindingContext("", PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER, afterUpdateUi);
        ElementBinding binding = spy(createBinding(context));
        context.add(binding, TestComponentWrapper.with(binding));
        reset(binding);

        context.uiUpdated();

        verify(binding).updateFromPmo();
        verify(afterUpdateUi, never()).apply();
    }

    @Test
    public void testModelChangedBindings_noBindingInContext() {
        Handler afterUpdateUi = mock(Handler.class);

        BindingContext context = new BindingContext("", PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER, afterUpdateUi);
        ElementBinding binding = spy(createBinding(context));

        context.uiUpdated();

        verify(binding, never()).updateFromPmo();
        verify(binding, never()).displayMessages(any(MessageList.class));
        verify(afterUpdateUi, never()).apply();
    }

    @Test
    public void testModelChangedBindingsAndValidate_noBindingInContext() {
        Handler afterUpdateUi = mock(Handler.class);

        BindingContext context = new BindingContext("", PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER, afterUpdateUi);
        context.modelChanged();
        verify(afterUpdateUi).apply();
    }

    @Test
    public void testModelChangedBindingsAndValidate() {
        Handler afterUpdateUi = mock(Handler.class);
        BindingContext context = new BindingContext("", PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER, afterUpdateUi);
        ElementBinding binding = spy(createBinding(context));
        context.add(binding, TestComponentWrapper.with(binding));
        reset(binding);

        context.modelChanged();

        verify(binding).updateFromPmo();
        verify(afterUpdateUi).apply();
    }

    @Test
    public void testChangeBoundObject() {
        BindingContext context = new BindingContext();
        ElementBinding binding = spy(createBinding(context));
        context.uiUpdated();
        verify(binding, never()).updateFromPmo();
        context.add(binding, TestComponentWrapper.with(binding));
        reset(binding);

        context.uiUpdated();

        verify(binding).updateFromPmo();
    }

    @Test
    public void testRemoveBindingsForComponent() {
        BindingContext context = new BindingContext();
        TestPmo testPmo = new TestPmo();
        ElementBinding binding1 = createBinding(context, testPmo, field1);
        ElementBinding binding2 = createBinding(context, testPmo, field2);
        context.add(binding1, TestComponentWrapper.with(binding1));
        context.add(binding2, TestComponentWrapper.with(binding2));

        assertThat(context.getBindings(), hasSize(2));

        TestUiLayoutComponent layout = new TestUiLayoutComponent(field1, field2);

        context.removeBindingsForComponent(layout);
        assertThat(context.getBindings(), is(empty()));
    }

    @Test
    public void testRemoveBindingsForComponent_Container() {
        BindingContext context = new BindingContext();
        TestContainerPmo containerPmo = new TestContainerPmo(new TestRowPmo());
        bindAddItemButton(context, containerPmo);
        TestUiLayoutComponent table = bindTable(context, containerPmo);

        assertThat(context.getBindings(), hasSize(2));
        ContainerBinding binding = (ContainerBinding)context.getBindings().stream()
                .filter(ContainerBinding.class::isInstance).findFirst().get();
        assertThat(binding.getBindings(), hasSize(2));

        context.removeBindingsForComponent(table);
        assertThat(context.getBindings(), hasSize(1));
    }

    @Test
    public void testRemoveBindingsForComponent_InContainerBinding() {
        BindingContext context = new BindingContext();
        TestContainerPmo containerPmo = new TestContainerPmo(new TestRowPmo());
        bindAddItemButton(context, containerPmo);
        TestUiLayoutComponent table = bindTable(context, containerPmo);
        TestUiComponent childComponent = table.getChildren().get(0);

        assertThat(context.getBindings(), hasSize(2));
        ContainerBinding containerBinding = (ContainerBinding)context.getBindings().stream()
                .filter(ContainerBinding.class::isInstance).findFirst().get();
        assertThat(containerBinding.getBindings(), hasSize(2));

        context.removeBindingsForComponent(childComponent);
        assertThat(context.getBindings(), hasSize(2));
        assertThat(containerBinding.getBindings(), hasSize(1));
    }

    private TestUiLayoutComponent bindTable(BindingContext context, TestContainerPmo containerPmo) {
        ColumnBasedComponentFactory columnBasedComponentFactory = new ColumnBasedComponentFactory(
                new TestColumnBasedComponentCreator());
        TestUiLayoutComponent table = (TestUiLayoutComponent)columnBasedComponentFactory
                .createContainerComponent(containerPmo, context);
        return table;
    }

    private void bindAddItemButton(BindingContext context, TestContainerPmo containerPmo) {
        TestButtonPmo addItemButtonPmo = new TestButtonPmo();
        containerPmo.setAddItemButtonPmo(addItemButtonPmo);
        context.bind(addItemButtonPmo, BoundProperty.of(""), Arrays.asList(new TestComponentClickAspectDefinition()),
                     new TestComponentWrapper(new TestUiComponent()));
    }

    @Test
    public void testRemoveBindingsForComponent_Button() {
        BindingContext context = new BindingContext();
        TestPmoWithButton testPmoWithButton = new TestPmoWithButton();
        Optional<ButtonPmo> editButtonPmo = testPmoWithButton.getEditButtonPmo();
        editButtonPmo.ifPresent(buttonPmo -> context.bind(buttonPmo, BoundProperty.of(""),
                                                          Arrays.asList(new TestComponentClickAspectDefinition()),
                                                          new TestComponentWrapper(field1)));

        assertThat(context.getBindings(), hasSize(1));

        context.removeBindingsForComponent(new TestUiLayoutComponent(field1));
        assertThat(context.getBindings(), is(empty()));
    }

    @Test
    public void testRemoveBindingsForPmo() {
        BindingContext context = new BindingContext();

        TestPmo pmo = new TestPmo();
        ElementBinding binding1 = createBinding(context, pmo, field1);
        ElementBinding binding2 = createBinding(context, pmo, field2);
        context.add(binding1, TestComponentWrapper.with(binding1));
        context.add(binding2, TestComponentWrapper.with(binding2));

        assertThat(context.getBindings(), hasSize(2));

        context.removeBindingsForPmo(pmo);
        assertThat(context.getBindings(), is(empty()));
    }

    @Test
    public void testRemoveBindingsForPmo_Container() {
        BindingContext context = new BindingContext();
        TestContainerPmo containerPmo = new TestContainerPmo(new TestRowPmo());
        bindAddItemButton(context, containerPmo);
        bindTable(context, containerPmo);

        assertThat(context.getBindings(), hasSize(2));
        ContainerBinding binding = (ContainerBinding)context.getBindings().stream()
                .filter(ContainerBinding.class::isInstance).findFirst().get();
        assertThat(binding.getBindings(), hasSize(2));
        context.removeBindingsForPmo(containerPmo);
        assertThat(context.getBindings(), is(empty()));
    }

    @Test
    public void testRemoveBindingsForPmo_Button() {
        BindingContext context = new BindingContext();
        TestPmoWithButton testPmoWithButton = new TestPmoWithButton();
        Optional<ButtonPmo> editButtonPmo = testPmoWithButton.getEditButtonPmo();
        editButtonPmo.ifPresent(buttonPmo -> context.bind(buttonPmo, BoundProperty.of(""),
                                                          Arrays.asList(new TestComponentClickAspectDefinition()),
                                                          new TestComponentWrapper(field1)));

        assertThat(context.getBindings(), hasSize(1));
        context.removeBindingsForPmo(testPmoWithButton);
        assertThat(context.getBindings(), is(empty()));
    }

    @Test
    public void testBind_ButtonPmoBindningToCheckUpdateFromPmo() {
        BindingContext context = new BindingContext();
        TestButtonPmo buttonPmo = new TestButtonPmo();
        TestUiComponent button = new TestUiComponent();
        buttonPmo.setEnabled(false);

        ComponentWrapper buttonWrapper = new TestComponentWrapper(button);
        context.bind(buttonPmo, BoundProperty.of(""), Arrays.asList(new EnabledAspectDefinition(EnabledType.DYNAMIC)),
                     buttonWrapper);

        assertThat(button.isEnabled(), is(false));
    }

    @Test
    public void testBind_WeakReferencedBinding() {
        BindingContext context = new BindingContext();
        ReferenceQueue<TestUiComponent> referenceQueue = new ReferenceQueue<>();
        Thread refRemoveThread = new Thread(() -> {

            try {
                TestUiComponent removed = referenceQueue.remove(10_000).get();
                assertThat(weakRefComponent.get(), is(removed));
            } catch (IllegalArgumentException | InterruptedException e) {
                throw new RuntimeException(e);
            }

        });
        refRemoveThread.start();

        weakRefComponent = setupBindingForWeakRefTest(context, referenceQueue);

        System.gc();

        try {
            refRemoveThread.join(10_000);
        } catch (InterruptedException e) {
            fail(e);
        }
        assertThat(context.getBindings(), is(empty()));
    }

    private WeakReference<TestUiComponent> setupBindingForWeakRefTest(BindingContext context,
            ReferenceQueue<TestUiComponent> referenceQueue) {
        TestButtonPmo buttonPmo = new TestButtonPmo();
        TestUiComponent button = new TestUiComponent();
        buttonPmo.setEnabled(false);

        ComponentWrapper buttonWrapper = new TestComponentWrapper(button);
        context.bind(buttonPmo, BoundProperty.of(""),
                     Arrays.asList(new EnabledAspectDefinition(EnabledType.DYNAMIC)),
                     buttonWrapper);
        WeakReference<TestUiComponent> weakReference = new WeakReference<>(button, referenceQueue);
        assertThat(weakReference.get(), is(button));
        assertThat(context.getBindings(), is(not(empty())));
        return weakReference;
    }

    public static class TestPmoWithButton implements PresentationModelObject {

        private static final ButtonPmo NOP_BUTTON_PMO = new TestButtonPmo();

        @Override
        public Optional<ButtonPmo> getEditButtonPmo() {
            return Optional.of(NOP_BUTTON_PMO);
        }
    }
}
