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

package org.linkki.core.ui;

import org.linkki.core.message.MessageList;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.core.ui.components.WrapperType;

public class TestComponentWrapper implements ComponentWrapper {

    private static final long serialVersionUID = 1L;

    private final TestUiComponent component;

    public TestComponentWrapper(TestUiComponent component) {
        this.component = component;
    }

    @Override
    public void setId(String id) {
        component.setId(id);
    }

    @Override
    public void setLabel(String labelText) {
        component.setLabelText(labelText);
    }

    @Override
    public void setEnabled(boolean enabled) {
        component.setEnabled(enabled);
    }

    @Override
    public void setVisible(boolean visible) {
        component.setVisible(visible);
    }

    @Override
    public void setTooltip(String text) {
        component.setTooltipText(text);
    }

    @Override
    public TestUiComponent getComponent() {
        return component;
    }

    @Override
    public WrapperType getType() {
        return WrapperType.COMPONENT;
    }

    @Override
    public void setValidationMessages(MessageList messagesForProperty) {
        component.setValidationMessages(messagesForProperty);
    }

}