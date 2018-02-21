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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.aspect.AspectAnnotationReader;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.exception.LinkkiRuntimeException;
import org.linkki.core.ui.components.LabelComponentWrapper;
import org.linkki.core.ui.section.descriptor.BindAnnotationDescriptor;
import org.linkki.core.ui.section.descriptor.BindingDescriptor;
import org.linkki.util.BeanUtils;

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
 * 
 * Note that the view does not necessarily have to be a Vaadin UI component, it is possible to bind
 * the annotated fields and methods in a POJO. Of course, the bound fields/methods still have to
 * be/return one of the following types
 * <ul>
 * <li>{@link com.vaadin.ui.Field}</li>
 * <li>{@link com.vaadin.ui.Label}</li>
 * <li>{@link com.vaadin.ui.Button}</li>
 * </ul>
 */
public class Binder {

    private final Object view;
    private final Object pmo;

    public Binder(Object view, Object pmo) {
        this.view = requireNonNull(view, "view must not be null");
        this.pmo = requireNonNull(pmo, "pmo must not be null");
    }

    /** Creates bindings between the view and the PMO and adds them to the given binding context. */
    public void setupBindings(BindingContext bindingContext) {
        LinkedHashMap<BindingDescriptor, Component> bindingDescriptors = readBindings();

        bindingDescriptors.forEach((descriptor, component) -> bindingContext
                .bind(pmo, descriptor, new LabelComponentWrapper(component)));
    }

    /** Reads the descriptors and the components to use for binding from the view. */
    private LinkedHashMap<BindingDescriptor, Component> readBindings() {
        LinkedHashMap<BindingDescriptor, Component> bindings = new LinkedHashMap<>();
        addFieldBindings(bindings);
        addMethodBindings(bindings);
        return bindings;
    }

    /**
     * Adds descriptors and component for the view's methods annotated with {@link Bind @Bind} to the
     * given map.
     */
    private void addMethodBindings(LinkedHashMap<BindingDescriptor, Component> bindings) {
        BeanUtils.getMethods(view.getClass(), m -> m.isAnnotationPresent(Bind.class))
                .forEach(m -> addMethodBinding(m, bindings));
    }

    /**
     * Adds the descriptor and component (returned by the method) for the given method to the given map.
     * 
     * @throws IllegalArgumentException if the method does not return a component or requires parameters
     * @throws NullPointerException if the component returned by the method is {@code null}
     */
    private void addMethodBinding(Method method, LinkedHashMap<BindingDescriptor, Component> bindings) {
        Validate.isAssignableFrom(Component.class, method.getReturnType(),
                                  "%s does not return a Component and cannot be annotated with @Bind", method);
        Validate.isTrue(method.getParameterCount() == 0, "%s has parameters and cannot be annotated with @Bind",
                        method);

        try {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }

            Component component = (Component)method
                    .invoke(view);

            if (component == null) {
                throw new NullPointerException("Cannot create binding for method " + method + " as it returned null");
            }

            @SuppressWarnings("null")
            @Nonnull
            Bind bindAnnotation = method.getAnnotation(Bind.class);

            List<LinkkiAspectDefinition> aspectDefinitions = Arrays.asList(method.getAnnotations()).stream()
                    .flatMap(a -> AspectAnnotationReader.createAspectDefinitionsFrom(a).stream())
                    .collect(Collectors.toList());

            BindAnnotationDescriptor descriptor = new BindAnnotationDescriptor(bindAnnotation, aspectDefinitions);
            bindings.put(descriptor, component);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new LinkkiRuntimeException(e);
        }
    }

    /**
     * Adds descriptors and component for the view's fields annotated with {@link Bind @Bind} to the
     * given map.
     */
    @SuppressWarnings("null")
    private void addFieldBindings(LinkedHashMap<BindingDescriptor, Component> bindings) {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(view.getClass(), Bind.class);
        fields.forEach(f -> addFieldBinding(f, bindings));
    }

    /**
     * Adds the descriptor and component for the given field to the given map.
     * 
     * @param field a Component typed field that is annotated with {@link Bind}
     * 
     * @throws IllegalStateException if the field does not hold a component
     * @throws NullPointerException if the component held by the field is {@code null}
     */
    private void addFieldBinding(Field field, LinkedHashMap<BindingDescriptor, Component> bindings) {
        Validate.validState(Component.class.isAssignableFrom(field.getType()),
                            "%s is not a Component-typed field and cannot be annotated with @Bind", field);

        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            Component component = requireNonNull((Component)field.get(view),
                                                 () -> "Cannot create binding for field " + field + " as it is null");

            @SuppressWarnings("null")
            @Nonnull
            Bind bindAnnotation = field.getAnnotation(Bind.class);

            List<LinkkiAspectDefinition> aspectDefinitions = Arrays.asList(field.getAnnotations()).stream()
                    .flatMap(a -> AspectAnnotationReader.createAspectDefinitionsFrom(a).stream())
                    .collect(Collectors.toList());

            BindAnnotationDescriptor descriptor = new BindAnnotationDescriptor(bindAnnotation, aspectDefinitions);
            bindings.put(descriptor, component);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new LinkkiRuntimeException(e);
        }
    }
}
