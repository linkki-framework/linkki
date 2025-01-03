package org.linkki.core.ui.aspects;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.aspects.annotation.BindTooltip;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UISection;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

class BindHelperTextIntegrationTest {

    public static final String TOOLTIP_LABEL = "Tooltip";
    public static final int FOCUS_DELAY = 10;
    public static final int HIDE_DELAY = 100;
    public static final int HOVER_DELAY = 1000;
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
    void testTooltip() {
        final var ui = UI.getCurrent().getChildren().findFirst().orElseThrow();
        assertThat(ui).isInstanceOf(MainView.class);

        final var component = _get(TextField.class, spec -> spec.withValue(TOOLTIP_LABEL));

        assertThat(component.getTooltip()).isNotNull().satisfies(tooltip -> {
            assertThat(tooltip.getText()).isEqualTo(TOOLTIP_LABEL);
            assertThat(tooltip.getPosition()).isEqualTo(Tooltip.TooltipPosition.BOTTOM);
            assertThat(tooltip.getFocusDelay()).isEqualTo(FOCUS_DELAY);
            assertThat(tooltip.getHideDelay()).isEqualTo(HIDE_DELAY);
            assertThat(tooltip.getHoverDelay()).isEqualTo(HOVER_DELAY);
        });
    }

    @UISection
    public static class TestPmo {

        @UITextField(position = 10, label = TOOLTIP_LABEL)
        @BindTooltip(value = TOOLTIP_LABEL, position = Tooltip.TooltipPosition.BOTTOM, focusDelay = FOCUS_DELAY, hideDelay = HIDE_DELAY, hoverDelay = HOVER_DELAY)
        public String getTooltip() {
            return TOOLTIP_LABEL;
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