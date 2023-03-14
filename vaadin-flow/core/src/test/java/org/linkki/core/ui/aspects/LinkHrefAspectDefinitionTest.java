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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.wrapper.LabelComponentWrapper;
import org.linkki.core.vaadin.component.ComponentFactory;
import org.linkki.core.vaadin.component.base.LinkkiAnchor;

class LinkHrefAspectDefinitionTest {

    @Test
    void testCreateComponentValueSetter_ValidHref_HrefAttributeShouldBeAvailable() {
        LinkHrefAspectDefinition linkHrefAspectDefinition = new LinkHrefAspectDefinition();
        LinkkiAnchor link = ComponentFactory.newLink("Standard Link");
        ComponentWrapper componentWrapper = new LabelComponentWrapper(link, WrapperType.FIELD);

        Consumer<String> componentValueSetter = linkHrefAspectDefinition.createComponentValueSetter(componentWrapper);

        componentValueSetter.accept("http://www.testlink.com");
        assertThat(link.getHref()).isEqualTo("http://www.testlink.com");
    }

    @Test
    void testCreateComponentValueSetter_NullHref_NoHrefAttributeShouldBeAvailable() {
        LinkHrefAspectDefinition linkHrefAspectDefinition = new LinkHrefAspectDefinition();
        LinkkiAnchor link = ComponentFactory.newLink("NullHref");
        ComponentWrapper componentWrapper = new LabelComponentWrapper(link, WrapperType.FIELD);

        Consumer<String> componentValueSetter = linkHrefAspectDefinition.createComponentValueSetter(componentWrapper);

        componentValueSetter.accept(null);
        assertThat(link.getElement().getAttribute("href")).isNull();
    }

    @Test
    void testCreateComponentValueSetter_EmptyHref_NoHrefAttributeShouldBeAvailable() {
        LinkHrefAspectDefinition linkHrefAspectDefinition = new LinkHrefAspectDefinition();
        LinkkiAnchor link = ComponentFactory.newLink("EmptyHref");
        ComponentWrapper componentWrapper = new LabelComponentWrapper(link, WrapperType.FIELD);

        Consumer<String> componentValueSetter = linkHrefAspectDefinition.createComponentValueSetter(componentWrapper);

        componentValueSetter.accept("");
        assertThat(link.getElement().getAttribute("href")).isNull();
    }

    @Test
    void testCreateComponentValueSetter_SpaceHref_NoHrefAttributeShouldBeAvailable() {
        LinkHrefAspectDefinition linkHrefAspectDefinition = new LinkHrefAspectDefinition();
        LinkkiAnchor link = ComponentFactory.newLink("SpaceHref");
        ComponentWrapper componentWrapper = new LabelComponentWrapper(link, WrapperType.FIELD);

        Consumer<String> componentValueSetter = linkHrefAspectDefinition.createComponentValueSetter(componentWrapper);

        componentValueSetter.accept(" ");
        assertThat(link.getElement().getAttribute("href")).isNull();
    }
}
