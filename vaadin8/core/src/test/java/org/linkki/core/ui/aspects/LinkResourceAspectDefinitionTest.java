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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.function.Consumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.linkki.core.ui.wrapper.LabelComponentWrapper;
import org.linkki.core.vaadin.component.ComponentFactory;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;

public class LinkResourceAspectDefinitionTest {

    @AfterEach
    public void cleanUpUi() {
        UI.setCurrent(null);
        VaadinSession.setCurrent(null);
    }

    @Test
    public void testCreateComponentValueSetter_ValidHref_ResourceShouldBeAvailable() {
        Link link = ComponentFactory.newLink("Standard Link");
        Consumer<String> componentValueSetter = new LinkResourceAspectDefinition()
                .createComponentValueSetter(new LabelComponentWrapper(link));

        componentValueSetter.accept("http://www.testlink.com");

        assertThat(((ExternalResource)link.getResource()).getURL(), is("http://www.testlink.com"));
    }

    @Test
    public void testCreateComponentValueSetter_NullHref_NoResourceShouldBeAvailable() {
        Link link = ComponentFactory.newLink("NullHref");
        Consumer<String> componentValueSetter = new LinkResourceAspectDefinition()
                .createComponentValueSetter(new LabelComponentWrapper(link));

        componentValueSetter.accept(null);

        assertThat(link.getResource(), is(nullValue()));
    }

    @Test
    public void testCreateComponentValueSetter_EmptyHref_NoResourceShouldBeAvailable() {
        Link link = ComponentFactory.newLink("EmptyHref");
        Consumer<String> componentValueSetter = new LinkResourceAspectDefinition()
                .createComponentValueSetter(new LabelComponentWrapper(link));

        componentValueSetter.accept("");

        assertThat(link.getResource(), is(nullValue()));
    }

    @Test
    public void testCreateComponentValueSetter_SpaceHref_NoResourceShouldBeAvailable() {
        Link link = ComponentFactory.newLink("SpaceHref");
        Consumer<String> componentValueSetter = new LinkResourceAspectDefinition()
                .createComponentValueSetter(new LabelComponentWrapper(link));

        componentValueSetter.accept(" ");

        assertThat(link.getResource(), is(nullValue()));
    }

}
