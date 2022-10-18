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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext.BindingContextBuilder;
import org.linkki.core.binding.descriptor.BindingDescriptor;
import org.linkki.core.binding.descriptor.aspect.base.TestComponentClickAspectDefinition;
import org.linkki.core.binding.descriptor.messagehandler.DefaultMessageHandler;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehavior;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.fallback.ExceptionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.reflection.ReflectionPropertyDispatcher;
import org.linkki.core.binding.validation.message.Message;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.validation.message.Severity;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.columnbased.TestColumnBasedComponentFactory;
import org.linkki.core.defaults.columnbased.pmo.TestContainerPmo;
import org.linkki.core.defaults.columnbased.pmo.TestRowPmo;
import org.linkki.core.defaults.nls.TestComponentWrapper;
import org.linkki.core.defaults.nls.TestUiComponent;
import org.linkki.core.defaults.nls.TestUiLayoutComponent;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.pmo.ButtonPmo;
import org.linkki.core.pmo.PresentationModelObject;
import org.linkki.util.handler.Handler;

public class BindingContextTest {

    private static final String MSG_CODE = "TEST";
    private final TestUiComponent field1 = spy(new TestUiComponent());
    private final TestUiComponent field2 = spy(new TestUiComponent());

    private WeakReference<TestUiComponent> weakRefComponent;
    private final BindingDescriptor clickBindingDescriptor = new BindingDescriptor(BoundProperty.empty(),
            new TestComponentClickAspectDefinition());
    private final BindingDescriptor enabledBindingDescriptor = new BindingDescriptor(BoundProperty.empty(),
            new EnabledAspectDefinition(EnabledType.DYNAMIC));

    private ElementBinding createBinding(BindingContext context) {
        return createBinding(context, new TestPmo(), new TestUiComponent());
    }

    private ElementBinding createBinding(BindingContext context, TestPmo pmo, TestUiComponent component) {
        return new ElementBinding(new TestComponentWrapper(component),
                new ReflectionPropertyDispatcher(() -> pmo, "value",
                        new ExceptionPropertyDispatcher("value", pmo)),
                context::modelChanged, new ArrayList<>(), new DefaultMessageHandler());
    }

    @Test
    void testAdd() {
        BindingContext context = new BindingContext();
        ElementBinding binding = createBinding(context);

        assertThat(context.getBindings()).isEmpty();
        context.add(binding, TestComponentWrapper.with(binding));
        assertThat(context.getBindings()).hasSize(1);
    }

    @Test
    void testAdd_DisplayCurrentMessages() {
        BindingContext context = new BindingContext();
        ElementBinding binding = createBinding(context);
        Message message = Message.builder("test", Severity.ERROR)
                .invalidObjectWithProperties(binding.getPmo(), binding.getPropertyDispatcher().getProperty())
                .code(MSG_CODE)
                .create();
        context.displayMessages(new MessageList(message));

        context.add(binding, TestComponentWrapper.with(binding));

        assertThat(((TestUiComponent)binding.getBoundComponent()).getValidationMessages().stream().map(Message::getCode)
                .anyMatch(MSG_CODE::equals)).isTrue();
    }

    @Test
    void testModelChangedBindings() {
        Handler afterUpdateUi = mock(Handler.class);
        BindingContext context = new BindingContextBuilder().afterUpdateHandler(afterUpdateUi).build();
        ElementBinding binding = spy(createBinding(context));
        context.add(binding, TestComponentWrapper.with(binding));
        reset(binding);

        context.uiUpdated();

        verify(binding).updateFromPmo();
        verify(afterUpdateUi, never()).apply();
    }

    @Test
    void testModelChangedBindings_noBindingInContext() {
        Handler afterUpdateUi = mock(Handler.class);

        BindingContext context = new BindingContextBuilder().afterUpdateHandler(afterUpdateUi).build();
        ElementBinding binding = spy(createBinding(context));

        context.uiUpdated();

        verify(binding, never()).updateFromPmo();
        verify(binding, never()).displayMessages(any(MessageList.class));
        verify(afterUpdateUi, never()).apply();
    }

