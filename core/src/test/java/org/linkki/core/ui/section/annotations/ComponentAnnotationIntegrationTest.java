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

package org.linkki.core.ui.section.annotations;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.TestBindingContext;
import org.linkki.core.ui.components.LabelComponentWrapper;
import org.linkki.core.ui.section.descriptor.ElementDescriptor;
import org.linkki.core.ui.section.descriptor.UIAnnotationReader;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;

public abstract class ComponentAnnotationIntegrationTest<C extends AbstractComponent, P extends AnnotationTestPmo> {

    protected static final String PROPERTY_VALUE = "value";
    protected static final String PROPERTY_STATIC_VALUE = "staticValue";

    private Object defaultModelObject;
    private P defaultPmo;
    private TestBindingContext bindingContext;
    private Function<Object, ? extends @NonNull P> pmoCreator;
    private Supplier<Object> modelObjectSupplier;
    private GridLayout defaultSection;

    @SuppressWarnings("null")
    public ComponentAnnotationIntegrationTest(Supplier<Object> modelObjectSupplier,
            Function<Object, ? extends P> pmoCreator) {
        this.modelObjectSupplier = modelObjectSupplier;
        this.pmoCreator = pmoCreator;
    }

    @Before
    public void setUp() {
        defaultModelObject = newDefaultModelObject();
        defaultPmo = newPmo(defaultModelObject);

        bindingContext = TestBindingContext.create();
        defaultSection = TestUiUtil.createSectionWith(defaultPmo, bindingContext);
    }

    TestBindingContext getBindingContext() {
        return bindingContext;
    }

    protected void bind(Object pmo, String propertyName, Component component) {
        UIAnnotationReader uiAnnotationReader = new UIAnnotationReader(pmo.getClass());
        ElementDescriptor elementDescriptor = uiAnnotationReader.getUiElements().stream()
                .filter(d -> d.getPmoPropertyName().equals(propertyName))
                .findFirst().get().getDescriptor(pmo);
        bindingContext.bind(pmo, elementDescriptor, new LabelComponentWrapper(component));
    }

    protected void modelChanged() {
        bindingContext.modelChanged();
    }

    @Test
    public void testPosition() {
        @SuppressWarnings("null")
        @NonNull
        Component component1 = getDefaultSection().getComponent(1, 0);
        assertThat(component1.getId(), is(getDynamicComponent().getId()));
        @SuppressWarnings("null")
        @NonNull
        Component component2 = getDefaultSection().getComponent(1, 1);
        assertThat(component2.getId(), is(getStaticComponent().getId()));
    }

    @Test
    public void testVisible() {
        testBinding(C::isVisible, AnnotationTestPmo::setVisible, true);
    }

    @Test
    public void testEnabled() {
        testBinding(C::isEnabled, AnnotationTestPmo::setEnabled, true);
    }

    @Test
    public void testLabelBinding() {
        assertThat(TestUiUtil.getLabelOfComponentAt(defaultSection, 0), is(emptyString()));
        assertThat(TestUiUtil.getLabelOfComponentAt(defaultSection, 1), is(AnnotationTestPmo.TEST_LABEL));
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
        return (C)createSection(newPmo(modelObject)).getComponent(1, 0);
    }

    protected C createFirstComponent() {
        return createFirstComponent(newDefaultModelObject());
    }

    protected GridLayout createSection(AnnotationTestPmo pmo) {
        return TestUiUtil.createSectionWith(pmo, bindingContext);
    }

    protected GridLayout createSection() {
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

    @SuppressWarnings("null")
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

    protected GridLayout getDefaultSection() {
        return defaultSection;
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