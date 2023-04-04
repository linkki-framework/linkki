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

package org.linkki.samples.playground.ts.section;

import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.core.ui.nested.annotation.UINestedComponent;
import org.linkki.core.ui.theme.LinkkiTheme;

@UIVerticalLayout
public class SectionsWithPlaceholder {

    @UINestedComponent(position = 10)
    public PlaceholderLabelSectionPmo getFormSection() {
        return new PlaceholderLabelSectionPmo();
    }

    @UINestedComponent(position = 20)
    public PlaceholderlabelHorizontalSectionPmo getHorizontalSection() {
        return new PlaceholderlabelHorizontalSectionPmo();
    }

    @UISection(caption = "Form layout section with placeholder label", layout = SectionLayout.FORM)
    public static class PlaceholderLabelSectionPmo {

        @UILabel(position = 10, label = "", styleNames = LinkkiTheme.PLACEHOLDER_LABEL)
        public String getPlaceholder() {
            return "This is a placeholder.";
        }

    }

    @UISection(caption = "Horizonal section with placeholder label", layout = SectionLayout.HORIZONTAL)
    public static class PlaceholderlabelHorizontalSectionPmo {

        @UILabel(position = 10, label = "", styleNames = LinkkiTheme.PLACEHOLDER_LABEL)
        public String getPlaceholder() {
            return "This is a placeholder.";
        }

    }

}
