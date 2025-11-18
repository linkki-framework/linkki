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

package org.linkki.samples.playground.ts.aspects;

import java.util.List;
import java.util.stream.IntStream;

import org.linkki.core.ui.aspects.annotation.BindHeight;
import org.linkki.core.ui.aspects.annotation.BindVariantNames;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITableComponent;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.section.LinkkiSection;
import org.linkki.framework.ui.dialogs.DialogPmo;
import org.linkki.framework.ui.dialogs.UIOpenDialogButton;
import org.linkki.util.handler.Handler;

@UISection(layout = SectionLayout.VERTICAL)
public class BindHeightPmo {

    private final List<TestRowPmo> rows = IntStream.rangeClosed(0, 100)
            .mapToObj(i -> new TestRowPmo())
            .toList();

    @UIOpenDialogButton(position = 10)
    public DialogPmo getFixedHeightDialog() {
        return new DialogPmo() {
            @Override
            public String getCaption() {
                return "Dialog with content with fixed height";
            }

            @Override
            public Handler getOkHandler() {
                return Handler.NOP_HANDLER;
            }

            @Override
            public Object getContentPmo() {
                return new FixedHeightPmo();
            }
        };
    }

    @BindHeight("200px")
    @UITableComponent(position = 20, rowPmoClass = TestRowPmo.class)
    public List<TestRowPmo> getRows() {
        return rows;
    }

    @BindVariantNames(LinkkiSection.THEME_VARIANT_CARD)
    @BindHeight("500px")
    @UISection(layout = SectionLayout.VERTICAL)
    public static class FixedHeightPmo {

        @UILabel(position = 0)
        public String getText() {
            return "The layout has a fixed height of 500px";
        }
    }

    public static class TestRowPmo {

        @UILabel(position = 0, label = "Table with 200px height")
        public String getValue() {
            return "Row";
        }
    }
}
