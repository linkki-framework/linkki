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
import static org.hamcrest.Matchers.is;

import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.AbstractField;

public abstract class FieldAnnotationIntegrationTest<F extends AbstractField<?, ?>, P extends AnnotationTestPmo>
        extends ComponentAnnotationIntegrationTest<F, P> {

    public FieldAnnotationIntegrationTest(Supplier<Object> modelObjectSupplier,
            Function<Object, ? extends P> pmoCreator) {
        super(modelObjectSupplier, pmoCreator);
    }

    @Test
    public void testRequired() {
        assertThat(getStaticComponent().isRequiredIndicatorVisible(), is(true));
        testBinding(F::isRequiredIndicatorVisible, AnnotationTestPmo::setRequired, false);
    }

    /**
     * Tests if null value is correctly transfered to the model if the field is required.
     * 
     * @see FieldValueAspectDefinition#prepareFieldToHandleNullForRequiredFields(AbstractField)
     */
    @SuppressWarnings("javadoc")
    public abstract void testNullInputIfRequired();

    @Test
    public void testReadonlyWithoutSetter() {
        assertThat(getStaticComponent().isReadOnly(), is(true));
        assertThat(getDynamicComponent().isReadOnly(), is(false));
    }

    @Test
    public void testDynamicTooltip() {
        testBinding(item -> item.getElement().getAttribute("title"), AnnotationTestPmo::setTooltip,
                    AnnotationTestPmo.DEFAULT_TOOLTIP,
                    AnnotationTestPmo.TEST_TOOLTIP);
    }
}
