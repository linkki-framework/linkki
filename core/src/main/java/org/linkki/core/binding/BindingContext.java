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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.linkki.core.ButtonPmo;
import org.linkki.core.PresentationModelObject;
import org.linkki.core.binding.behavior.PropertyBehavior;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.message.MessageList;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.section.descriptor.BindingDescriptor;
import org.linkki.core.ui.table.ContainerPmo;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
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

    private final Map<Object, Binding> bindings = new ConcurrentHashMap<>();
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
     * Adds a binding to the context.
     */
    public BindingContext add(Binding binding) {
        requireNonNull(binding, "binding must not be null");

        bindings.put(binding.getBoundComponent(), binding);
        return this;
    }

    /**
     * Returns all bindings in the context.
     */
    public Collection<Binding> getBindings() {
        return Collections.unmodifiableCollection(bindings.values());
    }

    /**
     * Removes all bindings in this context that refer to the given component. If the component is a
     * container component, all bindings for the components children and their children are removed as
     * well.
     */
    public void removeBindingsForComponent(Component c) {
        Binding removedBinding = bindings.remove(c);
        if (c instanceof HasComponents) {
            ((HasComponents)c).iterator().forEachRemaining(this::removeBindingsForComponent);
        }

        if (removedBinding instanceof BindingContext) {
            ((BindingContext)removedBinding).bindings.clear();
        }
    }

    /**
     * Removes all bindings in this context that refer to the given presentation model object. If the
     * presentation model is bound to a component and that component is a container component, all
     * bindings for the components children and their children are removed as well.
     * <p>
     * If the PMO includes other PMOs (like {@link PresentationModelObject#getEditButtonPmo()} or
     * {@link ContainerPmo}), all bindings for those PMOs are removed as well.
     */
    public void removeBindingsForPmo(Object pmo) {
        Set<Object> keysToRemove = bindings.entrySet().stream().filter(e -> e.getValue().getPmo() == pmo)
                .map(Entry::getKey).collect(Collectors.toSet());
        keysToRemove.forEach(key -> {
            if (key instanceof Component) {
                removeBindingsForComponent((Component)key);
            } else {
                bindings.remove(key);
            }
        });
        if (pmo instanceof PresentationModelObject) {
            ((PresentationModelObject)pmo).getEditButtonPmo().ifPresent(this::removeBindingsForPmo);
        }
        if (pmo instanceof ContainerPmo) {
            ((ContainerPmo<?>)pmo).getAddItemButtonPmo().ifPresent(this::removeBindingsForPmo);
        }
    }

    /**
     * Updates the UI with the data retrieved via bindings registered in this context. Executes
     * afterUpdateHandler that is set in the constructor.
     * 
     * @deprecated This method is deprecated since August 1st, 2018 and may be removed in future
     *             versions. Use {@link #modelChanged()} or {@link #uiUpdated()} instead.
     */
    @Deprecated
    public void updateUI() {
        /* inline this code into modelChanged() once updateUI() is removed */

        updateFromPmo();

        // Notify handler that the UI was updated for this context and the messages in all
        // contexts should now be updated
        afterUpdateHandler.apply();
    }

    /**
     * Updates the UI with the data retrieved via bindings registered in this context. Executes
     * afterUpdateHandler that is set in the constructor.
     * <p>
     * This method should be called when the UI should be updated after a model change to update all
     * {@link Binding Bindings} of this {@link BindingContext} and notify the
     * after-update-handler(provided in the constructor) that the model has changed. This may trigger
     * other {@link UiUpdateObserver observers}.
     * 
     * @see #uiUpdated()
     */
    public void modelChanged() {
        updateUI();
    }

    /**
     * Triggers the update of all registered bindings. This method is called by {@link BindingManager}
     * if this {@link BindingContext} is registered as {@link UiUpdateObserver}.
     * <p>
     * Call this method if it is necessary to update the UI after any changes that only affect bindings
     * in this context. This will not trigger other {@link UiUpdateObserver}.
     * 
     * @see #modelChanged()
     */
    @Override
    public void uiUpdated() {
        updateFromPmo();
    }

    void updateFromPmo() {
        bindings.values().forEach(binding -> binding.updateFromPmo());
    }

    /**
     * Updates all bindings with the given message list.
     * <p>
     * This method is used by a {@link BindingManager} to push validation results to all registered
     * {@linkplain BindingContext}s.
     * 
     * @see BindingManager#updateMessages(MessageList)
     * @deprecated This method is deprecated since August 1st, 2018 and may be removed in future
     *             versions. Use {@link #displayMessages(MessageList)} instead.
     */
    @Deprecated
    public final void updateMessages(MessageList messages) {
        displayMessages(messages);
    }

    /**
     * Updates all bindings with the given message list.
     * <p>
     * This method is used by a {@link BindingManager} to push validation results to all registered
     * {@linkplain BindingContext}s.
     * 
     * @see BindingManager#updateMessages(MessageList)
     */
    public MessageList displayMessages(MessageList messages) {
        return bindings.values().stream()
                .map(binding -> binding.displayMessages(messages))
                .flatMap(MessageList::stream)
                .distinct()
                .collect(MessageList.collector());
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
                                                                 this::modelChanged, componentWrapper);
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
                this::modelChanged);
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
