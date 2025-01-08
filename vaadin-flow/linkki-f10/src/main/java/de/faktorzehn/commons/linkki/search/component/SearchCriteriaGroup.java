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
package de.faktorzehn.commons.linkki.search.component;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

/**
 * Component that displays controls to enter the search criteria.
 */
@Tag("search-criteria-group")
public class SearchCriteriaGroup extends FlexLayout {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new component for search criteria with a given PMO. The PMO should contain all
     * input controls. Note that the annotation on the PMO itself is ignored. Only the elements are
     * used.
     */
    public SearchCriteriaGroup(String captionText) {
        super();
        setFlexDirection(FlexDirection.COLUMN);
        getStyle().set("gap", "var(--lumo-space-m)");
        if (StringUtils.isNotBlank(captionText)) {
            add(new H4(captionText));
        }
    }
}