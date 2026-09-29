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
package org.linkki.core.vaadin.component.tablayout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.linkki.util.handler.Handler;
import org.mockito.Mockito;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.component.tabs.Tabs.SelectedChangeEvent;
import com.vaadin.flow.router.AfterNavigationEvent;

class LinkkiTabLayoutTest {

    private boolean tabVisibility = true;

    @Test
    void testLinkkiTabLayout_VerticalOrientation() {
        var tabLayout = new LinkkiTabLayout(Orientation.VERTICAL);

        assertThat(tabLayout.getElement().hasAttribute(LinkkiTabLayout.PROPERTY_ORIENTATION)).isTrue();
        assertThat(tabLayout.getElement().getAttribute(LinkkiTabLayout.PROPERTY_ORIENTATION)).isEqualTo("vertical");
    }

    @Test
    void testLinkkiTabLayout_HorizontalOrientation() {
        var tabLayout = new LinkkiTabLayout(Orientation.HORIZONTAL);

        assertThat(tabLayout.getElement().hasAttribute(LinkkiTabLayout.PROPERTY_ORIENTATION)).isTrue();
        assertThat(tabLayout.getElement().getAttribute(LinkkiTabLayout.PROPERTY_ORIENTATION)).isEqualTo("horizontal");
    }

    @Test
    void testAddTabSheet_WithoutIndex() {
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();

        var tabLayout = new LinkkiTabLayout();
        tabLayout.addTabSheet(tabSheet1);
        tabLayout.addTabSheet(tabSheet2);

        assertThat(tabLayout.getTabsComponent().getTabCount()).isEqualTo(2);
        assertThat(tabLayout.getTabsComponent().getChildren().toList())
                .containsExactly(tabSheet1.getTab(), tabSheet2.getTab());
    }

    @Test
    void testAddTabSheet_WithIndex() {
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();

        var tabLayout = new LinkkiTabLayout();
        tabLayout.addTabSheet(tabSheet1);
        tabLayout.addTabSheet(tabSheet2, 0);

        assertThat(tabLayout.getTabsComponent().getTabCount()).isEqualTo(2);
        assertThat(tabLayout.getTabsComponent().getChildren().toList())
                .containsExactly(tabSheet2.getTab(), tabSheet1.getTab());
    }

    @Test
    void testAddTabSheet_FirstSheetIsSelected() {
        var onSelectionHandler1 = mock(Handler.class);
        var onSelectionHandler2 = mock(Handler.class);
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        tabSheet1.addTabSelectionChangeListener(e -> onSelectionHandler1.apply());
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();
        tabSheet2.addTabSelectionChangeListener(e -> onSelectionHandler2.apply());

        var tabLayout = new LinkkiTabLayout();
        tabLayout.addTabSheet(tabSheet1);
        tabLayout.addTabSheet(tabSheet2);

        verify(onSelectionHandler1).apply();
        verify(onSelectionHandler2, never()).apply();
        assertThat(tabLayout.getTabsComponent().getSelectedTab()).isEqualTo(tabSheet1.getTab());
        assertThat(tabSheet1.getContent().getParent()).isPresent();
        assertThat(tabSheet1.getContent().isVisible()).isTrue();
        assertThat(tabSheet2.getContent().getParent()).isEmpty();
    }

    @Test
    void testGetTabSheets() {
        var tabLayout = new LinkkiTabLayout();
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();
        tabLayout.addTabSheet(tabSheet1);
        tabLayout.addTabSheet(tabSheet2, 0);

        assertThat(tabLayout.getTabSheets()).containsExactly(tabSheet2, tabSheet1);
    }

    @Test
    void testAddTabSheetsVararg() {
        var tabLayout = new LinkkiTabLayout();
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();
        tabLayout.addTabSheets(tabSheet2, tabSheet1);

        assertThat(tabLayout.getTabSheets()).containsExactly(tabSheet2, tabSheet1);
    }

    @Test
    void testAddTabSheetsStream() {
        var tabLayout = new LinkkiTabLayout();
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();
        tabLayout.addTabSheets(Stream.of(tabSheet2, tabSheet1));

        assertThat(tabLayout.getTabSheets()).containsExactly(tabSheet2, tabSheet1);
    }

    @Test
    void testAddTabSheetsIterable() {
        var tabLayout = new LinkkiTabLayout();
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();
        tabLayout.addTabSheets(Arrays.asList(tabSheet2, tabSheet1));

        assertThat(tabLayout.getTabSheets()).containsExactly(tabSheet2, tabSheet1);
    }

