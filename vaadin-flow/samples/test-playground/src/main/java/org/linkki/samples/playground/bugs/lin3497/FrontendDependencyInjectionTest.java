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
package org.linkki.samples.playground.bugs.lin3497;

import java.io.Serial;

import org.linkki.core.ui.layout.annotation.UISection;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@Component
public class FrontendDependencyInjectionTest implements FrontendDependencyInjectionTestInterface {

    @Override
    public com.vaadin.flow.component.Component createComponent() {
        return new VerticalLayout(new Span("Should show \"caption in resource\""),
                new ComponentWithCustomResource());
    }

    @Override
    public Object createPmo() {
        return new PmoWithCssImport();
    }

    @CssImport("./styles/pmo-with-css-import.css")
    @UISection(caption = "PMO with CSS Import (should have a grey background)")
    public static class PmoWithCssImport {
        // no implementation needed
    }

    @Tag("component-with-custom-resource")
    @JsModule("./src/custom-resource.ts")
    public static class ComponentWithCustomResource extends HtmlComponent {

        @Serial
        private static final long serialVersionUID = 1L;

        // no implementation needed
    }
}
