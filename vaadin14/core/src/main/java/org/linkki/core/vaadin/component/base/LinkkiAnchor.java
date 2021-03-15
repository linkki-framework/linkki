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

package org.linkki.core.vaadin.component.base;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.defaults.style.LinkkiTheme;
import org.linkki.core.vaadin.component.HasIcon;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * An anchor component that can have an additional {@link VaadinIcon}
 */
@CssImport(value = "./styles/linkki-has-icon.css")
public class LinkkiAnchor extends Anchor implements HasIcon {

    private static final long serialVersionUID = -1027646873177686722L;

    @CheckForNull
    private VaadinIcon icon;

    private String text;

    public LinkkiAnchor() {
        this.icon = null;
        this.text = "";
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        if (!StringUtils.equals(text, this.text)) {
            this.text = text;
            update();
        }
    }

    @CheckForNull
    @Override
    public VaadinIcon getIcon() {
        return this.icon;
    }

    @Override
    public void setIcon(VaadinIcon icon) {
        if (!Objects.equals(this.icon, icon)) {
            this.icon = icon;
            if (icon != null) {
                addClassName(LinkkiTheme.HAS_ICON);
            } else {
                removeClassName(LinkkiTheme.HAS_ICON);
            }
            update();
        }
    }

    private void update() {
        removeAll();

        super.setText(text);
        if (icon != null) {
            add(icon.create());
        }
    }

}
