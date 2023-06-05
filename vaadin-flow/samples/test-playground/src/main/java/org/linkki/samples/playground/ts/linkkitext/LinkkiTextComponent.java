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
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LinkkiTextComponent extends VerticalLayout {

    private static final long serialVersionUID = 2720424951808627806L;

    public LinkkiTextComponent() {
        var htmlText = createHtmlText();
        var htmlTextWithIcon = createHtmlTextWithVaadinIcon();
        var textWithPrefixAndSuffix = createTextWithPrefixAndSuffix();
        var textWithIconRight = createTextWithIconRight();
        var textWithIconLeft = createTextWithIconLeft();
        var actions = createActionsOnText(textWithIconLeft);

        add(new FormLayout(htmlText),
            new FormLayout(htmlTextWithIcon),
            new FormLayout(textWithPrefixAndSuffix),
            new FormLayout(textWithIconRight),
            new FormLayout(textWithIconLeft),
            actions);
    }

    private LinkkiText createHtmlText() {
        var htmlText = new LinkkiText();
        htmlText.setText("HTML sanitization: <b><iframe onload=\"alert('LIN-3319 :: Should not be visible!');\"/>" +
                "This should be bold text without showing the stripped tag 'iframe'</b>", true);
        return htmlText;
    }

    private LinkkiText createHtmlTextWithVaadinIcon() {
        var htmlText = new LinkkiText();
        var icon = VaadinIcon.PLUS.create();
        icon.setColor("red");
        var iconHtml = icon.getElement().getOuterHTML();
        htmlText.setText("This sanitized HTML content should end with a red plus icon " + iconHtml, true);
        return htmlText;
    }

    private LinkkiText createTextWithIconLeft() {
        var textWithIcon = new LinkkiText();
        updateIconWithText(textWithIcon);
        return textWithIcon;
    }

    private LinkkiText createTextWithIconRight() {
        var textWithIconRight = new LinkkiText();
        textWithIconRight.setIconPosition(IconPosition.RIGHT);
        textWithIconRight.setText("Label with an icon on the right");
        textWithIconRight.setIcon(VaadinIcon.ABACUS);
        return textWithIconRight;
    }

    private LinkkiText createTextWithPrefixAndSuffix() {
        var textWithPrefixAndSuffix = new LinkkiText("Label with prefix and suffix", null);
        textWithPrefixAndSuffix.setPrefixComponent(new Button("Prefix Button", VaadinIcon.AIRPLANE.create()));
        textWithPrefixAndSuffix.setSuffixComponent(new Button("Suffix Button", VaadinIcon.ARCHIVE.create()));
        return textWithPrefixAndSuffix;
    }

    private HorizontalLayout createActionsOnText(LinkkiText text) {
        var actions = new HorizontalLayout();
        actions.setPadding(false);
        actions.add(new Span("Choose an action on the text above:"));
        actions.add(new Button("Set static text", e -> updateIconWithText(text)));
        actions.add(new Button("Set random text", e -> updateIconWithTextRandom(text)));
        actions.add(new Button("Remove Icon", e -> text.setIcon(null)));
        actions.add(new Button("Set Icon", e -> text.setIcon(VaadinIcon.ABACUS)));
        return actions;
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
