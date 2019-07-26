/*******************************************************************************
 * Copyright (c) Faktor Zehn GmbH - www.faktorzehn.de
 * 
 * All Rights Reserved - Alle Rechte vorbehalten.
 *******************************************************************************/
package org.linkki.core.uicreation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.ContainerBinding;
import org.linkki.core.binding.descriptor.ElementDescriptor;
import org.linkki.core.binding.descriptor.UIElementAnnotationReader;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectAnnotationReader;
import org.linkki.core.binding.descriptor.bindingdefinition.annotation.LinkkiBindingDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyAnnotationReader;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.ComponentWrapperFactory;
import org.linkki.core.defaults.section.Sections;
import org.linkki.core.uicreation.layout.LayoutAnnotationReader;
import org.linkki.core.uicreation.layout.LinkkiLayout;
import org.linkki.core.uicreation.layout.LinkkiLayoutDefinition;
import org.linkki.core.uiframework.UiFramework;
import org.linkki.util.Optionals;

/**
 * A utility class that creates UI components from presentation model objects. These PMOs must be
 * annotated with annotations which are themselves annotated with the meta-annotation
 * {@link LinkkiComponent @LinkkiComponent}.
 * <p>
 * There are two options:
 * <ol>
 * <li>Creating all components from method-level annotations (such as {@code @UITextField}) with
 * {@link #createUiElements(Object, BindingContext, Function)}</li>
 * <li>Creating a component from a class-level annotation (such as {@code @UISection}) with
 * {@link #createComponent(Object, BindingContext)}. Depending on a {@link LinkkiLayout @LinkkiLayout}
 * annotation accompanying the {@link LinkkiComponent @LinkkiComponent}, the first option,
 * {@link #createUiElements(Object, BindingContext, Function)}, will be called as well (as is the case
 * for {@code @UISection's SectionLayoutDefinition}).</li>
 * </ol>
 */
public class UiCreator {

    private static final ComponentWrapperFactory COMPONENT_WRAPPER_FACTORY = UiFramework.getComponentWrapperFactory();

    private UiCreator() {
        // prevents instantiation
    }

    /**
     * Creates and binds all UI fields that were found in the PMO by searching for methods annotated
     * with UI field annotations. Those annotations must provide a
     * {@link LinkkiBoundProperty @LinkkiBoundProperty} and {@link LinkkiComponent @LinkkiComponent}
     * annotation.
     * <p>
     * <em>The created UI components are not added to any parent. You can do that in the
     * {@code componentWrapperCreator} {@link Function} or afterwards retrieve the components from their
     * {@link ComponentWrapper#getComponent() ComponentWrappers}.</em>
     * 
     * @implNote UI element annotations like {@code @UITextField} are identified by the meta-annotation
     *           {@link LinkkiBindingDefinition @LinkkiBindingDefinition}. This utility simply uses the
     *           {@link UIElementAnnotationReader} to find these UI elements. For every
     *           {@link ElementDescriptor element},
     *           <ul>
     *           <li>a new UI component is {@link ElementDescriptor#newComponent(Object) created},</li>
     *           <li>passed to the {@code componentWrapperCreator} {@link Function} to wrap it into an
     *           appropriate {@link ComponentWrapper}</li>
     *           <li>on which the {@link ElementDescriptor#getPmoPropertyName() bound property name} is
     *           {@link ComponentWrapper#setId(String) set as ID}</li>
     *           <li>after which it is handed over to the {@link BindingContext} for
     *           {@link BindingContext#bind(Object, org.linkki.core.binding.descriptor.BindingDescriptor, ComponentWrapper)
     *           binding}.</li>
     *           </ul>
     * @param <C> the UI component class created by {@link ElementDescriptor#newComponent(Object)} and
     *            handed to the {@code componentWrapperCreator}
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
        UIElementAnnotationReader annotationReader = new UIElementAnnotationReader(pmo.getClass());
        return annotationReader.getUiElements().map(elementDescriptors -> {
            ElementDescriptor uiElement = elementDescriptors.getDescriptor(pmo);
            @SuppressWarnings("unchecked")
            C component = (C)uiElement.newComponent(pmo);
            W componentWrapper = componentWrapperCreator.apply(component);
            componentWrapper.setId(uiElement.getPmoPropertyName());
            bindingContext.bind(pmo, uiElement.getBoundProperty(), uiElement.getAspectDefinitions(), componentWrapper);
            return componentWrapper;
        });
    }

    /**
     * Creates a UI component by way of the {@link LinkkiComponent @LinkkiComponent} annotated
     * {@link Annotation} on the given presentation model object. The {@link LinkkiLayout @LinkkiLayout}
     * is then used to create all children for the component.
     * 
     * @param pmo a presentation model object annotated with annotations annotated with
     *            {@link LinkkiComponent @LinkkiComponent} and {@link LinkkiLayout @LinkkiLayout}
     * @param bindingContext used to bind the component and its children
     * @return a {@link ComponentWrapper} containing the created UI component
     * 
     * @throws IllegalArgumentException if a {@link LinkkiComponentDefinition} or
     *             {@link LinkkiLayoutDefinition} cannot be found
     */
    public static ComponentWrapper createComponent(Object pmo,
            BindingContext bindingContext) {
        return createComponent(pmo, bindingContext, ComponentAnnotationReader::findComponentDefinition,
                               LayoutAnnotationReader::findLayoutDefinition);
    }


