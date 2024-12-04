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

package org.linkki.testbench.pageobjects;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.vaadin.component.section.LinkkiSection;
import org.openqa.selenium.By;

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
        return $(DivElement.class).withAttribute("slot", "content").first();
    }

    /**
     * Returns all components in content. If the component is wrapped in a form item, the component
     * itself is returned.
     */
    public List<TestBenchElement> getContentComponents() {
        if (LinkkiSection.THEME_VARIANT_FORM.equals(getAttribute("theme"))) {
            return getContent().findElements(By.cssSelector("vaadin-form-item > :not(label)")).stream()
                    .map(TestBenchElement.class::cast).collect(Collectors.toList());
        } else {
            return getContent().findElements(By.cssSelector("*")).stream().map(TestBenchElement.class::cast)
                    .collect(Collectors.toList());
        }
    }

    public ElementQuery<ButtonElement> getCloseToggle() {
        return $(ButtonElement.class).withAttribute("slot", "close-toggle");
    }

    public ElementQuery<TestBenchElement> getHeaderComponents() {
        return getHeaderComponents(TestBenchElement.class);
    }

    public <T extends TestBenchElement> ElementQuery<T> getHeaderComponents(Class<T> clazz) {
        return $(clazz).withAttribute("slot", "header-components");
    }

    public DivElement getHeader() {
        return $(DivElement.class).withAttribute("class", "linkki-section-header").first();
    }

    public void close() {
        if (getCloseToggle().exists() && isOpen()) {
            getCloseToggle().first().click();
        }
    }

    public void open() {
        if (getCloseToggle().exists() && !isOpen()) {
            getCloseToggle().first().click();
        }
    }

    public boolean isOpen() {
        return getContent().isDisplayed();
    }

    public Optional<String> getPlaceholderText() {
        var contentOfAfter =
                executeScript("return window.getComputedStyle(arguments[0],'::after').getPropertyValue('content')",
                              getContent()).toString();
        var strippedContent = StringUtils.strip(contentOfAfter, "\"");
        return StringUtils.isBlank(strippedContent) || "none".equals(strippedContent)
                ? Optional.empty()
                : Optional.of(strippedContent);
    }

}