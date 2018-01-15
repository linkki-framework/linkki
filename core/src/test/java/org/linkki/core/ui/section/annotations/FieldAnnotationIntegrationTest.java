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

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.GridLayout;

public abstract class FieldAnnotationIntegrationTest<F extends AbstractField<?>, P extends FieldAnnotationTestPmo> {

    private static final String PROPERTY_VALUE = "value";
    private static final String PROPERTY_STATIC_VALUE = "staticValue";

    protected GridLayout defaultSection;

    private Object defaultModelObject;
    private P defaultPmo;

    private TestBindingContext bindingContext;
    private Supplier<Object> modelObjectSupplier;
    private Function<Object, ? extends P> pmoCreator;

    @SuppressWarnings("null")
    public FieldAnnotationIntegrationTest(Supplier<Object> modelObjectSupplier,
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
    public void testDynamicEnabledType() {
        testDynamicBinding(F::isEnabled, FieldAnnotationTestPmo::setEnabled, true);
    }

    @Test
    public void testDynamicRequiredType() {
        testDynamicBinding(F::isRequired, FieldAnnotationTestPmo::setRequired, false);
    }

    @Test
    public void testDynamicVisibleType() {
        testDynamicBinding(F::isVisible, FieldAnnotationTestPmo::setVisible, true);
    }

    @Test
    public void testLabelBinding() {
        assertThat(TestUiUtil.getLabelOfComponentAt(defaultSection, 0), is(emptyString()));
        assertThat(TestUiUtil.getLabelOfComponentAt(defaultSection, 1), is(FieldAnnotationTestPmo.TEST_LABEL));
    }

    protected void testDynamicBinding(Predicate<F> predicate,
            BiConsumer<FieldAnnotationTestPmo, Boolean> setter,
            boolean defaultValue) {

        assertThat(predicate.test(getStaticComponent()), is(!defaultValue));

        setter.accept(defaultPmo, !defaultValue);
        updateUi();
        F dynamicComponent = getDynamicComponent();
        assertThat(predicate.test(dynamicComponent), is(!defaultValue));
        setter.accept(defaultPmo, defaultValue);
        updateUi();
        assertThat(predicate.test(dynamicComponent), is(defaultValue));
    }

    @Test
    public void testReadonlyWithoutSetter() {
        assertThat(getStaticComponent().isReadOnly(), is(true));
        assertThat(getDynamicComponent().isReadOnly(), is(false));
    }

    /**
     * Creates a PMO section with a rudimentary Vaadin environment is in place and returns the first
     * control component.
     */
    @SuppressWarnings("unchecked")
    protected F createFirstComponent(Object modelObject) {
        return (F)createSection(newPmo(modelObject)).getComponent(1, 0);
    }

    protected F createFirstComponent() {
        return createFirstComponent(newDefaultModelObject());
    }

    protected GridLayout createSection(FieldAnnotationTestPmo pmo) {
        return TestUiUtil.createSectionWith(pmo);
    }

    protected GridLayout createSection() {
        return createSection(newPmo(newDefaultModelObject()));
    }

    protected F getComponentById(String id) {
        return TestUiUtil.getComponentById(defaultSection, id);
    }

    protected F getDynamicComponent() {
        return getComponentById(PROPERTY_VALUE);
    }

    protected F getStaticComponent() {
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
     * Base class for model object to be used with {@link FieldAnnotationTestPmo}. The property
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
