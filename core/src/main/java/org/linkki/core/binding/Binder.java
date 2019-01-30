/*
 * Copyright Faktor Zehn GmbH.
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

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.linkki.core.binding.annotations.Bind;
import org.linkki.core.binding.aspect.AspectAnnotationReader;
import org.linkki.core.binding.aspect.definition.LinkkiAspectDefinition;
import org.linkki.core.binding.property.BoundProperty;
import org.linkki.core.binding.property.BoundPropertyAnnotationReader;
import org.linkki.core.ui.components.LabelComponentWrapper;
import org.linkki.util.BeanUtils;

import com.vaadin.ui.Component;

/**
 * A Binder is a utility class used to create data-bindings between the UI elements (such as text-
 * or combo-boxes, buttons etc.) of a view and a PMO .
 * <p>
 * The view is annotated with annotations (e.g. {@link Bind @Bind}) that define which UI elements
 * are bound to which properties of the PMO. It is possible to annotate fields as well as methods.
 * The PMO is just a POJO. Typically, the usage of the Binder looks something like this:
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
        addFieldBindings(bindingContext);
        addMethodBindings(bindingContext);
    }

    /**
     * Adds descriptors and component for the view's fields annotated with {@link Bind @Bind} to the
     * given map.
     * 
     * @param bindingContext
     */
    private void addFieldBindings(BindingContext bindingContext) {
        FieldUtils.getAllFieldsList(view.getClass())
                .stream()
                .filter(BoundPropertyAnnotationReader::isBoundPropertyPresent)
                .forEach(f -> addBinding(bindingContext, f, getComponentFrom(f)));
    }

    private Component getComponentFrom(Field field) {
        Validate.validState(Component.class.isAssignableFrom(field.getType()),
                            "%s is not a Component-typed field and cannot be annotated with @Bind", field);
        try {
            Component component = requireNonNull((Component)BeanUtils.getValueFromField(view, field),
                                                 () -> "Cannot create binding for field " + field + " as it is null");
            return component;
        } catch (IllegalArgumentException e) {
            throw new LinkkiBindingException("Cannot access field " + field, e);
        }
    }

    /**
     * Adds descriptors and component for the view's methods annotated with {@link Bind @Bind} to the
     * given map.
     */
    private void addMethodBindings(BindingContext bindingContext) {
        BeanUtils.getMethods(view.getClass(), BoundPropertyAnnotationReader::isBoundPropertyPresent)
                .forEach(m -> addBinding(bindingContext, m, getComponentFrom(m)));
    }

    private Component getComponentFrom(Method method) {
        Validate.isAssignableFrom(Component.class, method.getReturnType(),
                                  "%s does not return a Component and cannot be annotated with @Bind", method);
        Validate.isTrue(method.getParameterCount() == 0, "%s has parameters and cannot be annotated with @Bind",
                        method);
        try {
            Component component = (Component)method.invoke(view);

            if (component == null) {
                throw new NullPointerException("Cannot create binding for method " + method + " as it returned null");
            }
            return component;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new LinkkiBindingException("Cannot call method " + method, e);
        }
    }

    private void addBinding(BindingContext bindingContext,
            AnnotatedElement annotatedElement,
            Component component) {
        BoundProperty boundProperty = BoundPropertyAnnotationReader.getBoundProperty(annotatedElement);
        List<LinkkiAspectDefinition> aspectDefinitions = AspectAnnotationReader
                .createAspectDefinitionsFor(annotatedElement);
        bindingContext.bind(pmo, boundProperty, aspectDefinitions,
                            new LabelComponentWrapper(component));
    }

}
