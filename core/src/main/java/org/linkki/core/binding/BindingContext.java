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

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import org.linkki.core.ButtonPmo;
import org.linkki.core.binding.behavior.PropertyBehavior;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.message.MessageList;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.section.descriptor.BindingDescriptor;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;

/**
 * A binding context binds fields and tables in a part of the user interface like a page or a dialog
 * to properties of presentation model objects. If the value in one of the fields is changed, all
 * fields in the context are updated from the presentation model objects via their bindings.
 * <p>
 * {@linkplain BindingContext}s are usually managed by a {@link BindingManager} that handles events
 * across multiple contexts.
 * 
 * @see BindingManager#getExistingContextOrStartNewOne(String)
 */
public class BindingContext implements UiUpdateObserver {

    private final String name;
    private final PropertyBehaviorProvider behaviorProvider;
    private final Handler afterUpdateHandler;

    private final Map<Object, ElementBinding> elementBindings = new ConcurrentHashMap<>();
    private final Map<Object, List<ElementBinding>> elementBindingsByPmo = Collections.synchronizedMap(new HashMap<>());
    private final Map<Component, TableBinding<?>> tableBindings = new ConcurrentHashMap<>();
    private final Set<PropertyDispatcher> propertyDispatchers = new HashSet<>();
    private final PropertyDispatcherFactory dispatcherFactory = new PropertyDispatcherFactory();


    /**
     * Creates a new binding context with an empty name that defines no property behavior and uses no
     * after update handler.
     * 
     */
    public BindingContext() {
        this("");
    }

    /**
     * Creates a new binding context with the given name that defines no property behavior and uses no
     * after update handler.
     * 
     * @param contextName name of this context that is used as identifier in a
     *            {@linkplain BindingManager}
     */
    public BindingContext(String contextName) {
        this(contextName, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER, Handler.NOP_HANDLER);
    }

    /**
     * Creates a new binding context with the given name, using the behavior provider to decorate its
     * bindings and notifying a handler after every UI update.
     * <p>
     * In general, the <code>afterUpdateHandler</code> can be used to trigger any global event outside
     * of this {@linkplain BindingContext}. Usually, {@link BindingManager#afterUpdateUi()} is used by
     * {@link BindingManager} to trigger the validation service and to notify all
     * {@link UiUpdateObserver}s in the manager to show the validation result.
     * 
     * @param contextName name of this context that is used as identifier in a
     *            {@linkplain BindingManager}
     * @param behaviorProvider used to retrieve all {@link PropertyBehavior}s that are relevant to this
     *            context
     * @param afterUpdateHandler a handler that is applied after the UI update. Usually
     *            {@link BindingManager#afterUpdateUi()}
     */
    public BindingContext(String contextName, PropertyBehaviorProvider behaviorProvider,
            Handler afterUpdateHandler) {
        this.name = requireNonNull(contextName, "contextName must not be null");
        this.behaviorProvider = requireNonNull(behaviorProvider, "behaviorProvider must not be null");
        this.afterUpdateHandler = requireNonNull(afterUpdateHandler, "afterUpdateHandler must not be null");
    }

    /**
     * Returns the context's name that uniquely identifies it in a {@linkplain BindingManager}.
     */
    public String getName() {
        return name;
    }

    /**
     * Adds an element binding to the context.
     */
    public BindingContext add(ElementBinding binding) {
        requireNonNull(binding, "binding must not be null");

        elementBindings.put(binding.getBoundComponent(), binding);
        elementBindingsByPmo.computeIfAbsent(binding.getPmo(), (pmo) -> Collections.synchronizedList(new ArrayList<>()))
                .add(binding);
        propertyDispatchers.add(binding.getPropertyDispatcher());
        return this;
    }

    /**
     * Adds a table binding to the context.
     */

    public BindingContext add(TableBinding<?> tableBinding) {
        requireNonNull(tableBinding, "tableBinding must not be null");

        tableBindings.put(tableBinding.getBoundComponent(), tableBinding);
        return this;
    }

    /**
     * Returns all element bindings in the context.
     */
    public Collection<ElementBinding> getElementBindings() {
        return Collections.unmodifiableCollection(elementBindings.values());
    }

    /**
     * Returns all table bindings in the context.
     */
    public Collection<TableBinding<?>> getTableBindings() {
        return Collections.unmodifiableCollection(tableBindings.values());
    }

