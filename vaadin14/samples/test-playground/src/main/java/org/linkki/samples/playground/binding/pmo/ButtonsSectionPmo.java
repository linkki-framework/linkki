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
package org.linkki.samples.playground.binding.pmo;

import java.util.function.BooleanSupplier;

import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.core.vaadin.component.KeyCode;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;

@UIHorizontalLayout
public class ButtonsSectionPmo {

    private BooleanSupplier canSaveSupplier;
    private Handler saveAction;
    private Handler resetAction;

    public ButtonsSectionPmo(BooleanSupplier canSavePredicate, Handler saveAction, Handler resetAction) {
        this.canSaveSupplier = canSavePredicate;
        this.saveAction = saveAction;
        this.resetAction = resetAction;
    }

    @UIButton(position = 10, showIcon = true, icon = VaadinIcon.CHECK, //
            captionType = CaptionType.NONE, enabled = EnabledType.DYNAMIC, shortcutKeyCode = KeyCode.ENTER, variants = ButtonVariant.LUMO_PRIMARY)
    public void save() {
        saveAction.apply();
    }

    public boolean isSaveEnabled() {
        return canSaveSupplier.getAsBoolean();
    }

    @UIButton(position = 20, captionType = CaptionType.STATIC, caption = "reset", variants = ButtonVariant.LUMO_TERTIARY)
    public void reset() {
        resetAction.apply();
    }
}