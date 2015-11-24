package org.linkki.core.binding;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.linkki.core.PresentationModelObject;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;

import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * A binding context binds fields and tables in a part of the user interface like a page or a dialog
 * to properties of presentation model objects. If the value in one of the fields is changed, all
 * fields in the context are updated from the presentation model objects via their bindings.
 */
public class BindingContext {

    private String name;
    private Map<Component, ElementBinding> elementBindings = Maps.newConcurrentMap();
    private Map<Component, TableBinding<?>> tableBindings = Maps.newConcurrentMap();
    private Set<PropertyDispatcher> propertyDispatchers = new HashSet<PropertyDispatcher>();

    /**
     * Creates a new binding context with the name 'DefaultContext'.
     */
    public BindingContext() {
        this("DefaultContext");
    }

    /**
     * Creates a new binding context with the given name.
     */
    public BindingContext(@Nonnull String contextName) {
        Objects.nonNull(contextName);
        this.name = contextName;
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
        Objects.nonNull(binding);
        elementBindings.put(binding.getBoundComponent(), binding);
        propertyDispatchers.add(binding.getPropertyDispatcher());
        return this;
    }

    /**
     * Adds a table binding to the context.
     */
    @Nonnull
    public BindingContext add(@Nonnull TableBinding<?> tableBinding) {
        Objects.nonNull(tableBinding);
        tableBindings.put(tableBinding.getBoundComponent(), tableBinding);
        return this;
    }

    /**
     * Returns all element bindings in the context.
     */
    @Nonnull
    public Collection<ElementBinding> getElementBindings() {
        return Collections.unmodifiableCollection(elementBindings.values());
    }

    /**
     * Returns all table bindings in the context.
     */
    @Nonnull
    public Collection<TableBinding<?>> getTableBindings() {
        return Collections.unmodifiableCollection(tableBindings.values());
    }

    /**
     * Removes all bindings in this context that refer to the given PMO.
     */
    public void removeBindingsForPmo(PresentationModelObject pmo) {
        List<ElementBinding> toRemove = elementBindings.values().stream() //
                .filter(b -> b.getPmo() == pmo) //
                .collect(Collectors.toList());
        toRemove.stream().map(b -> b.getPropertyDispatcher()).forEach(propertyDispatchers::remove);
        elementBindings.values().removeAll(toRemove);
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
     * Updates the UI with the data retrieved via all bindings registered in this context.
     */
    public void updateUI() {
        propertyDispatchers.forEach(pd -> pd.prepareUpdateUI());
        // table bindings have to be updated first, as their update removes bindings
        // and creates new bindings if the table content has changed.
        tableBindings.values().forEach(binding -> binding.updateFromPmo());
        elementBindings.values().forEach(binding -> binding.updateFromPmo());
    }

    @Override
    public String toString() {
        return "BindingContext[" + name + "]";
    }

}
