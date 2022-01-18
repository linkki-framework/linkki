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

package org.linkki.core.ui.element.annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;

import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.ElementDescriptor;
import org.linkki.core.binding.descriptor.UIElementAnnotationReader;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

import edu.umd.cs.findbugs.annotations.NonNull;

public abstract class ComponentAnnotationIntegrationTest<C extends Component, P extends AnnotationTestPmo> {

    protected static final String PROPERTY_VALUE = "value";
    protected static final String PROPERTY_STATIC_VALUE = "staticValue";

    private Object defaultModelObject;
    private P defaultPmo;
    private BindingContext bindingContext;
    private final Function<Object, ? extends P> pmoCreator;
    private Supplier<Object> modelObjectSupplier;
    private FlexLayout defaultSection;

    // needs to be a field due to weak reference
    private UI ui;

    public ComponentAnnotationIntegrationTest(Supplier<Object> modelObjectSupplier,
            Function<Object, ? extends P> pmoCreator) {
        this.modelObjectSupplier = modelObjectSupplier;
        this.pmoCreator = pmoCreator;
    }

    /**
     * Constructor for annotations that do not support model binding
     */
    public ComponentAnnotationIntegrationTest(Function<Object, ? extends P> pmoCreator) {
        this.modelObjectSupplier = Object::new;
        this.pmoCreator = pmoCreator;
    }

    @BeforeEach
    public void setUp() {
        ui = new UI();
        ui.setLocale(Locale.GERMAN);
        UI.setCurrent(ui);

        defaultModelObject = newDefaultModelObject();
        defaultPmo = newPmo(defaultModelObject);

        bindingContext = new BindingContext();
        defaultSection = TestUiUtil.createSectionWith(defaultPmo, bindingContext);
    }

    @AfterEach
    public void tearDown() {
        UI.setCurrent(null);
    }

    BindingContext getBindingContext() {
        return bindingContext;
    }

    protected void bind(Object pmo, String propertyName, Component component) {
        UIElementAnnotationReader uiAnnotationReader = new UIElementAnnotationReader(pmo.getClass());
        ElementDescriptor elementDescriptor = uiAnnotationReader.getUiElements()
                .filter(d -> d.getPmoPropertyName().equals(propertyName))
                .findFirst().get().getDescriptor(pmo);
        bindingContext.bind(pmo, elementDescriptor, new NoLabelComponentWrapper(component));
    }

    protected void modelChanged() {
        bindingContext.modelChanged();
    }

    @Test
    public void testPosition() {

        @NonNull
        Component component1 = TestUiUtil.getComponentAtIndex(0, getDefaultSection());
        assertThat(component1.getId(), is(getDynamicComponent().getId()));

        @NonNull
        Component component2 = TestUiUtil.getComponentAtIndex(1, getDefaultSection());
        assertThat(component2.getId(), is(getStaticComponent().getId()));
    }

    @Test
    public void testVisible() {
        testBinding(C::isVisible, AnnotationTestPmo::setVisible, true);
    }

    @Test
    public void testLabelBinding() {
        assertThat(TestUiUtil.getLabelOfComponentAt(defaultSection, 0), is(emptyString()));
        assertThat(TestUiUtil.getLabelOfComponentAt(defaultSection, 1),
                   is(AnnotationTestPmo.TEST_LABEL));
    }

    /**
     * Tests a dynamic boolean binding. Assumes that the {@link #getStaticComponent() static component}
     * is annotated with the non default boolean value.
     * 
     * @param predicate value of the boolean property
     * @param setter setter for the property in pmo
     * @param defaultValue default value of the property
     */
    protected void testBinding(Predicate<C> predicate,
            BiConsumer<AnnotationTestPmo, Boolean> setter,
            boolean defaultValue) {
        testBinding(v -> predicate.test(v), setter, defaultValue, !defaultValue);
    }

    /**
     * Tests that
     * <ul>
     * <li>the {@link #getStaticComponent()} has the <code>testValue</code></li>
     * <li>the {@link #getDynamicComponent()} has the <code>testValue</code> initially and changes its
     * value to <code>defaultValue</code> after using the <code>setter</code>
     * </ul>
     * 
     * @param componentValueGetter getter of the property in component
     * @param setter setter of the property in PMO
     * @param defaultValue default value of the aspect
     * @param testValue test value of the apsect, should be the initial value of both components
     */
    protected <V> void testBinding(Function<C, V> componentValueGetter,
            BiConsumer<AnnotationTestPmo, V> setter,
            V defaultValue,
            V testValue) {
        assertThat(componentValueGetter.apply(getStaticComponent()), is(testValue));

        setter.accept(defaultPmo, testValue);
        modelChanged();

        C dynamicComponent = getDynamicComponent();
        assertThat(componentValueGetter.apply(dynamicComponent), is(testValue));
        setter.accept(defaultPmo, defaultValue);
        modelChanged();
        assertThat(componentValueGetter.apply(dynamicComponent), is(defaultValue));
    }

    /**
     * Creates a PMO section with a rudimentary Vaadin environment is in place and returns the first
     * control component.
     */
    @SuppressWarnings("unchecked")
    protected C createFirstComponent(Object modelObject) {
        return (C)TestUiUtil.getComponentAtIndex(0, createSection(newPmo(modelObject)));
    }

    protected C createFirstComponent() {
        return createFirstComponent(newDefaultModelObject());
    }

    protected FlexLayout createSection(AnnotationTestPmo pmo) {
        return TestUiUtil.createSectionWith(pmo, bindingContext);
    }

    protected FlexLayout createSection() {
        return createSection(newPmo(newDefaultModelObject()));
    }

    protected C getComponentById(String id) {
        return TestUiUtil.getComponentById(defaultSection, id);
    }

    protected C getDynamicComponent() {
        return getComponentById(PROPERTY_VALUE);
    }

    protected C getStaticComponent() {
        return getComponentById(PROPERTY_STATIC_VALUE);
    }


    protected P newPmo(Object modelObject) {
        return pmoCreator.apply(modelObject);
    }

    protected P newDefaultPmo() {
        return newPmo(newDefaultModelObject());
    }

    protected P getDefaultPmo() {
        return defaultPmo;
    }

    protected Object newDefaultModelObject() {
        return modelObjectSupplier.get();
    }

    protected Object getDefaultModelObject() {
        return defaultModelObject;
    }

    protected FlexLayout getDefaultSection() {
        return defaultSection;
    }

    /**
     * Uses the new given supplier to {@link #setUp()} everything again.
     */
    public void setModelObjectSupplier(Supplier<Object> modelObjectSupplier) {
        this.modelObjectSupplier = modelObjectSupplier;
        setUp();
    }

    /**
     * Base class for model object to be used with {@link AnnotationTestPmo}. The property
     * <code>value</code> should not have a setter so that it can be used for read-only behavior
     * testing.
     */
    protected static abstract class TestModelObject<T> {

        public T getStaticValue() {
            return getValue();
        }

        public abstract T getValue();

        public abstract void setValue(T value);
    }

}