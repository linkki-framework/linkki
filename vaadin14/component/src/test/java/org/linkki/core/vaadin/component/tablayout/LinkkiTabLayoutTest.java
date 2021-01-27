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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.linkki.test.matcher.Matchers.absent;
import static org.linkki.test.matcher.Matchers.hasValue;
import static org.linkki.test.matcher.Matchers.present;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
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
    public void testAddTab_WithoutIndex() {
        LinkkiTabSheet tabSheet1 = new LinkkiTabSheet("id1", "caption1", "description1", new Span("content1"));
        LinkkiTabSheet tabSheet2 = new LinkkiTabSheet("id2", "caption2", "description2", new Span("content2"));

        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        tabLayout.addTab(tabSheet1);
        tabLayout.addTab(tabSheet2);

        assertThat(tabLayout.getTabsComponent().getComponentCount(), is(2));
        assertThat(tabLayout.getTabsComponent().getChildren().collect(Collectors.toList()),
                   Matchers.contains(tabSheet1.getTab(), tabSheet2.getTab()));
    }

    @Test
    public void testAddTab_WithIndex() {
        LinkkiTabSheet tabSheet1 = new LinkkiTabSheet("id1", "caption1", "description1", new Span("content1"));
        LinkkiTabSheet tabSheet2 = new LinkkiTabSheet("id2", "caption2", "description2", new Span("content2"));

        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        tabLayout.addTab(tabSheet1);
        tabLayout.addTab(tabSheet2, 0);

        assertThat(tabLayout.getTabsComponent().getComponentCount(), is(2));
        assertThat(tabLayout.getTabsComponent().getChildren().collect(Collectors.toList()),
                   Matchers.contains(tabSheet2.getTab(), tabSheet1.getTab()));
    }

    @Test
    public void testAddTab_FirstSheetIsSelected() {
        LinkkiTabSheet tabSheet1 = new LinkkiTabSheet("id1", "caption1", "description1", new Span("content1"));
        LinkkiTabSheet tabSheet2 = new LinkkiTabSheet("id2", "caption2", "description2", new Span("content2"));

        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        tabLayout.addTab(tabSheet1);
        tabLayout.addTab(tabSheet2);

        assertThat(tabLayout.getTabsComponent().getSelectedTab(), is(tabSheet1.getTab()));
        assertThat(tabSheet1.getContent().getParent(), is(present()));
        assertThat(tabSheet1.getContent().isVisible(), is(true));
        assertThat(tabSheet2.getContent().getParent(), is(absent()));
    }


    @Test
    public void testSetSelectedTab() {
        LinkkiTabSheet tabSheet1 = new LinkkiTabSheet("id1", "caption1", "description1", new Span("content1"));
        LinkkiTabSheet tabSheet2 = new LinkkiTabSheet("id2", "caption2", "description2", new Span("content2"));

        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        tabLayout.addTab(tabSheet1);
        tabLayout.addTab(tabSheet2);

        tabLayout.setSelectedTab(tabSheet2);

        assertThat(tabLayout.getTabsComponent().getSelectedTab(), is(tabSheet2.getTab()));
        assertThat(tabSheet2.getContent().isVisible(), is(true));
        assertThat(tabSheet1.getContent().isVisible(), is(false));
    }

    @Test
    public void testSetSelectedTab_LazyInstantiation() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        LinkkiTabSheet tabSheet1 = new LinkkiTabSheet("id1", "caption1", "description1", new Span("content1"));
        tabLayout.addTab(tabSheet1);
        assertThat(tabLayout.getContent().getChildren().filter(c -> !Tabs.class.isInstance(c))
                .collect(Collectors.toList()), contains(tabSheet1.getContent()));

        Supplier<Component> content2Supplier = spy(new Supplier<Component>() {

            @Override
            public Component get() {
                return new Span();
            }

        });
        LinkkiTabSheet tabSheet2 = new LinkkiTabSheet("id2", "caption2", "description2", content2Supplier);

        tabLayout.addTab(tabSheet2);
        assertThat(tabLayout.getContent().getChildren().filter(c -> !Tabs.class.isInstance(c))
                .collect(Collectors.toList()), contains(tabSheet1.getContent()));
        Mockito.verifyNoInteractions(content2Supplier);

        tabLayout.setSelectedTab(tabSheet2);

        assertThat(tabLayout.getContent().getChildren().filter(c -> !Tabs.class.isInstance(c))
                .collect(Collectors.toList()), contains(tabSheet1.getContent(), tabSheet2.getContent()));
    }

    @Test
    public void testSetSelectedTab_NotAddedTabSheet() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        tabLayout.addTab(new LinkkiTabSheet("id1", "caption1", "description1", new Span("content1")));

        assertThrows(IllegalArgumentException.class, () -> tabLayout
                .setSelectedTab(new LinkkiTabSheet("id2", "caption2", "description2", new Span("content2"))));
    }

    @Test
    public void testSetSelectedTab_Null() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        LinkkiTabSheet tabSheet = new LinkkiTabSheet("id1", "caption1", "description1", new Span("content1"));
        tabLayout.addTab(tabSheet);

        tabLayout.setSelectedTab(null);

        assertThat(tabLayout.getTabsComponent().getSelectedTab(), is(nullValue()));
        assertThat(tabSheet.getContent().isVisible(), is(false));
    }

    @Test
    public void testGetSelectedTab() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        LinkkiTabSheet tabSheet1 = new LinkkiTabSheet("id1", "caption1", "description1", new Span("content1"));
        LinkkiTabSheet tabSheet2 = new LinkkiTabSheet("id2", "caption2", "description2", new Span("content2"));
        tabLayout.addTab(tabSheet1);
        tabLayout.addTab(tabSheet2);
        tabLayout.setSelectedTab(tabSheet2);

        LinkkiTabSheet selectedTabSheet = tabLayout.getSelectedTabSheet();

        assertThat(selectedTabSheet, is(tabSheet2));
    }

    @Test
    public void testSetSelectedIndex() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        LinkkiTabSheet tabSheet1 = new LinkkiTabSheet("id1", "caption1", "description1", new Span("content1"));
        LinkkiTabSheet tabSheet2 = new LinkkiTabSheet("id2", "caption2", "description2", new Span("content2"));
        tabLayout.addTab(tabSheet1);
        tabLayout.addTab(tabSheet2);

        tabLayout.setSelectedIndex(1);

        assertThat(tabLayout.getTabsComponent().getSelectedIndex(), is(1));
        assertThat(tabSheet2.getContent().isVisible(), is(true));
        assertThat(tabSheet1.getContent().isVisible(), is(false));
    }

    @Test
    public void testSetSelectedIndex_InvalidIndex() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        tabLayout.addTab(new LinkkiTabSheet("id1", "caption1", "description1", new Span("content1")));

        assertThrows(IllegalArgumentException.class, () -> tabLayout.setSelectedIndex(1));
    }

    @Test
    public void testSetSelectedIndex_DeselectAll() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        LinkkiTabSheet tabSheet1 = new LinkkiTabSheet("id1", "caption1", "description1", new Span("content1"));
        LinkkiTabSheet tabSheet2 = new LinkkiTabSheet("id2", "caption2", "description2", new Span("content2"));
        tabLayout.addTab(tabSheet1);
        tabLayout.addTab(tabSheet2);
        // Make sure content of tab sheet 2 is ever instantiated
        tabLayout.setSelectedTab(tabSheet2);

        tabLayout.setSelectedIndex(-1);

        assertThat(tabLayout.getTabsComponent().getSelectedIndex(), is(-1));
        assertThat(tabLayout.getTabsComponent().getSelectedTab(), is(nullValue()));
        assertThat(tabSheet1.getContent().isVisible(), is(false));
        assertThat(tabSheet2.getContent().isVisible(), is(false));
    }

    @Test
    public void testGetSelectedIndex() {
        LinkkiTabSheet tabSheet1 = new LinkkiTabSheet("id1", "caption1", "description1", new Span("content1"));
        LinkkiTabSheet tabSheet2 = new LinkkiTabSheet("id2", "caption2", "description2", new Span("content2"));
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        tabLayout.addTab(tabSheet1);
        tabLayout.addTab(tabSheet2);

        tabLayout.setSelectedIndex(1);

        assertThat(tabLayout.getSelectedIndex(), is(1));
    }

    @Test
    public void testRemoveTab() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        LinkkiTabSheet tabSheet1 = new LinkkiTabSheet("id1", "caption1", "description1", new Span("content1"));
        LinkkiTabSheet tabSheet2 = new LinkkiTabSheet("id2", "caption2", "description2", new Span("content2"));
        LinkkiTabSheet tabSheet3 = new LinkkiTabSheet("id3", "caption3", "description3", new Span("content3"));
        tabLayout.addTab(tabSheet1);
        tabLayout.addTab(tabSheet2);
        tabLayout.addTab(tabSheet3);

        tabLayout.removeTab(tabSheet2);

        assertThat(tabSheet2.getTab().getParent(), is(absent()));
        assertThat(tabSheet1.getTab().getParent(), is(present()));
        assertThat(tabSheet3.getTab().getParent(), is(present()));
    }

    @Test
    public void testRemoveTab_SelectedTab() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        LinkkiTabSheet tabSheet1 = new LinkkiTabSheet("id1", "caption1", "description1", new Span("content1"));
        LinkkiTabSheet tabSheet2 = new LinkkiTabSheet("id2", "caption2", "description2", new Span("content2"));
        tabLayout.addTab(tabSheet1);
        tabLayout.addTab(tabSheet2);
        tabLayout.setSelectedTab(tabSheet2);

        tabLayout.removeTab(tabSheet2);

        assertThat(tabSheet2.getTab().getParent(), is(absent()));
        assertThat(tabSheet1.getTab().getParent(), is(present()));
        assertThat(tabLayout.getSelectedTabSheet(), is(tabSheet1));
    }

    @Test
    public void testRemoveAllTabs() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        LinkkiTabSheet tabSheet1 = new LinkkiTabSheet("id1", "caption1", "description1", new Span("content1"));
        LinkkiTabSheet tabSheet2 = new LinkkiTabSheet("id2", "caption2", "description2", new Span("content2"));
        tabLayout.addTab(tabSheet1);
        tabLayout.addTab(tabSheet2);
        // Make sure content of tab sheet 2 is ever instantiated
        tabLayout.setSelectedTab(tabSheet2);

        tabLayout.removeAllTabs();

        assertThat(tabLayout.getTabsComponent().getChildren().collect(Collectors.toList()), is(empty()));
        assertThat(tabSheet1.getContent().getParent().isPresent(), is(false));
        assertThat(tabSheet1.getTab().getParent().isPresent(), is(false));
        assertThat(tabSheet2.getContent().getParent().isPresent(), is(false));
        assertThat(tabSheet2.getTab().getParent().isPresent(), is(false));
    }

    @Test
    public void testGetTabSheet() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        LinkkiTabSheet tabSheet1 = new LinkkiTabSheet("id1", "caption1", "description1", new Span("content1"));
        LinkkiTabSheet tabSheet2 = new LinkkiTabSheet("id2", "caption2", "description2", new Span("content2"));
        tabLayout.addTab(tabSheet1);
        tabLayout.addTab(tabSheet2);

        assertThat(tabLayout.getTabSheet("id1"), is(hasValue(tabSheet1)));
        assertThat(tabLayout.getTabSheet("id2"), is(hasValue(tabSheet2)));
        assertThat(tabLayout.getTabSheet("id3"), is(absent()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAddSelectedTabChangeListener() {
        LinkkiTabLayout tabLayout = new LinkkiTabLayout();
        LinkkiTabSheet tabSheet1 = new LinkkiTabSheet("id1", "caption1", "description1", new Span("content1"));
        LinkkiTabSheet tabSheet2 = new LinkkiTabSheet("id2", "caption2", "description2", new Span("content2"));
        tabLayout.addTab(tabSheet1);
        tabLayout.addTab(tabSheet2);
        ComponentEventListener<SelectedChangeEvent> listener = mock(ComponentEventListener.class);

        tabLayout.addSelectedTabChangeListener(listener);
        tabLayout.setSelectedTab(tabSheet2);

        verify(listener).onComponentEvent(any());
    }
}