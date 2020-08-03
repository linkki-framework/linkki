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

package org.linkki.core.ui.creation.table;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.linkki.core.ui.creation.table.container.LinkkiInMemoryContainer;
import org.linkki.core.ui.table.hierarchy.SimpleRowPmo;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("deprecation")
public class TableComponentWrapperTest {

    private com.vaadin.v7.ui.TreeTable table;
    private TableComponentWrapper<SimpleRowPmo> wrapper;

    @Mock
    private com.vaadin.v7.data.Container.ItemSetChangeListener listener;

    @BeforeEach
    public void setUp() {
        table = new com.vaadin.v7.ui.TreeTable("table");
        wrapper = new TableComponentWrapper<SimpleRowPmo>("id", table);
        ((LinkkiInMemoryContainer<?>)table.getContainerDataSource()).addListener(listener);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testListenerTriggered_NewItem(boolean collapsed) {
        SimpleRowPmo item = SimpleRowPmo.create("item");
        table.setCollapsed(item, collapsed);

        wrapper.setItems(Arrays.asList(item));

        verify(listener, only()).containerItemSetChange(any());
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testListenerTriggered_SameItem(boolean collapsed) {
        SimpleRowPmo item = SimpleRowPmo.create("item");
        table.setCollapsed(item, collapsed);
        wrapper.setItems(Arrays.asList(item));
        clearInvocations(listener);

        wrapper.setItems(Arrays.asList(item));

        verify(listener, never()).containerItemSetChange(any());
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testListenerTriggered_DifferentItem(boolean collapsed) {
        SimpleRowPmo item1 = SimpleRowPmo.create("item1");
        table.setCollapsed(item1, collapsed);
        wrapper.setItems(Arrays.asList(item1));
        SimpleRowPmo item2 = SimpleRowPmo.create("item2");
        clearInvocations(listener);

        wrapper.setItems(Arrays.asList(item2));

        verify(listener, only()).containerItemSetChange(any());
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testListenerTriggered_ItemRemoved(boolean collapsed) {
        SimpleRowPmo item = SimpleRowPmo.create("item1");
        table.setCollapsed(item, collapsed);
        wrapper.setItems(Arrays.asList(item));
        clearInvocations(listener);

        wrapper.setItems(Collections.emptyList());

        verify(listener, only()).containerItemSetChange(any());
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testListenerTriggered_NewChild(boolean collapsed) {
        SimpleRowPmo item = SimpleRowPmo.create("item");
        table.setCollapsed(item, collapsed);
        wrapper.setItems(Arrays.asList(item));
        item.createChild("child");
        clearInvocations(listener);

        wrapper.setItems(Arrays.asList(item));

        verify(listener, only()).containerItemSetChange(any());
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testListenerTriggered_SameChild(boolean collapsed) {
        SimpleRowPmo item = SimpleRowPmo.create("item");
        table.setCollapsed(item, collapsed);
        item.createChild("child");
        wrapper.setItems(Arrays.asList(item));
        clearInvocations(listener);

        wrapper.setItems(Arrays.asList(item));

        verify(listener, never()).containerItemSetChange(any());
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testListenerTriggered_DifferentChild(boolean collapsed) {
        SimpleRowPmo item = SimpleRowPmo.create("item");
        table.setCollapsed(item, collapsed);
        item.createChild("child");
        wrapper.setItems(Arrays.asList(item));
        item.removeChild("child");
        item.createChild("new child");
        clearInvocations(listener);

        wrapper.setItems(Arrays.asList(item));

        verify(listener, only()).containerItemSetChange(any());
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testListenerTriggered_ChildRemoved(boolean collapsed) {
        SimpleRowPmo item = SimpleRowPmo.create("item");
        table.setCollapsed(item, collapsed);
        item.createChild("child");
        wrapper.setItems(Arrays.asList(item));
        item.removeChild("child");
        clearInvocations(listener);

        wrapper.setItems(Arrays.asList(item));

        verify(listener, only()).containerItemSetChange(any());
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testListenerTriggered_NewSubChild(boolean collapsed) {
        SimpleRowPmo item = SimpleRowPmo.create("item");
        table.setCollapsed(item, collapsed);
        SimpleRowPmo child = item.createChild("child");
        wrapper.setItems(Arrays.asList(item));
        child.createChild("subchild");
        clearInvocations(listener);

        wrapper.setItems(Arrays.asList(item));

        if (collapsed) {
            // child is not visible, so we do not care about its added child
            verify(listener, never()).containerItemSetChange(any());
        } else {
            // child is visible, so an update has to be made for the expand button to be visible
            verify(listener, only()).containerItemSetChange(any());
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testListenerTriggered_SameSubChild(boolean collapsed) {
        SimpleRowPmo item = SimpleRowPmo.create("item");
        table.setCollapsed(item, collapsed);
        SimpleRowPmo child = item.createChild("child");
        child.createChild("subchild");
        wrapper.setItems(Arrays.asList(item));
        clearInvocations(listener);

        wrapper.setItems(Arrays.asList(item));

        verify(listener, never()).containerItemSetChange(any());
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testListenerTriggered_DifferentSubChild_CollapsedChild(boolean collapsed) {
        SimpleRowPmo item = SimpleRowPmo.create("item");
        table.setCollapsed(item, collapsed);
        SimpleRowPmo child = item.createChild("child");
        child.createChild("subchild");
        wrapper.setItems(Arrays.asList(item));
        child.removeChild("subchild");
        child.createChild("new subchild");
        clearInvocations(listener);

        wrapper.setItems(Arrays.asList(item));

        if (collapsed) {
            verify(listener, never()).containerItemSetChange(any());
        } else {
            // would theoretically be OK to not update from a UI perspective
            verify(listener, only()).containerItemSetChange(any());
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testListenerTriggered_DifferentSubChild_ExpandedChild(boolean collapsed) {
        SimpleRowPmo item = SimpleRowPmo.create("item");
        table.setCollapsed(item, collapsed);
        SimpleRowPmo child = item.createChild("child");
        table.setCollapsed(child, false);
        child.createChild("subchild");
        wrapper.setItems(Arrays.asList(item));
        child.removeChild("subchild");
        child.createChild("new subchild");
        clearInvocations(listener);

        wrapper.setItems(Arrays.asList(item));

        if (collapsed) {
            verify(listener, never()).containerItemSetChange(any());
        } else {
            verify(listener, only()).containerItemSetChange(any());
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testListenerTriggered_SubChildDeleted_CollapsedChild(boolean collapsed) {
        SimpleRowPmo item = SimpleRowPmo.create("item");
        table.setCollapsed(item, collapsed);
        SimpleRowPmo child = item.createChild("child");
        child.createChild("subchild");
        wrapper.setItems(Arrays.asList(item));
        child.removeChild("subchild");
        clearInvocations(listener);

        wrapper.setItems(Arrays.asList(item));

        if (collapsed) {
            // child is not visible, so we do not care about its removed child
            verify(listener, never()).containerItemSetChange(any());
        } else {
            // child is visible, so an update has to be made for the expand button to be removed
            verify(listener, only()).containerItemSetChange(any());
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testListenerTriggered_SubChildDeleted_ExpandedChild(boolean collapsed) {
        SimpleRowPmo item = SimpleRowPmo.create("item");
        table.setCollapsed(item, collapsed);
        SimpleRowPmo child = item.createChild("child");
        table.setCollapsed(child, false);
        child.createChild("subchild");
        wrapper.setItems(Arrays.asList(item));
        child.removeChild("subchild");
        clearInvocations(listener);

        wrapper.setItems(Arrays.asList(item));

        if (collapsed) {
            // child is not visible, so we do not care about its removed child
            verify(listener, never()).containerItemSetChange(any());
        } else {
            // child is visible, so an update has to be made for the expand button to be removed
            verify(listener, only()).containerItemSetChange(any());
        }
    }

}
