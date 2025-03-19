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

package de.faktorzehn.commons.linkki;

import static de.faktorzehn.commons.linkki.utils.ComponentConditions.anyChildrenSatisfying;
import static de.faktorzehn.commons.linkki.utils.ComponentConditions.childOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import de.faktorzehn.commons.linkki.board.BoardComponent;

@SuppressWarnings("deprecation")
class BoardComponentTest {

    @Test
    void testCaptionIsDisplayedSomewhere() {
        String caption = "caption";

        BoardComponent boardComponent =
                new BoardComponent(caption, new Text(""), BoardComponent.BoardComponentVariant.MEDIUM);

        assertThat(boardComponent).has(anyChildrenSatisfying(c -> c.getElement().getText().contentEquals(caption),
                                                             "having the text \"" + caption + "\""));
    }

    @Test
    void testComponentIsAdded() {
        Text component = new Text("");

        BoardComponent boardComponent =
                new BoardComponent("caption", component, BoardComponent.BoardComponentVariant.MEDIUM);

        assertThat(component).is(childOf(boardComponent));
    }

    @Test
    void testVariant_SmallerThan1() {
        Text component = new Text("");
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new BoardComponent("caption", component, null));
    }

    @Test
    void testVariant_DefaultIsMedium() {
        BoardComponent boardComponent = new BoardComponent("caption", new Text(""));

        assertThat(boardComponent.getThemeName())
                .contains(BoardComponent.BoardComponentVariant.MEDIUM.getVariantName());
    }

    @Test
    void testVariant_IsSetAsTheme() {
        BoardComponent boardComponent =
                new BoardComponent("caption", new Text(""), BoardComponent.BoardComponentVariant.MEDIUM);

        assertThat(boardComponent.getThemeName())
                .contains(BoardComponent.BoardComponentVariant.MEDIUM.getVariantName());
    }

    @Test
    void testPmoIsCreatedWithoutVariant() {
        BoardComponent boardComponent = BoardComponent.withPmo("caption", new TestPmo());

        assertThat(boardComponent)
                .has(anyChildrenSatisfying(HorizontalLayout.class::isInstance, "being a HorizontalLayout"));
    }

    @Test
    void testPmoIsCreated() {
        BoardComponent boardComponent =
                BoardComponent.withPmo("caption", new TestPmo(), BoardComponent.BoardComponentVariant.MEDIUM);

        assertThat(boardComponent)
                .has(anyChildrenSatisfying(HorizontalLayout.class::isInstance, "being a HorizontalLayout"));
    }

    @UIHorizontalLayout
    private static class TestPmo {
        // no components
    }
}
