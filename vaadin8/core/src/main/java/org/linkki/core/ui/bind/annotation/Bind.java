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
package org.linkki.core.ui.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.Binder;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.defaults.ui.element.aspects.types.AvailableValuesType;
import org.linkki.core.defaults.ui.element.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.element.aspects.types.RequiredType;
import org.linkki.core.defaults.ui.element.aspects.types.VisibleType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.bind.annotation.Bind.BindAnnotationBoundPropertyCreator;
import org.linkki.util.BeanUtils;

/**
 * This annotation is used to bind manually created components with a PMO and optionally a model object.
 * It is used to manually create UI layouts where linkki @UI... annotations do not give the necessary
 * flexibility. To still keep the linkki binding it is possible to annotate the created field components
 * using this annotation. Afterwards just call
 * {@link Binder#setupBindings(org.linkki.core.binding.BindingContext)} to bring the UI and the model
 * together.
 * 
 * @see Binder
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
@LinkkiBoundProperty(BindAnnotationBoundPropertyCreator.class)
@LinkkiAspect(BindAnnotationAspectDefinitionCreator.class)
public @interface Bind {

    /**
     * The name of the PMO's property to which the UI element is bound. If it is empty (default) the
     * property name is derived from the annotated element, that means:
     * <ul>
     * <li>in case of a field it is the field name</li>
     * <li>it case of a getter method it is the method name without is/get</li>
     * <li>otherwise it is the full name of the annotated method</li>
     * </ul>
     * <p>
     * Note that for each aspect, the {@link #pmoProperty()} is evaluated before
     * {@link #modelAttribute()}. That means if an aspect method can be found with the defined
     * {@link #pmoProperty()} in the PMO, that method is used. If no method can be found in the PMO, the
     * {@link #modelAttribute()} is then used to find the method in the {@link #modelObject()}. If no
     * {@link #modelAttribute()} is defined, {@link #pmoProperty()} is used to find the method in the
     * {@link #modelObject()}.
     */
    String pmoProperty() default "";

    /**
     * The name of the {@link ModelObject} this component is bound to, by default
     * {@link ModelObject#DEFAULT_NAME}.
     */
    String modelObject() default ModelObject.DEFAULT_NAME;

    /**
     * Name of the {@linkplain #modelObject() model object's} attribute that is bound to this component.
     * If none is specified, {@link #pmoProperty()} is used.
     */
    String modelAttribute() default "";

    /** If and how the enabled state of the UI element is bound to the PMO. */
    EnabledType enabled() default EnabledType.ENABLED;

    /** If and how the visible state of the UI element is bound to the PMO. */
    VisibleType visible() default VisibleType.VISIBLE;

    /**
     * If and how the required state of the UI element is bound to the PMO. Ignored for UI elements that
     * cannot be required, e.g. buttons.
     */
    RequiredType required() default RequiredType.NOT_REQUIRED;

    /**
     * If and how the available values are bound to the PMO. Relevant only for UI elements that have
     * available values (e.g. combo boxes), ignored for all other elements.
     */
    AvailableValuesType availableValues() default AvailableValuesType.NO_VALUES;

    /**
     * Initializes a {@link BoundProperty} with the values of the {@link Bind @Bind} annotation's
     * {@link Bind#pmoProperty() pmoProperty}, {@link Bind#modelObject() modelObject}, and
     * {@link Bind#modelAttribute() modelAttribute} attributes. If the {@link Bind#pmoProperty()
     * pmoProperty} is not set, the annotated field/method's name is used.
     */
    class BindAnnotationBoundPropertyCreator implements BoundPropertyCreator<Bind> {

        @Override
        public BoundProperty createBoundProperty(Bind annotation, AnnotatedElement annotatedElement) {
            return BoundProperty.of(getPmoProperty(annotation, annotatedElement))
                    .withModelObject(annotation.modelObject())
                    .withModelAttribute(annotation.modelAttribute());
        }

        private String getPmoProperty(Bind annotation, AnnotatedElement annotatedElement) {
            String pmoProperty = annotation.pmoProperty();
            if (StringUtils.isEmpty(pmoProperty)) {
                if (annotatedElement instanceof Method) {
                    pmoProperty = BeanUtils.getPropertyName((Method)annotatedElement);
                } else if (annotatedElement instanceof Field) {
                    pmoProperty = ((Field)annotatedElement).getName();
                } else {
                    throw new IllegalArgumentException("The @" + Bind.class.getSimpleName()
                            + " annotation only supports reading the property name from " + Field.class.getSimpleName()
                            + "s and " + Method.class.getSimpleName() + "s");
                }
            }
            return pmoProperty;
        }

    }

}
