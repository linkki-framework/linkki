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

package org.linkki.samples.playground.uitest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.allelements.AbstractAllUiElementsSectionPmo;
import org.linkki.samples.playground.allelements.AbstractAllUiElementsSectionPmo.AllUiElementsUiSectionPmo;

import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;


public class BindStyleNamesTest extends AbstractUiTest {

    private <T> VerticalLayoutElement getSection(Class<T> cls) {
        return $(VerticalLayoutElement.class).id(cls.getSimpleName());
    }

    @Test
    public void testDynamicSectionStyleNames() {
        VerticalLayoutElement allElements = getSection(AllUiElementsUiSectionPmo.class);

        assertThat(allElements.hasClassName(AbstractAllUiElementsSectionPmo.CSS_NAME), is(true));
    }

}