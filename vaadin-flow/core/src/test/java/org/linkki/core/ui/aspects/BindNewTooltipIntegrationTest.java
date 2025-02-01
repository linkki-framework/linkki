package org.linkki.core.ui.aspects;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.aspects.annotation.BindHelperText;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.base.LinkkiText;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

class BindNewTooltipIntegrationTest {

    public static final String HELPER_TEXT_LABEL_HTML = "HelperTextHtml";
    public static final String HELPER_TEXT_LABEL = "HelperText";
    public static final String HELPER_TEXT_CONTENT = "<b>HelperText</b>";
    private static Routes routes;

    @BeforeAll
    static void beforeAll() {
        routes = new Routes();
        routes.getRoutes().add(MainView.class);
    }

    @BeforeEach
    void setUp() {
        MockVaadin.setup(routes);
    }

    @AfterEach
    void tearDown() {
        MockVaadin.tearDown();
    }

    @Test
    void testHelperText_Html() {
        final var ui = UI.getCurrent().getChildren().findFirst().orElseThrow();
        assertThat(ui).isInstanceOf(MainView.class);

        final var component = _get(TextField.class, spec -> spec.withValue(HELPER_TEXT_LABEL_HTML));

        assertThat(component.getThemeNames()).containsAnyElementsOf(List.of("helper-above-field"));

        assertThat(component.getHelperComponent()).isNotNull().isInstanceOf(LinkkiText.class).satisfies(helper -> {
            final var text = (LinkkiText)helper;

            assertThat(text.getText()).isEqualTo(HELPER_TEXT_CONTENT);
            assertThat(text.getIcon()).isEqualTo(VaadinIcon.INFO_CIRCLE_O);
        });
    }

    @Test
    void testHelperText_PlainText() {
        final var ui = UI.getCurrent().getChildren().findFirst().orElseThrow();
        assertThat(ui).isInstanceOf(MainView.class);

        final var component = _get(TextField.class, spec -> spec.withValue(HELPER_TEXT_LABEL));

        assertThat(component.getThemeNames()).doesNotContain("helper-above-field");

        assertThat(component.getHelperComponent()).isNull();
        assertThat(component.getHelperText()).isEqualTo(HELPER_TEXT_CONTENT);
    }

    @UISection
    public static class TestPmo {

        @UITextField(position = 20, label = HELPER_TEXT_LABEL_HTML)
        @BindHelperText(value = HELPER_TEXT_CONTENT, showIcon = true, placeAboveElement = true, htmlContent = true)
        public String getHelperTextHtml() {
            return HELPER_TEXT_LABEL_HTML;
        }

        @UITextField(position = 30, label = HELPER_TEXT_LABEL)
        @BindHelperText(value = HELPER_TEXT_CONTENT)
        public String getHelperText() {
            return HELPER_TEXT_LABEL;
        }
    }

    @Route(value = "")
    public static class MainView extends VerticalLayout {
        public MainView() {
            this.setSizeFull();
            final var pmo = new TestPmo();
            final var component = VaadinUiCreator.createComponent(pmo, new BindingContext());
            add(component);
        }
    }

}