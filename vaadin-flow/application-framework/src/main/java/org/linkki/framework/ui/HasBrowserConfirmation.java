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

package org.linkki.framework.ui;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;

/**
 * Enable or disable the default browser confirm dialog. This is used when a view is leaving through
 * navigation or refresh to show the confirm dialog.
 * 
 * @since 2.8.0
 */
@JsModule(value = "./src/confirm.js")
public interface HasBrowserConfirmation extends HasElement, BeforeLeaveObserver {

    /**
     * This method is used to enable the browser confirmation dialog when refresh or navigate.
     */
    default void enableBrowserConfirmation() {
        UI.getCurrent().getPage().executeJs("window.enableOnbeforeunloadConfirmation()");
    }

    /**
     * This method is used to disable the browser confirmation dialog when refresh or navigate.
     */
    default void disableBrowserConfirmation() {
        UI.getCurrent().getPage().executeJs("window.disableOnbeforeunloadConfirmation()");
    }

    @Override
    default void beforeLeave(BeforeLeaveEvent event) {
        disableBrowserConfirmation();
    }

}
