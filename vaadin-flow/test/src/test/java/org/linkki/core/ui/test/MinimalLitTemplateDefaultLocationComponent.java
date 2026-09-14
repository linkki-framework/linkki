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
package org.linkki.core.ui.test;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;

/**
 * Minimal {@link LitTemplate} with its template at Karibu-Testing's default lookup location
 * ({@code META-INF/resources/frontend}), used to verify that this location keeps working independently of the
 * {@code META-INF/frontend} workaround in {@link KaribuUI}.
 */
@Tag("minimal-lit-template-default-location")
@JsModule("./src/minimal-lit-template-default-location.ts")
public class MinimalLitTemplateDefaultLocationComponent extends LitTemplate {

    @Id("content")
    Div content;
}
