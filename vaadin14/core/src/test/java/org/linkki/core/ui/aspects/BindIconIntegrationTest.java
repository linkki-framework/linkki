/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.linkki.core.ui.aspects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.IconType;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UILink;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.wrapper.FormItemComponentWrapper;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.core.vaadin.component.base.LinkkiAnchor;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class BindIconIntegrationTest {

    private static final String ABACUS_ICON_NAME = "vaadin:abacus";
    private static final String AIRPLANE_ICON_NAME = "vaadin:airplane";
    private static final String ACADEMY_CAP_NAME = "vaadin:academy-cap";

    private final BindingContext bindingContext = new BindingContext();


    @Test
    public void testAspectBindIconAnnotation_Static_withButton() {
        List<Component> uiElements = createUiElements(new TestPmoWithStaticIcon());

        Component button = uiElements.get(1);
        assertThat(button, is(instanceOf(Button.class)));
        Component icon = getIcon(button).get();
        assertThat(icon, is(instanceOf(Icon.class)));
        assertThat(getIconAttribute(icon), is(ABACUS_ICON_NAME));
    }

    @Test
    public void testAspectBindIconAnnotation_Static_withLink() {
        List<Component> uiElements = createUiElements(new TestPmoWithStaticIcon());

        Component uiLink = uiElements.get(2);
        assertThat(uiLink, is(instanceOf(LinkkiAnchor.class)));
        Component icon = getIcon(uiLink).get();
        assertThat(icon, is(instanceOf(Icon.class)));
        assertThat(getIconAttribute(icon), is(ACADEMY_CAP_NAME));
    }

    @Test
    public void testAspectBindIconAnnotation_Auto_withButton() {
        TestPmoWithAutoIcon pmo = new TestPmoWithAutoIcon(VaadinIcon.ABACUS);
        Component button = createUiElements(pmo).get(1);

        assertThat(button, is(instanceOf(Button.class)));
        Component icon = getIcon(button).get();
        assertThat(icon, is(instanceOf(Icon.class)));
        assertThat(getIconAttribute(icon), is(ABACUS_ICON_NAME));

        pmo.setIcon(null);
        bindingContext.modelChanged();

        assertThat(getIcon(button).isPresent(), is(false));
    }

    @Test
    public void testAspectBindIconAnnotation_Auto_withLink() {
        TestPmoWithAutoIcon pmo = new TestPmoWithAutoIcon(VaadinIcon.ABACUS);
        Component link = createUiElements(pmo).get(2);

        assertThat(link, is(instanceOf(LinkkiAnchor.class)));
        Component icon = link.getChildren().findFirst().get();
        assertThat(icon, is(instanceOf(Icon.class)));
        assertThat(getIconAttribute(icon), is(ABACUS_ICON_NAME));

        pmo.setIcon(null);
        bindingContext.modelChanged();

        assertThat(getIcon(link).isPresent(), is(false));
    }

    @Test
    public void testAspectBindIconAnnotation_Dynamic_withButton() {
        TestPmoWithDynamicIcon pmo = new TestPmoWithDynamicIcon(VaadinIcon.ABACUS);

        Component button = createUiElements(pmo).get(1);
        assertThat(button, is(instanceOf(Button.class)));
        Component icon = getIcon(button).get();
        assertThat(icon, is(instanceOf(Icon.class)));
        assertThat(getIconAttribute(icon), is(ABACUS_ICON_NAME));

        pmo.setIcon(VaadinIcon.AIRPLANE);
        bindingContext.modelChanged();

        // Icon on button changed
        icon = getIcon(button).get();
        assertThat(getIconAttribute(icon), is(AIRPLANE_ICON_NAME));
    }

    @Test
    public void testAspectBindIconAnnotation_Dynamic_withLink() {
        TestPmoWithDynamicIcon pmo = new TestPmoWithDynamicIcon(VaadinIcon.ABACUS);
        Component link = createUiElements(pmo).get(2);

        assertThat(link, is(instanceOf(LinkkiAnchor.class)));
        Component icon = getIcon(link).get();
        assertThat(icon, is(instanceOf(Icon.class)));
        assertThat(getIconAttribute(icon), is(ABACUS_ICON_NAME));

        pmo.setIcon(VaadinIcon.AIRPLANE);
        bindingContext.modelChanged();

        // Icon on link changed
        icon = getIcon(link).get();
        assertThat(getIconAttribute(icon), is(AIRPLANE_ICON_NAME));
    }

    @Test
    public void testAspectBindIconAnnotation_Dynamic_withMethodMissing() {
        TestPmoMissingDynamicMethod pmo = new TestPmoMissingDynamicMethod();

        Assertions.assertThrows(LinkkiBindingException.class, () -> {
            UiCreator.createUiElements(pmo, bindingContext, c -> new FormItemComponentWrapper((Component)c))
                    .collect(Collectors.toList());
        });

    }

    private List<Component> createUiElements(Object pmo) {
        return UiCreator.createUiElements(pmo, bindingContext, c -> new FormItemComponentWrapper((Component)c))
                .map(FormItemComponentWrapper::getComponent)
                .collect(Collectors.toList());
    }

    private Optional<Component> getIcon(Component parent) {
        return parent.getChildren().findFirst();
    }

    private String getIconAttribute(Component icon) {
        if (icon == null) {
            return null;
        }
        return icon.getElement().getAttribute("icon");
    }

    public static class TestPmoWithStaticIcon {

        @BindIcon(VaadinIcon.ALARM)
        @UITextField(label = "static icon", position = 0)
        public String getPropertyWithStaticIcon() {
            return "";
        }

        @BindIcon(VaadinIcon.ABACUS)
        @UIButton(label = "Button static icon", position = 1)
        public String getButtonPropertyWithStaticIcon() {
            return "";
        }

        @BindIcon(VaadinIcon.ACADEMY_CAP)
        @UILink(label = "", caption = "", position = 2, captionType = CaptionType.STATIC)
        public String getLinkPropertyWithStaticIconAndLabel() {
            return "";
        }

    }

    public static class TestPmoWithAutoIcon {

        private VaadinIcon icon;

        public TestPmoWithAutoIcon(VaadinIcon icon) {
            this.icon = icon;
        }

        public void setIcon(VaadinIcon icon) {
            this.icon = icon;
        }

        @BindIcon
        @UITextField(label = "dynamic icon", position = 0)
        public String getProperty() {
            return "";
        }

        public VaadinIcon getPropertyIcon() {
            return icon;
        }

        @BindIcon
        @UIButton(label = "Button dynamic icon", position = 1)
        public String getButtonProperty() {
            return "";
        }

        public VaadinIcon getButtonPropertyIcon() {
            return getPropertyIcon();
        }

        @BindIcon
        @UILink(label = "", caption = "Link static caption", position = 2, captionType = CaptionType.STATIC)
        public String getLinkProperty() {
            return "";
        }

        public VaadinIcon getLinkPropertyIcon() {
            return getPropertyIcon();
        }
    }

    public static class TestPmoWithDynamicIcon {

        private VaadinIcon icon;

        public TestPmoWithDynamicIcon(VaadinIcon icon) {
            this.icon = icon;
        }

        public void setIcon(VaadinIcon icon) {
            this.icon = icon;
        }

        @BindIcon(iconType = IconType.DYNAMIC)
        @UITextField(label = "dynamic icon", position = 0)
        public String getProperty() {
            return "";
        }

        public VaadinIcon getPropertyIcon() {
            return icon;
        }

        @BindIcon(iconType = IconType.DYNAMIC)
        @UIButton(label = "Button dynamic icon", position = 1)
        public String getButtonProperty() {
            return "";
        }

        public VaadinIcon getButtonPropertyIcon() {
            return getPropertyIcon();
        }

        @BindIcon(iconType = IconType.DYNAMIC)
        @UILink(label = "", caption = "", position = 2, captionType = CaptionType.STATIC)
        public String getLinkProperty() {
            return "";
        }

        public VaadinIcon getLinkPropertyIcon() {
            return getPropertyIcon();
        }


        @BindIcon(iconType = IconType.DYNAMIC)
        @UILabel(label = "Some label with an icon", position = 4)
        public String getLabelPropertyWithIconAndLabel() {
            return "";
        }

        public VaadinIcon getLabelPropertyWithIconAndLabelIcon() {
            return getPropertyIcon();
        }
    }

    public static class TestPmoMissingDynamicMethod {

        @BindIcon(iconType = IconType.DYNAMIC)
        @UITextField(label = "missing dynamic method", position = 0)
        public String getProperty() {
            return "";
        }
    }
}
