package org.linkki.core.binding;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.faktorips.runtime.MessageList;
import org.linkki.core.ButtonPmo;
import org.linkki.core.PresentationModelObject;
import org.linkki.core.binding.dispatcher.BehaviourDependentDispatcher;
import org.linkki.core.binding.dispatcher.BindingAnnotationDispatcher;
import org.linkki.core.binding.dispatcher.ExceptionPropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.ReflectionPropertyDispatcher;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.ui.section.annotations.ButtonDescriptor;
import org.linkki.core.ui.section.annotations.ElementDescriptor;

import com.google.gwt.thirdparty.guava.common.collect.ArrayListMultimap;
import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.google.gwt.thirdparty.guava.common.collect.Multimap;
import com.google.gwt.thirdparty.guava.common.collect.Multimaps;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

/**
 * A binding context binds fields and tables in a part of the user interface like a page or a dialog
 * to properties of presentation model objects. If the value in one of the fields is changed, all
 * fields in the context are updated from the presentation model objects via their bindings.
 */
public class BindingContext {

    private final String name;
    private final ValidationService validationService;
    private final Map<Component, ElementBinding> elementBindings = Maps.newConcurrentMap();
    private final Multimap<Object, ElementBinding> elementBindingsByPmo = Multimaps
            .synchronizedListMultimap(ArrayListMultimap.create());
    private final Map<Component, TableBinding<?>> tableBindings = Maps.newConcurrentMap();
    private final Set<PropertyDispatcher> propertyDispatchers = new HashSet<PropertyDispatcher>();
    private final PropertyBehaviorProvider behaviorProvider;

    /**
     * Creates a new binding context with the given name.
     */
    public BindingContext(@Nonnull String contextName, @Nonnull ValidationService validationService,
            @Nonnull PropertyBehaviorProvider behaviorProvider) {
        this.name = requireNonNull(contextName);
        this.validationService = requireNonNull(validationService);
        this.behaviorProvider = requireNonNull(behaviorProvider);
    }

    /**
     * Returns the context's name that uniquely identifies it in a binding manager.
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * Adds an element binding to the context.
     */
    @Nonnull
    public BindingContext add(@Nonnull ElementBinding binding) {
        requireNonNull(binding);
        elementBindings.put(binding.getBoundComponent(), binding);
        elementBindingsByPmo.put(binding.getPmo(), binding);
        propertyDispatchers.add(binding.getPropertyDispatcher());
        return this;
    }

    /**
     * Adds a table binding to the context.
     */
    @Nonnull
    public BindingContext add(@Nonnull TableBinding<?> tableBinding) {
        requireNonNull(tableBinding);
        tableBindings.put(tableBinding.getBoundComponent(), tableBinding);
        return this;
    }

    /**
     * Returns all element bindings in the context.
     */
    @Nonnull
    public Collection<ElementBinding> getElementBindings() {
        return Collections.unmodifiableCollection(elementBindings.values());
    }

    /**
     * Returns all table bindings in the context.
     */
    @Nonnull
    public Collection<TableBinding<?>> getTableBindings() {
        return Collections.unmodifiableCollection(tableBindings.values());
    }

    /**
     * Removes all bindings in this context that refer to the given PMO.
     */
    public void removeBindingsForPmo(PresentationModelObject pmo) {
        Collection<ElementBinding> toRemove = elementBindingsByPmo.get(pmo);
        toRemove.stream().map(b -> b.getPropertyDispatcher()).forEach(propertyDispatchers::remove);
        elementBindings.values().removeAll(toRemove);
        elementBindingsByPmo.removeAll(pmo);
    }

    /**
     * Removes all bindings in this context that refer to the given component. If the component is a
     * container component, all bindings for the components children and their children are removed,
     * too.
     */
    public void removeBindingsForComponent(Component c) {
        elementBindings.remove(c);
        tableBindings.remove(c);
        if (c instanceof ComponentContainer) {
            ComponentContainer container = (ComponentContainer)c;
            container.iterator().forEachRemaining(this::removeBindingsForComponent);
        }
    }

    /**
     * Updates the UI with the data retrieved via all bindings registered in this context and
     * displays the messages that relates to bound components. The messages are retrieved from the
     * validation services.
     */
    public void updateUI() {
        propertyDispatchers.forEach(pd -> pd.prepareUpdateUI());
        // table bindings have to be updated first, as their update removes bindings
        // and creates new bindings if the table content has changed.
        tableBindings.values().forEach(binding -> binding.updateFromPmo());
        elementBindings.values().forEach(binding -> binding.updateFromPmo());
        updateMessages();
    }

    private void updateMessages() {
        MessageList messages = validationService.getValidationMessages();
        // TODO merken welches binding welche messages anzeigt
        elementBindings.values().forEach(binding -> binding.displayMessages(messages));
        tableBindings.values().forEach(binding -> binding.displayMessages(messages));
    }

    @Nonnull
    public ValidationService getValidationService() {
        return validationService;
    }

    @Nonnull
    public PropertyBehaviorProvider getBehaviorProvider() {
        return behaviorProvider;
    }

    @Override
    public String toString() {
        return "BindingContext [name=" + name + ", validationService=" + validationService + ", behaviorProvider="
                + behaviorProvider + "]";
    }

