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
package org.linkki.core.ui.creation.section;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.Binding;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.pmo.ButtonPmo;
import org.linkki.core.ui.creation.section.ButtonPmoBinder.ButtonPmoAspectDefinition;
import org.linkki.core.ui.test.TestButtonPmo;
import org.linkki.core.ui.wrapper.CaptionComponentWrapper;

import com.vaadin.ui.Button;

public class ButtonPmoBinderTest {

    private final PropertyDispatcher wrappedDispatcher = mock(PropertyDispatcher.class);
    private final BindingContext bindingContext = new BindingContext();
    private final Button button = new Button();

    @BeforeEach
    public void setUp() {
        when(wrappedDispatcher.getProperty()).thenReturn(StringUtils.EMPTY);
    }

    @Test
    public void testButtonClickIsForwaredToPmo() {
        ButtonPmo pmo = mock(ButtonPmo.class);
        when(pmo.isEnabled()).thenReturn(true);
        bind(pmo);

        button.click();

        verify(pmo).onClick();
    }

    private Binding bind(ButtonPmo pmo) {
        ComponentWrapper buttonWrapper = new CaptionComponentWrapper("buttonPmo", button, WrapperType.FIELD);
        return bindingContext.bind(pmo, BoundProperty.of(""), Arrays.asList(new ButtonPmoAspectDefinition()),
                                   buttonWrapper);
    }

    @Test
    public void testUpdateFromPmo_PmoSublass() {
        TestButtonPmo pmo = new TestButtonPmo();
        Binding binding = bind(pmo);

        pmo.setEnabled(true);
        pmo.setVisible(true);
        binding.updateFromPmo();
        assertThat(button.isVisible(), is(true));
        assertThat(button.isEnabled(), is(true));

        pmo.setEnabled(false);
        pmo.setVisible(false);
        binding.updateFromPmo();
        assertThat(button.isVisible(), is(false));
        assertThat(button.isEnabled(), is(false));
    }

}
