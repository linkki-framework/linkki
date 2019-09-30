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

package org.linkki.framework.ui.component.sidebar;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class SidebarSheetTest {

    private boolean triggered = false;

    @Test
    public void testUnselect_checkVisibilitySetToFalse() {
        HorizontalLayout content = new HorizontalLayout();
        SidebarSheet sheet = createSideBarSheet(content);

        sheet.unselect();

        assertThat(content.isVisible(), is(false));
    }

    @Test
    public void testSelect_checkVisibilitySetToTrue() {
        HorizontalLayout content = new HorizontalLayout();
        SidebarSheet sheet = createSideBarSheet(content);

        sheet.select();

        assertThat(content.isVisible(), is(true));
    }

    @Test
    public void testSelect_CheckObserver() {
        HorizontalLayout content = new HorizontalLayout();

        SidebarSheet sidebarSheet = new SidebarSheet(FontAwesome.STAR_HALF_FULL, "Test SidebarSheet", content,
                () -> triggered = true);

        sidebarSheet.select();

        assertThat(triggered, is(true));
    }

    @Test
    public void testToString() {
        SidebarSheet sheet = new SidebarSheet(FontAwesome.ADJUST, "Foo", new TextField());
        assertThat(sheet.toString(), is("Foo"));
    }

    private static SidebarSheet createSideBarSheet(Component content) {
        return new SidebarSheet(FontAwesome.STAR_HALF_FULL, "Test SidebarSheet", content);
    }

}
