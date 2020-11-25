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

package org.linkki.samples.playground.table;

import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.pmo.ModelObject;
import org.linkki.core.ui.aspects.annotation.BindReadOnly;
import org.linkki.core.ui.aspects.annotation.BindReadOnly.ReadOnlyType;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UILink;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.samples.playground.allelements.AllUiElementsTabsheetArea;
import org.linkki.samples.playground.nls.NlsText;
import org.linkki.samples.playground.ui.PlaygroundView;
import org.linkki.util.handler.Handler;

import com.vaadin.icons.VaadinIcons;

public class PlaygroundRowPmo {

    private final TableModelObject modelObject;

    private final Handler delete;

    public PlaygroundRowPmo(TableModelObject modelObject, Handler delete) {
        this.modelObject = modelObject;
        this.delete = delete;
    }

    @ModelObject
    public TableModelObject getModelObject() {
        return modelObject;
    }

    @UICheckBox(position = 10, modelAttribute = TableModelObject.PROPERTY_ACTIVE, caption = "", label = "Editable")
    public void active() {
        // model binding
    }

    @UILabel(position = 20, modelAttribute = TableModelObject.PROPERTY_INDEX, label = "#")
    public void index() {
        // model binding
    }

    @UITextField(position = 30, modelAttribute = TableModelObject.PROPERTY_NAME)
    @BindReadOnly(ReadOnlyType.DYNAMIC)
    public void name() {
        // model binding
    }

    public boolean isNameReadOnly() {
        return !getModelObject().isActive();
    }

    @UILabel(position = 35, label = "Placeholder to click")
    public String getText() {
        return "Simple label to select the row";
    }


    @UIComboBox(position = 40, modelAttribute = TableModelObject.PROPERTY_OPTION)
    @BindReadOnly(ReadOnlyType.DYNAMIC)
    public void option() {
        // model binding
    }

    public boolean isOptionReadOnly() {
        return !getModelObject().isActive();
    }

    @UIDateField(position = 50, modelAttribute = TableModelObject.PROPERTY_DATE)
    @BindReadOnly(ReadOnlyType.DYNAMIC)
    public void date() {
        // model binding
    }

    public boolean isDateReadOnly() {
        return !getModelObject().isActive();
    }

    @UIButton(position = 60, showIcon = true, icon = VaadinIcons.TRASH, caption = "", label = "Del")
    public void delete() {
        delete.apply();
    }

    @UILink(position = 140, label = NlsText.I18n, caption = "A UI Link", captionType = CaptionType.STATIC)
    public String getLink() {
        return "main#!/" + PlaygroundView.PARAM_SHEET + "=" + AllUiElementsTabsheetArea.ID;
    }

}