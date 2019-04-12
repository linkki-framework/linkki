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

package org.linkki.core.defaults.section;

import java.util.Optional;

import org.linkki.core.binding.TestModelObject;
import org.linkki.core.binding.TestPmo;
import org.linkki.core.defaults.section.annotations.TestUIField;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.pmo.ButtonPmo;
import org.linkki.core.pmo.PresentationModelObject;
import org.linkki.core.pmo.SectionID;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public class TestSectionPmo extends TestPmo implements PresentationModelObject {

    private String id = "TestSection";
    @CheckForNull
    private ButtonPmo editButtonPmo;
    private boolean modelPropEnabled = true;

    public TestSectionPmo() {
        super(new TestModelObject());
    }

    @SectionID
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Optional<ButtonPmo> getEditButtonPmo() {
        return Optional.ofNullable(editButtonPmo);
    }

    public void setEditButtonPmo(@CheckForNull ButtonPmo editButtonPmo) {
        this.editButtonPmo = editButtonPmo;
    }

    @TestUIField(position = 10, label = "val", enabled = EnabledType.DYNAMIC)
    @BindTooltip(tooltipType = TooltipType.DYNAMIC)
    @Override
    public String getValue() {
        return super.getValue();
    }

    @TestUIField(position = 20, label = "model property", enabled = EnabledType.DYNAMIC, visible = VisibleType.DYNAMIC)
    public void modelProp() {
        // model binding
    }

    public boolean isModelPropEnabled() {
        return modelPropEnabled;
    }

    public void setModelPropEnabled(boolean modelPropEnabled) {
        this.modelPropEnabled = modelPropEnabled;
    }

}