    @Test
    void testSetSelectedTabSheet() {
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();

        var tabLayout = new LinkkiTabLayout();
        tabLayout.addTabSheets(tabSheet1, tabSheet2);

        tabLayout.setSelectedTabSheet(tabSheet2.getId());

        assertThat(tabLayout.getTabsComponent().getSelectedTab()).isEqualTo(tabSheet2.getTab());
        assertThat(tabSheet2.getContent().isVisible()).isTrue();
        assertThat(tabSheet1.getContent().isVisible()).isFalse();
    }

    @Test
    void testCallSelectionHandler_SelectionOnTabsComponent() {
        var onSelectionHandler1 = mock(Handler.class);
        var onSelectionHandler2 = mock(Handler.class);

        var tabLayout = new LinkkiTabLayout();
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        tabSheet1.addTabSelectionChangeListener(e -> onSelectionHandler1.apply());
        tabLayout.addTabSheet(tabSheet1);
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();
        tabSheet2.addTabSelectionChangeListener(e -> onSelectionHandler2.apply());
        tabLayout.addTabSheet(tabSheet2);
        clearInvocations(onSelectionHandler1);

        tabLayout.getTabsComponent().setSelectedIndex(1);

        verify(onSelectionHandler2).apply();

        tabLayout.getTabsComponent().setSelectedIndex(0);

        verify(onSelectionHandler1).apply();
    }

    @Test
    void testSetSelectedTabSheet_LazyInstantiation() {
        var tabLayout = new LinkkiTabLayout();
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        tabLayout.addTabSheet(tabSheet1);
        assertThat(tabLayout.getContent().getChildren().toList())
                .containsExactly(tabSheet1.getContent());

        Supplier<Component> content2Supplier = spy(new Supplier<Component>() {
            @Override
            public Component get() {
                return new Span();
            }
        });
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(content2Supplier).build();

        tabLayout.addTabSheet(tabSheet2);
        assertThat(tabLayout.getContent().getChildren().toList())
                .containsExactly(tabSheet1.getContent());
        Mockito.verifyNoInteractions(content2Supplier);

        tabLayout.setSelectedTabSheet("id2");

        assertThat(tabLayout.getContent().getChildren().toList())
                .containsExactly(tabSheet1.getContent(), tabSheet2.getContent());
    }

    @Test
    void testSetSelectedTabSheet_NotAddedTabSheet() {
        var tabLayout = new LinkkiTabLayout();
        tabLayout.addTabSheet(LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build());

        assertThrows(IllegalArgumentException.class, () -> tabLayout.setSelectedTabSheet("id2"));
    }

    @Test
    void testGetSelectedTabSheet() {
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();
        var tabLayout = new LinkkiTabLayout();
        tabLayout.addTabSheets(tabSheet1, tabSheet2);
        tabLayout.setSelectedTabSheet(tabSheet2.getId());

        var selectedTabSheet = tabLayout.getSelectedTabSheet();

        assertThat(selectedTabSheet).isEqualTo(tabSheet2);
    }

    @Test
    void testGetSelectedTabSheet_NoneSelected() {
        var tabLayout = new LinkkiTabLayout();

        assertThrows(NoSuchElementException.class, tabLayout::getSelectedTabSheet);

        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        tabLayout.addTabSheets(tabSheet1);
        tabLayout.getTabsComponent().setSelectedTab(null);

        assertThrows(NoSuchElementException.class, tabLayout::getSelectedTabSheet);
    }

    @Test
    void testSetSelectedIndex() {
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();
        var tabLayout = new LinkkiTabLayout();
        tabLayout.addTabSheets(tabSheet1, tabSheet2);

        tabLayout.setSelectedIndex(1);

        assertThat(tabLayout.getTabsComponent().getSelectedIndex()).isEqualTo(1);
        assertThat(tabSheet2.getContent().isVisible()).isTrue();
        assertThat(tabSheet1.getContent().isVisible()).isFalse();
    }

    @Test
    void testSetSelectedIndex_CallSelectionHandler() {
        var onSelectionHandler1 = mock(Handler.class);
        var onSelectionHandler2 = mock(Handler.class);
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        tabSheet1.addTabSelectionChangeListener(e -> onSelectionHandler1.apply());
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();
        tabSheet2.addTabSelectionChangeListener(e -> onSelectionHandler2.apply());

        var tabLayout = new LinkkiTabLayout();
        tabLayout.addTabSheets(tabSheet1, tabSheet2);
        clearInvocations(onSelectionHandler1);

        tabLayout.setSelectedIndex(1);

        verify(onSelectionHandler2).apply();

        tabLayout.setSelectedIndex(0);

        verify(onSelectionHandler1).apply();
    }

