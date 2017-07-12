/*******************************************************************************
 * Copyright (c) 2014 Faktor Zehn AG.
 * 
 * Alle Rechte vorbehalten.
 *******************************************************************************/

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
