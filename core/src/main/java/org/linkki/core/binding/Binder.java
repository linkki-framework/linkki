/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.exception.LinkkiRuntimeException;
import org.linkki.core.ui.section.annotations.BindAnnotationDescriptor;
import org.linkki.core.ui.section.annotations.BindingDescriptor;
import org.linkki.util.BeanUtils;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.vaadin.ui.Component;

/**
 * A Binder is a utility class used to create data-bindings between the UI elements (such as text-
 * or combo-boxes, buttons etc.) of a view and a PMO .
 * <p>
 * The view is annotated with {@link Bind @Bind} annotations that define which UI elements are bound
 * to which properties of the PMO. It is possible to annotate fields as well as methods. The PMO is
 * just a POJO. Typically, the usage of the Binder looks something like this:
 * 
 * <pre>
 * <code>
 * Component view = ...;
 * PresentationModelObject pmo = ...;
 * BindingContext bindingCtx = ...;
 * 
 * Binder binder = new Binder(view, pmo);
 * binder.setupBindings(bindingCtx);
 * </code>
 * </pre>
 */
public class Binder {

    private final Component view;
    private final Object pmo;

    public Binder(@Nonnull Component view, @Nonnull Object pmo) {
        super();
        this.view = requireNonNull(view, "View must not be null");
        this.pmo = requireNonNull(pmo, "PMO must not be null");
    }

    /** Creates bindings between the view and the PMO and adds them to the given binding context. */
    public void setupBindings(BindingContext bindingContext) {
        LinkedHashMap<BindingDescriptor, Component> bindingDescriptors = readBindings();
        bindingDescriptors.forEach((descriptor, component) -> bindingContext.bind(pmo, descriptor, component, null));
    }

    /** Reads the descriptors and the components to use for binding from the view. */
    private LinkedHashMap<BindingDescriptor, Component> readBindings() {
        LinkedHashMap<BindingDescriptor, Component> bindings = Maps.newLinkedHashMap();
        addFieldBindings(bindings);
        addMethodBindings(bindings);
        return bindings;
    }

    /**
     * Adds descriptors and component for the view's methods annotated with {@link Bind @Bind} to
     * the given map.
     */
    private void addMethodBindings(LinkedHashMap<BindingDescriptor, Component> bindings) {
        BeanUtils.getMethods(view.getClass(), m -> m.isAnnotationPresent(Bind.class))
                .forEach(m -> addMethodBinding(m, bindings));
    }

    /**
     * Adds the descriptor and component (returned by the method) for the given method to the given
     * map.
     * 
     * @throws IllegalStateException if the method does not return a component or requires
     *             parameters
     * @throws NullPointerException if the component returned by the method is {@code null}
     */
    private void addMethodBinding(Method method, LinkedHashMap<BindingDescriptor, Component> bindings) {
        Preconditions.checkState(Component.class.isAssignableFrom(method.getReturnType()),
                                 method + " does not return a Component and cannot be annotated with @Bind");
        Preconditions.checkState(method.getParameterCount() == 0,
                                 method + " has parameters and cannot be annotated with @Bind");
        try {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            Component component = (Component)method.invoke(view);
            Preconditions.checkNotNull(component,
                                       "Cannot create binding for method " + method + " as it returned null");
            BindAnnotationDescriptor descriptor = new BindAnnotationDescriptor(method.getAnnotation(Bind.class));
            bindings.put(descriptor, component);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new LinkkiRuntimeException(e);
        }
    }

    /**
     * Adds descriptors and component for the view's fields annotated with {@link Bind @Bind} to the
     * given map.
     */
    private void addFieldBindings(LinkedHashMap<BindingDescriptor, Component> bindings) {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(view.getClass(), Bind.class);
        fields.forEach(f -> addFieldBinding(f, bindings));

    }

    /**
     * Adds the descriptor and component for the given field to the given map.
     * 
     * @throws IllegalStateException if the field does not hold a component
     * @throws NullPointerException if the component held by the field is {@code null}
     */
    private void addFieldBinding(Field field, LinkedHashMap<BindingDescriptor, Component> bindings) {
        Preconditions.checkState(Component.class.isAssignableFrom(field.getType()),
                                 field + " is not a Component-typed field and cannot be annotated with @Bind");
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Component component = (Component)field.get(view);
            Preconditions.checkNotNull(component, "Cannot create binding for field " + field + " as it is null");
            BindAnnotationDescriptor descriptor = new BindAnnotationDescriptor(field.getAnnotation(Bind.class));
            bindings.put(descriptor, component);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new LinkkiRuntimeException(e);
        }
    }
}
