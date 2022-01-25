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

package org.linkki.samples.playground.pageobjects;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.html.testbench.H4Element;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

/**
 * Page object for a linkki section
 */
@Element("linkki-section")
public class LinkkiSectionElement extends TestBenchElement {

    public String getCaption() {
        return $(H4Element.class).first().getText();
    }

    public DivElement getContent() {
        return $(DivElement.class).attribute("slot", "content").first();
    }

    public ElementQuery<ButtonElement> getCloseToggle() {
        return $(ButtonElement.class).attribute("slot", "close-toggle");
    }

    public ElementQuery<TestBenchElement> getHeaderComponents() {
        return getHeaderComponents(TestBenchElement.class);
    }

    public <T extends TestBenchElement> ElementQuery<T> getHeaderComponents(Class<T> clazz) {
        return $(clazz).attribute("slot", "header-components");
    }
}