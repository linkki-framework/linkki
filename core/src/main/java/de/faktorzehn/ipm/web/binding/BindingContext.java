package de.faktorzehn.ipm.web.binding;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

import de.faktorzehn.ipm.web.PresentationModelObject;
import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;

/**
 * A binding context binds fields in a section of the user interface like a page or a dialog to
 * properties of a single presentation model object. If the value in one of the fields is changed,
 * all fields in the context are updated from the presentation model object via their bindings.
 */
public class BindingContext {

    private String name;
    private Map<Component, Binding> bindings = Maps.newConcurrentMap();
    private Set<PropertyDispatcher> propertyDispatchers = new HashSet<PropertyDispatcher>();

    public BindingContext() {
        this("DefaultContext");
    }

    public BindingContext(String contextName) {
        this.name = contextName;
    }

    public String getName() {
        return name;
    }

    public BindingContext add(ElementBinding binding) {
        bindings.put(binding.getBoundComponent(), binding);
        propertyDispatchers.add(binding.getPropertyDispatcher());
        return this;
    }

    public BindingContext add(TableBinding<?> tableBinding) {
        bindings.put(tableBinding.getBoundComponent(), tableBinding);
        return this;
    }

    public Collection<Binding> getBindings() {
        return Collections.unmodifiableCollection(bindings.values());
    }

    public void removeBindingsForPmo(PresentationModelObject pmo) {
        List<ElementBinding> toRemove = bindings.values().stream() //
                .filter(b -> b instanceof ElementBinding) //
                .map(b -> ((ElementBinding)b)) //
                .filter(b -> b.getPmo() == pmo) //
                .collect(Collectors.toList());
        toRemove.stream().map(b -> b.getPropertyDispatcher()).forEach(propertyDispatchers::remove);
        bindings.values().removeAll(toRemove);
    }

    public void removeBindingsForComponent(Component c) {
        bindings.remove(c);
        if (c instanceof ComponentContainer) {
            ComponentContainer container = (ComponentContainer)c;
            container.iterator().forEachRemaining(this::removeBindingsForComponent);
        }
    }

    public void updateUI() {
        propertyDispatchers.forEach(pd -> pd.prepareUpdateUI());
        bindings.values().forEach(binding -> binding.updateFromPmo());
    }

    @Override
    public String toString() {
        return "BindingContext [name=" + name + "]";
    }

}
