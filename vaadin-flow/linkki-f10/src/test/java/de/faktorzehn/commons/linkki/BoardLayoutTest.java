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

import static de.faktorzehn.commons.linkki.utils.ComponentConditions.childOf;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.Text;

import de.faktorzehn.commons.linkki.board.BoardComponent;
import de.faktorzehn.commons.linkki.board.BoardComponent.BoardComponentVariant;
import de.faktorzehn.commons.linkki.board.BoardLayout;

class BoardLayoutTest {

    @Test
    void testBoardComponentsAreAdded() {
        BoardComponent boardComponent = new BoardComponent("caption", new Text("test"), BoardComponentVariant.MEDIUM);

        TestBoardLayout boardLayout = new TestBoardLayout(boardComponent);

        assertThat(boardComponent).is(childOf(boardLayout));
    }

    private static class TestBoardLayout extends BoardLayout {

        public TestBoardLayout(BoardComponent... boardComponents) {
            super(boardComponents);
        }

        private static final long serialVersionUID = 1L;

    }
}
