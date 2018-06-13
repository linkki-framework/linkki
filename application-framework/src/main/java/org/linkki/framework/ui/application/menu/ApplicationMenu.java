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
package org.linkki.framework.ui.application.menu;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.linkki.core.ui.application.ApplicationStyles;
import org.linkki.framework.ui.LinkkiStyles;
import org.linkki.framework.ui.application.ApplicationHeader;

import com.vaadin.ui.MenuBar;

/**
 * A menu displayed on the left of the {@link ApplicationHeader}.
 */
public class ApplicationMenu extends MenuBar {

    private static final long serialVersionUID = 1L;

    public ApplicationMenu(ApplicationMenuItemDefinition... itemDefs) {
        this(Arrays.asList(itemDefs));
    }

    public ApplicationMenu(List<ApplicationMenuItemDefinition> itemDefs) {
        addStyleName(LinkkiStyles.APPLICATION_MENU);
        addStyleName(ApplicationStyles.BORDERLESS);
        setSizeUndefined();
        SortedSet<ApplicationMenuItemDefinition> sorted = new TreeSet<ApplicationMenuItemDefinition>();
        itemDefs.forEach(sorted::add);
        sorted.forEach(item -> item.createItem(this));
    }

}