    @Test
    void testModelChangedBindingsAndValidate_noBindingInContext() {
        Handler afterUpdateUi = mock(Handler.class);

        BindingContext context = new BindingContextBuilder().afterModelChangedHandler(afterUpdateUi).build();
        context.modelChanged();
        verify(afterUpdateUi).apply();
    }

    @Test
    void testModelChangedBindingsAndValidate() {
        Handler afterModelChangedHandler = mock(Handler.class);
        BindingContext context = new BindingContextBuilder().afterModelChangedHandler(afterModelChangedHandler).build();
        ElementBinding binding = spy(createBinding(context));
        context.add(binding, TestComponentWrapper.with(binding));
        reset(binding);

        context.modelChanged();

        verify(binding).updateFromPmo();
        verify(afterModelChangedHandler).apply();
    }

    @Test
    void testUpdateUiBindingsAndValidate() {
        Handler afterUpdateUi = mock(Handler.class);
        BindingContext context = new BindingContextBuilder().afterUpdateHandler(afterUpdateUi).build();
        ElementBinding binding = spy(createBinding(context));
        context.add(binding, TestComponentWrapper.with(binding));
        reset(binding);

        context.updateUi();

        verify(binding).updateFromPmo();
        verify(afterUpdateUi).apply();
    }

    @Test
    void testChangeBoundObject() {
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
    void testRemoveBindingsForComponent() {
        BindingContext context = new BindingContext();
        TestPmo testPmo = new TestPmo();
        ElementBinding binding1 = createBinding(context, testPmo, field1);
        ElementBinding binding2 = createBinding(context, testPmo, field2);
        context.add(binding1, TestComponentWrapper.with(binding1));
        context.add(binding2, TestComponentWrapper.with(binding2));

        assertThat(context.getBindings()).hasSize(2);

        TestUiLayoutComponent layout = new TestUiLayoutComponent(field1, field2);

        context.removeBindingsForComponent(layout);
        assertThat(context.getBindings()).isEmpty();
    }

    @Test
    void testRemoveBindingsForComponent_Container() {
        BindingContext context = new BindingContext();
        TestContainerPmo containerPmo = new TestContainerPmo(new TestRowPmo());
        bindAddItemButton(context, containerPmo);
        TestUiLayoutComponent table = bindTable(context, containerPmo);

        assertThat(context.getBindings()).hasSize(2);
        ContainerBinding binding = (ContainerBinding)context.getBindings().stream()
                .filter(ContainerBinding.class::isInstance).findFirst().get();
        assertThat(binding.getBindings()).hasSize(2);

        context.removeBindingsForComponent(table);
        assertThat(context.getBindings()).hasSize(1);
    }

    @Test
    void testRemoveBindingsForComponent_InContainerBinding() {
        BindingContext context = new BindingContext();
        TestContainerPmo containerPmo = new TestContainerPmo(new TestRowPmo());
        bindAddItemButton(context, containerPmo);
        TestUiLayoutComponent table = bindTable(context, containerPmo);
        TestUiComponent childComponent = table.getChildren().get(0);

        assertThat(context.getBindings()).hasSize(2);
        ContainerBinding containerBinding = (ContainerBinding)context.getBindings().stream()
                .filter(ContainerBinding.class::isInstance).findFirst().get();
        assertThat(containerBinding.getBindings()).hasSize(2);

        context.removeBindingsForComponent(childComponent);
        assertThat(context.getBindings()).hasSize(2);
        assertThat(containerBinding.getBindings()).hasSize(1);
    }

    @Test
    void testRemoveBindingsForComponent_Button() {
        BindingContext context = new BindingContext();
        TestPmoWithButton testPmoWithButton = new TestPmoWithButton();
        Optional<ButtonPmo> editButtonPmo = testPmoWithButton.getEditButtonPmo();
        editButtonPmo.ifPresent(buttonPmo -> context.bind(buttonPmo, clickBindingDescriptor,
                                                          new TestComponentWrapper(field1)));

        assertThat(context.getBindings()).hasSize(1);

        context.removeBindingsForComponent(new TestUiLayoutComponent(field1));
        assertThat(context.getBindings()).isEmpty();
    }

    @Test
    void testRemoveBindingsForPmo() {
        BindingContext context = new BindingContext();

        TestPmo pmo = new TestPmo();
        ElementBinding binding1 = createBinding(context, pmo, field1);
        ElementBinding binding2 = createBinding(context, pmo, field2);
        context.add(binding1, TestComponentWrapper.with(binding1));
        context.add(binding2, TestComponentWrapper.with(binding2));

        assertThat(context.getBindings()).hasSize(2);

        context.removeBindingsForPmo(pmo);
        assertThat(context.getBindings()).isEmpty();
    }

    @Test
    void testRemoveBindingsForPmo_Container() {
        BindingContext context = new BindingContext();
        TestContainerPmo containerPmo = new TestContainerPmo(new TestRowPmo());
        bindAddItemButton(context, containerPmo);
        bindTable(context, containerPmo);

        assertThat(context.getBindings()).hasSize(2);
        ContainerBinding binding = (ContainerBinding)context.getBindings().stream()
                .filter(ContainerBinding.class::isInstance).findFirst().get();
        assertThat(binding.getBindings()).hasSize(2);
        context.removeBindingsForPmo(containerPmo);
        assertThat(context.getBindings()).isEmpty();
    }

    @Test
    void testRemoveBindingsForPmo_Button() {
        BindingContext context = new BindingContext();
        TestPmoWithButton testPmoWithButton = new TestPmoWithButton();
        Optional<ButtonPmo> editButtonPmo = testPmoWithButton.getEditButtonPmo();
        editButtonPmo.ifPresent(buttonPmo -> context.bind(buttonPmo, clickBindingDescriptor,
                                                          new TestComponentWrapper(field1)));

        assertThat(context.getBindings()).hasSize(1);
        context.removeBindingsForPmo(testPmoWithButton);
        assertThat(context.getBindings()).isEmpty();
    }

    @Test
    void testBind_ButtonPmoBindningToCheckUpdateFromPmo() {
        BindingContext context = new BindingContext();
        TestButtonPmo buttonPmo = new TestButtonPmo();
        TestUiComponent button = new TestUiComponent();
        buttonPmo.setEnabled(false);

        ComponentWrapper buttonWrapper = new TestComponentWrapper(button);
        context.bind(buttonPmo,
                     enabledBindingDescriptor,
                     buttonWrapper);

        assertThat(button.isEnabled()).isFalse();
    }

    @Test
    void testBind_WeakReferencedBinding() {
        BindingContext context = new BindingContext();
        ReferenceQueue<TestUiComponent> referenceQueue = new ReferenceQueue<>();
        Thread refRemoveThread = new Thread(() -> {

            try {
                TestUiComponent removed = referenceQueue.remove(10_000).get();
                assertThat(weakRefComponent.get()).isEqualTo(removed);
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
        assertThat(context.getBindings()).isEmpty();
    }

    @Test
    void testBuilder_WithDefaultNameAndBehaviorProdiver() {
        BindingContext context = new BindingContextBuilder().build();

        assertThat(context.getName()).isEmpty();
        assertThat(context.getBehaviorProvider()).isEqualTo(PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER);
    }

    @Test
    void testBuilder_WithName() {
        BindingContext context = new BindingContextBuilder().name("testBindingContext").build();

        assertThat(context.getName()).isEqualTo("testBindingContext");
    }

    @Test
    void testBuilder_WithNameAndPropertyBehaviorProvider() {
        PropertyBehaviorProvider testPropertyBehaviorProvider = new PropertyBehaviorProvider() {
            @Override
            public Collection<PropertyBehavior> getBehaviors() {
                return Collections.emptyList();
            }

        };
        BindingContext context = new BindingContextBuilder().propertyBehaviorProvider(testPropertyBehaviorProvider)
                .build();

        assertThat(context.getBehaviorProvider()).isEqualTo(testPropertyBehaviorProvider);
    }

    @Test
    void testBuilder_WithPropertyDispatcherFactory() {
        AtomicBoolean createDispatcherChainCalled = new AtomicBoolean(false);
        PropertyDispatcherFactory testPropertyDispatcherFactory = new PropertyDispatcherFactory() {
            @Override
            public PropertyDispatcher createDispatcherChain(Object pmo,
                    BoundProperty boundProperty,
                    PropertyBehaviorProvider behaviorProvider) {
                createDispatcherChainCalled.set(true);
                return super.createDispatcherChain(pmo, boundProperty, behaviorProvider);
            }
        };

        BindingContext context = new BindingContextBuilder().propertyDispatcherFactory(testPropertyDispatcherFactory)
                .build();

        TestButtonPmo buttonPmo = new TestButtonPmo();
        TestUiComponent button = new TestUiComponent();
        buttonPmo.setEnabled(false);
        ComponentWrapper buttonWrapper = new TestComponentWrapper(button);
        context.bind(buttonPmo, enabledBindingDescriptor,
                     buttonWrapper);

        assertThat(createDispatcherChainCalled.get()).isTrue();
    }

    @Test
    void testModelChanged_BothHandlersShouldBeCalled() {
        AtomicBoolean afterModelChangedHandlerCalled = new AtomicBoolean(false);
        AtomicBoolean afterUpdateHandlerCalled = new AtomicBoolean(false);
        BindingContext context = new BindingContextBuilder()
                .afterModelChangedHandler(() -> afterModelChangedHandlerCalled.set(true))
                .afterUpdateHandler(() -> afterUpdateHandlerCalled.set(true))
                .build();

        context.modelChanged();

        assertThat(afterModelChangedHandlerCalled.get()).isTrue();
        assertThat(afterUpdateHandlerCalled.get()).isTrue();
    }

    @Test
    void testUpdateUi_WithAfterModelChangedHandler_ShouldNotBeCalled() {
        AtomicBoolean afterModelChangedHandlerCalled = new AtomicBoolean(false);
        AtomicBoolean afterUpdateHandlerCalled = new AtomicBoolean(false);
        BindingContext context = new BindingContextBuilder()
                .afterModelChangedHandler(() -> afterModelChangedHandlerCalled.set(true))
                .afterUpdateHandler(() -> afterUpdateHandlerCalled.set(true))
                .build();

        context.updateUi();

        assertThat(afterModelChangedHandlerCalled.get()).isFalse();
        assertThat(afterUpdateHandlerCalled.get()).isTrue();
    }

    @Test
    void testBuilder_RequiredNonNullValues() {
        BindingContextBuilder builder = new BindingContextBuilder();

        assertThatThrownBy(() -> builder.name(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> builder.propertyBehaviorProvider(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> builder.propertyDispatcherFactory(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> builder.afterUpdateHandler(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> builder.afterModelChangedHandler(null)).isInstanceOf(NullPointerException.class);
    }

    private WeakReference<TestUiComponent> setupBindingForWeakRefTest(BindingContext context,
            ReferenceQueue<TestUiComponent> referenceQueue) {
        TestButtonPmo buttonPmo = new TestButtonPmo();
        TestUiComponent button = new TestUiComponent();
        buttonPmo.setEnabled(false);

        ComponentWrapper buttonWrapper = new TestComponentWrapper(button);
        context.bind(buttonPmo, new BindingDescriptor(BoundProperty.empty(),
                new EnabledAspectDefinition(EnabledType.DYNAMIC)),
                     buttonWrapper);
        WeakReference<TestUiComponent> weakReference = new WeakReference<>(button, referenceQueue);
        assertThat(weakReference.get()).isEqualTo(button);
        assertThat(context.getBindings()).isNotEmpty();
        return weakReference;
    }

    private TestUiLayoutComponent bindTable(BindingContext context, TestContainerPmo containerPmo) {
        TestColumnBasedComponentFactory columnBasedComponentFactory = new TestColumnBasedComponentFactory();
        TestUiLayoutComponent table = (TestUiLayoutComponent)columnBasedComponentFactory
                .createContainerComponent(containerPmo, context);
        return table;
    }

    private void bindAddItemButton(BindingContext context, TestContainerPmo containerPmo) {
        TestButtonPmo addItemButtonPmo = new TestButtonPmo();
        containerPmo.setAddItemButtonPmo(addItemButtonPmo);
        context.bind(addItemButtonPmo, clickBindingDescriptor,
                     new TestComponentWrapper(new TestUiComponent()));
    }

    public static class TestPmoWithButton implements PresentationModelObject {

        private static final ButtonPmo NOP_BUTTON_PMO = new TestButtonPmo();

        @Override
        public Optional<ButtonPmo> getEditButtonPmo() {
            return Optional.of(NOP_BUTTON_PMO);
        }
    }

}
