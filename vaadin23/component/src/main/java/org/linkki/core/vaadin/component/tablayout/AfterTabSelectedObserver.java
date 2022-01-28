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

import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet.TabSheetSelectionChangeEvent;

import com.vaadin.flow.component.ComponentEventListener;

/**
 * Components implementing this interface will receive a `TabSheetSelectionChangeEvent` when the
 * `LinkkiTabSheet` they are attached to is selected.
 * <p>
 * The event is only propagated to components which are attached and visible at the time the selection
 * listeners are triggered. That means components that are added or become visible within this observer
 * will not receive the current event, even if they also implement this interface.
 * <p>
 * This event is fired after other selection change listeners added by
 * {@link LinkkiTabSheet#addTabSelectionChangeListener(ComponentEventListener)}.
 */
@FunctionalInterface
public interface AfterTabSelectedObserver {

    /**
     * Callback executed after the {@link LinkkiTabSheet} was selected.
     *
     * @param event {@link TabSheetSelectionChangeEvent} with details of the selection event
     */
    void afterTabSelected(TabSheetSelectionChangeEvent event);

}
