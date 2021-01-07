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

import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.aspects.annotation.BindVisible;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.SectionHeader;
import org.linkki.core.ui.layout.annotation.UIFormSection;

@UIFormSection(caption = "@BindVisibleSectionPmo")
public class BindVisibleSectionPmo {

    private boolean visible = true;
    // TODO LIN-2125
    // private TestSubSectionPmo testSubSectionPmo = new TestSubSectionPmo();

    @SectionHeader
    @UIButton(position = 10, captionType = CaptionType.DYNAMIC)
    public void headerButton() {
        visible = !visible;
    }

    public String getHeaderButtonCaption() {
        return visible ? "hide" : "show";
    }

    // TextField, visible = VisibleType.INVISIBLE, wird aber durch isTextFieldVisible überschrieben!
    @BindVisible
    @UITextField(position = 20, label = "TextField", visible = VisibleType.INVISIBLE)
    public String getTextField() {
        return "visible = VisibleType.INVISIBLE, overriden by @BindVisible";
    }

    public boolean isTextFieldVisible() {
        return visible;
    }
    // TODO LIN-2125
    // ContainerPmo / UISection
    // @BindVisible
    // @UINestedComponent(position = 30, label = "")
    // public ContainerSectionTablePmo getContainerSectionTable() {
    // return new ContainerSectionTablePmo();
    // }
    //
    // public boolean isContainerSectionTableVisible() {
    // return visible;
    // }
    //
    // UIFormSection
    // @BindVisible
    // @UINestedComponent(position = 40, label = "")
    // public FormSectionPmo getFormSection() {
    // return new FormSectionPmo();
    // }
    //
    // public boolean isFormSectionVisible() {
    // return visible;
    // }
    //
    // UISection / NestedComponent
    // @BindVisible
    // @UINestedComponent(position = 50, label = "")
    // public TestSubSectionPmo getTestSubSection() {
    // return testSubSectionPmo;
    // }
    //
    // public boolean isTestSubSectionVisible() {
    // return visible;
    // }
    //
    // @UISection(caption = "@UISection")
    // public class TestSubSectionPmo {
    //
    // @UILabel(position = 10, label = "")
    // public String getLabel() {
    // return "This is another label in a sub section";
    // }
    //
    // }
    //
    // @UISection(caption = "@UISection :: ContainerPmo")
    // public class ContainerSectionTablePmo implements
    // ContainerPmo<ContainerSectionTablePmo.SectionRowPmo> {
    //
    // @Override
    // public List<ContainerSectionTablePmo.SectionRowPmo> getItems() {
    // return Collections.singletonList(new ContainerSectionTablePmo.SectionRowPmo());
    // }
    //
    // @Override
    // public int getPageLength() {
    // return 1;
    // }
    //
    // class SectionRowPmo {
    // @UILabel(position = 0)
    // public String getValue() {
    // return "foo";
    // }
    // }
    // }

}
