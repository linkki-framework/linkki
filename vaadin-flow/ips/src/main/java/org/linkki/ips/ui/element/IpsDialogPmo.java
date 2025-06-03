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
package org.linkki.ips.ui.element;

import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.framework.ui.dialogs.DialogPmo;
import org.linkki.ips.binding.dispatcher.IpsPropertyDispatcherFactory;
import org.linkki.ips.messages.MessageConverter;

/**
 * Partial implementation of {@link DialogPmo}, adapted for use with a Faktor-IPS model object.
 */
public abstract class IpsDialogPmo implements DialogPmo {

    @Override
    public MessageList validate() {
        return MessageConverter.convert(getIpsMessages());
    }

    @Override
    public PropertyDispatcherFactory getPropertyDispatcherFactory() {
        return new IpsPropertyDispatcherFactory();
    }

    /**
     * Returns the Faktor-IPS validation messages for the dialog.
     */
    public abstract org.faktorips.runtime.MessageList getIpsMessages();
}
