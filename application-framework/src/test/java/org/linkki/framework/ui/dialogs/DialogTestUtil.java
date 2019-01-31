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

package org.linkki.framework.ui.dialogs;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;

import org.linkki.util.StreamUtil;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class DialogTestUtil {

    private DialogTestUtil() {
        // prevents instantiation
    }

    public static void clickOkButton(OkCancelDialog dialog) {
        getOkButton(dialog).click();
    }

    public static void clickCancelButton(OkCancelDialog dialog) {
        getButtons(dialog).get(1).click();
    }

    public static Button getOkButton(OkCancelDialog dialog) {
        return getButtons(dialog).get(0);
    }

    public static List<Button> getButtons(OkCancelDialog dialog) {
        VerticalLayout dialogLayout = (VerticalLayout)requireNonNull(dialog.getContent());
        HorizontalLayout buttonBar = (HorizontalLayout)dialogLayout
                .getComponent(dialogLayout.getComponentCount() - 1);
        return StreamUtil.stream(buttonBar).map(Button.class::cast).collect(Collectors.toList());
    }

    public static List<Component> getContents(OkCancelDialog dialog) {
        VerticalLayout dialogLayout = (VerticalLayout)requireNonNull(dialog.getContent());
        VerticalLayout contentArea = (VerticalLayout)dialogLayout.getComponent(0);
        VerticalLayout mainArea = (VerticalLayout)contentArea.getComponent(0);
        return StreamUtil.stream(mainArea).collect(Collectors.toList());
    }

    public static Component getFirstContentComponent(OkCancelDialog dialog) {
        return getContents(dialog).get(0);
    }
}
