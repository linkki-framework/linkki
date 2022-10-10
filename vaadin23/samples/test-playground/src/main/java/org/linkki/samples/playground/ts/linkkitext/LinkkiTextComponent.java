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

package org.linkki.samples.playground.ts.linkkitext;

import org.linkki.core.ui.aspects.types.IconPosition;
import org.linkki.core.vaadin.component.base.LinkkiText;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LinkkiTextComponent extends VerticalLayout {

    private static final long serialVersionUID = 2720424951808627806L;

    public LinkkiTextComponent() {

        FormLayout formLayout = new FormLayout();
        LinkkiText textWithIcon = new LinkkiText();
        updateIconWithText(textWithIcon);
        formLayout.add(textWithIcon);

        HorizontalLayout actions = new HorizontalLayout();
        actions.setPadding(false);
        actions.add(new Button("Set static text", e -> updateIconWithText(textWithIcon)));
        actions.add(new Button("Set random text", e -> updateIconWithTextRandom(textWithIcon)));
        actions.add(new Button("Remove Icon", e -> textWithIcon.setIcon(null)));
        actions.add(new Button("Set Icon", e -> textWithIcon.setIcon(VaadinIcon.ABACUS)));

        FormLayout prefixSuffix = new FormLayout();
        LinkkiText textWithPrefixAndSuffix = new LinkkiText("Label with prefix and suffix", null);
        textWithPrefixAndSuffix.setPrefixComponent(new Button("Prefix Button", VaadinIcon.AIRPLANE.create()));
        textWithPrefixAndSuffix.setSuffixComponent(new Button("Suffix Button", VaadinIcon.ARCHIVE.create()));
        prefixSuffix.add(textWithPrefixAndSuffix);

        FormLayout iconRightLayout = new FormLayout();
        LinkkiText textWithIconRight = new LinkkiText();
        textWithIconRight.setIconPosition(IconPosition.RIGHT);
        textWithIconRight.setText("Label with an icon on the right");
        textWithIconRight.setIcon(VaadinIcon.ABACUS);
        iconRightLayout.add(textWithIconRight);

        add(formLayout, actions, prefixSuffix, iconRightLayout);
    }

    private void updateIconWithText(LinkkiText text) {
        text.setText("Label with an icon on the left");
        text.setIcon(VaadinIcon.ABACUS);
    }

    private void updateIconWithTextRandom(LinkkiText text) {
        text.setText("" + System.currentTimeMillis());
        text.setIcon(VaadinIcon.TIMER);
    }
}
