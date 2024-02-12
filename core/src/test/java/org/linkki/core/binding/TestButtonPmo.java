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

package org.linkki.core.binding;

import java.util.Collections;
import java.util.List;

import org.linkki.core.pmo.ButtonPmo;

public class TestButtonPmo implements ButtonPmo {

    private boolean visible = true;

    private boolean enabled = true;

    private int clickCount;

    @Override
    public void onClick() {
        clickCount++;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public List<String> getStyleNames() {
        return Collections.emptyList();
    }

    @Override
    public Object getButtonIcon() {
        throw new UnsupportedOperationException();
    }

    public int getClickCount() {
        return clickCount;
    }

}
