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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.linkki.core.vaadin.component.menu.SingleItemMenuBar;

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
        assertThat(layout.getComponentAt(0), instanceOf(SingleItemMenuBar.class));
        assertThat(layout.getComponentAt(1), instanceOf(SingleItemMenuBar.class));
    }

    @Test
    void testMenuButton_CaptionAppliedToMenuItem() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(0);
        MenuItem theItem = menuButton.getItems().get(0);

        assertThat(menuButton.getCaption(), is(MenuButtonTestPmo.BUTTON_CAPTION));
        assertThat(theItem.getText(), is(MenuButtonTestPmo.BUTTON_CAPTION));
    }

    @Test
    void testMenuButton_IconAppliedToMenuItem() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(0);
        MenuItem theItem = menuButton.getItems().get(0);

        assertThat(menuButton.getIcon(), is(VaadinIcon.BUTTON));
        Icon icon = (Icon)theItem.getChildren().toList().get(0);
        assertThat(icon.getElement().getAttribute("icon"), is("vaadin:button"));
    }

    @Test
    void testMenuButton_CaptionOverwrittenWithBindCaption() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(1);

        assertThat(menuButton.getCaption(), is(MenuButtonTestPmo.BOUND_CAPTION));
    }

    @Test
    void testMenuButton_HideIcon() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(2);
        MenuItem theItem = menuButton.getItems().get(0);

        // no icon
        List<Component> children = theItem.getChildren().collect(Collectors.toList());
        assertThat(children, contains(instanceOf(Text.class)));
    }

    @Test
    void testMenuButton_IconOverwrittenWithBindIcon() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(1);

        assertThat(menuButton.getIcon(), is(VaadinIcon.ABACUS));
    }

    @Test
    void testMenuButton_DynamicCaption() {
        SingleItemMenuBar dynamicButton = (SingleItemMenuBar)layout.getComponentAt(0);
        assertThat(dynamicButton.getCaption(), is(MenuButtonTestPmo.BUTTON_CAPTION));
        testPmo.setDynamicButtonCaption("A Brave New Caption");
        bindingContext.modelChanged();

        assertThat(dynamicButton.getCaption(), is("A Brave New Caption"));
    }

    @Test
    void testMenuButton_DynamicEnabled() {
        SingleItemMenuBar dynamicButton = (SingleItemMenuBar)layout.getComponentAt(0);
        assertThat(dynamicButton.isEnabled(), is(true));
        testPmo.setDynamicEnabled(false);
        bindingContext.modelChanged();

        assertThat(dynamicButton.isEnabled(), is(false));
    }

    @Test
    void testMenuButton_Invoke() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(0);
        MenuItem theItem = menuButton.getItems().get(0);
        assertThat(testPmo.getDummyValue(), is(StringUtils.EMPTY));

        ComponentUtil.fireEvent(theItem, new ClickEvent<>(theItem));

        assertThat(testPmo.getDummyValue(), is(MenuButtonTestPmo.BUTTON_CLICKED_VALUE));
    }

    @Test
    void testMenuButton_WithVariant() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(0);

        assertThat(menuButton.getContent().getThemeNames(), contains(MenuBarVariant.LUMO_PRIMARY.getVariantName()));
    }

    @Test
    void testMenuButton_NoVariant() {
        SingleItemMenuBar menuButton = (SingleItemMenuBar)layout.getComponentAt(1);

        assertThat(menuButton.getContent().getThemeNames(), is(empty()));
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