    @Test
    void testSetSelectedIndex_InvalidIndex() {
        var tabLayout = new LinkkiTabLayout();
        tabLayout.addTabSheet(LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build());
        tabLayout.setSelectedIndex(0);

        tabLayout.setSelectedIndex(1);

        assertThat(tabLayout.getSelectedIndex())
                .as("If an invalid index is selected, the tab layout should revert to the previously selected valid index.")
                .isZero();
    }

    @Test
    void testSetSelectedIndex_DeselectAll() {
        var tabLayout = new LinkkiTabLayout();
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();
        tabLayout.addTabSheets(tabSheet1, tabSheet2);
        // Make sure content of tab sheet 2 is ever instantiated
        tabLayout.setSelectedTabSheet(tabSheet2.getId());

        tabLayout.setSelectedIndex(-1);

        assertThat(tabLayout.getTabsComponent().getSelectedIndex()).isEqualTo(-1);
        assertThat(tabLayout.getTabsComponent().getSelectedTab()).isNull();
        assertThat(tabSheet1.getContent().isVisible()).isFalse();
        assertThat(tabSheet2.getContent().isVisible()).isFalse();
    }

    @Test
    void testGetSelectedIndex() {
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();
        var tabLayout = new LinkkiTabLayout();
        tabLayout.addTabSheets(tabSheet1, tabSheet2);

        tabLayout.setSelectedIndex(1);

        assertThat(tabLayout.getSelectedIndex()).isEqualTo(1);
    }

    @Test
    void testRemoveTab() {
        var tabLayout = new LinkkiTabLayout();
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();
        var tabSheet3 = LinkkiTabSheet.builder("id3").content(() -> new Span("content3")).build();
        tabLayout.addTabSheets(tabSheet1, tabSheet2, tabSheet3);

        tabLayout.removeTabSheet(tabSheet2);

        assertThat(tabSheet2.getTab().getParent()).isEmpty();
        assertThat(tabSheet1.getTab().getParent()).isPresent();
        assertThat(tabSheet3.getTab().getParent()).isPresent();
    }

    @Test
    void testRemoveTabSheet_SelectedTab() {
        var tabLayout = new LinkkiTabLayout();
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();
        tabLayout.addTabSheets(tabSheet1, tabSheet2);
        tabLayout.setSelectedTabSheet(tabSheet2.getId());

        tabLayout.removeTabSheet(tabSheet2);

        assertThat(tabSheet2.getTab().getParent()).isEmpty();
        assertThat(tabSheet1.getTab().getParent()).isPresent();
        assertThat(tabLayout.getSelectedTabSheet()).isEqualTo(tabSheet1);
    }

    @Test
    void testRemoveTabSheet_DoesNotCreateContent() {
        var tabLayout = new LinkkiTabLayout();
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(Assertions::fail).build();
        tabLayout.addTabSheets(tabSheet1, tabSheet2);

        tabLayout.removeTabSheet(tabSheet2);

        assertThat(tabLayout.getTabSheets()).containsExactly(tabSheet1);
    }

    @Test
    void testRemoveAllTabSheets() {
        var tabLayout = new LinkkiTabLayout();
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();
        tabLayout.addTabSheets(tabSheet1, tabSheet2);
        // Make sure content of tab sheet 2 is ever instantiated
        tabLayout.setSelectedTabSheet(tabSheet2.getId());

        tabLayout.removeAllTabSheets();

        assertThat(tabLayout.getTabsComponent().getChildren().toList()).isEmpty();
        assertThat(tabSheet1.getContent().getParent()).isEmpty();
        assertThat(tabSheet1.getTab().getParent()).isEmpty();
        assertThat(tabSheet2.getContent().getParent()).isEmpty();
        assertThat(tabSheet2.getTab().getParent()).isEmpty();
    }

    @Test
    void testRemoveAllTabSheets_DoesNotCreateContent() {
        var tabLayout = new LinkkiTabLayout();
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(Assertions::fail).build();
        tabLayout.addTabSheets(tabSheet1, tabSheet2);

        tabLayout.removeAllTabSheets();

        assertThat(tabLayout.getTabSheets()).isEmpty();
    }

