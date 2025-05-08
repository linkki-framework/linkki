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
package org.linkki.framework.ui.dialogs;

import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.util.handler.Handler;

/**
 * Defines a dialog that can be used with {@link UIOpenDialogButton}.
 * 
 * @see UIOpenDialogButton
 * @since 2.8.0
 */
public interface DialogPmo {

    /**
     * Returns the caption that is used to create the dialog.
     */
    String getCaption();

    /**
     * Returns the handler that is called when the Ok button is pressed
     */
    Handler getOkHandler();

    /**
     * Returns the validation messages for the dialog.
     */
    default MessageList validate() {
        return new MessageList();
    }

    /**
     * Returns the {@link PropertyDispatcherFactory} that is used in the dialog.
     */
    default PropertyDispatcherFactory getPropertyDispatcherFactory() {
        return new PropertyDispatcherFactory();
    }

    /**
     * Returns the PMO for the content of the dialog.
     */
    Object getContentPmo();
}
