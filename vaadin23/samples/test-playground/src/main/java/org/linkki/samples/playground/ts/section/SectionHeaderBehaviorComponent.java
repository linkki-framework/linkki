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

package org.linkki.samples.playground.ts.section;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.vaadin.component.section.LinkkiSection;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;

public class SectionHeaderBehaviorComponent {

    private SectionHeaderBehaviorComponent() {
        throw new IllegalStateException("Utility class");
    }

    public static LinkkiSection createClosableSection() {
        LinkkiSection section = (LinkkiSection)VaadinUiCreator.createComponent(new ClosableSectionPmo(),
                                                                               new BindingContext());
        section.addHeaderButton(new Button("Button 1"));
        section.addHeaderComponent(new ComboBox<String>("Filter", "item1"));
        section.addHeaderButton(new Button("Button 2"));
        section.addHeaderComponent(new Checkbox("Check"));
        // expected order: button 2, button 1, combo box, check box, close toggle
        return section;
    }

    public static LinkkiSection createNotClosableSection() {
        LinkkiSection section = (LinkkiSection)VaadinUiCreator.createComponent(new NotClosableSectionPmo(),
                                                                               new BindingContext());
        return section;
    }

    public static LinkkiSection createSectionWithRightComponent() {
        LinkkiSection section = new LinkkiSection("Section with right aligned component", true, 1);
        section.addRightHeaderComponent(new Button("Right aligned"));
        return section;
    }

}
