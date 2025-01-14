package de.faktorzehn.commons.linkki.ui.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.icon.VaadinIcon;

@SuppressWarnings("deprecation")
class MenuItemDefinitionTest {

    @Test
    void testConstructor_VisibleAndEnabledByDefault() {
        var itemDefinition = new MenuItemDefinition("caption", null, Handler.NOP_HANDLER, "id");
        assertThat(itemDefinition.isVisible()).isTrue();
        assertThat(itemDefinition.isEnabled()).isTrue();
    }

    @Test
    void testBuilder() {
        var id = "1";
        var caption = "sample";
        var icon = VaadinIcon.BUG;
        Handler command = System.out::println;

        var itemDefinition = MenuItemDefinition.builder("1")
                .caption(id)
                .caption(caption)
                .icon(icon)
                .command(command)
                .enabledIf(false)
                .visibleIf(false)
                .build();

        assertThat(itemDefinition.getId()).isEqualTo(id);
        assertThat(itemDefinition.getCaption()).isEqualTo(caption);
        assertThat(itemDefinition.getCommand()).isEqualTo(command);
        assertThat(itemDefinition.getIcon()).isEqualTo(icon);
        assertThat(itemDefinition.isEnabled()).isFalse();
        assertThat(itemDefinition.isVisible()).isFalse();
    }

    @Test
    void testBuilder_ValuesAreNull() {
        assertThrows(NullPointerException.class, () -> MenuItemDefinition.builder(null).build());
        assertThrows(NullPointerException.class, () -> MenuItemDefinition.builder("1").caption(null).build());
        assertThrows(NullPointerException.class,
                     () -> MenuItemDefinition.builder("1").caption("caption").command(null).build());
    }

    @Test
    void testBuilder_EnabledAndVisibleByDefault() {
        var itemDefinition = MenuItemDefinition.builder("id").build();

        assertThat(itemDefinition.isVisible()).isTrue();
        assertThat(itemDefinition.isEnabled()).isTrue();
    }
}