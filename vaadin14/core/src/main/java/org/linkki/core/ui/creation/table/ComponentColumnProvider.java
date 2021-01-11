package org.linkki.core.ui.creation.table;

import static java.util.Objects.requireNonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectAnnotationReader;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyAnnotationReader;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;
import org.linkki.core.uicreation.ComponentAnnotationReader;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.function.ValueProvider;

/** Column generator that generates a column for a field of a PMO. */
class ComponentColumnProvider<ROW> implements ValueProvider<ROW, Component> {

    private static final long serialVersionUID = 1L;

    private final Method method;
    private final BindingContext bindingContext;

    public ComponentColumnProvider(Method method,
            BindingContext bindingContext) {
        this.method = requireNonNull(method, "method must not be null");
        this.bindingContext = requireNonNull(bindingContext, "bindingContext must not be null");
    }

    @Override
    public Component apply(ROW source) {
        Annotation componentDefinitionAnnotation = ComponentAnnotationReader.getComponentDefinitionAnnotation(method,
                                                                                                              source);
        Component component = (Component)ComponentAnnotationReader
                .getComponentDefinition(componentDefinitionAnnotation, method)
                .createComponent(source);
        BoundProperty boundProperty = BoundPropertyAnnotationReader.getBoundProperty(componentDefinitionAnnotation,
                                                                                     method);
        List<LinkkiAspectDefinition> aspectDefs = AspectAnnotationReader
                .createAspectDefinitionsFor(componentDefinitionAnnotation, method);
        bindingContext.bind(source, boundProperty, aspectDefs,
                            new NoLabelComponentWrapper(component, WrapperType.FIELD));

        // TODO LIN-2129
        // component.addStyleName(LinkkiTheme.BORDERLESS);
        // component.addStyleName(LinkkiTheme.TABLE_CELL);
        if (component instanceof HasValue && component instanceof HasSize) {
            ((HasSize)component).setWidthFull();
        }

        return component;
    }
}
