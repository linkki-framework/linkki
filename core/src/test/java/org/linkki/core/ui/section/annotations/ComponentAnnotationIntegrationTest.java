/*
 * Copyright Faktor Zehn AG.
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

import org.junit.Before;
import org.junit.Test;
import org.linkki.core.binding.TestBindingContext;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.GridLayout;

public abstract class ComponentAnnotationIntegrationTest<C extends AbstractComponent, P extends AnnotationTestPmo> {

    private static final String PROPERTY_VALUE = "value";
    private static final String PROPERTY_STATIC_VALUE = "staticValue";
    protected GridLayout defaultSection;
    private Object defaultModelObject;
    private P defaultPmo;
    private TestBindingContext bindingContext;
    protected Supplier<Object> modelObjectSupplier;
    protected Function<Object, ? extends P> pmoCreator;

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

    protected void updateUi() {
        bindingContext.updateUI();
    }

    @Test
    public void testDynamicVisibleType() {
        testDynamicBinding(C::isVisible, AnnotationTestPmo::setVisible, true);
    }

    @Test
    public void testLabelBinding() {
        assertThat(TestUiUtil.getLabelOfComponentAt(defaultSection, 0), is(emptyString()));
        assertThat(TestUiUtil.getLabelOfComponentAt(defaultSection, 1), is(AnnotationTestPmo.TEST_LABEL));
    }

    protected void testDynamicBinding(Predicate<C> predicate,
            BiConsumer<AnnotationTestPmo, Boolean> setter,
            boolean defaultValue) {

        assertThat(predicate.test(getStaticComponent()), is(!defaultValue));

        setter.accept(defaultPmo, !defaultValue);
        updateUi();
        C dynamicComponent = getDynamicComponent();
        assertThat(predicate.test(dynamicComponent), is(!defaultValue));
        setter.accept(defaultPmo, defaultValue);
        updateUi();
        assertThat(predicate.test(dynamicComponent), is(defaultValue));
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
        return TestUiUtil.createSectionWith(pmo);
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