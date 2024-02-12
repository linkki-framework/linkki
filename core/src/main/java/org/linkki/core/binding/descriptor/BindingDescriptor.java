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

package org.linkki.core.binding.descriptor;

import static java.util.Objects.requireNonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectAnnotationReader;
import org.linkki.core.binding.descriptor.messagehandler.LinkkiMessageHandler;
import org.linkki.core.binding.descriptor.messagehandler.annotation.MessageHandlerAnnotationReader;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyAnnotationReader;
import org.linkki.core.binding.validation.handler.DefaultMessageHandler;
import org.linkki.core.pmo.ModelObject;

/**
 * Describes how to bind a specific component to a specific model. Neither the component nor the
 * model are part of the descriptor, it only consists of the binding information.
 */
public final class BindingDescriptor {

    private final BoundProperty boundProperty;
    private final List<LinkkiAspectDefinition> aspectDefinitions;
    private final LinkkiMessageHandler messageHandler;

    /**
     * Constructs a {@link BindingDescriptor} consisting of the given {@link BoundProperty} and
     * {@link LinkkiAspectDefinition LinkkiAspectDefinitions}.
     */
    public BindingDescriptor(BoundProperty boundProperty, LinkkiAspectDefinition... aspectDefinitions) {
        this.boundProperty = requireNonNull(boundProperty, "boundProperty must not be null");
        // avoid additional array copy by not using this(...)
        this.aspectDefinitions = Arrays.asList(requireNonNull(aspectDefinitions, "aspectDefinitions must not be null"));
        this.messageHandler = new DefaultMessageHandler();
    }

    /**
     * Constructs a {@link BindingDescriptor} consisting of the given {@link BoundProperty} and
     * {@link LinkkiAspectDefinition LinkkiAspectDefinitions}.
     */
    public BindingDescriptor(BoundProperty boundProperty, List<LinkkiAspectDefinition> aspectDefinitions) {
        this(boundProperty, aspectDefinitions, new DefaultMessageHandler());
    }

    private BindingDescriptor(BoundProperty boundProperty, List<LinkkiAspectDefinition> aspectDefinitions,
            LinkkiMessageHandler messageHandler) {
        this.boundProperty = requireNonNull(boundProperty, "boundProperty must not be null");
        this.aspectDefinitions = new ArrayList<>(
                requireNonNull(aspectDefinitions, "aspectDefinitions must not be null"));
        this.messageHandler = requireNonNull(messageHandler, "messageHandler must not be null");
    }

    /**
     * Returns the {@link BoundProperty} that is used to access the model.
     */
    public BoundProperty getBoundProperty() {
        return boundProperty;
    }

    /**
     * Returns the name of the property that the bound UI element displays. For an UI element that
     * accesses the field of a model/PMO class, this is the name of that field. For an UI element
     * that invokes a method (i.e. a button) this is the name of the method.
     * 
     * @deprecated Call {@link BoundProperty#getModelAttribute()} on the object returned by
     *             {@link #getBoundProperty()} instead.
     */
    @Deprecated(since = "2.3.0")
    public String getModelAttributeName() {
        return getBoundProperty().getModelAttribute();
    }

    /**
     * Returns the name of the model object containing the {@link #getModelAttributeName() property}
     * if the bound PMO itself does not contain the property. The PMO has to have a
     * {@link ModelObject @ModelObject} annotation with that name on the method that returns the
     * model object.
     * 
     * @deprecated Call {@link BoundProperty#getModelObject()} on the object returned by
     *             {@link #getBoundProperty()} instead.
     */
    @Deprecated(since = "2.3.0")
    public String getModelObjectName() {
        return getBoundProperty().getModelObject();
    }

    /**
     * Returns name of the property from the PMO.
     * 
     * @deprecated Call {@link BoundProperty#getPmoProperty()} on the object returned by
     *             {@link #getBoundProperty()} instead.
     */
    @Deprecated(since = "2.3.0")
    public String getPmoPropertyName() {
        return getBoundProperty().getPmoProperty();
    }

    /**
     * Returns the configured {@link LinkkiAspectDefinition aspect definitions} that sync the data
     * between UI component and model. The returned list is unmodifiable.
     */
    public List<LinkkiAspectDefinition> getAspectDefinitions() {
        return Collections.unmodifiableList(aspectDefinitions);
    }

    /**
     * Returns the configured {@link LinkkiMessageHandler} that is responsible for processing the
     * validation messages of a specific bound component.
     */
    public LinkkiMessageHandler getMessageHandler() {
        return messageHandler;
    }

    /**
     * Creates a {@link BindingDescriptor} for the given PMO class. The class must be annotated with
     * exactly one layout annotation that describes the bound property.
     */
    public static BindingDescriptor forPmoClass(Class<?> pmoClass) {
        return forAnnotatedElement(pmoClass);
    }

    /**
     * Creates a {@link BindingDescriptor} for the given method. The method must be annotated with
     * exactly one component annotation that describes the bound property.
     * <p>
     * To create a binding descriptor for a method with multiple component annotations, use
     * {@link #forMethod(Method, Annotation)}.
     */
    public static BindingDescriptor forMethod(Method method) {
        return forAnnotatedElement(method);
    }

    /**
     * Creates a {@link BindingDescriptor} for the given method. The method is allowed to have
     * multiple component annotations describing a bound property, the annotation from which to
     * derive the bound property must be passed as a parameter.
     */
    public static BindingDescriptor forMethod(Method method, Annotation annotation) {
        return new BindingDescriptor(BoundPropertyAnnotationReader
                .getBoundProperty(annotation, method),
                AspectAnnotationReader
                        .createAspectDefinitionsFor(annotation, method),
                MessageHandlerAnnotationReader.getMessageHandler(method));
    }

    /**
     * Creates a {@link BindingDescriptor} for the given field. The field must be annotated with
     * exactly one component annotation that describes the bound property.
     */
    public static BindingDescriptor forField(Field field) {
        return forAnnotatedElement(field);
    }

    private static BindingDescriptor forAnnotatedElement(AnnotatedElement annotatedElement) {
        var boundProperty = BoundPropertyAnnotationReader
                .findBoundProperty(annotatedElement)
                .orElseGet(BoundProperty::empty);

        return new BindingDescriptor(boundProperty,
                AspectAnnotationReader.createAspectDefinitionsFor(annotatedElement),
                MessageHandlerAnnotationReader.getMessageHandler(annotatedElement));
    }

}