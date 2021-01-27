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

package org.linkki.core.vaadin.component.tablayout;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class LinkkiTabSheetTest {

    @Test
    public void testGetTab_WithCaption() {
        LinkkiTabSheet tabSheet = new LinkkiTabSheet("id", "caption", "description", new Span("content"));

        assertThat(tabSheet.getTab().getLabel(), is("caption"));
        assertThat(tabSheet.getTab().getId().get(), is("id"));
    }

    @Test
    public void testGetTab_WithCaptionComponent() {
        Icon captionComponent = VaadinIcon.PLUS.create();

        LinkkiTabSheet tabSheet = new LinkkiTabSheet("id", captionComponent, "description", new Span("content"));

        assertThat(tabSheet.getTab().getChildren().findFirst().get(), is(captionComponent));
        assertThat(tabSheet.getTab().getId().get(), is("id"));
    }

    @Test
    public void testGetComponent_LazyInstantiation() {
        Supplier<Component> contentSupplier = spy(new Supplier<Component>() {

            @Override
            public Component get() {
                return new Span("content");
            }
        });

        LinkkiTabSheet tabSheet = new LinkkiTabSheet("id", VaadinIcon.PLUS.create(), "description", contentSupplier);
        verifyNoInteractions(contentSupplier);

        Component content = tabSheet.getContent();
        verify(contentSupplier).get();

        // Further calls should return the same instance
        assertThat(tabSheet.getContent(), is(content));
        verifyNoMoreInteractions(contentSupplier);
    }
}
