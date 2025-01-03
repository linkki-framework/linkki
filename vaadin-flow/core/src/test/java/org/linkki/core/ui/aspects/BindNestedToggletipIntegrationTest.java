package org.linkki.core.ui.aspects;

import static com.github.mvysny.kaributesting.v10.LocatorJ._click;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.core.ui.aspects.annotation.BindToggletip.ToogletipPosition.PREFIX;
import static org.linkki.core.ui.aspects.annotation.BindToggletip.ToogletipPosition.SUFFIX;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.aspects.annotation.UINestedToggleTip;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.base.LinkkiText;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.router.Route;

class BindNestedToggletipIntegrationTest {

    public static final String LABEL_TEXT = "Toggletip";
    public static final String TOGGLETIP_LABEL = "Toggletip";
    private static final String LABEL_TEXT_PREFIX = "ToggletipPrefix";
    private static Routes routes;

    @BeforeAll
    static void beforeAll() {
        routes = new Routes();
        routes.getRoutes()
                .add(MainView.class);
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
    void testToggletip() {
        final var ui = UI.getCurrent()
                .getChildren()
                .findFirst()
                .orElseThrow();
        assertThat(ui).isInstanceOf(MainView.class);

        final var component = _get(LinkkiText.class, spec -> spec.withText(LABEL_TEXT));
        final var layoutComponent = component.getParent();

        assertThat(layoutComponent).hasValueSatisfying(l -> {
            assertThat(l).isInstanceOf(HorizontalLayout.class);
            final var children = l.getChildren()
                    .toList();
            assertThat(children).hasSize(2)
                    .hasExactlyElementsOfTypes(LinkkiText.class, Button.class);
        });
        assertThat(component.getPrefixComponent()).isNull();
        assertThat(component.getSuffixComponent()).isNull();

        final var toggletip = (Button)layoutComponent.get()
                .getChildren()
                .toList()
                .get(1);
        assertThat(component.getTooltip()
                .isOpened()).isFalse();

        _click(toggletip);

        assertThat(component.getTooltip()).isNotNull()
                .satisfies(tooltip -> {
                    assertThat(tooltip.isOpened()).isTrue();
                    assertThat(tooltip.getText()).isEqualTo(TOGGLETIP_LABEL);
                    assertThat(tooltip.getPosition()).isEqualTo(Tooltip.TooltipPosition.TOP);
                });

        _click(toggletip);

        assertThat(component.getTooltip()
                .isOpened()).isFalse();
    }

    @Test
    void testToggletipPrefix() {
        final var ui = UI.getCurrent()
                .getChildren()
                .findFirst()
                .orElseThrow();
        assertThat(ui).isInstanceOf(MainView.class);

        final var component = _get(LinkkiText.class, spec -> spec.withText(LABEL_TEXT_PREFIX));
        final var layout = component.getParent();

        assertThat(layout).hasValueSatisfying(l -> {
            assertThat(l).isInstanceOf(HorizontalLayout.class);
            final var children = l.getChildren()
                    .toList();
            assertThat(children).hasSize(2)
                    .hasExactlyElementsOfTypes(Button.class, LinkkiText.class);
        });
        assertThat(component.getPrefixComponent()).isNull();
        assertThat(component.getSuffixComponent()).isNull();
    }

    @UISection
    public static class TestPmo {

        @UILabel(position = 0)
        @UINestedToggleTip(position = 0, value = TOGGLETIP_LABEL, toggletipPosition = SUFFIX)
        public String getLabel() {
            return LABEL_TEXT;
        }

        public Class<?> getLabelComponentType() {
            return UINestedToggleTip.class;
        }

        @UILabel(position = 1)
        @UINestedToggleTip(position = 1, value = TOGGLETIP_LABEL, toggletipPosition = PREFIX)
        public String getLabelPrefix() {
            return LABEL_TEXT_PREFIX;
        }

        public Class<?> getLabelPrefixComponentType() {
            return UINestedToggleTip.class;
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