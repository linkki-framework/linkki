package org.linkki.framework.ui.component;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UICssLayout;
import org.linkki.core.ui.test.KaribuUtils;

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
}
