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

package org.linkki.samples.playground.ui.dialogs;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.ui.wrapper.CaptionComponentWrapper;
import org.linkki.core.uicreation.UiCreator;
import org.linkki.samples.playground.ui.SidebarSheetDefinition;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

public class DialogsLayout extends HorizontalLayout implements SidebarSheetDefinition {
    private static final long serialVersionUID = 1L;

    public static final String ID = "dialogs-layout";

    public DialogsLayout() {
        super();
        setMargin(true);
        UiCreator.createUiElements(new DialogButtonsPmo(), new BindingContext(getClass().getName()),
                                   c -> new CaptionComponentWrapper((Component)c, WrapperType.FIELD))
                .map(CaptionComponentWrapper::getComponent)
                .forEach(this::addComponent);
    }

    @Override
    public String getSidebarSheetName() {
        return ID;
    }

    @Override
    public Resource getSidebarSheetIcon() {
        return VaadinIcons.MODAL;
    }

    @Override
    public String getSidebarSheetId() {
        return ID;
    }
}
