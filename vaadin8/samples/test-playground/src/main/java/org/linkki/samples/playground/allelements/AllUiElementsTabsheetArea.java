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

package org.linkki.samples.playground.allelements;

import org.linkki.core.defaults.style.LinkkiTheme;
import org.linkki.core.vaadin.component.area.TabSheetArea;
import org.linkki.samples.playground.ui.SidebarSheetDefinition;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;

public class AllUiElementsTabsheetArea extends TabSheetArea implements SidebarSheetDefinition {

    private static final long serialVersionUID = 1L;
    public static final String ID = "all";

    public AllUiElementsTabsheetArea() {
        init();
    }

    @Override
    public void createContent() {
        addTab(new AllUiElementsPage(), "default");

        AllUiElementsPage fixedWidthPage = new AllUiElementsPage();
        fixedWidthPage.addStyleName(LinkkiTheme.LABEL_FIXED_WIDTH);
        addTab(fixedWidthPage, "fixed width");

        AllUiElementsPage longWidthPage = new AllUiElementsPage();
        longWidthPage.addStyleName(LinkkiTheme.LABEL_FIXED_WIDTH_LONG);
        addTab(longWidthPage, "long width");
    }

    @Override
    public void updateContent() {
        reloadBindings();
    }

    @Override
    public String getSidebarSheetName() {
        return "All @UI Elements";
    }

    @Override
    public Resource getSidebarSheetIcon() {
        return VaadinIcons.LIST;
    }

    @Override
    public String getSidebarSheetId() {
        return ID;
    }
}
