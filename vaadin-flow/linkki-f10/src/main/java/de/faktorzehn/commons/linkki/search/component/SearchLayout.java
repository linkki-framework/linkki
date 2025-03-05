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

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.component.template.Id;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Component that displays controls to enter the search criteria.
 *
 * @deprecated moved to linkki-search-vaadin-flow. Use org.linkki.search.component.SearchLayout instead
 */
@Deprecated(since = "2.8.0")
@Tag("search-layout")
@JsModule("./src/search-layout.ts")
public class SearchLayout extends LitTemplate implements HasSize {

    public static final String SLOT_HEADLINE = "headline";
    public static final String SLOT_INPUT = "input";
    public static final String SLOT_RESULT = "result";

    private static final long serialVersionUID = 1L;

    @Id("search-split")
    private SplitLayout splitLayout;

    @SuppressFBWarnings("UR_UNINIT_READ")
    public SearchLayout() {
        super();
        splitLayout.setSplitterPosition(25);
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        setSizeFull();
    }
}