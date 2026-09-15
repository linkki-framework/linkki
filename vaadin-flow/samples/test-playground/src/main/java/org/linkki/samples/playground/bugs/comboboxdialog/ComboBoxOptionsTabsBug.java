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

package org.linkki.samples.playground.bugs.comboboxdialog;

import java.io.Serial;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

public class ComboBoxOptionsTabsBug extends VerticalLayout {

    public static final String ID = "LIN-5053";
    public static final String CAPTION = ID + " :: ComboBox.getOptions and clicking afterwards";
    public static final String TAB_COMBOBOX = "comboBoxTab";
    public static final String TAB_BUTTON = "buttonTab";
    public static final String PROPERTY_COMBOBOX = "combobox";
    public static final String PROPERTY_BUTTON = "button";

    @Serial
    private static final long serialVersionUID = 1L;

    public ComboBoxOptionsTabsBug() {
        var comboBox = new ComboBox<String>("choice");
        comboBox.setId(PROPERTY_COMBOBOX);
        comboBox.setItems("Option 1", "Option 2", "Option 3");
        comboBox.setValue("Option 1");

        var button = new Button("Do nothing", e -> {
        });
        button.setId(PROPERTY_BUTTON);
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        var comboBoxTab = new Tab("ComboBox");
        comboBoxTab.setId(TAB_COMBOBOX);
        var buttonTab = new Tab("Button");
        buttonTab.setId(TAB_BUTTON);

        var tabs = new Tabs(comboBoxTab, buttonTab);

        var comboBoxContent = new VerticalLayout(comboBox);
        var buttonContent = new VerticalLayout(button);
        buttonContent.setVisible(false);

        tabs.addSelectedChangeListener(event -> {
            comboBoxContent.setVisible(tabs.getSelectedTab() == comboBoxTab);
            buttonContent.setVisible(tabs.getSelectedTab() == buttonTab);
        });

        add(new H4(CAPTION), tabs, comboBoxContent, buttonContent);
    }
}
