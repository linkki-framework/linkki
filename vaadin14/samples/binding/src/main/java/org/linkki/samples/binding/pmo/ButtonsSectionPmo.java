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
package org.linkki.samples.binding.pmo;

import java.util.function.BooleanSupplier;

import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.icon.VaadinIcon;

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

    // TODO LIN-2142
    // TODO LIN-2050
    @UIButton(position = 10, showIcon = true, icon = VaadinIcon.CHECK_SQUARE_O, //
            captionType = CaptionType.NONE, enabled = EnabledType.DYNAMIC // ,
    /* shortcutKeyCode = KeyCode.ENTER, styleNames = ValoTheme.BUTTON_PRIMARY */)
    public void save() {
        saveAction.apply();
    }

    public boolean isSaveEnabled() {
        return canSaveSupplier.getAsBoolean();
    }

    // TODO LIN-2142
    @UIButton(position = 20, captionType = CaptionType.STATIC, caption = "reset"/*
                                                                                 * , styleNames =
                                                                                 * ValoTheme.BUTTON_LINK
                                                                                 */)
    public void reset() {
        resetAction.apply();
    }
}