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

package org.linkki.samples.playground.ui;

import org.linkki.framework.ui.component.sidebar.SidebarSheet;

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;

public interface SidebarSheetDefinition extends Component {

    String getSidebarSheetName();

    Resource getSidebarSheetIcon();

    String getSidebarSheetId();

    default SidebarSheet createSheet() {
        SidebarSheet sidebarSheet = new SidebarSheet(getSidebarSheetIcon(), getSidebarSheetName(), this);
        sidebarSheet.getButton().setId(getSidebarSheetId());
        return sidebarSheet;
    }
}
