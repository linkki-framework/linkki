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

import java.util.Optional;

import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.AnchorTargetValue;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.server.AbstractStreamResource;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * An anchor component that can have an additional {@link VaadinIcon}
 */
public class LinkkiAnchor extends LinkkiText implements Focusable<LinkkiAnchor> {

    private static final long serialVersionUID = -1027646873177686722L;

    public LinkkiAnchor() {
        super(new Anchor(), "", null);
    }

    @Override
    protected Anchor getContent() {
        return (Anchor)super.getContent();
    }

    @Override
    protected void setIconOnComponent(@CheckForNull VaadinIcon icon) {
        setSuffixComponent(icon == null ? null : icon.create());
    }

    /**
     * @see Anchor#removeHref()
     */
    public void removeHref() {
        getContent().removeHref();
    }

    /**
     * @see Anchor#getHref()
     */
    public String getHref() {
        return getContent().getHref();
    }

    /**
     * @see Anchor#setHref(String)
     */
    public void setHref(String href) {
        getContent().setHref(href);
    }

    /**
     * @see Anchor#setHref(AbstractStreamResource)
     */
    public void setHref(AbstractStreamResource href) {
        getContent().setHref(href);
    }

    /**
     * @see Anchor#onEnabledStateChanged(boolean)
     */
    @Override
    public void onEnabledStateChanged(boolean enabled) {
        super.onEnabledStateChanged(enabled);
        getContent().onEnabledStateChanged(enabled);
    }

    /**
     * @see Anchor#getTarget()
     */
    public Optional<String> getTarget() {
        return getContent().getTarget();
    }

    /**
     * @see Anchor#setTarget(String)
     */
    public void setTarget(String target) {
        getContent().setTarget(target);
    }

    /**
     * @see Anchor#setTarget(AnchorTargetValue)
     */
    public void setTarget(AnchorTargetValue target) {
        getContent().setTarget(target);
    }

    /**
     * @see Anchor#getTargetValue()
     */
    public AnchorTargetValue getTargetValue() {
        return getContent().getTargetValue();
    }
}