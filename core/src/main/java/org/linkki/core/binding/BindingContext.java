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

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.stream.Stream;

import org.linkki.core.binding.descriptor.BindingDescriptor;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehavior;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.UiUpdateObserver;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.pmo.ButtonPmo;
import org.linkki.core.pmo.PresentationModelObject;
import org.linkki.core.uiframework.UiFramework;
import org.linkki.util.handler.Handler;

/**
 * A binding context binds fields and tables in a part of the user interface like a page or a dialog to
 * properties of presentation model objects. If the value in one of the fields is changed, all fields in
 * the context are updated from the presentation model objects via their bindings.
 * <p>
 * {@link BindingContext BindingContexts} are usually managed by a {@link BindingManager} that handles
 * events across multiple contexts.
 * 
 * @see BindingManager#getContext(String)
 */
public class BindingContext implements UiUpdateObserver {

    private final String name;
    private final PropertyBehaviorProvider behaviorProvider;
    private final Handler afterUpdateHandler;
    private final Handler afterModelChangedHandler;
    private final PropertyDispatcherFactory dispatcherFactory;

    private final Map<Object, WeakReference<Binding>> bindings = new WeakHashMap<>();

    private MessageList currentMessages = new MessageList();

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
     * {@link UiUpdateObserver UiUpdateObserver} in the manager to show the validation result.
     * 
     * @param contextName name of this context that is used as identifier in a
     *            {@linkplain BindingManager}
     * @param behaviorProvider used to retrieve all {@link PropertyBehavior PropertyBehaviors} that are
     *            relevant to this context
     * @param afterUpdateHandler a handler that is applied after the UI update. Usually
     *            {@link BindingManager#afterUpdateUi()}
     * 
     * @deprecated Use {@link BindingContextBuilder}
     */
    @Deprecated(since = "2.1.0")
    public BindingContext(String contextName, PropertyBehaviorProvider behaviorProvider,
            Handler afterUpdateHandler) {
        this(contextName, behaviorProvider, new PropertyDispatcherFactory(), afterUpdateHandler);
    }

    /**
     * Creates a new binding context with the given name, using the behavior provider to decorate its
     * bindings and notifying a handler after every UI update.
     * <p>
     * In general, the <code>afterUpdateHandler</code> can be used to trigger any global event outside
     * of this {@linkplain BindingContext}. Usually, {@link BindingManager#afterUpdateUi()} is used by
     * {@link BindingManager} to trigger the validation service and to notify all
     * {@link UiUpdateObserver UiUpdateObservers} in the manager to show the validation result.
     * 
     * @param contextName name of this context that is used as identifier in a
     *            {@linkplain BindingManager}
     * @param behaviorProvider used to retrieve all {@link PropertyBehavior PropertyBehaviors} that are
     *            relevant to this context
     * @param dispatcherFactory the factory used to create the {@link PropertyDispatcher} chain for any
     *            property
     * @param afterUpdateHandler a handler that is applied after the UI update. Usually
     *            {@link BindingManager#afterUpdateUi()}
     * 
     * @deprecated Use {@link BindingContextBuilder}
     */
    @Deprecated(since = "2.1.0")
    public BindingContext(String contextName, PropertyBehaviorProvider behaviorProvider,
            PropertyDispatcherFactory dispatcherFactory, Handler afterUpdateHandler) {
        this(contextName, behaviorProvider, dispatcherFactory, afterUpdateHandler, Handler.NOP_HANDLER);
    }

    /**
     * Creates a new binding context with the given name, using the behavior provider to decorate its
     * bindings and notifying a handler after every UI update.
     * <p>
     * In general, the <code>afterUpdateHandler</code> can be used to trigger any global event outside
     * of this {@linkplain BindingContext}. Usually, {@link BindingManager#afterUpdateUi()} is used by
     * {@link BindingManager} to trigger the validation service and to notify all
     * {@link UiUpdateObserver UiUpdateObservers} in the manager to show the validation result.
     * 
     * @param contextName name of this {@link BindingContext} that is used as identifier in a
     *            {@linkplain BindingManager}
     * @param behaviorProvider used to retrieve all {@link PropertyBehavior PropertyBehaviors} that are
     *            relevant to this context
     * @param dispatcherFactory the factory used to create the {@link PropertyDispatcher} chain for any
     *            property
     * @param afterUpdateHandler a {@link Handler} that is applied after the UI update. Usually
     *            {@link BindingManager#afterUpdateUi()}
     * @param afterModelChangedHandler a {@link Handler} that is applied after the model update.
     * 
     * @since 2.1.0
     */
    protected BindingContext(String contextName, PropertyBehaviorProvider behaviorProvider,
            PropertyDispatcherFactory dispatcherFactory, Handler afterUpdateHandler, Handler afterModelChangedHandler) {
        this.name = requireNonNull(contextName, "contextName must not be null");
        this.behaviorProvider = requireNonNull(behaviorProvider, "behaviorProvider must not be null");
        this.afterUpdateHandler = requireNonNull(afterUpdateHandler, "afterUpdateHandler must not be null");
        this.afterModelChangedHandler = requireNonNull(afterModelChangedHandler,
                                                       "afterModelChangedHandler must not be null");
        this.dispatcherFactory = requireNonNull(dispatcherFactory, "dispatcherFactory must not be null");
    }

    /**
     * Returns the context's name that uniquely identifies it in a {@linkplain BindingManager}.
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a binding to the context.
     * 
     * @param binding the Binding that should be added
     * @param componentWrapper the component wrapper used to register the binding calling
     *            {@link ComponentWrapper#registerBinding(Binding)}
     */
    public BindingContext add(Binding binding, ComponentWrapper componentWrapper) {
        requireNonNull(binding, "binding must not be null");

        binding.updateFromPmo();
        binding.displayMessages(currentMessages);

        bindings.put(binding.getBoundComponent(), new WeakReference<>(binding));
        componentWrapper.registerBinding(binding);
        return this;
    }

    /**
     * Returns all bindings in the context.
     */
    public Collection<Binding> getBindings() {
        return Collections.unmodifiableCollection(getBindingStream()
                .collect(toList()));
    }

    private Stream<Binding> getBindingStream() {
        return bindings.entrySet().stream()
                .map(this::getExistingBinding);
    }

    private Binding getExistingBinding(Entry<Object, WeakReference<Binding>> entry) {
        Object component = entry.getKey();
        Binding binding = entry.getValue().get();
        if (binding == null) {
            System.out.println("****************************************************************************");
            System.out.println("Binding for component " + component + " was removed too early");
            System.out.println("****************************************************************************");
            throw new RuntimeException(
                    "Binding for component " + component + " was removed too early");
        }
        return binding;
    }

    /**
     * Removes all bindings in this context that refer to the given framework specific UI component
     * (e.g. text field) . If the UI component is a container component, all bindings for the components
     * children and their children are removed as well.
     * <p>
     * If this {@link BindingContext} contains bindings that are themselves a {@link BindingContext},
     * the component is removed recursively from all child binding contexts.
     * 
     * @param uiComponent that is given to find and remove the bindings that refer to it
     */
    public void removeBindingsForComponent(Object uiComponent) {
        bindings.remove(uiComponent);
        UiFramework.getChildComponents(uiComponent)
                .iterator()
                .forEachRemaining(this::removeBindingsForComponent);
        getBindingStream()
                .filter(BindingContext.class::isInstance)
                .map(BindingContext.class::cast)
                .forEach(bc -> bc.removeBindingsForComponent(uiComponent));
    }

    /**
     * Removes all bindings in this context that refer to the given presentation model object.
     * <p>
     * If the PMO includes other PMOs (like {@link ContainerPmo}), all bindings for those PMOs are
     * removed as well.
     * <p>
     * If this {@link BindingContext} contains bindings that are themselves a {@link BindingContext},
     * the PMO is removed recursively from all child binding contexts.
     * 
     * @implNote Removing all bindings for included PMOs does not work for getter methods that return a
     *           new instance for each call, like mostly done for {@link ButtonPmo ButtonPmos}:
     * 
     *           <code>
     *           ContainerPmo.getAddItemButtonPmo() {
     *              return Optional.of(ButtonPmo.newAddButton(..));
     *           }
     *           </code>
     * 
     *           In order to be properly removed, the same instance has to be returned on each call of
     *           the getter method.
     * 
     * @param pmo that is given to find and remove the bindings that refer to it
     */
    public void removeBindingsForPmo(Object pmo) {
        bindings.values().removeIf(ref -> {
            Binding binding = ref.get();
            return binding != null && binding.getPmo() == pmo;
        });
        getBindingStream()
                .filter(BindingContext.class::isInstance)
                .map(BindingContext.class::cast)
                .forEach(bc -> bc.removeBindingsForPmo(pmo));

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
     * <p>
     * This method should be called when the UI should be updated after a model change to update all
     * {@link Binding Bindings} of this {@link BindingContext} and notify the
     * after-update-handler(provided in the constructor) that the model has changed. This may trigger
     * other {@link UiUpdateObserver observers}.
     * 
     * @see #uiUpdated()
     */
    public void modelChanged() {
        updateFromPmo();

        // Notify handler that the UI was updated for this context and the messages in all
        // contexts should now be updated
        afterUpdateHandler.apply();
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
        getBindingStream().forEach(binding -> binding.updateFromPmo());
    }

    /**
     * Updates all bindings with the given message list.
     * <p>
     * This method is used by a {@link BindingManager} to push validation results to all registered
     * {@linkplain BindingContext BindingContexts}.
     * 
     */
    public MessageList displayMessages(MessageList messages) {
        currentMessages = messages;
        return getBindingStream()
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
        return "BindingContext [name=" + name + ", behaviorProvider=" + behaviorProvider + ", dispatcherFactory="
                + dispatcherFactory + ", afterUpdateHandler=" + afterUpdateHandler + ", afterModelChangedHandler="
                + afterModelChangedHandler + "]";
    }

    /**
     * Creates a binding between the presentation model object and framework specific UI components
     * (e.g. text field) as described by the given descriptor.
     * <p>
     * If the label is {@code null} it is ignored for the binding
     * 
     * @param pmo a presentation model object
     * @param bindingDescriptor the descriptor describing the binding
     * @param componentWrapper the {@link ComponentWrapper} that wraps the UI component that should be
     *            bound
     */
    public Binding bind(Object pmo,
            BindingDescriptor bindingDescriptor,
            ComponentWrapper componentWrapper) {
        requireNonNull(bindingDescriptor, "bindingDescriptor must not be null");
        return bind(pmo, bindingDescriptor.getBoundProperty(), bindingDescriptor.getAspectDefinitions(),
                    componentWrapper);
    }

    /**
     * Creates a binding between the presentation model object and framework specific UI components
     * (e.g. text fields, sections, tables) for the {@link Aspect Aspects} defined by the given
     * {@link LinkkiAspectDefinition LinkkiAspectDefinitions}.
     * <p>
     * If the label is {@code null} it is ignored for the binding
     * 
     * @param pmo a presentation model object
     * @param boundProperty the (presentation) model property to be bound
     * @param aspectDefs the definitions for the aspects to be bound
     * @param componentWrapper the {@link ComponentWrapper} that wraps the UI component that should be
     *            bound
     */
    public Binding bind(Object pmo,
            BoundProperty boundProperty,
            List<LinkkiAspectDefinition> aspectDefs,
            ComponentWrapper componentWrapper) {
        requireNonNull(pmo, "pmo must not be null");
        requireNonNull(boundProperty, "boundProperty must not be null");
        requireNonNull(aspectDefs, "aspectDefs must not be null");
        requireNonNull(componentWrapper, "componentWrapper must not be null");
        Binding binding = createBinding(pmo, boundProperty, aspectDefs, componentWrapper);
        add(binding, componentWrapper);
        return binding;
    }

    public ContainerBinding bindContainer(Object pmo,
            BoundProperty boundProperty,
            List<LinkkiAspectDefinition> aspectDefs,
            ComponentWrapper componentWrapper) {
        Binding elementBinding = createBinding(pmo, boundProperty, aspectDefs, componentWrapper);
        ContainerBinding containerBinding = new ContainerBinding(elementBinding, getBehaviorProvider(),
                dispatcherFactory, this::modelChanged);
        add(containerBinding, componentWrapper);
        return containerBinding;
    }

    /**
     * Creates a binding with the given dispatcher, the given handler for updating the UI and the given
     * UI components using the binding information from this descriptor.
     */
    private ElementBinding createBinding(Object pmo,
            BoundProperty boundProperty,
            List<LinkkiAspectDefinition> aspectDefinitions,
            ComponentWrapper componentWrapper) {
        return new ElementBinding(componentWrapper,
                dispatcherFactory.createDispatcherChain(pmo, boundProperty, getBehaviorProvider()), this::modelChanged,
                aspectDefinitions);
    }

    /**
     * Builder for creating {@link BindingContext}
     * 
     * @since 2.1.0
     */
    public static class BindingContextBuilder {

        private String contextName;
        private PropertyBehaviorProvider propertyBehaviorProvider;
        private PropertyDispatcherFactory propertyDispatcherFactory;
        private Handler afterUpdateHandler;
        private Handler afterModelChangedHandler;

        public BindingContextBuilder() {
            this.contextName = "";
            this.propertyBehaviorProvider = PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER;
            this.propertyDispatcherFactory = new PropertyDispatcherFactory();
            this.afterUpdateHandler = Handler.NOP_HANDLER;
            this.afterModelChangedHandler = Handler.NOP_HANDLER;
        }

        /**
         * Specifies the name of the {@link BindingContext context}
         * 
         * @param name name of this {@link BindingContext context} that is used as identifier in a
         *            {@link BindingManager}
         * @return {@code this} for method chaining
         */
        public BindingContextBuilder name(String name) {
            this.contextName = requireNonNull(name, "contextName must not be null");
            return this;
        }

        /**
         * Specifies the {@link PropertyBehaviorProvider} of the {@link BindingContext context}
         * 
         * @param behaviorProvider {@link PropertyBehaviorProvider} of this {@link BindingContext
         *            context} used to retrieve all {@link PropertyBehavior PropertyBehaviors} that are
         *            relevant to this {@link BindingContext context}
         * @return {@code this} for method chaining
         */
        public BindingContextBuilder propertyBehaviorProvider(PropertyBehaviorProvider behaviorProvider) {
            this.propertyBehaviorProvider = requireNonNull(behaviorProvider, "behaviorProvider must not be null");
            return this;
        }

        /**
         * Specifies the {@link PropertyDispatcherFactory} of the {@link BindingContext context}
         * 
         * @param dispatcherFactory the factory used to create the {@link PropertyDispatcher} chain for
         *            any property
         * @return {@code this} for method chaining
         */
        public BindingContextBuilder propertyDispatcherFactory(PropertyDispatcherFactory dispatcherFactory) {
            this.propertyDispatcherFactory = requireNonNull(dispatcherFactory, "dispatcherFactory must not be null");
            return this;
        }

        /**
         * Specifies the {@link Handler afterUpdateHandler} of the {@link BindingContext context}
         * 
         * @param handler a {@link Handler handler} that is applied after the UI update. Usually
         *            {@link BindingManager#afterUpdateUi()}
         * @return {@code this} for method chaining
         */
        public BindingContextBuilder afterUpdateHandler(Handler handler) {
            this.afterUpdateHandler = requireNonNull(handler, "afterUpdateHandler must not be null");
            return this;
        }

        /**
         * Specifies the {@link Handler afterModelChangedHandler} of the {@link BindingContext context}
         * 
         * @param handler a {@link Handler handler} that is applied after the model update.
         * @return {@code this} for method chaining
         */
        public BindingContextBuilder afterModelChangedHandler(Handler handler) {
            this.afterModelChangedHandler = requireNonNull(handler,
                                                           "afterModelChangedHandler must not be null");
            return this;
        }

        /**
         * Builds a {@link BindingContext} instance using the values in this builder.
         * <p>
         * If no custom properties are set, the {@link BindingContext} is created with its default
         * values.
         * 
         * @return a new {@link BindingContext}
         */
        public BindingContext build() {
            return new BindingContext(contextName, propertyBehaviorProvider, propertyDispatcherFactory,
                    afterUpdateHandler, afterModelChangedHandler);
        }

    }
}
