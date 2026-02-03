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

import static java.util.Objects.requireNonNull;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * PMO for a headline component depicting the title as well as an optional pmo that is added to the
 * title and an optional pmo that is attached at the end, e.g., to add menu buttons. The title can
 * be updated using {@link HeadlinePmo}#setHeaderTitle.
 *
 * @since 2.10.0
 */
@UIHeadlineLayout
public class HeadlinePmo {

    private String headerTitle;

    @CheckForNull
    private final Object titleComponentPmo;

    @CheckForNull
    private final Object rightComponentPmo;

    public HeadlinePmo(String initialHeaderTitle, @CheckForNull Object titleComponentPmo,
            @CheckForNull Object rightComponentPmo) {
        this.headerTitle = requireNonNull(initialHeaderTitle);
        this.titleComponentPmo = titleComponentPmo;
        this.rightComponentPmo = rightComponentPmo;
    }

    public HeadlinePmo(String headerTitle) {
        this(headerTitle, null, null);
    }

    /**
     * Defines the text that should be displayed in the {@link Headline}.
     */
    public String getHeaderTitle() {
        return headerTitle;
    }

    /**
     * Sets the text that should be displayed in the {@link Headline}
     */
    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = requireNonNull(headerTitle);
    }

    /**
     * Defines an additional component that is displayed after the title. {@code null} if only the
     * {@link #getHeaderTitle()} should be displayed.
     */
    @CheckForNull
    public Object getTitleComponentPmo() {
        return titleComponentPmo;
    }

    /**
     * Defines a component that is displayed at the end of the
     * {@link org.linkki.framework.ui.component.Headline}, usually actions. {@code null} if no
     * component should be displayed.
     */
    @CheckForNull
    public Object getRightComponentPmo() {
        return rightComponentPmo;
    }

}