    @Test
    void testGetTabSheet() {
        var tabLayout = new LinkkiTabLayout();
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();
        tabLayout.addTabSheets(tabSheet1, tabSheet2);

        assertThat(tabLayout.getTabSheet("id1")).hasValue(tabSheet1);
        assertThat(tabLayout.getTabSheet("id2")).hasValue(tabSheet2);
        assertThat(tabLayout.getTabSheet("id3")).isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Test
    void testAddSelectedTabChangeListener() {
        var tabLayout = new LinkkiTabLayout();
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1")).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2")).build();
        tabLayout.addTabSheets(tabSheet1, tabSheet2);
        ComponentEventListener<SelectedChangeEvent> listener = mock(ComponentEventListener.class);

        tabLayout.addSelectedChangeListener(listener);
        tabLayout.setSelectedTabSheet(tabSheet2.getId());

        verify(listener).onComponentEvent(any());
    }

    @Test
    void testNewSidebarLayout() {
        var sidebarLayout = LinkkiTabLayout.newSidebarLayout();

        assertThat(sidebarLayout.getElement().hasAttribute(LinkkiTabLayout.PROPERTY_ORIENTATION)).isTrue();
        assertThat(sidebarLayout.getElement().getAttribute(LinkkiTabLayout.PROPERTY_ORIENTATION)).isEqualTo("vertical");
        assertThat(sidebarLayout.getElement().getThemeList()).containsExactly(LinkkiTabLayout.THEME_VARIANT_SOLID);
    }

    @Test
    void testUpdateSheetVisibility() {
        var tabLayout = new LinkkiTabLayout();
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1"))
                .visibleWhen(() -> false).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2"))
                .visibleWhen(() -> false).build();
        var tabSheet3 = LinkkiTabSheet.builder("id3").content(() -> new Span("content3")).build();
        tabLayout.addTabSheet(tabSheet1);
        tabLayout.addTabSheet(tabSheet2);
        tabLayout.addTabSheet(tabSheet3);
        tabLayout.setSelectedTabSheet("id1");

        tabLayout.updateSheetVisibility();

        // first 2 tabs are invisible
        assertThat(tabLayout.getSelectedTabSheet()).isEqualTo(tabSheet3);
    }

    @Test
    void testUpdateSheetVisibility_NoTabVisible() {
        var tabLayout = new LinkkiTabLayout();
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1"))
                .visibleWhen(() -> false).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2"))
                .visibleWhen(() -> false).build();
        var tabSheet3 = LinkkiTabSheet.builder("id3").content(() -> new Span("content3"))
                .visibleWhen(() -> false).build();
        tabLayout.addTabSheet(tabSheet1);
        tabLayout.addTabSheet(tabSheet2);
        tabLayout.addTabSheet(tabSheet3);
        tabLayout.setSelectedTabSheet("id1");

        tabLayout.updateSheetVisibility();

        assertThat(tabLayout.getSelectedIndex()).isEqualTo(-1);
    }

    @Test
    void testInitialVisibility() {
        var tabLayout = new LinkkiTabLayout();
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1"))
                .visibleWhen(() -> false).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2"))
                .visibleWhen(() -> true).build();
        tabLayout.addTabSheet(tabSheet1);
        tabLayout.addTabSheet(tabSheet2);

        assertThat(tabLayout.getTabSheet("id1").get().getTab().isVisible()).isFalse();
    }

    @Test
    void testVisibilityWithText() {
        // LIN-2567 Text does not support isVisible
        var tabLayout = new LinkkiTabLayout();
        var sheet = LinkkiTabSheet.builder("id1")
                .content(() -> new Div(new Text("test")))
                .build();

        assertDoesNotThrow(() -> {
            tabLayout.addTabSheet(sheet);
            tabLayout.updateSheetVisibility();
        });
    }

    @Test
    void testAfterNavigation_CallsUpdateSheetVisibility() {
        var tabLayout = new LinkkiTabLayout();
        var tabSheet1 = LinkkiTabSheet.builder("id1").content(() -> new Span("content1"))
                .visibleWhen(() -> tabVisibility).build();
        var tabSheet2 = LinkkiTabSheet.builder("id2").content(() -> new Span("content2"))
                .visibleWhen(() -> true).build();
        tabLayout.addTabSheet(tabSheet1);
        tabLayout.addTabSheet(tabSheet2);
        tabVisibility = false;

        tabLayout.afterNavigation(mock(AfterNavigationEvent.class));

        assertThat(tabLayout.getTabSheet("id1").get().getTab().isVisible()).isFalse();
    }

}