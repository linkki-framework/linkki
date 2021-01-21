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
package org.linkki.core.vaadin.component.tabsheet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.component.tabs.Tabs.SelectedChangeEvent;

public class LinkkiTabLayoutTest {

    @Test
    public void testLinkkiTabLayout_VerticalOrientation() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout(Orientation.VERTICAL);

        Component layout = tabLayout.getContent();

        assertThat(layout, is(instanceOf(HorizontalLayout.class)));
    }

    @Test
    public void testLinkkiTabLayout_HorizontalOrientation() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout(Orientation.HORIZONTAL);

        Component layout = tabLayout.getContent();

        assertThat(layout, is(instanceOf(VerticalLayout.class)));
    }

    @Test
    public void testAddTab_WithCaption() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        Span content = new Span("content");

        Tab tab = tabLayout.addTab("id", "caption", content);

        assertThat(tabLayout.getTabs(), contains(tab));
        assertThat(tab.getLabel(), is("caption"));
        assertThat(tab.getId().get(), is("id"));
        assertThat(tabLayout.getContent(tab), is(content));
        assertThat(tabLayout.getSelectedTab(), is(tab));
        assertThat(tabLayout.getContent(tab).isVisible(), is(true));
    }

    @Test
    public void testAddTab_WithCaptionAndIndex() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        Tab initialTab = tabLayout.addTab("initial", "initial", new Span());
        Span content = new Span("content");

        Tab tab = tabLayout.addTab("id", "caption", content, 0);

        assertThat(tabLayout.getTabs(), contains(tab, initialTab));
        assertThat(tab.getLabel(), is("caption"));
        assertThat(tab.getId().get(), is("id"));
        assertThat(tabLayout.getContent(tab), is(content));
        // initial tab keeps selection, new content is therefore not visible
        assertThat(tabLayout.getSelectedTab(), is(initialTab));
        assertThat(tabLayout.getContent(tab).isVisible(), is(false));
    }

    @Test
    public void testAddTab_WithCaptionComponent() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        Span caption = new Span("caption");
        Span content = new Span("content");

        Tab tab = tabLayout.addTab("id", caption, content);

        assertThat(tabLayout.getTabs(), contains(tab));
        assertThat(tab.getChildren().collect(Collectors.toList()), contains(caption));
        assertThat(tab.getLabel(), is(""));
        assertThat(tab.getId().get(), is("id"));
        assertThat(tabLayout.getContent(tab), is(content));
        assertThat(tabLayout.getSelectedTab(), is(tab));
        assertThat(tabLayout.getContent(tab).isVisible(), is(true));
    }

    @Test
    public void testAddTab_WithCaptionComponentAndIndex() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        Tab initialTab = tabLayout.addTab("initial", "initial", new Span());
        Span caption = new Span("caption");
        Span content = new Span("content");

        Tab tab = tabLayout.addTab("id", caption, content, 0);

        assertThat(tabLayout.getTabs(), contains(tab, initialTab));
        assertThat(tab.getChildren().collect(Collectors.toList()), contains(caption));
        assertThat(tab.getLabel(), is(""));
        assertThat(tab.getId().get(), is("id"));
        assertThat(tabLayout.getContent(tab), is(content));
        // initial tab keeps selection, new content is therefore not visible
        assertThat(tabLayout.getSelectedTab(), is(initialTab));
        assertThat(tabLayout.getContent(tab).isVisible(), is(false));
    }

    @Test
    public void testAddTab_FirstTabAddedIsSelected() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        Span content1 = new Span("content1");
        Span content2 = new Span("content2");

        Tab tab1 = tabLayout.addTab("tab1", "tab1", content1);
        Tab tab2 = tabLayout.addTab("tab2", "tab2", content2);

        assertThat(tabLayout.getSelectedTab(), is(tab1));
        assertThat(tabLayout.getContent(tab1).isVisible(), is(true));
        assertThat(tabLayout.getContent(tab2).isVisible(), is(false));
    }

    @Test
    public void testRemoveTab() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        Span content = new Span("content");
        Tab tab = tabLayout.addTab("id", "caption", content);

        tabLayout.removeTab(tab);

        assertThat(tabLayout.getTabs(), is(empty()));
        assertThat(content.getParent().isPresent(), is(false));
        assertThat(tab.getParent().isPresent(), is(false));
    }

    @Test
    public void testRemoveTab_MultipleTabs() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        Tab tab1 = tabLayout.addTab("tab1", "tab1", new Span());
        Tab tab2 = tabLayout.addTab("tab2", "tab2", new Span());
        Tab tab3 = tabLayout.addTab("tab3", "tab3", new Span());

        tabLayout.removeTab(tab2);

        assertThat(tabLayout.getTabs(), contains(tab1, tab3));
    }

    @Test
    public void testRemoveTab_SelectedTab() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        Tab tab1 = tabLayout.addTab("tab1", "tab1", new Span());
        Tab tab2 = tabLayout.addTab("tab2", "tab2", new Span());
        assertThat(tabLayout.getSelectedTab(), is(tab1));

        tabLayout.removeTab(tab1);

        assertThat(tabLayout.getSelectedTab(), is(tab2));
    }

    @Test
    public void testRemoveAllTabs() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        Span content = new Span("content");
        Tab tab = tabLayout.addTab("id", "caption", content);

        tabLayout.removeAllTabs();

        assertThat(tabLayout.getTabs(), is(empty()));
        assertThat(content.getParent().isPresent(), is(false));
        assertThat(tab.getParent().isPresent(), is(false));
    }


    @Test
    public void testGetTabContent() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        Span content = new Span("content");
        Tab tab = tabLayout.addTab("tab", "tab", content);

        Component component = tabLayout.getContent(tab);

        assertThat(component, is(content));
    }

    @Test
    public void testGetTabContent_InvalidTab() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        tabLayout.addTab("tab1", "tab1", new Span());
        tabLayout.addTab("tab2", "tab2", new Span());

        assertThrows(IllegalArgumentException.class, () -> tabLayout.getContent(new Tab()));
    }

    @Test
    public void testGetSelectedTab() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        tabLayout.addTab("tab1", "tab1", new Span());
        Tab tab2 = tabLayout.addTab("tab2", "tab2", new Span());
        tabLayout.addTab("tab3", "tab3", new Span());
        tabLayout.setSelectedTab(tab2);

        Tab selected = tabLayout.getSelectedTab();

        assertThat(selected, is(tab2));
    }

    @Test
    public void testSetSelectedTab() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        Span content1 = new Span("content1");
        Span content2 = new Span("content2");
        Tab tab1 = tabLayout.addTab("tab1", "tab1", content1);
        Tab tab2 = tabLayout.addTab("tab2", "tab2", content2);

        tabLayout.setSelectedTab(tab2);

        assertThat(tabLayout.getSelectedTab(), is(tab2));
        assertThat(tabLayout.getSelectedIndex(), is(1));
        assertThat(tabLayout.getContent(tab1).isVisible(), is(false));
        assertThat(tabLayout.getContent(tab2).isVisible(), is(true));
    }

    @Test
    public void testGetSelectedIndex() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        tabLayout.addTab("tab1", "tab1", new Span());
        tabLayout.addTab("tab2", "tab2", new Span());
        tabLayout.addTab("tab3", "tab3", new Span());
        tabLayout.setSelectedIndex(1);

        int selected = tabLayout.getSelectedIndex();

        assertThat(selected, is(1));
    }

    @Test
    public void testSetSelectedIndex() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        Span content1 = new Span("content1");
        Span content2 = new Span("content2");
        Tab tab1 = tabLayout.addTab("tab1", "tab1", content1);
        Tab tab2 = tabLayout.addTab("tab2", "tab2", content2);

        tabLayout.setSelectedIndex(1);

        assertThat(tabLayout.getSelectedTab(), is(tab2));
        assertThat(tabLayout.getSelectedIndex(), is(1));
        assertThat(tabLayout.getContent(tab1).isVisible(), is(false));
        assertThat(tabLayout.getContent(tab2).isVisible(), is(true));
    }


    @Test
    public void testSetSelectedIndex_InvalidIndex() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        tabLayout.addTab("tab1", "tab1", new Span());

        assertThrows(IllegalArgumentException.class, () -> tabLayout.setSelectedIndex(1));
    }

    @Test
    public void testSetSelectedIndex_DeselectAll() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        Span content1 = new Span("tab1");
        Span content2 = new Span("tab2");
        tabLayout.addTab("tab1", "tab1", content1);
        tabLayout.addTab("tab2", "tab2", content2);

        tabLayout.setSelectedIndex(-1);

        assertThat(tabLayout.getSelectedIndex(), is(-1));
        assertThat(tabLayout.getSelectedTab(), is(nullValue()));
        assertThat(content1.isVisible(), is(false));
        assertThat(content2.isVisible(), is(false));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAddSelectedTabChangeListener() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        tabLayout.addTab("tab1", "tab1", new Span());
        Tab tab2 = tabLayout.addTab("tab2", "tab2", new Span());
        ComponentEventListener<SelectedChangeEvent> listener = mock(ComponentEventListener.class);

        tabLayout.addSelectedTabChangeListener(listener);
        tabLayout.setSelectedTab(tab2);

        verify(listener).onComponentEvent(any());
    }

}