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

import java.time.LocalDate;

import org.linkki.core.ui.aspects.annotation.BindVariantNames;
import org.linkki.core.ui.aspects.annotation.BindWidth;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.section.LinkkiSection;
import org.linkki.framework.ui.dialogs.DialogPmo;
import org.linkki.framework.ui.dialogs.UIOpenDialogButton;
import org.linkki.util.handler.Handler;

@UISection(layout = SectionLayout.VERTICAL)
public class BindWidthPmo {

    @BindWidth("500px")
    @UIDateField(position = 10)
    public LocalDate getFixedWidthDateField() {
        return LocalDate.now();
    }

    @UIOpenDialogButton(position = 20)
    public DialogPmo getFixedWidthDialog() {
        return new DialogPmo() {
            @Override
            public String getCaption() {
                return "Dialog with content with fixed width";
            }

            @Override
            public Handler getOkHandler() {
                return Handler.NOP_HANDLER;
            }

            @Override
            public Object getContentPmo() {
                return new FixedWidthPmo();
            }
        };
    }

    @BindVariantNames(LinkkiSection.THEME_VARIANT_CARD)
    @BindWidth("500px")
    @UISection(layout = SectionLayout.VERTICAL)
    public static class FixedWidthPmo {

        @UILabel(position = 0)
        public String getText() {
            return "The layout has a fixed width of 500px";
        }
    }
}
