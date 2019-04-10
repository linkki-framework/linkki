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
package org.linkki.framework.ui.component;


import org.linkki.core.binding.annotations.Bind;
import org.linkki.framework.ui.LinkkiApplicationTheme;

import com.vaadin.server.Responsive;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

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
 *           This class is intended to be subclassed to add additional components.
 */
public class Headline extends HorizontalLayout {

    public static final String HEADER_TITLE = "headerTitle";
    private static final long serialVersionUID = 1L;

    @Bind(pmoProperty = HEADER_TITLE)
    private final Label headerTitle;

    /**
     * Creates a new {@link Headline} without a label.
     */
    public Headline() {
        this(new Label());
    }

    /**
     * Creates a new {@link Headline} with a predefined {@link Label}.
     * 
     * @param headerTitle the {@link Label} which holds the {@link Headline}'s title to be shown
     */
    public Headline(Label headerTitle) {
        super();
        this.headerTitle = headerTitle;
        setHeightUndefined();
        initHeaderLayout();
    }

    /**
     * Creates a new {@link Headline} component with the given title.
     * 
     * @param title shown by the {@link Headline}
     */
    public Headline(String title) {
        this(new Label(title));
    }

    /**
     * Initializes the layout.
     * 
     * @implNote Override this method to add additional components to the headline.
     */
    protected void initHeaderLayout() {
        addStyleName(LinkkiApplicationTheme.HEADLINE);
        setWidth("100%"); //$NON-NLS-1$
        setHeightUndefined();
        setSpacing(true);

        Responsive.makeResponsive(this);

        headerTitle.setWidthUndefined();
        headerTitle.addStyleName(ValoTheme.LABEL_HUGE);
        addComponent(headerTitle);
    }

    /**
     * Sets the {@link Headline Headline}'s title.
     * 
     * @param title which will be shown by the {@link Headline}
     * @deprecated Deprecated since version 18.7 and will be removed in future releases. Please use
     *             {@link #setTitle(String)} instead.
     */
    @Deprecated
    public void setHeadline(String title) {
        headerTitle.setValue(title);
    }

    /**
     * Sets the {@link Headline}'s title.
     * 
     * @param title which will be shown by the {@link Headline}
     */
    public void setTitle(String title) {
        headerTitle.setValue(title);
    }
}