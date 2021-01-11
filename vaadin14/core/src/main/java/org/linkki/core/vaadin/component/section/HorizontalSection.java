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
package org.linkki.core.vaadin.component.section;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * A horizontal section is a section that contains label / control pairs one after another.
 */
public class HorizontalSection extends BaseSection {

    private static final long serialVersionUID = 1L;

    private final HorizontalLayout content;

    /**
     * Creates a new section with the given caption that is not closable.
     */
    public HorizontalSection(String caption) {
        this(caption, false);
    }

    /**
     * Creates a new section with a caption.
     * 
     * @param caption the caption
     * @param closeable <code>true</code> if the section can be closed/opened.
     */
    public HorizontalSection(String caption, boolean closeable) {
        super(caption, closeable);
        content = new HorizontalLayout();
        super.add(content);
    }

    @Override
    public void add(Span label, Component component) {
        add(label);
        add(component);
    }

    public void add(Component component) {
        content.add(component);
        content.setVerticalComponentAlignment(Alignment.CENTER, component);

        // TODO LIN-2064
        // if (UiUtil.isWidth100Pct((HasSize)component)) {
        // updateExpandRatio();
        // }
    }

    // private void updateExpandRatio() {
    // float ratio = 1f / getNumOfComponentsWith100PctWidth();
    // for (Iterator<Component> it = content.iterator(); it.hasNext();) {
    // Component c = it.next();
    // if (UiUtil.isWidth100Pct(c)) {
    // content.setExpandRatio(c, ratio);
    // }
    // }
    // }
    //
    // private int getNumOfComponentsWith100PctWidth() {
    // int num = 0;
    // for (Iterator<Component> it = content.iterator(); it.hasNext();) {
    // if (UiUtil.isWidth100Pct(it.next())) {
    // num++;
    // }
    // }
    // return num;
    // }

    @Override
    public Component getSectionContent() {
        return content;
    }

}
