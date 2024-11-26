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

package org.linkki.samples.playground.ts.section;

import org.linkki.core.ui.aspects.annotation.BindVariantNames;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.core.ui.nested.annotation.UINestedComponent;
import org.linkki.core.vaadin.component.section.LinkkiSection;
import org.linkki.samples.playground.ts.layouts.AbstractBasicElementsLayoutBehaviorPmo;

@UIVerticalLayout
public class SectionThemeVariantPmo {

    @UINestedComponent(position = 10)
    public CardSectionPmo getSectionCard() {
        return new CardSectionPmo();
    }

    @UINestedComponent(position = 20)
    public PlainSectionPmo getSectionPlain() {
        return new PlainSectionPmo();
    }

    @BindVariantNames(LinkkiSection.THEME_VARIANT_CARD)
    @UISection(caption = "Section with variant card", closeable = true)
    public static class CardSectionPmo extends AbstractBasicElementsLayoutBehaviorPmo {

    }

    @UISection(caption = "Section without any variant", closeable = true)
    public static class PlainSectionPmo extends AbstractBasicElementsLayoutBehaviorPmo {

    }

}
