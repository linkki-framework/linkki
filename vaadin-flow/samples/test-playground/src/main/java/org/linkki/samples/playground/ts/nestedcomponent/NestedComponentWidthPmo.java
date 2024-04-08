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
package org.linkki.samples.playground.ts.nestedcomponent;

import java.io.Serial;

import org.linkki.core.ui.aspects.annotation.BindStyleNames;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.core.ui.nested.annotation.UINestedComponent;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

@UISection
public class NestedComponentWidthPmo {

    @UINestedComponent(position = 10, label = "Default width horizontal layout in section")
    public HorizontalLayoutInUISectionPmo getHorizontalLayoutInSectionDefaultWidth() {
        return new HorizontalLayoutInUISectionPmo();
    }

    @UINestedComponent(position = 20, label = "50% width horizontal layout in UIVerticalLayout", width = "50%")
    public HorizontalLayoutInUIVerticalLayoutPmo getHorizontalLayoutInUIVerticalLayoutHalfWidth() {
        return new HorizontalLayoutInUIVerticalLayoutPmo();
    }

    @UINestedComponent(position = 30, label = "Empty width horizontal layout", width = "")
    public HorizontalLayoutPmo getHorizontalLayout() {
        return new HorizontalLayoutPmo();
    }

    @UINestedComponent(position = 40, label = "Empty width horizontal layout small", width = "")
    public HorizontalLayoutSmallPmo getHorizontalLayoutSmall() {
        return new HorizontalLayoutSmallPmo();
    }

    @UISection(caption = "Section with horizontal layout")
    public static class HorizontalLayoutInUISectionPmo extends HorizontalLayout {

        @Serial
        private static final long serialVersionUID = 1L;

        @UITextField(position = 10, label = "Firstname")
        public String getFirstname() {
            return "Max";
        }

        @UITextField(position = 20, label = "Lastname")
        public String getLastname() {
            return "Mustermann";
        }

    }

    @UIVerticalLayout
    public static class HorizontalLayoutInUIVerticalLayoutPmo extends HorizontalLayout {

        @Serial
        private static final long serialVersionUID = -1L;

        @UITextField(position = 10, label = "Firstname")
        public String getFirstname() {
            return "Max";
        }

        @UITextField(position = 20, label = "Lastname")
        public String getLastname() {
            return "Mustermann";
        }

    }

    @UIHorizontalLayout
    @BindStyleNames(LumoUtility.Width.SMALL)
    public static class HorizontalLayoutSmallPmo {

        @UITextField(position = 10, label = "Firstname")
        public String getFirstname() {
            return "Max";
        }

        @UITextField(position = 20, label = "Lastname")
        public String getLastname() {
            return "Mustermann";
        }

    }

    @UIHorizontalLayout
    public static class HorizontalLayoutPmo {

        @UITextField(position = 10, label = "Firstname", width = "200px")
        public String getFirstname() {
            return "Max";
        }

        @UITextField(position = 20, label = "Lastname", width = "200px")
        public String getLastname() {
            return "Mustermann";
        }

    }

}
