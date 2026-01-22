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
package org.linkki.samples.playground.ts.components;

import java.io.Serial;
import java.util.List;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.core.vaadin.component.base.LinkkiText;
import org.linkki.framework.ui.component.Headline;
import org.linkki.framework.ui.component.UIHeadline;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class HeadlineLayout extends VerticalLayout {

    @Serial
    private static final long serialVersionUID = 5210322534054245351L;

    public HeadlineLayout() {
        add(createHeadline());
        add(VaadinUiCreator.createComponent(new DynamicHeadlinePmo(), new BindingContext()));
    }

    private Component createHeadline() {
        var headline = new Headline("Headline title");
        headline.setId("headline-component");
        var badge = new LinkkiText();
        badge.setText("addToTitle");
        badge.getElement().getThemeList().addAll(List.of("badge", "pill"));
        headline.addToTitle(badge);
        headline.getContent().add(new Button("Button added with getContent().add"));
        return headline;
    }

    @UIVerticalLayout
    public static class DynamicHeadlinePmo {

        private int counter = 0;

        @UIHeadline
        public String getHeadline() {
            return "UIHeadline - " + counter;
        }

        @UIButton(position = 10, caption = "Click on this button. The headline should be updated.")
        public void increment() {
            counter++;
        }

    }

}
