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

import java.util.List;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.H4Element;
import com.vaadin.flow.component.orderedlayout.testbench.HorizontalLayoutElement;
import com.vaadin.testbench.annotations.Attribute;

/**
 * Page object for a linkki section caption
 */
@Attribute(name = "class", contains = "linkki-section-caption")
public class LinkkiSectionCaptionElement extends HorizontalLayoutElement {

    public H4Element getTitle() {
        return $(H4Element.class).first();
    }

    public List<ButtonElement> getHeaderButtons() {
        return $(ButtonElement.class).all();
    }

    public ButtonElement getHeaderButton(String id) {
        return $(ButtonElement.class).id(id);
    }

}