package org.linkki.core.ui.table.column;

import static java.util.Objects.requireNonNull;

import org.linkki.core.binding.Binding;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.ElementDescriptor;
import org.linkki.core.binding.descriptor.PropertyElementDescriptors;
import org.linkki.core.defaults.style.LinkkiTheme;
import org.linkki.core.ui.wrapper.LabelComponentWrapper;

import com.vaadin.ui.Component;

import edu.umd.cs.findbugs.annotations.Nullable;

/** Column generator that generates a column for a field of a PMO. */
@SuppressWarnings("deprecation")
class FieldColumnGenerator implements com.vaadin.v7.ui.Table.ColumnGenerator {

    private static final long serialVersionUID = 1L;

    private final PropertyElementDescriptors elementDescriptors;
    private final BindingContext bindingContext;

    @Nullable
    private Binding binding;

    public FieldColumnGenerator(PropertyElementDescriptors elementDescriptors,
            BindingContext bindingContext) {
        this.elementDescriptors = requireNonNull(elementDescriptors, "elementDescriptors must not be null");
        this.bindingContext = requireNonNull(bindingContext, "bindingContext must not be null");
    }

    @Override
    public Object generateCell(com.vaadin.v7.ui.Table source,
            Object itemId,
            Object columnId) {
        requireNonNull(itemId, "itemId must not be null");
        ElementDescriptor elementDescriptor = elementDescriptors.getDescriptor(itemId);
        Component component = (Component)elementDescriptor.newComponent(itemId);
        component.addStyleName(LinkkiTheme.BORDERLESS);
        component.addStyleName(LinkkiTheme.TABLE_CELL);

        bindingContext.bind(itemId, elementDescriptor, new LabelComponentWrapper(component));

        return component;
    }

    public void setBinding(Binding binding) {
        this.binding = binding;
    }

}
