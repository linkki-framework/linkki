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
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

class LinkkiTabSheetTest {

    @Test
    void testGetTab() {
        Icon captionComponent = VaadinIcon.PLUS.create();
        LinkkiTabSheet tabSheet = new LinkkiTabSheet("id", "caption", captionComponent, "description",
                () -> new Span("content"),
                Handler.NOP_HANDLER);

        assertThat(tabSheet.getTab().getLabel(), is("caption"));
        assertThat(tabSheet.getTab().getChildren().findFirst().get(), is(captionComponent));
        assertThat(tabSheet.getTab().getId().get(), is("id"));
        assertThat(tabSheet.getTab().getElement().getAttribute("title"), is("description"));
        // TODO LIN-2054 description as tooltip
    }

    @Test
    void testGetComponent_LazyInstantiation() {
        Supplier<Component> contentSupplier = spy(new Supplier<Component>() {

            @Override
            public Component get() {
                return new Span("content");
            }
        });

        LinkkiTabSheet tabSheet = new LinkkiTabSheet("id", "", VaadinIcon.PLUS.create(), "description",
                contentSupplier,
                Handler.NOP_HANDLER);
        verifyNoInteractions(contentSupplier);

        Component content = tabSheet.getContent();
        verify(contentSupplier).get();

        // Further calls should return the same instance
        assertThat(tabSheet.getContent(), is(content));
        verifyNoMoreInteractions(contentSupplier);
    }


    @Test
    void testBuilder_CaptionDefaultsToId() {
        LinkkiTabSheet tabSheet = LinkkiTabSheet.builder("id").content(new Span("content")).build();
        assertThat(tabSheet.getTab().getLabel(), is("id"));
    }

    @Test
    void testBuilder_DescriptionDefaultsToBlank() {
        LinkkiTabSheet tabSheet = LinkkiTabSheet.builder("id").content(new Span("content")).build();
        assertThat(tabSheet.getTab().getElement().getAttribute("title"), is(""));
        // TODO LIN-2054
    }

    @Test
    void testBuilder_WithCaption() {
        Span content = new Span("content");
        LinkkiTabSheet tabSheet = LinkkiTabSheet.builder("id").caption("caption").content(content).build();
        assertThat(tabSheet.getTab().getLabel(), is("caption"));
    }

    @Test
    void testBuilder_CaptionOverwritesExistingCaptionComponent() {
        Span content = new Span("content");
        LinkkiTabSheet tabSheet = LinkkiTabSheet.builder("id").caption(VaadinIcon.PLUS.create())
                .caption("caption").content(content).build();
        assertThat(tabSheet.getTab().getChildren().count(), is(0l));
    }


    @Test
    void testBuilder_WithCaptionComponent() {
        Span content = new Span("content");
        Icon icon = VaadinIcon.PLUS.create();
        LinkkiTabSheet tabSheet = LinkkiTabSheet.builder("id").caption(icon).content(content).build();
        assertThat(tabSheet.getTab().getLabel(), is(""));
        assertThat(tabSheet.getTab().getChildren().findFirst().get(), is(icon));
    }

    @Test
    void testBuilder_WithCaptionComponent_OverwritesExistingCaption() {
        Span content = new Span("content");
        Icon icon = VaadinIcon.PLUS.create();
        LinkkiTabSheet tabSheet = LinkkiTabSheet.builder("id").caption("caption").caption(icon)
                .content(content).build();
        assertThat(tabSheet.getTab().getLabel(), is(""));
    }
}
