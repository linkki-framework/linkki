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

import com.vaadin.flow.component.html.Div;

public class AllUiElementsTabsheetArea extends Div {

    private static final long serialVersionUID = 1L;
    public static final String ID = "all";
    private boolean readOnlyMode;

    public AllUiElementsTabsheetArea(boolean readOnlyMode) {
        this.readOnlyMode = readOnlyMode;
        add(new AllUiElementsPage(() -> readOnlyMode));
        // init();
        // }
        //
        // @Override
        // public void createContent() {
        // Tab defaultTab = new Tab("default");
        // defaultTab.add(new AllUiElementsPage(() -> readOnlyMode));
        // add(defaultTab);
        //
        // AllUiElementsPage fixedWidthPage = new AllUiElementsPage(() -> readOnlyMode);
        // fixedWidthPage.addClassName(LinkkiTheme.LABEL_FIXED_WIDTH);
        // Tab fixedWidthTab = new Tab("fixed width");
        // fixedWidthTab.add(fixedWidthPage);
        // add(fixedWidthTab);
        //
        // AllUiElementsPage longWidthPage = new AllUiElementsPage(() -> readOnlyMode);
        // longWidthPage.addClassName(LinkkiTheme.LABEL_FIXED_WIDTH_LONG);
        // Tab longWidthTab = new Tab("long width");
        // longWidthTab.add(fixedWidthPage);
        // add(longWidthTab);
    }
    //
    // @Override
    // public void updateContent() {
    // reloadBindings();
    // }

}
