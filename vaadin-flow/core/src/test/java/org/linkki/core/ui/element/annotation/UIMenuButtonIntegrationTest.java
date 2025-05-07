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

package org.linkki.core.ui.element.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.core.vaadin.component.menu.SingleItemMenuBar;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

class UIMenuButtonIntegrationTest {

    private BindingContext bindingContext;
    private VerticalLayout layout;
    private MenuButtonTestPmo testPmo;

    @BeforeEach
    void setup() {
        bindingContext = new BindingContext();
        testPmo = new MenuButtonTestPmo();
        layout = (VerticalLayout)VaadinUiCreator.createComponent(testPmo, bindingContext);
    }

    @Test
    void testMenuButton_CreatesSingleItemMenuBar() {
        assertThat(layout.getComponentAt(0)).isInstanceOf(SingleItemMenuBar.class);
        assertThat(layout.getComponentAt(1)).isInstanceOf(SingleItemMenuBar.class);
    }

    @Test
    void testMenuButton_CaptionAppliedToMenuItem() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(0);
        MenuItem theItem = menuButton.getItems().get(0);

        assertThat(menuButton.getCaption()).isEqualTo(MenuButtonTestPmo.BUTTON_CAPTION);
        assertThat(theItem.getText()).isEqualTo(MenuButtonTestPmo.BUTTON_CAPTION);
    }

    @Test
    void testMenuButton_IconAppliedToMenuItem() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(0);
        MenuItem theItem = menuButton.getItems().get(0);

        assertThat(menuButton.getIcon()).isEqualTo(VaadinIcon.BUTTON);
        Icon icon = (Icon)theItem.getChildren().toList().get(0);
        assertThat(icon.getElement().getAttribute("icon")).isEqualTo("vaadin:button");
    }

    @Test
    void testMenuButton_CaptionOverwrittenWithBindCaption() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(1);

        assertThat(menuButton.getCaption()).isEqualTo(MenuButtonTestPmo.BOUND_CAPTION);
    }

    @Test
    void testMenuButton_HideIcon() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(2);
        MenuItem theItem = menuButton.getItems().get(0);

        // no icon
        List<Component> children = theItem.getChildren().toList();
        assertThat(children).hasOnlyElementsOfType(Text.class);
    }

    @Test
    void testMenuButton_IconOverwrittenWithBindIcon() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(1);

        assertThat(menuButton.getIcon()).isEqualTo(VaadinIcon.ABACUS);
    }

    @Test
    void testMenuButton_DynamicCaption() {
        SingleItemMenuBar dynamicButton = (SingleItemMenuBar)layout.getComponentAt(0);
        assertThat(dynamicButton.getCaption()).isEqualTo(MenuButtonTestPmo.BUTTON_CAPTION);
        testPmo.setDynamicButtonCaption("A Brave New Caption");
        bindingContext.modelChanged();

        assertThat(dynamicButton.getCaption()).isEqualTo("A Brave New Caption");
    }

    @Test
    void testMenuButton_DynamicEnabled() {
        SingleItemMenuBar dynamicButton = (SingleItemMenuBar)layout.getComponentAt(0);
        assertThat(dynamicButton.isEnabled()).isTrue();
        testPmo.setDynamicEnabled(false);
        bindingContext.modelChanged();

        assertThat(dynamicButton.isEnabled()).isFalse();
    }

    @Test
    void testMenuButton_Invoke() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(0);
        MenuItem theItem = menuButton.getItems().get(0);
        assertThat(testPmo.getDummyValue()).isEmpty();

        ComponentUtil.fireEvent(theItem, new ClickEvent<>(theItem));

        assertThat(testPmo.getDummyValue()).isEqualTo(MenuButtonTestPmo.BUTTON_CLICKED_VALUE);
    }

    @Test
    void testMenuButton_WithVariant() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(0);

        assertThat(menuButton.getContent().getThemeNames()).contains(MenuBarVariant.LUMO_PRIMARY.getVariantName());
    }

    @Test
    void testMenuButton_NoVariant() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(1);

        assertThat(menuButton.getContent().getThemeNames()).isEmpty();
    }

    @Test
    void testMenuButton_WithDefaultCaption() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(3);

        assertThat(menuButton.getCaption()).isEqualTo("ButtonWithDefaultProperties");
    }

    @UIVerticalLayout
    public static class MenuButtonTestPmo {
        private static final String BUTTON_CLICKED_VALUE = "buttonclick";
        private static final String BOUND_CAPTION = "BoundCaption";
        private static final String BUTTON_CAPTION = "MenuButtonCaption";
        private boolean dynamicEnabled = true;
        private String dynamicButtonCaption = BUTTON_CAPTION;
        private String dummyValue = "";

        @UIMenuButton(position = 2, captionType = CaptionType.DYNAMIC, caption = BUTTON_CAPTION, //
                icon = VaadinIcon.BUTTON, enabled = EnabledType.DYNAMIC, variants = MenuBarVariant.LUMO_PRIMARY)
        public void button() {
            dummyValue = BUTTON_CLICKED_VALUE;
        }

        public boolean isButtonEnabled() {
            return dynamicEnabled;
        }

        public String getButtonCaption() {
            return dynamicButtonCaption;
        }

        @UIMenuButton(position = 3, caption = BUTTON_CAPTION, icon = VaadinIcon.BUTTON)
        @BindIcon(VaadinIcon.ABACUS)
        @BindCaption(captionType = CaptionType.STATIC, value = BOUND_CAPTION)
        public void buttonWithBindAnnotations() {
            //
        }

        @UIMenuButton(caption = BUTTON_CAPTION, icon = VaadinIcon.BUTTON, position = 4, showIcon = false)
        public void buttonWithoutIcon() {
            //
        }

        @UIMenuButton(position = 5, icon = VaadinIcon.BUTTON)
        public void buttonWithDefaultProperties() {
            //
        }

        public void setDynamicEnabled(boolean dynamicEnabled) {
            this.dynamicEnabled = dynamicEnabled;
        }

        public void setDynamicButtonCaption(String dynamicButtonCaption) {
            this.dynamicButtonCaption = dynamicButtonCaption;
        }

        public String getDummyValue() {
            return dummyValue;
        }
    }
}