    /**
     * Removes all bindings in this context that refer to the given PMO.
     */
    public void removeBindingsForPmo(Object pmo) {
        requireNonNull(pmo, "pmo must not be null");

        Collection<ElementBinding> toRemove = elementBindingsByPmo.get(pmo);
        if (toRemove != null) {
            toRemove.stream().map(b -> b.getPropertyDispatcher()).forEach(propertyDispatchers::remove);
            elementBindings.values().removeAll(toRemove);
            elementBindingsByPmo.remove(pmo);
        }
    }

    /**
     * Removes all bindings in this context that refer to the given component. If the component is a
     * container component, all bindings for the components children and their children are removed,
     * too.
     */
    public void removeBindingsForComponent(Component c) {
        elementBindings.remove(c);
        tableBindings.remove(c);
        if (c instanceof ComponentContainer) {
            ComponentContainer container = (ComponentContainer)c;
            container.iterator().forEachRemaining(this::removeBindingsForComponent);
        }
    }

    /**
     * Updates the UI with the data retrieved via bindings registered in this context. Executes
     * afterUpdateHandler that is set in the constructor.
     */
    public void updateUI() {
        updateBindings();

        // Notify handler that the UI was updated for this context and the messages in all
        // contexts should now be updated
        afterUpdateHandler.apply();
    }

    @Override
    public void uiUpdated() {
        updateBindings();
    }

    private void updateBindings() {
        // table bindings have to be updated first, as their update removes bindings
        // and creates new bindings if the table content has changed
        tableBindings.values().forEach(binding -> binding.updateFromPmo());
        elementBindings.values().forEach(binding -> binding.updateFromPmo());
    }

    /**
     * Updates all bindings with the given message list.
     * <p>
     * This method is used by a {@link BindingManager} to push validation results to all registered
     * {@linkplain BindingContext}s.
     * 
     * @see BindingManager#updateMessages(MessageList)
     */
    public void updateMessages(@Nullable MessageList messages) {
        // TODO merken welches binding welche messages anzeigt
        elementBindings.values().forEach(binding -> binding.displayMessages(messages));
        tableBindings.values().forEach(binding -> binding.displayMessages(messages));
    }


    public PropertyBehaviorProvider getBehaviorProvider() {
        return behaviorProvider;
    }

    @Override
    public String toString() {
        return "BindingContext [name=" + name + ", behaviorProvider=" + behaviorProvider + "]";
    }

    /**
     * Creates a binding between the presentation model object and UI elements (i.e. {@linkplain Label}
     * and {@linkplain Component}) as described by the given descriptor.
     * <p>
     * If the label is {@code null} it is ignored for the binding
     * 
     * @param pmo a presentation model object
     * @param bindingDescriptor the descriptor describing the binding
     * @param componentWrapper the {@link ComponentWrapper} that wraps the component that should be
     *            bound
     */
    public void bind(Object pmo,
            BindingDescriptor bindingDescriptor,
            ComponentWrapper componentWrapper) {
        requireNonNull(pmo, "pmo must not be null");
        requireNonNull(bindingDescriptor, "bindingDescriptor must not be null");
        requireNonNull(componentWrapper, "component must not be null");
        ElementBinding binding = bindingDescriptor.createBinding(createDispatcherChain(pmo, bindingDescriptor),
                                                                 this::updateUI, componentWrapper);
        binding.updateFromPmo();
        add(binding);
    }

    /**
     * Binds the {@linkplain ButtonPmo} to the {@linkplain Button}.
     * 
     * @param pmo a button model object
     * @param button the button to be bound
     * @return the {@link ButtonPmoBinding} connecting the button and its model
     */
    public ButtonPmoBinding bind(ButtonPmo pmo, Button button) {
        requireNonNull(pmo, "pmo must not be null");
        requireNonNull(button, "button must not be null");

        ButtonPmoBinding buttonPmoBinding = new ButtonPmoBinding(button, createDispatcherChain(pmo),
                this::updateUI);
        buttonPmoBinding.updateFromPmo();
        add(buttonPmoBinding);
        return buttonPmoBinding;
    }

    protected PropertyDispatcher createDispatcherChain(Object pmo,
            BindingDescriptor bindingDescriptor) {
        requireNonNull(pmo, "pmo must not be null");
        requireNonNull(bindingDescriptor, "bindingDescriptor must not be null");

        return dispatcherFactory.createDispatcherChain(pmo, bindingDescriptor, getBehaviorProvider());
    }

    protected PropertyDispatcher createDispatcherChain(ButtonPmo buttonPmo) {
        requireNonNull(buttonPmo, "buttonPmo must not be null");

        return dispatcherFactory.createDispatcherChain(buttonPmo, getBehaviorProvider());
    }
}
