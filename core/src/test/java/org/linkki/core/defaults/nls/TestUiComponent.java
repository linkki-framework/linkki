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

package org.linkki.core.defaults.nls;

import org.linkki.core.binding.Binding;
import org.linkki.core.binding.validation.message.MessageList;
import org.linkki.util.handler.Handler;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class TestUiComponent {

    @CheckForNull
    private String id;
    private boolean visible;
    private boolean enabled;
    @CheckForNull
    private String labelText;
    @CheckForNull
    private String tooltipText;
    @CheckForNull
    private MessageList validationMessages;

    // used to hold the reference
    @CheckForNull
    private Binding binding;

    private Handler clickHandler = Handler.NOP_HANDLER;

    public void click() {
        getClickHandler().apply();
    }

    @CheckForNull
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

    @CheckForNull
    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    @CheckForNull
    public String getTooltipText() {
        return tooltipText;
    }

    public void setTooltipText(String tooltipText) {
        this.tooltipText = tooltipText;
    }

    @CheckForNull
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

    public void setBinding(Binding binding) {
        this.binding = binding;
    }

}
