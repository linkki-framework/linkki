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

import org.linkki.core.ui.bind.annotation.Bind;
import org.linkki.framework.ui.LinkkiApplicationTheme;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.io.Serial;

/**
 * A component that renders a header with a title.
 * 
 * @implNote The {@link Headline}'s title can be bound to a pmoProperty {@link #HEADER_TITLE}:
 *           <p>
 *           The PMO must provide the corresponding getter method:
 * 
 *           <pre>
 *           public String getHeaderTitle() {
 *               return "Any Title";
 *           }
 *           </pre>
 * 
 *           To bind the PMO with the Headline use:
 * 
 *           <pre>
 *           new Binder(headline, new HeadlinePmo()).setupBindings(getBindingContext());
 *           </pre>
 *           <p>
 *           This class is intended to be extended to add additional components.
 */
@CssImport(value = "./styles/linkki-headline.css")
public class Headline extends Composite<HorizontalLayout> {

    public static final String HEADER_TITLE = "headerTitle";
    @Serial
    private static final long serialVersionUID = 1L;

    @Bind(pmoProperty = HEADER_TITLE)
    private final H2 headerTitle;

    /**
     * Creates a new {@link Headline} without a label.
     */
    public Headline() {
        this(new H2());
    }

    /**
     * Creates a new {@link Headline} with a predefined {@link H2}.
     * 
     * @param headerTitle the {@link H2} which holds the {@link Headline}'s title to be shown
     */
    public Headline(H2 headerTitle) {
        super();
        this.headerTitle = headerTitle;
        initHeaderLayout();
    }

    /**
     * Creates a new {@link Headline} component with the given title.
     * 
     * @param title shown by the {@link Headline}
     */
    public Headline(String title) {
        this(new H2(title));
    }

    /**
     * Initializes the layout.
     * 
     * @implNote Override this method to add additional components to the headline.
     */
    protected void initHeaderLayout() {
        getContent().setPadding(true);
        getContent().addClassName(LinkkiApplicationTheme.HEADLINE);
        getContent().add(headerTitle);
        getContent().setWidthFull();
        getContent().setFlexGrow(1, headerTitle);
        getContent().setAlignItems(Alignment.CENTER);
    }

    /**
     * Sets the {@link Headline}'s title.
     * 
     * @param title which will be shown by the {@link Headline}
     */
    public void setTitle(String title) {
        headerTitle.setText(title);
    }
}