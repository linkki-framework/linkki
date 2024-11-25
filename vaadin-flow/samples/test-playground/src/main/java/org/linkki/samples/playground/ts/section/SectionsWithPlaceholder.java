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

import java.util.List;

import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.aspects.annotation.BindPlaceholder;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITableComponent;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.core.ui.nested.annotation.UINestedComponent;
import org.linkki.core.ui.theme.LinkkiTheme;

@UIVerticalLayout
public class SectionsWithPlaceholder {

    @UINestedComponent(position = 10)
    public EmptySectionWithDynamicPlaceholderPmo getEmptySectionWithDynamicPlaceholderPmo() {
        return new EmptySectionWithDynamicPlaceholderPmo();
    }

    @UINestedComponent(position = 20)
    public SectionWithInvisibleChildrenPlaceholderPmo getSectionWithInvisibleChildrenPlaceholderPmo() {
        return new SectionWithInvisibleChildrenPlaceholderPmo();
    }

    @UINestedComponent(position = 30)
    public SectionWithEmptyTablePmo getSectionWithEmptyTablePmo() {
        return new SectionWithEmptyTablePmo();
    }

    @UINestedComponent(position = 40)
    public ClosableEmptySectionWithDynamicPlaceholderPmo getClosableEmptySectionWithDynamicPlaceholderPmo() {
        return new ClosableEmptySectionWithDynamicPlaceholderPmo();
    }

    @UINestedComponent(position = 100)
    public SectionWithUILabelAsPlaceholderPmo getSectionWithUILabelAsPlaceholderPmo() {
        return new SectionWithUILabelAsPlaceholderPmo();
    }

    // tag::bindPlaceholder-section[]
    @BindPlaceholder
    @UISection(caption = "Section with no child elements")
    public static class EmptySectionWithDynamicPlaceholderPmo {

        public String getPlaceholder() {
            return "This section contains no child elements.";
        }

    }
    // end::bindPlaceholder-section[]

    @BindPlaceholder("This section contains no visible child elements.")
    @UISection(caption = "Section with no visible child elements", layout = SectionLayout.VERTICAL)
    public static class SectionWithInvisibleChildrenPlaceholderPmo {

        private boolean visible = false;

        @SectionHeader
        @UICheckBox(position = -10, caption = "Children visible")
        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        @UILabel(position = 0, visible = VisibleType.DYNAMIC)
        public String getLabel() {
            return "I am an invisible label.";
        }

        @UICheckBox(position = 10, visible = VisibleType.DYNAMIC)
        public boolean getCheckbox() {
            return true;
        }

        public boolean isLabelVisible() {
            return visible;
        }

        public boolean isCheckboxVisible() {
            return visible;
        }

    }

    @BindPlaceholder("This placeholder should not be visible.")
    @UISection(caption = "Section with an empty table", layout = SectionLayout.VERTICAL)
    public static class SectionWithEmptyTablePmo {

        @BindPlaceholder("This table is empty.")
        @UITableComponent(position = 0, rowPmoClass = Void.class)
        public List<Void> getRowPmos() {
            return List.of();
        }

    }

    @UISection(caption = "Section with @UILabel as placeholder")
    public static class SectionWithUILabelAsPlaceholderPmo {

        @UILabel(position = 0, styleNames = LinkkiTheme.Text.TEXT_PLACEHOLDER)
        public String getPlaceholder() {
            return "This section is empty.";
        }

    }

    @BindPlaceholder
    @UISection(caption = "Closable section with no child elements", closeable = true)
    public static class ClosableEmptySectionWithDynamicPlaceholderPmo {

        public String getPlaceholder() {
            return "This closable section contains no child elements.";
        }

    }

}
