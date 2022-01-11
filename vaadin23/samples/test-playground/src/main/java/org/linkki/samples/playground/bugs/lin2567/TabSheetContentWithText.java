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

package org.linkki.samples.playground.bugs.lin2567;

import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;

public class TabSheetContentWithText extends Div {

    public static final String CAPTION = "LIN-2567";

    private static final long serialVersionUID = 1L;

    public TabSheetContentWithText() {
        add(new H4(CAPTION));

        Span span = new Span();
        span.add("This section contains Text components that do not support isVisible/setVisible.");
        add(span);
        add(new HtmlComponent("br"));
        add(new Text("Previously, this used to through an exception."));
    }

}