    /**
     * Creates a UI component by way of the {@link LinkkiComponentDefinition} found on the PMO class by
     * way of the {@code componentDefinitionFinder}. The {@link LinkkiLayoutDefinition} found via the
     * {@code layoutDefinitionFinder} is then used to create all children for the component.
     * 
     * @param pmo a presentation model object
     * @param bindingContext used to bind the component and its children
     * @param componentDefinitionFinder a function to extract a {@link LinkkiComponentDefinition} from
     *            an {@link AnnotatedElement}, for example
     *            {@link ComponentAnnotationReader#findComponentDefinition(AnnotatedElement)}
     * @param layoutDefinitionFinder a function to extract a {@link LinkkiLayoutDefinition} from an
     *            {@link AnnotatedElement}, for example
     *            {@link LayoutAnnotationReader#findLayoutDefinition(AnnotatedElement)}
     * @return a {@link ComponentWrapper} containing the created UI component
     * 
     * @throws IllegalArgumentException if a {@link LinkkiComponentDefinition} or
     *             {@link LinkkiLayoutDefinition} cannot be found
     */
    public static ComponentWrapper createComponent(Object pmo,
            BindingContext bindingContext,
            Function<Class<?>, Optional<LinkkiComponentDefinition>> componentDefinitionFinder,
            Function<Class<?>, Optional<LinkkiLayoutDefinition>> layoutDefinitionFinder) {
        Class<?> annotatedElement = pmo.getClass();
        return UiCreator.createComponent(annotatedElement,
                                         pmo,
                                         bindingContext,
                                         componentDefinitionFinder,
                                         layoutDefinitionFinder);
    }

    /**
     * Creates a UI component by way of the {@link LinkkiComponentDefinition} found on the
     * {@link AnnotatedElement} from the given presentation model object by way of the
     * {@code componentDefinitionFinder}. The {@link LinkkiLayoutDefinition} found via the
     * {@code layoutDefinitionFinder} is then used to create all children for the component.
     * 
     * @param annotatedElement an {@link AnnotatedElement} from which the
     *            {@code componentDefinitionFinder} and {@code layoutDefinitionFinder} can extract a
     *            {@link LinkkiComponentDefinition} and {@link LinkkiLayoutDefinition}
     * @param pmo a presentation model object
     * @param bindingContext used to bind the component and its children
     * @param componentDefinitionFinder a function to extract a {@link LinkkiComponentDefinition} from
     *            an {@link AnnotatedElement}, for example
     *            {@link ComponentAnnotationReader#findComponentDefinition(AnnotatedElement)}
     * @param layoutDefinitionFinder a function to extract a {@link LinkkiLayoutDefinition} from an
     *            {@link AnnotatedElement}, for example
     *            {@link LayoutAnnotationReader#findLayoutDefinition(AnnotatedElement)}
     * @return a {@link ComponentWrapper} containing the created UI component
     * 
     * @throws IllegalArgumentException if a {@link LinkkiComponentDefinition} or
     *             {@link LinkkiLayoutDefinition} cannot be found
     */
    /* Package private for test. If/when we make this method public, it must consider dynamic fields. */
    /* private */ static <E extends AnnotatedElement> ComponentWrapper createComponent(E annotatedElement,
            Object pmo,
            BindingContext bindingContext,
            Function<? super E, Optional<LinkkiComponentDefinition>> componentDefinitionFinder,
            Function<? super E, Optional<LinkkiLayoutDefinition>> layoutDefinitionFinder) {
        LinkkiComponentDefinition componentDefinition = componentDefinitionFinder.apply(annotatedElement)
                .orElseThrow(noDefinitionFound(LinkkiComponentDefinition.class, annotatedElement));
        Object component = componentDefinition.createComponent(pmo);

        ComponentWrapper componentWrapper = COMPONENT_WRAPPER_FACTORY.createComponentWrapper(component);
        componentWrapper.setId(Sections.getSectionId(pmo));

        BoundProperty boundProperty = BoundPropertyAnnotationReader
                .findBoundProperty(annotatedElement)
                .orElseGet(BoundProperty::empty);
        List<LinkkiAspectDefinition> aspectDefs = AspectAnnotationReader.createAspectDefinitionsFor(annotatedElement);

        Optionals.ifPresentOrElse(layoutDefinitionFinder.apply(annotatedElement),
                                  layoutDefinition -> {
                                      ContainerBinding containerBinding = bindingContext
                                              .bindContainer(pmo, boundProperty, aspectDefs,
                                                             componentWrapper);
                                      layoutDefinition.createChildren(component, pmo, containerBinding);
                                  },
                                  () -> bindingContext.bind(pmo, boundProperty, aspectDefs, componentWrapper));

        return componentWrapper;
    }

    private static Supplier<? extends IllegalArgumentException> noDefinitionFound(Class<?> definitionClass,
            AnnotatedElement annotatedElement) {
        return () -> new IllegalArgumentException("No " + definitionClass.getSimpleName()
                + " was found on " + annotatedElement + ".");
    }

}