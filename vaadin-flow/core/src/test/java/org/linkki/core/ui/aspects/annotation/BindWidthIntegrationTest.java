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

package org.linkki.core.ui.aspects.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UICssLayout;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.html.Div;

class BindWidthIntegrationTest {

    @Test
    void testBindWidth() {
        var layout = (Div)VaadinUiCreator.createComponent(new TestPmo(), new BindingContext());
        var component = (HasSize)layout.getComponentAt(0);

        assertThat(component.getWidth()).isEqualTo(TestPmo.WIDTH);
    }

    @UICssLayout
    static class TestPmo {

        private static final String WIDTH = "200px";

        @BindWidth(WIDTH)
        @UITextField(position = 0)
        public String getValue() {
            return "";
        }
    }
}
