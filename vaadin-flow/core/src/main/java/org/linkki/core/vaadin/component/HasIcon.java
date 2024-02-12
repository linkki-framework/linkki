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

package org.linkki.core.vaadin.component;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.VaadinIcon;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * A component that can have a {@link VaadinIcon}.
 */
@CssImport(value = "./styles/linkki-has-icon.css")
public interface HasIcon extends HasStyle {

    /**
     * Returns the icon of this component. When no icon is present, {@code null} is returned.
     */
    @CheckForNull
    public VaadinIcon getIcon();

    /**
     * Sets the icon of this component. A value of {@code null} can be used to remove the icon.
     */
    public void setIcon(@Nullable VaadinIcon icon);

}
