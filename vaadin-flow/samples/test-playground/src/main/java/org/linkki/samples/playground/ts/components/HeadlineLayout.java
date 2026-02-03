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
import java.util.concurrent.atomic.AtomicInteger;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.core.vaadin.component.base.LinkkiText;
import org.linkki.framework.ui.component.Headline;
import org.linkki.framework.ui.component.HeadlinePmo;
import org.linkki.framework.ui.component.UIHeadline;
import org.linkki.framework.ui.notifications.NotificationUtil;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class HeadlineLayout extends VerticalLayout {

    public static final String CHANGE_HEADLINE_TITLE_BUTTON_CAPTION =
            "Click on this button. The headline above should be updated.";
    public static final String BUTTONS_PMO_COUNTER_TEXT = "ButtonsPmo Counter - ";

    @Serial
    private static final long serialVersionUID = 5210322534054245351L;

    public HeadlineLayout() {
        add(createHeadline());
        add(createWithHeadlinePmo());
        add(VaadinUiCreator.createComponent(new DynamicHeadlineTitlePmo(), new BindingContext()));
        add(VaadinUiCreator.createComponent(new DynamicHeadlineTitleWithComponentsPmo(), new BindingContext()));
    }

    private Component createHeadline() {
        var headline = new Headline("Headline Component");
        headline.setId("headline-component");
        var badge = new LinkkiText();
        badge.setText("addToTitle");
        badge.getElement().getThemeList().addAll(List.of("badge", "pill"));
        headline.addToTitle(badge);
        headline.getContent().add(new Button("Button added with getContent().add"));
        return headline;
    }

    private Component createWithHeadlinePmo() {
        var headlinePmoCounter = new AtomicInteger();
        var headlineButtonsPmoCounter = new AtomicInteger();

        var label = new NativeLabel(BUTTONS_PMO_COUNTER_TEXT + headlineButtonsPmoCounter);
        var headlinePmo = new HeadlinePmo("HeadlinePmo - " + headlinePmoCounter, new TitleComponentPmo(),
                new ButtonPmo(
                        () -> label.setText(BUTTONS_PMO_COUNTER_TEXT + headlineButtonsPmoCounter.incrementAndGet())));
        var bindingContext = new BindingContext();
        var button = new Button(CHANGE_HEADLINE_TITLE_BUTTON_CAPTION);
        button.addClickListener(event -> {
            headlinePmo.setHeaderTitle("HeadlinePmo - " + headlinePmoCounter.incrementAndGet());
            bindingContext.updateUi();
        });

        var div = new Div(VaadinUiCreator.createComponent(headlinePmo, bindingContext),
                new VerticalLayout(button,
                        label));
        div.setWidth("100%");
        return div;
    }

    @UIVerticalLayout
    public static class DynamicHeadlineTitlePmo {

        private int headlinePmoTitleCounter = 0;

        @UIHeadline
        public String getHeadlineTitle() {
            return "UIHeadline with title only - " + headlinePmoTitleCounter;
        }

        @UIButton(position = 10, caption = CHANGE_HEADLINE_TITLE_BUTTON_CAPTION)
        public void incrementHeadlinePmoTitleCounter() {
            headlinePmoTitleCounter++;
        }
    }

    @UIVerticalLayout
    public static class DynamicHeadlineTitleWithComponentsPmo {

        private final HeadlinePmo headlinePmo;
        private int headlinePmoCounter = 0;
        private int headlineButtonsPmoCounter = 0;

        public DynamicHeadlineTitleWithComponentsPmo() {
            this.headlinePmo = new HeadlinePmo("UIHeadline with HeadlinePmo - " + headlinePmoCounter,
                    new TitleComponentPmo(),
                    new ButtonPmo(() -> headlineButtonsPmoCounter++));
        }

        @UIHeadline
        public HeadlinePmo getHeadlinePmo() {
            return headlinePmo;
        }

        @UIButton(position = 10, caption = CHANGE_HEADLINE_TITLE_BUTTON_CAPTION)
        public void incrementHeadlinePmoCounter() {
            headlinePmo.setHeaderTitle("UIHeadline with HeadlinePmo - " + ++headlinePmoCounter);
        }

        @UILabel(position = 20)
        public String getHeadlineEndButtonCounter() {
            return BUTTONS_PMO_COUNTER_TEXT + headlineButtonsPmoCounter;
        }
    }

    @UIHorizontalLayout
    public static class TitleComponentPmo {

        @UILabel(position = 10)
        public String getSubheadline() {
            return "sub headline";
        }
    }

    @UIHorizontalLayout
    public static class ButtonPmo {

        private final Handler buttonHandler;

        public ButtonPmo() {
            this(Handler.NOP_HANDLER);
        }

        public ButtonPmo(Handler buttonHandler) {
            this.buttonHandler = buttonHandler;
        }

        @UIButton(position = 10, caption = "Button 1")
        public void button1() {
            NotificationUtil.showInfo("Button 1", "Button 1");
            buttonHandler.apply();
        }

        @UIButton(position = 20, caption = "Button 2")
        public void button2() {
            NotificationUtil.showInfo("Button 2", "Button 2");
            buttonHandler.apply();
        }
    }
}
