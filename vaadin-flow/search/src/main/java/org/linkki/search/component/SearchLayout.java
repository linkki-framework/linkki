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

import static java.util.Objects.requireNonNull;

import java.io.Serial;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.component.template.Id;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Layout for showing a search consisting of a headline, inputs and results.
 *
 * @since 2.8.0
 */
@Tag("search-layout")
@JsModule("./src/search-layout.ts")
public class SearchLayout extends LitTemplate implements HasSize {

    public static final String SLOT_HEADLINE = "headline";
    public static final String SLOT_INPUT = "input";
    public static final String SLOT_RESULT = "result";

    @Serial
    private static final long serialVersionUID = 1L;

    @Id("search-split")
    @CheckForNull
    private SplitLayout splitLayout;

    @SuppressFBWarnings("UR_UNINIT_READ")
    public SearchLayout() {
        super();
        requireNonNull(splitLayout, "splitLayout should be mapped from template by ID");
        splitLayout.setSplitterPosition(25);
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
        setSizeFull();
    }
}