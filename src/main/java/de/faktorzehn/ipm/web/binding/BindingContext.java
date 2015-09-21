package de.faktorzehn.ipm.web.binding;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.faktorzehn.ipm.web.binding.dispatcher.PropertyDispatcher;

/**
 * A binding context binds fields in a section of the user interface like a page or a dialog to
 * properties of a single presentation model object. If the value in one of the fields is changed,
 * all fields in the context are updated from the presentation model object via their bindings.
 */
public class BindingContext {

    private String name;
    private List<ElementBinding> bindings = new LinkedList<>();
    private Set<PropertyDispatcher> propertyDispatcher = new HashSet<PropertyDispatcher>();

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
        bindings.add(binding);
        propertyDispatcher.add(binding.getPropertyDispatcher());
        return this;
    }

    public List<ElementBinding> getBindings() {
        return Collections.unmodifiableList(bindings);
    }

    public void updateUI() {
        propertyDispatcher.forEach(pd -> pd.prepareUpdateUI());
        bindings.forEach(binding -> binding.updateFromPmo());
    }

    @Override
    public String toString() {
        return "BindingContext [name=" + name + "]";
    }

}