    /**
     * Binds the model object's property described by the {@linkplain ElementDescriptor} to the
     * {@linkplain Label} and {@linkplain Field}.
     * 
     * @param pmo a model object
     * @param elementDescriptor the descriptor for a property of the model
     * @param label the label to be bound (optional)
     * @param field the field to be bound
     * @return the {@link FieldBinding} connecting model property and UI elements
     */
    @Nonnull
    public <T> FieldBinding<T> bind(@Nonnull Object pmo,
            @Nonnull ElementDescriptor elementDescriptor,
            Label label,
            @Nonnull Field<T> field) {
        requireNonNull(pmo, "PresentationModelObject must not be null");
        requireNonNull(elementDescriptor, "ElementDescriptor must not be null");
        requireNonNull(field, "Field must not be null");
        FieldBinding<T> fieldBinding = new FieldBinding<>(label, field, createDispatcherChain(pmo, elementDescriptor),
                this::updateUI);
        add(fieldBinding);
        return fieldBinding;
    }

    /**
     * Binds the model object's behavior described by the {@linkplain ButtonDescriptor} to the
     * {@linkplain Label} and {@linkplain Button}.
     * 
     * @param pmo a model object
     * @param buttonDescriptor the descriptor for a button
     * @param label the label to be bound (optional)
     * @param button the button to be bound
     * @return the {@link ButtonBinding} connecting model and UI elements
     */
    @Nonnull
    public ButtonBinding bind(@Nonnull Object pmo,
            @Nonnull ButtonDescriptor buttonDescriptor,
            Label label,
            @Nonnull Button button) {
        requireNonNull(pmo, "PresentationModelObject must not be null");
        requireNonNull(buttonDescriptor, "ButtonDescriptor must not be null");
        requireNonNull(button, "Button must not be null");
        ButtonBinding buttonBinding = new ButtonBinding(label, button, createDispatcherChain(pmo, buttonDescriptor),
                this::updateUI);
        add(buttonBinding);
        return buttonBinding;
    }

    /**
     * Binds the {@linkplain ButtonPmo} to the {@linkplain Button}.
     * 
     * @param pmo a button model object
     * @param button the button to be bound
     * @return the {@link ButtonPmoBinding} connecting the button and its model
     */
    @Nonnull
    public ButtonPmoBinding bind(@Nonnull ButtonPmo pmo, @Nonnull Button button) {
        requireNonNull(pmo, "ButtonPmo must not be null");
        requireNonNull(button, "Button must not be null");
        ButtonPmoBinding buttonPmoBinding = new ButtonPmoBinding(button, createDispatcherChain(pmo), this::updateUI);
        add(buttonPmoBinding);
        return buttonPmoBinding;
    }

    @Nonnull
    protected PropertyDispatcher createDispatcherChain(@Nonnull Object pmo,
            @Nonnull ElementDescriptor elementDescriptor) {
        requireNonNull(pmo, "PresentationModelObject must not be null");
        requireNonNull(elementDescriptor, "ElementDescriptor must not be null");

        // @formatter:off
        String propertyName = elementDescriptor.getPropertyName();
        ExceptionPropertyDispatcher exceptionDispatcher = newExceptionDispatcher(pmo, propertyName);
        ReflectionPropertyDispatcher reflectionDispatcher = newReflectionDispatcher(pmo, propertyName, exceptionDispatcher);
        BindingAnnotationDispatcher bindingAnnotationDispatcher = new BindingAnnotationDispatcher(reflectionDispatcher, elementDescriptor);
        return new BehaviourDependentDispatcher(bindingAnnotationDispatcher, behaviorProvider);
        // @formatter:on
    }

    @Nonnull
    protected PropertyDispatcher createDispatcherChain(@Nonnull ButtonPmo buttonPmo) {
        requireNonNull(buttonPmo, "ButtonPmo must not be null");

        // @formatter:off
        ExceptionPropertyDispatcher exceptionDispatcher = newExceptionDispatcher(buttonPmo, StringUtils.EMPTY);
        ReflectionPropertyDispatcher reflectionDispatcher = newReflectionDispatcher(buttonPmo, StringUtils.EMPTY, exceptionDispatcher);
        @SuppressWarnings("deprecation")
        org.linkki.core.binding.dispatcher.ButtonPmoDispatcher buttonPmoDispatcher = new org.linkki.core.binding.dispatcher.ButtonPmoDispatcher(reflectionDispatcher);
        return new BehaviourDependentDispatcher(buttonPmoDispatcher, behaviorProvider);
        // @formatter:on
    }

    private ReflectionPropertyDispatcher newReflectionDispatcher(Object pmo,
            String property,
            PropertyDispatcher wrappedDispatcher) {
        if (pmo instanceof PresentationModelObject) {
            ReflectionPropertyDispatcher modelObjectDispatcher = new ReflectionPropertyDispatcher(
                    ((PresentationModelObject)pmo)::getModelObject, property, wrappedDispatcher);
            return new ReflectionPropertyDispatcher(() -> pmo, property, modelObjectDispatcher);
        } else {
            return new ReflectionPropertyDispatcher(() -> pmo, property, wrappedDispatcher);
        }
    }

    private ExceptionPropertyDispatcher newExceptionDispatcher(Object pmo, String property) {
        if (pmo instanceof PresentationModelObject) {
            return new ExceptionPropertyDispatcher(property, ((PresentationModelObject)pmo).getModelObject(), pmo);
        } else {
            return new ExceptionPropertyDispatcher(property, pmo);
        }
    }

}
