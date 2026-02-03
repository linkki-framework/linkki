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

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.core.ui.test.KaribuUtils;
import org.linkki.core.vaadin.component.base.LinkkiText;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;

class UIHeadlineLayoutIntegrationTest {

    public static final String HEADLINE_TITLE = "Headline";

    @Test
    void testUIHeadline_withoutAdditionalPmos() {
        var bindingContext = new BindingContext();
        var pmo = new HeadlinePmo(HEADLINE_TITLE);

        var headline = VaadinUiCreator.createComponent(pmo, bindingContext);

        assertThat(headline).isInstanceOf(Headline.class);
        H2 h2 = _get(headline, H2.class);
        assertThat(h2).isNotNull();
        assertThat(h2.getText()).isEqualTo("Headline");
        assertThat(h2.getChildren().toList()).hasSize(1);
    }

    @Test
    void testUIHeadlineWithTitleAndRightComponentPmo() {
        @UIHorizontalLayout
        class TestPmo {
            private String title = "initial";

            @UILabel(position = 10)
            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        @UIHorizontalLayout
        class TestButtonPmo {
            @UIButton(position = 10, caption = "Button")
            public void button() {
                // do nothing
            }

        }

        var bindingContext = new BindingContext();
        var pmo = new HeadlinePmo("Headline", new TestPmo(), new TestButtonPmo());

        var headline = VaadinUiCreator.createComponent(pmo, bindingContext);

        assertThat(headline).isInstanceOf(Headline.class);

        H2 h2 = _get(headline, H2.class);
        assertThat(h2).isNotNull();
        assertThat(h2.getText()).isEqualTo("Headline");
        assertThat(h2.getChildren().toList()).hasSize(2);

        assertThat(KaribuUtils.getWithId(headline, LinkkiText.class, "title")).isNotNull()
                .extracting(LinkkiText::getText).isEqualTo("initial");

        assertThat(KaribuUtils.getWithId(headline, Button.class, "button")).isNotNull()
                .extracting(Button::getText).isEqualTo("Button");
    }

}
