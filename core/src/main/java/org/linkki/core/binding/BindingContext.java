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

import javax.annotation.Nonnull;

import org.faktorips.runtime.MessageList;
import org.linkki.core.ButtonPmo;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.section.annotations.BindingDescriptor;
import org.linkki.util.handler.Handler;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;

/**
 * A binding context binds fields and tables in a part of the user interface like a page or a dialog
 * to properties of presentation model objects. If the value in one of the fields is changed, all
 * fields in the context are updated from the presentation model objects via their bindings.
 */
public class BindingContext {

    @Nonnull
    private final String name;
    private final Map<Component, ElementBinding> elementBindings = new ConcurrentHashMap<>();
    private final Map<Object, List<ElementBinding>> elementBindingsByPmo = Collections.synchronizedMap(new HashMap<>());
    private final Map<Component, TableBinding<?>> tableBindings = new ConcurrentHashMap<>();
    private final Set<PropertyDispatcher> propertyDispatchers = new HashSet<>();
    private final PropertyDispatcherFactory dispatcherFactory = new PropertyDispatcherFactory();

    @Nonnull
    private final PropertyBehaviorProvider behaviorProvider;

    @Nonnull
    private Handler afterUpdateHandler;

    /**
     * Creates a new binding context with the given name, using the behavior provider to decorate
     * it's bindings and notifying the after-update handler after every UI update.
     */
    @SuppressWarnings("null")
    public BindingContext(@Nonnull String contextName, @Nonnull PropertyBehaviorProvider behaviorProvider,
            @Nonnull Handler afterUpdateHandler) {
        this.name = requireNonNull(contextName);
        this.behaviorProvider = requireNonNull(behaviorProvider);
        this.afterUpdateHandler = requireNonNull(afterUpdateHandler);
    }

    /**
     * Returns the context's name that uniquely identifies it in a binding manager.
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * Adds an element binding to the context.
     */
    @Nonnull
    public BindingContext add(@Nonnull ElementBinding binding) {
        requireNonNull(binding);
        elementBindings.put(binding.getBoundComponent(), binding);
        elementBindingsByPmo.computeIfAbsent(binding.getPmo(), (pmo) -> Collections.synchronizedList(new ArrayList<>()))
                .add(binding);
        propertyDispatchers.add(binding.getPropertyDispatcher());
        return this;
    }

    /**
     * Adds a table binding to the context.
     */
    @Nonnull
    public BindingContext add(@Nonnull TableBinding<?> tableBinding) {
        requireNonNull(tableBinding);
        tableBindings.put(tableBinding.getBoundComponent(), tableBinding);
        return this;
    }

    /**
     * Returns all element bindings in the context.
     */
    @SuppressWarnings("null")
    @Nonnull
    public Collection<ElementBinding> getElementBindings() {
        return Collections.unmodifiableCollection(elementBindings.values());
    }

    /**
     * Returns all table bindings in the context.
     */
    @SuppressWarnings("null")
    @Nonnull
    public Collection<TableBinding<?>> getTableBindings() {
        return Collections.unmodifiableCollection(tableBindings.values());
    }

    /**
     * Removes all bindings in this context that refer to the given PMO.
     */
    public void removeBindingsForPmo(Object pmo) {
        Collection<ElementBinding> toRemove = elementBindingsByPmo.get(pmo);
        toRemove.stream().map(b -> b.getPropertyDispatcher()).forEach(propertyDispatchers::remove);
        elementBindings.values().removeAll(toRemove);
        elementBindingsByPmo.remove(pmo);
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
     * Updates the UI with the data retrieved via all bindings registered in this context. Notifies
     * the binding manager that messages (in all contexts) should be updated.
     */
    public void updateUI() {
        // table bindings have to be updated first, as their update removes bindings
        // and creates new bindings if the table content has changed
        tableBindings.values().forEach(binding -> binding.updateFromPmo());
        elementBindings.values().forEach(binding -> binding.updateFromPmo());

        // Notify handler that the UI was updated for this context and the messages in all
        // contexts should now be updated
        afterUpdateHandler.apply();
    }

    public void updateMessages(MessageList messages) {
        // TODO merken welches binding welche messages anzeigt
        elementBindings.values().forEach(binding -> binding.displayMessages(messages));
        tableBindings.values().forEach(binding -> binding.displayMessages(messages));
    }

    @Nonnull
    public PropertyBehaviorProvider getBehaviorProvider() {
        return behaviorProvider;
    }

    @Override
    public String toString() {
        return "BindingContext [name=" + name + ", behaviorProvider=" + behaviorProvider + "]";
    }

    /**
     * Creates a binding between the presentation model object and UI elements (i.e.
     * {@linkplain Label} and {@linkplain Component}) as described by the given descriptor.
     * <p>
     * If the label is {@code null} it is ignored for the binding
     * 
     * @param pmo a presentation model object
     * @param bindingDescriptor the descriptor describing the binding
     * @param component the component to be bound
     * @param label the label to be bound or {@code null} if no label is bound
     */
    public void bind(@Nonnull Object pmo,
            @Nonnull BindingDescriptor bindingDescriptor,
            @Nonnull Component component,
            Label label) {
        requireNonNull(pmo, "PresentationModelObject must not be null");
        requireNonNull(bindingDescriptor, "BindingDescriptor must not be null");
        requireNonNull(component, "Component must not be null");

        // Make bound components "immediate", i.e. let them update their PMO as soon as a field is
        // left, a checkbox is checked etc.
        if (component instanceof AbstractComponent) {
            ((AbstractComponent)component).setImmediate(true);
        }

        ElementBinding binding = bindingDescriptor.createBinding(createDispatcherChain(pmo, bindingDescriptor),
                                                                 this::updateUI, component, label);
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
    @Nonnull
    public ButtonPmoBinding bind(@Nonnull ButtonPmo pmo, @Nonnull Button button) {
        requireNonNull(pmo, "ButtonPmo must not be null");
        requireNonNull(button, "Button must not be null");
        ButtonPmoBinding buttonPmoBinding = new ButtonPmoBinding(button, createDispatcherChain(pmo), this::updateUI);
        add(buttonPmoBinding);
        return buttonPmoBinding;
    }

    @Nonnull
    protected PropertyDispatcher createDispatcherChain(@Nonnull Object pmo,
            @Nonnull BindingDescriptor bindingDescriptor) {
        requireNonNull(pmo, "PresentationModelObject must not be null");
        requireNonNull(bindingDescriptor, "BindingDescriptor must not be null");

        return dispatcherFactory.createDispatcherChain(pmo, bindingDescriptor, getBehaviorProvider());
    }

    @Nonnull
    protected PropertyDispatcher createDispatcherChain(@Nonnull ButtonPmo buttonPmo) {
        requireNonNull(buttonPmo, "ButtonPmo must not be null");

        return dispatcherFactory.createDispatcherChain(buttonPmo, getBehaviorProvider());
    }

}
