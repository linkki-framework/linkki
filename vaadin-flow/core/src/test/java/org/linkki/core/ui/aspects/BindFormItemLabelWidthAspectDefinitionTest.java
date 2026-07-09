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
package org.linkki.core.ui.aspects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.core.ui.wrapper.NoLabelComponentWrapper;

import com.vaadin.flow.component.formlayout.FormLayout;

class BindFormItemLabelWidthAspectDefinitionTest {

    @Test
    void testCreateUiUpdater_SetsLabelWidth() {
        var layout = new FormLayout();

        update("15em", layout);

        assertThat(layout.getStyle().get("--linkki-form-item-label-width"), is("15em"));
    }

    @Test
    void testCreateUiUpdater_OverwritesExistingLabelWidth() {
        var layout = new FormLayout();
        layout.getStyle().set("--linkki-form-item-label-width", "8em");

        update("20em", layout);

        assertThat(layout.getStyle().get("--linkki-form-item-label-width"), is("20em"));
    }

    private void update(String width, com.vaadin.flow.component.Component component) {
        new BindFormItemLabelWidthAspectDefinition(width)
                .createUiUpdater(null, new NoLabelComponentWrapper(component))
                .apply();
    }

}
