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

package org.linkki.samples.playground.bugs.uitest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.linkki.samples.playground.bugs.BugCollectionLayout;
import org.linkki.samples.playground.bugs.lin1608.PmoReadonlyModelNotReadonlyPmo;
import org.linkki.samples.playground.uitest.AbstractUiTest;

import com.vaadin.testbench.elements.TextFieldElement;

/**
 * LIN-1608
 */
public class PmoReadonlyModelNotReadonlyTest extends AbstractUiTest {

    @Test
    public void testTextFieldReadonly() {
        clickButton(BugCollectionLayout.ID);

        assertThat($(TextFieldElement.class).id(PmoReadonlyModelNotReadonlyPmo.ID_TEXT).isReadOnly(), is(true));

        assertThat($(TextFieldElement.class).id(PmoReadonlyModelNotReadonlyPmo.ID_TEXT_WITH_MODEL_ATTRIBUTE)
                .isReadOnly(), is(true));

        assertThat($(TextFieldElement.class).id(PmoReadonlyModelNotReadonlyPmo.ID_TEXT_WITH_MODEL_BINDING).isReadOnly(),
                   is(false));
    }
}
