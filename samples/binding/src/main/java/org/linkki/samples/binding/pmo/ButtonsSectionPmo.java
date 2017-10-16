/*
 * Copyright Faktor Zehn AG.
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
package org.linkki.samples.binding.pmo;

import java.util.function.BooleanSupplier;

import org.linkki.core.ui.section.annotations.CaptionType;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.SectionLayout;
import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.util.handler.Handler;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.themes.BaseTheme;

@UISection(layout = SectionLayout.HORIZONTAL)
public class ButtonsSectionPmo {

    private BooleanSupplier canSaveSupplier;
    private Handler saveAction;
    private Handler resetAction;

    public ButtonsSectionPmo(BooleanSupplier canSavePredicate, Handler saveAction, Handler resetAction) {
        this.canSaveSupplier = canSavePredicate;
        this.saveAction = saveAction;
        this.resetAction = resetAction;
    }

    // tag::button[]
    @UIButton(position = 10, showIcon = true, icon = FontAwesome.SAVE, captionType = CaptionType.NONE, shortcutKeyCode = KeyCode.ENTER, enabled = EnabledType.DYNAMIC)
    public void save() {
        saveAction.apply();
    }

    public boolean isSaveEnabled() {
        return canSaveSupplier.getAsBoolean();
    }

    @UIButton(position = 20, captionType = CaptionType.STATIC, caption = "reset", styleNames = BaseTheme.BUTTON_LINK)
    public void reset() {
        resetAction.apply();
    }
    // end::button[]
}
