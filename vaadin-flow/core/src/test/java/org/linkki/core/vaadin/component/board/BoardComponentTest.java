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

package org.linkki.core.vaadin.component.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.core.ui.test.ComponentConditions.childOf;
import static org.linkki.core.vaadin.component.board.BoardComponent.BoardComponentVariant.MEDIUM;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.core.ui.test.ComponentConditions;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.core.vaadin.component.board.BoardComponent.BoardComponentVariant;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

class BoardComponentTest {

    @Test
    void testConstructor_Caption() {
        var caption = "caption";

        var boardComponent = new BoardComponent(caption, new Text(""), MEDIUM);

        assertThat(KaribuUtils.getTextContent(boardComponent))
                .contains(caption);
    }

    @Test
    void testConstructor_component() {
        var component = new Text("");

        var boardComponent = new BoardComponent("caption", component, MEDIUM);

        assertThat(component).is(childOf(boardComponent));
    }

    @Test
    void testConstructor_Variant_Default() {
        var boardComponent = new BoardComponent("caption", new Text(""));

        assertThat(boardComponent.getThemeName())
                .contains(MEDIUM.getVariantName());
    }

    @EnumSource(BoardComponentVariant.class)
    @ParameterizedTest
    void testConstructor_Variant(BoardComponentVariant variant) {
        var boardComponent = new BoardComponent("caption", new Text(""),
                variant);

        assertThat(boardComponent.getThemeName())
                .contains(variant.getVariantName());
    }

    @Test
    void testWithPmo_NoVariant() {
        var boardComponent = BoardComponent.withPmo("caption", new TestPmo());

        assertThat(boardComponent)
                .has(ComponentConditions.anyChildSatisfying(HorizontalLayout.class::isInstance,
                                                               "being a HorizontalLayout"));
    }

    @EnumSource(BoardComponentVariant.class)
    @ParameterizedTest
    void testWithPmo_WithVariant(BoardComponentVariant variant) {
        var boardComponent = BoardComponent.withPmo("caption", new TestPmo(), variant);

        assertThat(boardComponent)
                .has(ComponentConditions.anyChildSatisfying(HorizontalLayout.class::isInstance,
                                                               "being a HorizontalLayout"));
        assertThat(boardComponent.getThemeName())
                .contains(variant.getVariantName());
    }

    @UIHorizontalLayout
    private static class TestPmo {
        // no components
    }
}
