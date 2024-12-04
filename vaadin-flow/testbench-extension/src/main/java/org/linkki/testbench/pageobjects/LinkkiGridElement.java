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

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("vaadin-grid")
public class LinkkiGridElement extends GridElement {

    /**
     * Retrieves the placeholder of the {@link com.vaadin.flow.component.grid.Grid} with the given
     * ID.
     *
     * @param tableId ID of the vaadin-grid element
     * @return placeholder text if there is any.
     * @deprecated since 2.7.0. Use {@link #getPlaceholderText()} instead
     */
    @Deprecated
    public Optional<String> getPlaceholderText(String tableId) {
        return getPlaceholderText();
    }

    /**
     * Retrieves the placeholder of the {@link com.vaadin.flow.component.grid.Grid}.
     *
     * @return placeholder text if there is any.
     */
    public Optional<String> getPlaceholderText() {
        var contentOfAfter =
                executeScript("return window.getComputedStyle(arguments[0],'::after').getPropertyValue('content')",
                              getWrappedElement()).toString();
        var strippedContent = StringUtils.strip(contentOfAfter, "\"");
        return StringUtils.isBlank(strippedContent) || "none".equals(strippedContent)
                ? Optional.empty()
                : Optional.of(strippedContent);
    }

}
