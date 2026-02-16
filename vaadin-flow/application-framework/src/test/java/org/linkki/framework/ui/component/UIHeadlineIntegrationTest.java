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
import static org.assertj.core.api.Assertions.assertThatException;

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

        pmo.setTitle(null);
        bindingContext.modelChanged();

        assertThat(KaribuUtils.getTextContent(headline))
                .doesNotContain("new")
                .doesNotContain("null");
    }

    @Test
    void testGetHeadlinePmo() {
        // given
        var pmo = new HeadlinePmoTestPmo();

        // when
        var container = VaadinUiCreator.createComponent(pmo, new BindingContext());

        // then
        var headline = _get(container, Headline.class);
        var headlineButton = _get(headline, Button.class);
        var containerButton = _get(container, Button.class, s -> s.withId("updateHeadline"));
        assertThat(KaribuUtils.getTextContent(container)).contains("HeadlineButtonCounter: 0");
        assertThat(KaribuUtils.getTextContent(container)).contains("initial");

        // when
        containerButton.click();

        // then
        assertThat(KaribuUtils.getTextContent(container)).contains("updated");

        // when
        headlineButton.click();

        // then
        assertThat(KaribuUtils.getTextContent(container)).contains("HeadlineButtonCounter: 1");
    }

    @Test
    void testGetHeadlinePmo_Null() {
        @UICssLayout
        class TestPmo {
            @UIHeadline
            public HeadlinePmo getTitle() {
                return null;
            }
        }

        assertThatException()
                .isThrownBy(() -> VaadinUiCreator.createComponent(new TestPmo(), new BindingContext()));
    }

    @Test
    void testGetHeadlinePmo_NeitherStringNorHeadlinePmo() {
        class NotAHeadlinePmo {
            // Doesn't extend HeadlinePmo
        }
        @UICssLayout
        class TestPmo {
            @UIHeadline
            public NotAHeadlinePmo getHeadline() {
                return new NotAHeadlinePmo();
            }
        }

        assertThatException()
                .isThrownBy(() -> VaadinUiCreator.createComponent(new TestPmo(), new BindingContext()));
    }

    @Test
    void testGetHeadlinePmo_SubclassOfHeadlinePmo() {
        class CustomHeadlinePmo extends HeadlinePmo {
            public CustomHeadlinePmo(String headerTitle) {
                super(headerTitle);
            }
        }
        var pmo = new CustomHeadlinePmo("HeaderTitle");

        var container = (Headline)VaadinUiCreator.createComponent(pmo, new BindingContext());

        assertThat(KaribuUtils.getTextContent(container)).contains("HeaderTitle");
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

    @UICssLayout
    public static class HeadlinePmoTestPmo {
        private final HeadlinePmo headlinePmo;
        private final AtomicInteger counter = new AtomicInteger(0);

        public HeadlinePmoTestPmo() {
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
    }

}
