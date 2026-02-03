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
package org.linkki.framework.ui.component;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UICssLayout;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.button.Button;

class UIHeadlineIntegrationTest {

    @Test
    void testGetTitle() {
        @UICssLayout
        class TestPmo {
            private String title = "initial";

            @UIHeadline
            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
        var bindingContext = new BindingContext();
        var pmo = new TestPmo();

        var headline = VaadinUiCreator.createComponent(pmo, bindingContext);

        assertThat(KaribuUtils.getTextContent(headline)).contains("initial");

        pmo.setTitle("new");
        bindingContext.modelChanged();

        assertThat(KaribuUtils.getTextContent(headline)).contains("new");
    }

    @Test
    void testGetHeadlinePmo() {
        var pmo = new TestPmo();
        var container = VaadinUiCreator.createComponent(pmo, new BindingContext());
        var headline = _get(container, Headline.class);
        var headlineButton = _get(headline, Button.class);
        var containerButton = _get(container, Button.class, s -> s.withId("updateHeadline"));
        assertThat(KaribuUtils.getTextContent(container)).contains("HeadlineButtonCounter: 0");
        assertThat(KaribuUtils.getTextContent(container)).contains("initial");

        containerButton.click();

        assertThat(KaribuUtils.getTextContent(container)).contains("updated");

        headlineButton.click();

        assertThat(KaribuUtils.getTextContent(container)).contains("HeadlineButtonCounter: 1");
    }

    @Test
    void testDefaultPosition() {
        @UICssLayout
        class TestPmo {

            @UILabel(position = 10)
            public String getText() {
                return "text";
            }

            @UIHeadline
            public String getTitle() {
                return "title";
            }
        }
        var bindingContext = new BindingContext();
        var pmo = new TestPmo();

        var headline = VaadinUiCreator.createComponent(pmo, bindingContext);

        assertThat(headline.getChildren().findFirst()).get().isInstanceOf(Headline.class);
    }

    @UIHorizontalLayout
    public static class TitlePmo {

        @UILabel(position = 10)
        public String getSubtitle() {
            return "subTitle";
        }
    }

    @UIHorizontalLayout
    public static class ButtonPmo {
        private final Handler buttonHandler;

        public ButtonPmo(Handler buttonHandler) {
            this.buttonHandler = buttonHandler;
        }

        @UIButton(position = 10)
        public void button() {
            buttonHandler.apply();
        }
    }

    @UICssLayout
    public static class TestPmo {
        private final HeadlinePmo headlinePmo;
        private final AtomicInteger counter = new AtomicInteger(0);

        public TestPmo() {
            this.headlinePmo = new HeadlinePmo("initial", new TitlePmo(), new ButtonPmo(counter::incrementAndGet));
        }

        @UIHeadline
        public HeadlinePmo getHeadlinePmo() {
            return headlinePmo;
        }

        @UILabel(position = 10)
        public String getButtonsPmoCounter() {
            return "HeadlineButtonCounter: " + counter;
        }

        @UIButton(position = 20)
        public void updateHeadline() {
            headlinePmo.setHeaderTitle("updated");
        }
    }
}
