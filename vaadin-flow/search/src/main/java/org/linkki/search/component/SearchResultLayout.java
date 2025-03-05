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
package org.linkki.search.component;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

import java.io.Serial;

/**
 * Layout for showing search results including input hints and error messages.
 *
 * @since 2.8.0
 */
@Tag("search-result-layout")
@CssImport("./src/search-result-layout.css")
public class SearchResultLayout extends FlexLayout {

    @Serial
    private static final long serialVersionUID = 1L;

    public SearchResultLayout() {
        setWidth("unset");
    }

}
