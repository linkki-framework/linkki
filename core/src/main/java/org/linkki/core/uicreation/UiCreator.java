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
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectAnnotationReader;
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
 * A utility class that creates UI components from presentation model objects (PMOs). These PMOs must be
 * annotated with annotations which are themselves annotated with the meta-annotation
 * {@link LinkkiComponent @LinkkiComponent}.
 * <p>
 * There are two options:
 * <ol>
 * <li>Creating all components from method-level annotations (such as {@code @UITextField}) with
 * {@link #createUiElements(Object, BindingContext, Function)}</li>
 * <li>Creating a component from a class-level annotation (such as {@code @UISection}) with
 * {@link #createComponent(Object, BindingContext)}. Depending on a {@link LinkkiLayout @LinkkiLayout}
 * annotation accompanying the {@link LinkkiComponent @LinkkiComponent}</li>
 * </ol>
 * Normally the first method is called from a {@link LinkkiLayoutDefinition} when populating the layout
 * with components.
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
     * The created UI components are not added to any parent. You can do that in the
     * {@code componentWrapperCreator} {@link Function} or afterwards retrieve the components from their
     * {@link ComponentWrapper#getComponent() ComponentWrappers}.
     * <p>
     * If the given {@link AnnotatedElement} provides a {@link LinkkiLayout @LinkkiLayout} annotation,
     * its layout definition will be called. This might create further child elements.
     * 
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
        return ComponentAnnotationReader.getComponentDefinitionMethods(pmo.getClass())
                .map(m -> createUiElement(m, pmo, bindingContext, componentWrapperCreator));
    }

    /**
     * Creates and binds a UI element for the given {@link AnnotatedElement}, which will usually be a
     * method or a class. It must provide a {@link LinkkiBoundProperty @LinkkiBoundProperty} and
     * {@link LinkkiComponent @LinkkiComponent} annotation.
     * <p>
     * The created UI component is not added to any parent. You can do that in the
     * {@code componentWrapperCreator} {@link Function} or afterwards retrieve the component from its
     * {@link ComponentWrapper#getComponent() ComponentWrapper}.
     * <p>
     * If the given {@link AnnotatedElement} provides a {@link LinkkiLayout @LinkkiLayout} annotation,
     * its layout definition will be called. This might create further child elements.
     * 
     * @param <C> the UI component class created by {@link ElementDescriptor#newComponent(Object)} and
     *            handed to the {@code componentWrapperCreator}
     * @param <W> the {@link ComponentWrapper} class created by the {@code componentWrapperCreator}
     * @param annotatedElement the element for which to create a component
     * @param pmo the PMO that contains the UI element annotations
     * @param bindingContext a {@link BindingContext} that is used to register the bindings for the
     *            created UI elements
     * @param componentWrapperCreator a function that wraps the given component in an appropriate
     *            {@link ComponentWrapper} for the binding
     * @return the created {@link ComponentWrapper}
     */
    public static <C, W extends ComponentWrapper> W createUiElement(AnnotatedElement annotatedElement,
            Object pmo,
            BindingContext bindingContext,
            Function<C, W> componentWrapperCreator) {

        Annotation componentDefAnnotation = ComponentAnnotationReader
                .getComponentDefinitionAnnotation(annotatedElement, pmo);
        List<LinkkiAspectDefinition> aspects = AspectAnnotationReader
                .createAspectDefinitionsFor(componentDefAnnotation, annotatedElement);
        BoundProperty boundProperty = BoundPropertyAnnotationReader
                .getBoundProperty(componentDefAnnotation, annotatedElement);

        return createComponent(pmo, bindingContext,
                               boundProperty,
                               ComponentAnnotationReader
                                       .getComponentDefinition(componentDefAnnotation, annotatedElement),
                               componentWrapperCreator,
                               aspects,
                               LayoutAnnotationReader.findLayoutDefinition(annotatedElement));
    }

    /**
     * Creates a UI component via the {@link LinkkiComponent @LinkkiComponent} annotated
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
     * Creates a UI component via the {@link LinkkiComponentDefinition} found on the PMO class by way of
     * the {@code componentDefinitionFinder}. The {@link LinkkiLayoutDefinition} found via the
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
     * @throws IllegalArgumentException if a {@link LinkkiComponentDefinition} cannot be found
     * 
     * @deprecated since 1.1.0 this method is deprecated, use
     *             {@link #createComponent(Object, BindingContext, LinkkiComponentDefinition, Optional)}
     *             instead. The functions are simply applied before calling the method because they take
     *             the PMO given as first parameter.
     */
    @Deprecated
    // necessary for javac compiler
    @SuppressWarnings("overloads")
    public static ComponentWrapper createComponent(Object pmo,
            BindingContext bindingContext,
            Function<Class<?>, Optional<LinkkiComponentDefinition>> componentDefinitionFinder,
            Function<Class<?>, Optional<LinkkiLayoutDefinition>> layoutDefinitionFinder) {
        Class<?> pmoClass = pmo.getClass();
        return createComponent(pmo,
                               bindingContext,
                               componentDefinitionFinder.apply(pmoClass)
                                       .orElseThrow(noDefinitionFound(LinkkiComponentDefinition.class,
                                                                      pmoClass)),
                               layoutDefinitionFinder.apply(pmoClass));
    }

    /**
     * Creates a UI component via the {@link LinkkiComponentDefinition} found on the PMO class by way of
     * the {@code componentDefinition}. The optional {@link LinkkiLayoutDefinition} is then used to
     * create all children for the component if it exists. Otherwise a component binding without
     * children is created.
     * 
     * @param pmo a presentation model object
     * @param bindingContext used to bind the component and its children
     * @param componentDefinition a {@link LinkkiComponentDefinition}, for example found via
     *            {@link ComponentAnnotationReader#findComponentDefinition(AnnotatedElement)}
     * @param layoutDefinition a {@link LinkkiLayoutDefinition} from an, for example found via
     *            {@link LayoutAnnotationReader#findLayoutDefinition(AnnotatedElement)}
     * @return a {@link ComponentWrapper} containing the created UI component
     * 
     * @throws IllegalArgumentException if a {@link LinkkiComponentDefinition} cannot be found
     */
    // necessary for javac compiler
    @SuppressWarnings("overloads")
    public static ComponentWrapper createComponent(Object pmo,
            BindingContext bindingContext,
            LinkkiComponentDefinition componentDefinition,
            Optional<LinkkiLayoutDefinition> layoutDefinition) {
        Class<?> annotatedElement = pmo.getClass();
        List<LinkkiAspectDefinition> aspectDefs = AspectAnnotationReader.createAspectDefinitionsFor(annotatedElement);
        BoundProperty boundProperty = BoundPropertyAnnotationReader
                .findBoundProperty(annotatedElement)
                .orElseGet(BoundProperty::empty);
        return createComponent(pmo,
                               bindingContext,
                               boundProperty,
                               componentDefinition,
                               COMPONENT_WRAPPER_FACTORY::createComponentWrapper,
                               aspectDefs,
                               layoutDefinition);
    }

    /**
     * Creates a UI component via the {@link LinkkiComponentDefinition} found on the
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
    /* Package private for test */
    /* private */ static <E extends AnnotatedElement, C, W extends ComponentWrapper> W createComponent(
            Object pmo,
            BindingContext bindingContext,
            BoundProperty boundProperty,
            LinkkiComponentDefinition componentDefinition,
            Function<C, W> componentWrapperCreator,
            List<LinkkiAspectDefinition> aspectDefs,
            Optional<LinkkiLayoutDefinition> optionalLayoutDefinitionFinder) {

        @SuppressWarnings("unchecked")
        C component = (C)componentDefinition.createComponent(pmo);

        W componentWrapper = componentWrapperCreator.apply(component);
        componentWrapper.setId(getComponentId(boundProperty, pmo));

        Optionals.ifPresentOrElse(optionalLayoutDefinitionFinder,
                                  layoutDefinition -> {
                                      ContainerBinding containerBinding = bindingContext
                                              .bindContainer(pmo, boundProperty, aspectDefs,
                                                             componentWrapper);
                                      layoutDefinition.createChildren(component, pmo, containerBinding);
                                  },
                                  () -> bindingContext.bind(pmo, boundProperty, aspectDefs, componentWrapper));

        return componentWrapper;
    }

    private static String getComponentId(BoundProperty boundProperty, Object pmo) {
        return boundProperty.getPmoProperty().isEmpty()
                ? Sections.getSectionId(pmo)
                : boundProperty.getPmoProperty();
    }

    private static Supplier<? extends IllegalArgumentException> noDefinitionFound(Class<?> definitionClass,
            AnnotatedElement annotatedElement) {
        return () -> new IllegalArgumentException("No " + definitionClass.getSimpleName()
                + " was found on " + annotatedElement + ".");
    }

}