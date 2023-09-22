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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.textfield.HasAutocomplete;

abstract class FieldAnnotationIntegrationTest<F extends AbstractField<?, ?>, P extends AnnotationTestPmo>
        extends ComponentAnnotationIntegrationTest<F, P> {

    FieldAnnotationIntegrationTest(Supplier<Object> modelObjectSupplier,
            Function<Object, ? extends P> pmoCreator) {
        super(modelObjectSupplier, pmoCreator);
    }

    @Test
    void testRequired() {
        assertThat(getStaticComponent().isRequiredIndicatorVisible()).isTrue();
        testBinding(F::isRequiredIndicatorVisible, AnnotationTestPmo::setRequired, false);
    }

    /**
     * Tests if null value is correctly transferred to the model if the field is required.
     */
    abstract void testNullInputIfRequired();

    @Test
    void testReadonlyWithoutSetter() {
        assertThat(getStaticComponent().isReadOnly()).isTrue();
        assertThat(getDynamicComponent().isReadOnly()).isFalse();
    }

    @Test
    void testDynamicTooltip() {
        testBinding(item -> item.getElement().getAttribute("title"), AnnotationTestPmo::setTooltip,
                    AnnotationTestPmo.DEFAULT_TOOLTIP,
                    AnnotationTestPmo.TEST_TOOLTIP);
    }


    /**
     * All default fields that support {@link HasAutocomplete autocomplete} should disable this feature.
     */
    @Test
    void testAutocompleteOff() {
        F component = createFirstComponent();

        if (component instanceof HasAutocomplete autocompleteComponent) {
            assertThat(autocompleteComponent.getAutocomplete()).isEqualTo(Autocomplete.OFF);
        }
    }

}
