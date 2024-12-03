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
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.core.ui.nested.annotation.UINestedComponent;
import org.linkki.core.ui.theme.LinkkiTheme;

@UIVerticalLayout
public class SectionsWithPlaceholder {

    @UINestedComponent(position = 10)
    public EmptySectionWithPlaceholderPmo getEmptySectionWithDynamicPlaceholderPmo() {
        return new EmptySectionWithPlaceholderPmo();
    }

    @UINestedComponent(position = 20)
    public SectionWithPlaceholderPmo getSectionWithInvisibleChildrenPlaceholderPmo() {
        return new SectionWithPlaceholderPmo();
    }

    @UINestedComponent(position = 30)
    public SectionWithEmptyTablePmo getSectionWithEmptyTablePmo() {
        return new SectionWithEmptyTablePmo();
    }

    @UINestedComponent(position = 40)
    public ClosableSectionWithDynamicPlaceholderPmo getClosableEmptySectionWithDynamicPlaceholderPmo() {
        return new ClosableSectionWithDynamicPlaceholderPmo();
    }

    @UINestedComponent(position = 100)
    public SectionWithUILabelAsPlaceholderPmo getSectionWithUILabelAsPlaceholderPmo() {
        return new SectionWithUILabelAsPlaceholderPmo();
    }

    // tag::bindPlaceholder-section[]
    @BindPlaceholder("This section has no visible children")
    @UISection(caption = "Section with no child elements")
    public static class EmptySectionWithPlaceholderPmo {

    }
    // end::bindPlaceholder-section[]

    @BindPlaceholder("This section contains no visible child elements.")
    @UISection(caption = "Section with placeholder", layout = SectionLayout.VERTICAL)
    public static class SectionWithPlaceholderPmo {

        private boolean childrenVisible = false;

        @SectionHeader
        @UICheckBox(position = -10, caption = "Children visible")
        public boolean isChildrenVisible() {
            return childrenVisible;
        }

        public void setChildrenVisible(boolean childrenVisible) {
            this.childrenVisible = childrenVisible;
        }

        @UILabel(position = 0, visible = VisibleType.DYNAMIC)
        public String getLabel() {
            return "I am a label.";
        }

        public boolean isLabelVisible() {
            return isChildrenVisible();
        }

        @UICheckBox(position = 10, visible = VisibleType.DYNAMIC)
        public boolean getCheckbox() {
            return true;
        }

        public boolean isCheckboxVisible() {
            return isChildrenVisible();
        }
    }

    @BindPlaceholder
    @UISection(caption = "Closable section", closeable = true)
    public static class ClosableSectionWithDynamicPlaceholderPmo {

        private boolean childrenVisible = false;
        private String placeholder = "dynamic placeholder";

        @SectionHeader
        @UICheckBox(position = -20, caption = "Children visible")
        public boolean isChildrenVisible() {
            return childrenVisible;
        }

        public void setChildrenVisible(boolean childrenVisible) {
            this.childrenVisible = childrenVisible;
        }

        @SectionHeader
        @UITextField(position = -10)
        public String getPlaceholder() {
            return placeholder;
        }

        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
        }

        @UILabel(position = 0, visible = VisibleType.DYNAMIC)
        public String getLabel() {
            return "I am a label.";
        }

        public boolean isLabelVisible() {
            return isChildrenVisible();
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

        @SuppressWarnings("deprecation")
        @UILabel(position = 0, styleNames = LinkkiTheme.Text.TEXT_PLACEHOLDER)
        public String getPlaceholder() {
            return "This section is empty.";
        }

    }
}
