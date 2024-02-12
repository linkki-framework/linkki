/*
 * Copyright Faktor Zehn GmbH.
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
package org.linkki.samples.playground.ts.nestedcomponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.ui.aspects.annotation.BindVisible;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UIVerticalLayout;
import org.linkki.core.ui.nested.annotation.UINestedComponent;
import org.linkki.framework.ui.notifications.NotificationUtil;
import org.linkki.samples.playground.table.PlaygroundTablePmo;
import org.linkki.samples.playground.table.TableModelObject;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorModelObject;
import org.linkki.samples.playground.ts.layouts.BasicElementsLayoutBehaviorUiSectionPmo;

@UIVerticalLayout
public class NullableModelObjectInInvisibleNestedPmo {

    private boolean nestedComponentVisible;
    private BasicElementsLayoutBehaviorModelObject modelObject;
    private final List<TableModelObject> tableModelObjects;

    public NullableModelObjectInInvisibleNestedPmo() {
        this.modelObject = null;
        this.tableModelObjects = new ArrayList<>();
        tableModelObjects.add(new TableModelObject(0));
        tableModelObjects.add(null);
        nestedComponentVisible = true;
    }

    @UILabel(position = 5)
    public String getModelObjectLabel() {
        return Optional.ofNullable(modelObject)
                .map(o -> "Model object present")
                .orElse("No model object");
    }

    @UIButton(position = 10, captionType = CaptionType.DYNAMIC)
    public void toggleModelObject() {
        if (modelObject != null) {
            modelObject = null;
        } else {
            modelObject = new BasicElementsLayoutBehaviorModelObject();
        }

        if (!tableModelObjects.contains(null) && tableModelObjects.size() > 1) {
            tableModelObjects.remove(1);
            tableModelObjects.add(null);
        } else {
            tableModelObjects.remove(null);
            tableModelObjects.add(new TableModelObject(1));
        }
    }

    public String getToggleModelObjectCaption() {
        return Optional.ofNullable(modelObject).map(o -> "Set model object to null")
                .orElse("Create new model object");
    }

    @UICheckBox(position = 20, caption = "Show nested component")
    public boolean isNestedComponentVisible() {
        return nestedComponentVisible;
    }

    public void setNestedComponentVisible(boolean nestedComponentVisible) {
        this.nestedComponentVisible = nestedComponentVisible;
    }

    @BindVisible
    @UINestedComponent(position = 50)
    public BasicElementsLayoutBehaviorUiSectionPmo getNestedPmoWithNullableModelObject() {
        return new BasicElementsLayoutBehaviorUiSectionPmo(() -> modelObject);
    }

    public boolean isNestedPmoWithNullableModelObjectVisible() {
        return isNestedComponentVisible();
    }

    @BindVisible
    @UINestedComponent(position = 60)
    public PlaygroundTablePmo getNestedTable() {
        return new PlaygroundTablePmo(() -> tableModelObjects,
                () -> NotificationUtil.showInfo("Adding not implemented here", ""),
                tableModelObjects::remove);
    }

    public boolean isNestedTableVisible() {
        return isNestedComponentVisible();
    }
}
