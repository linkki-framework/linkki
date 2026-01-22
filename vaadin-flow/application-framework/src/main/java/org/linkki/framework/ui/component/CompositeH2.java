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
package org.linkki.framework.ui.component;

import static com.vaadin.flow.dom.Style.AlignItems.CENTER;
import static com.vaadin.flow.dom.Style.Display.FLEX;
import static java.util.Objects.requireNonNull;

import java.io.Serial;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.H2;

/**
 * An {@link H2} component that allows adding child components alongside the title text.
 * <p>
 * Unlike the standard {@link H2}, calling {@link #setText(String)} preserves all child components
 * instead of replacing the entire content.
 *
 * @since 2.10.0
 */
public class CompositeH2 extends H2 {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Text title;

    public CompositeH2() {
        this("");
    }

    public CompositeH2(String text) {
        this.title = new Text(requireNonNull(text));

        getStyle().setDisplay(FLEX)
                .setGap("var(--lumo-space-m)")
                .setAlignItems(CENTER)
                .setFlexBasis("0");

        add(title);
    }

    @Override
    public String getText() {
        return title.getText();
    }

    @Override
    public void setText(String text) {
        title.setText(text);
    }

}
