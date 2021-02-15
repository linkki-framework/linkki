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
package org.linkki.core.binding.descriptor.bindingdefinition;

import static java.util.Objects.requireNonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.descriptor.UIElementAnnotationReader;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.uicreation.LinkkiPositioned;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * A common interface for annotations that are used to create and bind UI components in a view generated
 * from an annotated PMO.
 * <p>
 * As annotations can't implement an interface, the {@link UIElementAnnotationReader} is used to get
 * definition instances for the annotated methods of a (PMO) class.
 * <p>
 * The static methods {@link #isLinkkiBindingDefinition(Annotation)} and {@link #from(Annotation)} can
 * be used to check annotations and create {@link BindingDefinition} instances from them.
 * 
 * @see UIElementAnnotationReader
 * @see org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition
 * 
 * @deprecated since 1.4.0 because this concept was replaced. See "Custom UI element annotation" at
 *             <a href="https://doc.linkki-framework.org/">https://doc.linkki-framework.org/</a> for
 *             more information.
 */
@Deprecated
public interface BindingDefinition {

    Object newComponent();

    /**
     * Mandatory attribute that defines the order in which UI components are displayed
     * 
     * @deprecated Positions are no longer defined by the {@link BindingDefinition}. Use the annotation
     *             {@link LinkkiPositioned} to let a UI-Annotation define a position. Additionally add
     *             the annotation {@link org.linkki.core.uicreation.LinkkiPositioned.Position} to the
     *             position property in the annotation.
     */
    @Deprecated
    default int position() {
        return -1;
    }

    /** Provides a description label next to the UI component */
    String label();

    /** If and how the enabled state of the UI component is bound to the PMO. */
    EnabledType enabled();

    /** If and how the visible state of the UI component is bound to the PMO. */
    VisibleType visible();

    /**
     * If and how the required state of the UI component is bound to the PMO. Ignored for UI components
     * that cannot be required, e.g. buttons.
     */
    RequiredType required();

    /**
     * Name of the model object that is to be bound if multiple model objects are included for model
     * binding
     */
    String modelObject();

    /**
     * The name of a property in the class of the bound {@link ModelObject} to use model binding
     */
    String modelAttribute();

    /**
     * Returns {@code true} if the given annotation is a non-null annotation marked as
     * {@link org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition}.
     */
    public static boolean isLinkkiBindingDefinition(@CheckForNull Annotation annotation) {
        if (annotation == null) {
            return false;
        } else {
            return annotation.annotationType()
                    .isAnnotationPresent(org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition.class);
        }
    }

    /**
     * Returns the {@link BindingDefinition} for the given annotation. Throws an exception if the
     * annotation is {@code null} or not annotated as a
     * {@link org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition}.
     * In other words, this method should only be invoked if
     * {@link #isLinkkiBindingDefinition(Annotation)} returns {@code true} for the given annotation.
     */
    public static BindingDefinition from(Annotation annotation) {
        Class<? extends Annotation> annotationClass = requireNonNull(annotation, "annotation must not be null")
                .annotationType();

        org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition[] bindingDefinitions = annotationClass
                .getAnnotationsByType(org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition.class);
        if (bindingDefinitions.length == 0) {
            throw new IllegalArgumentException(
                    annotation + " is not annotated as "
                            + org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition.class
                                    .getName());
        }
        Class<? extends BindingDefinition> bindingDefinitionClass = bindingDefinitions[0].value();

        try {
            return bindingDefinitionClass.getConstructor(annotationClass).newInstance(annotation);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            throw new LinkkiBindingException("Cannot instantiate " + bindingDefinitionClass.getName(), e);
        }
    }

    /**
     * {@link BoundPropertyCreator} that reads {@link BoundProperty bound properties} from a
     * {@link BindingDefinition}.
     * 
     * @deprecated since 1.4.0 because this concept was replaced. See "Custom UI element annotation" at
     *             <a href="https://doc.linkki-framework.org/">https://doc.linkki-framework.org/</a> for
     *             more information.
     */
    @Deprecated
    public static class BindingDefinitionBoundPropertyCreator implements BoundPropertyCreator<Annotation> {

        @Override
        public BoundProperty createBoundProperty(Annotation annotation, AnnotatedElement annotatedElement) {
            BindingDefinition bindingDefinition = BindingDefinition.from(annotation);
            return BoundProperty.of((Method)annotatedElement)
                    .withModelAttribute(bindingDefinition.modelAttribute())
                    .withModelObject(bindingDefinition.modelObject());
        }
    }
}
