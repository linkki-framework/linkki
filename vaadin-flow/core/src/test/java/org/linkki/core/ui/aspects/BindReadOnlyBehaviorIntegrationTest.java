package org.linkki.core.ui.aspects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.linkki.core.binding.dispatcher.behavior.PropertyBehavior.readOnly;
import static org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider.with;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext.BindingContextBuilder;
import org.linkki.core.ui.aspects.annotation.BindReadOnlyBehavior;
import org.linkki.core.ui.aspects.types.ReadOnlyBehaviorType;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UIMenuList;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UICssLayout;
import org.linkki.core.vaadin.component.menu.MenuItemDefinition;

import com.vaadin.flow.component.html.Div;

class BindReadOnlyBehaviorIntegrationTest {

    @Test
    void testInvisible() {
        class UiState {
            boolean readOnly = false;
        }
        var pmo = new TestPmo();
        var uiState = new UiState();
        var bindingContext = new BindingContextBuilder()
                .propertyBehaviorProvider(with(readOnly(() -> uiState.readOnly)))
                .build();
        var layout = (Div)VaadinUiCreator.createComponent(pmo, bindingContext);
        var button = layout.getComponentAt(0);
        var hasValue = layout.getComponentAt(1);
        var noSetterTextField = layout.getComponentAt(2);
        var buttonWithGetter = layout.getComponentAt(3);
        var label = layout.getComponentAt(4);

        assertThat(button.isVisible()).isTrue();
        assertThat(hasValue.isVisible()).isTrue();
        assertThat(noSetterTextField.isVisible()).isFalse();
        assertThat(buttonWithGetter.isVisible()).isTrue();
        assertThat(label.isVisible()).isTrue();

        uiState.readOnly = true;
        bindingContext.modelChanged();

        assertThat(button.isVisible()).isFalse();
        assertThat(hasValue.isVisible()).isFalse();
        assertThat(noSetterTextField.isVisible()).isFalse();
        assertThat(buttonWithGetter.isVisible()).isFalse();
        assertThat(label.isVisible()).isFalse();
    }

    @UICssLayout
    public static class TestPmo {
        private String text;

        @BindReadOnlyBehavior(ReadOnlyBehaviorType.INVISIBLE)
        @UIButton(position = 0)
        public void button() {

        }

        @BindReadOnlyBehavior(ReadOnlyBehaviorType.INVISIBLE)
        @UITextField(position = 1)
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @BindReadOnlyBehavior(ReadOnlyBehaviorType.INVISIBLE)
        @UITextField(position = 2)
        public String getNoSetterText() {
            return "text";
        }

        @BindReadOnlyBehavior(ReadOnlyBehaviorType.INVISIBLE)
        @UIMenuList(position = 3)
        public List<MenuItemDefinition> getMenuItems() {
            return List.of(MenuItemDefinition.builder("id").build());
        }

        @BindReadOnlyBehavior(ReadOnlyBehaviorType.INVISIBLE)
        @UILabel(position = 4)
        public String getNoValueGetter() {
            return "text";
        }
    }
}
