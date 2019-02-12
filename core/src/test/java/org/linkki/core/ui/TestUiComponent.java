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

import org.eclipse.jdt.annotation.Nullable;
import org.linkki.core.message.MessageList;
import org.linkki.util.handler.Handler;

public class TestUiComponent {

    @Nullable
    private String id;
    private boolean visible;
    private boolean enabled;
    @Nullable
    private String labelText;
    @Nullable
    private String tooltipText;
    @Nullable
    private MessageList validationMessages;

    private Handler clickHandler = Handler.NOP_HANDLER;

    public void click() {
        getClickHandler().apply();
    }

    @Nullable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Nullable
    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    @Nullable
    public String getTooltipText() {
        return tooltipText;
    }

    public void setTooltipText(String tooltipText) {
        this.tooltipText = tooltipText;
    }

    @Nullable
    public MessageList getValidationMessages() {
        return validationMessages;
    }

    public void setValidationMessages(MessageList validationMessages) {
        this.validationMessages = validationMessages;
    }

    public Handler getClickHandler() {
        return clickHandler;
    }

    public void setClickHandler(Handler clickHandler) {
        this.clickHandler = clickHandler;
    }

}
