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

package org.linkki.core.ui.section;

import java.util.Optional;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;

/**
 * This section implementation uses a {@link CustomLayout} to give the ability to specify the layout
 * using a custom HTML file.
 * <p>
 * The name of the HTML file is the simple name of your PMO class. It has to be located in your
 * theme folder in the subfolder layouts, for example
 * <code>src/main/webapp/VAADIN/themes/valo/layouts</code>. The HTML file should contain
 * <code>div</code> elements with the attribute <code>location</code> as placeholder for the labels
 * and components. Use the PMO property name as placeholder name for the component and the PMO
 * property name with the suffix <code>-label</code> as placeholder name for the label.
 * 
 * @see CustomLayout
 * 
 */
public class CustomLayoutSection extends BaseSection {

    private static final String LABEL_LOCATION = "-label";

    private static final long serialVersionUID = 1L;

    private CustomLayout customLayout;

    public CustomLayoutSection(String pmoClassName, String caption, boolean closeable, Optional<Button> editButton) {
        super(caption, closeable, editButton);
        customLayout = new CustomLayout(pmoClassName);
        addComponent(customLayout);
    }

    @Override
    public void add(String propertyName, Label label, Component component) {
        customLayout.addComponent(label, propertyName + LABEL_LOCATION);
        customLayout.addComponent(component, propertyName);
    }

}
