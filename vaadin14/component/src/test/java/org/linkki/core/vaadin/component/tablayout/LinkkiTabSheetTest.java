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
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet.TabSheetSelectionChangeEvent;
import org.linkki.util.handler.Handler;
import org.mockito.ArgumentCaptor;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

class LinkkiTabSheetTest {

    @Test
    void testGetTab() {
        Icon captionComponent = VaadinIcon.PLUS.create();

        LinkkiTabSheet tabSheet = new LinkkiTabSheet("id", "caption", captionComponent, "description",
                () -> new Span("content"), () -> true, Collections.emptyList());

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
                contentSupplier, () -> true, Collections.emptyList());
        verifyNoInteractions(contentSupplier);

        Component content = tabSheet.getContent();
        verify(contentSupplier).get();

        // Further calls should return the same instance
        assertThat(tabSheet.getContent(), is(content));
        verifyNoMoreInteractions(contentSupplier);
    }

    @Test
    void testBuilder_CaptionDefaultsToId() {
        LinkkiTabSheet tabSheet = LinkkiTabSheet.builder("id").content(() -> new Span("content")).build();

        assertThat(tabSheet.getTab().getLabel(), is("id"));
    }

    @Test
    void testBuilder_DescriptionDefaultsToBlank() {
        LinkkiTabSheet tabSheet = LinkkiTabSheet.builder("id").content(() -> new Span("content")).build();


        assertThat(tabSheet.getTab().getElement().getAttribute("title"), is(""));
        // TODO LIN-2054
    }

    @Test
    void testBuilder_WithCaption() {
        LinkkiTabSheet tabSheet = LinkkiTabSheet.builder("id").caption("caption").content(() -> new Span("content"))
                .build();

        assertThat(tabSheet.getTab().getLabel(), is("caption"));
    }

    @Test
    void testBuilder_WithDescription() {
        LinkkiTabSheet tabSheet = LinkkiTabSheet
                .builder("id")
                .description("description")
                .content(() -> new Span("content"))
                .build();

        assertThat(tabSheet.getDescription(), is("description"));
        assertThat(tabSheet.getTab().getElement().getAttribute("title"), is("description"));
    }

    @Test
    void testBuilder_CaptionOverwritesExistingCaptionComponent() {
        LinkkiTabSheet tabSheet = LinkkiTabSheet.builder("id").caption(VaadinIcon.PLUS.create())
                .caption("caption").content(() -> new Span("content")).build();

        assertThat(tabSheet.getTab().getChildren().count(), is(0l));
    }


    @Test
    void testBuilder_WithCaptionComponent() {
        Icon icon = VaadinIcon.PLUS.create();

        LinkkiTabSheet tabSheet = LinkkiTabSheet.builder("id").caption(icon).content(() -> new Span("content")).build();

        assertThat(tabSheet.getTab().getLabel(), is(""));
        assertThat(tabSheet.getTab().getChildren().findFirst().get(), is(icon));
    }

    @Test
    void testBuilder_WithCaptionComponent_OverwritesExistingCaption() {
        Icon icon = VaadinIcon.PLUS.create();

        LinkkiTabSheet tabSheet = LinkkiTabSheet.builder("id").caption("caption").caption(icon)
                .content(() -> new Span("content")).build();

        assertThat(tabSheet.getTab().getLabel(), is(""));
    }

    @Test
    void testBuilder_WithVisibilitySupplier() {
        LinkedList<Boolean> visibility = new LinkedList<Boolean>(Arrays.asList(true, true, false));

        LinkkiTabSheet tabSheet = LinkkiTabSheet.builder("id").content(() -> new Span("content"))
                .visibleWhen(visibility::pop).build();

        // first isVisible was called by build()
        assertThat(tabSheet.isVisible(), is(true));
        assertThat(tabSheet.isVisible(), is(false));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testSelectionChangeListener() {
        ComponentEventListener<TabSheetSelectionChangeEvent> onSelectionHandler1 = mock(ComponentEventListener.class);
        Handler onSelectionHandler2 = mock(Handler.class);
        LinkkiTabSheet tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        tabSheet1.addTabSelectionChangeListener(onSelectionHandler1);
        LinkkiTabSheet tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();
        tabSheet2.addTabSelectionChangeListener(e -> onSelectionHandler2.apply());

        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        tabLayout.addTabSheets(tabSheet1, tabSheet2);
        clearInvocations(onSelectionHandler1);

        tabLayout.setSelectedIndex(1);

        verify(onSelectionHandler2).apply();

        tabLayout.setSelectedIndex(0);

        ArgumentCaptor<TabSheetSelectionChangeEvent> captor = ArgumentCaptor
                .forClass(TabSheetSelectionChangeEvent.class);
        verify(onSelectionHandler1).onComponentEvent(captor.capture());
        assertThat(captor.getValue().getTabSheet(), is(tabSheet1));
    }

    @Test
    void testAfterSelectionObserver_InitialCall() {
        TabContent content1 = new TabContent("id1");
        LinkkiTabSheet tabSheet1 = LinkkiTabSheet.builder("id1")
                .content(() -> content1)
                .build();
        TabContent content2 = new TabContent("id2");
        LinkkiTabSheet tabSheet2 = LinkkiTabSheet.builder("id2")
                .content(() -> content2)
                .build();

        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        tabLayout.addTabSheets(tabSheet1, tabSheet2);

        assertThat(content1.updateCount, is(1));
        assertThat(content2.updateCount, is(0));
    }

    @Test
    void testAfterSelectionObserver_SelectionChanged() {
        TabContent content1 = new TabContent("id1");
        LinkkiTabSheet tabSheet1 = LinkkiTabSheet.builder("id1")
                .content(() -> content1)
                .build();
        TabContent content2 = new TabContent("id2");
        LinkkiTabSheet tabSheet2 = LinkkiTabSheet.builder("id2")
                .content(() -> content2)
                .build();

        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        tabLayout.addTabSheets(tabSheet1, tabSheet2);
        tabLayout.setSelectedIndex(1);

        assertThat(content1.updateCount, is(1));
        assertThat(content2.updateCount, is(1));
    }

    @Test
    void testAfterSelectionObserver_SelectionChanged_TabsInTabs() {
        TabContent content1 = new TabContent("id1");
        LinkkiTabSheet tabSheet1 = LinkkiTabSheet.builder("id1")
                .content(() -> content1)
                .build();
        TabContent content2 = new TabContent("id2");
        LinkkiTabSheet tabSheet2 = LinkkiTabSheet.builder("id2")
                .content(() -> content2)
                .build();
        LinkkiTabLayout innerTabLayout = new LinkkiTabLayout();
        TabContent innerContent1 = new TabContent("inner1");
        TabContent innerContent2 = new TabContent("inner2");
        innerTabLayout.addTabSheets(
                                    LinkkiTabSheet.builder("inner1")
                                            .content(() -> innerContent1)
                                            .build(),
                                    LinkkiTabSheet.builder("inner2")
                                            .content(() -> innerContent2)
                                            .build());
        content2.add(innerTabLayout);


        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        tabLayout.addTabSheets(tabSheet1, tabSheet2);
        // select every tab one time to get attached to the dom
        tabLayout.setSelectedIndex(1);
        tabLayout.setSelectedIndex(0);
        innerTabLayout.setSelectedIndex(1);
        innerTabLayout.setSelectedIndex(0);
        // reset updateCounts
        content1.updateCount = 0;
        content2.updateCount = 0;
        innerContent1.updateCount = 0;
        innerContent2.updateCount = 0;

        tabLayout.setSelectedIndex(1);

        assertThat(content1.updateCount, is(0));
        assertThat(content2.updateCount, is(1));
        assertThat(innerContent1.updateCount, is(1));
        assertThat(innerContent2.updateCount, is(0));
    }

    static class TabContent extends Div implements AfterTabSelectedObserver {

        private static final long serialVersionUID = 1L;
        private int updateCount = 0;

        public TabContent(String id) {
            setId("content_" + id);
        }

        @Override
        public void afterTabSelected(TabSheetSelectionChangeEvent e) {
            updateCount++;
        }

        @Override
        public String toString() {
            return "Content " + getId().get();
        }

    }

}
