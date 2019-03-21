/*******************************************************************************
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 * 
 * All Rights Reserved - Alle Rechte vorbehalten.
 *******************************************************************************/
package org.linkki.core.defaults.uielement;

import java.util.function.Function;
import java.util.stream.Stream;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.ElementDescriptor;
import org.linkki.core.binding.descriptor.UIAnnotationReader;
import org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;

/**
 * A simple utility class that uses the {@link UIAnnotationReader} to find UI element annotations and
 * creates the defined components. The caller is responsible for adding the component to a surrounding
 * UI component and for returning a {@link ComponentWrapper} that could be handled over to the
 * {@link BindingContext}.
 */
public class UiElementCreator {

    private UiElementCreator() {
        // prevents instantiation
    }

    /**
     * Creates all UI elements that were found in the PMO by searching for methods annotated with UI
     * element annotations.
     * <p>
     * <em>The created UI components are not added to any parent. You can do that in the
     * {@code componentWrapperCreator} {@link Function} or afterwards retrieve the components from their
     * {@link ComponentWrapper#getComponent() ComponentWrappers}.</em>
     * 
     * @implNote UI element annotations like {@code @UITextField} are identified by the meta-annotation
     *           {@link LinkkiBindingDefinition @LinkkiBindingDefinition}. This utility simply uses the
     *           {@link UIAnnotationReader} to find these UI elements. For every
     *           {@link ElementDescriptor element},
     *           <ul>
     *           <li>a new UI component is {@link ElementDescriptor#newComponent() created},</li>
     *           <li>passed to the {@code componentWrapperCreator} {@link Function} to wrap it into an
     *           appropriate {@link ComponentWrapper}</li>
     *           <li>on which the {@link ElementDescriptor#getPmoPropertyName() bound property name} is
     *           {@link ComponentWrapper#setId(String) set as ID}</li>
     *           <li>after which it is handed over to the {@link BindingContext} for
     *           {@link BindingContext#bind(Object, org.linkki.core.binding.descriptor.BindingDescriptor, ComponentWrapper)
     *           binding}.</li>
     *           </ul>
     * @param <C> the UI component class created by {@link ElementDescriptor#newComponent()} and handed
     *            to the {@code componentWrapperCreator}
     * @param <W> the {@link ComponentWrapper} class created by the {@code componentWrapperCreator}
     * @param pmo the PMO that contains the UI element annotations
     * @param bindingContext a {@link BindingContext} that is used to register the bindings for the
     *            created UI elements
     * @param componentWrapperCreator a function that wraps the given component in an appropriate
     *            {@link ComponentWrapper} for the binding
     * @return the created {@link ComponentWrapper ComponentWrappers}
     */
    public static <C, W extends ComponentWrapper> Stream<W> createUiElements(Object pmo,
            BindingContext bindingContext,
            Function<C, W> componentWrapperCreator) {
        UIAnnotationReader annotationReader = new UIAnnotationReader(pmo.getClass());
        return annotationReader.getUiElements().map(elementDescriptors -> {
            ElementDescriptor uiElement = elementDescriptors.getDescriptor(pmo);
            @SuppressWarnings("unchecked")
            C component = (C)uiElement.newComponent();
            W componentWrapper = componentWrapperCreator.apply(component);
            componentWrapper.setId(uiElement.getPmoPropertyName());
            bindingContext.bind(pmo, uiElement, componentWrapper);
            return componentWrapper;
        });
    }

}