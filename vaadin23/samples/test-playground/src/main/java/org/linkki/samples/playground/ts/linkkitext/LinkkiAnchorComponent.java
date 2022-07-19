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

import org.linkki.core.vaadin.component.base.LinkkiAnchor;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.AnchorTarget;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LinkkiAnchorComponent extends VerticalLayout {

    private static final long serialVersionUID = 4770624919925533048L;

    public LinkkiAnchorComponent() {

        FormLayout formLayout = new FormLayout();
        LinkkiAnchor anchorWithIcon = new LinkkiAnchor();
        anchorWithIcon.setText("Anchor with icon");
        anchorWithIcon.setHref("https://www.faktorzehn.com/de/");
        anchorWithIcon.setIcon(VaadinIcon.ABACUS);
        formLayout.add(anchorWithIcon);

        LinkkiAnchor anchorWithTargetBlank = new LinkkiAnchor();
        anchorWithTargetBlank.setText("Page in new tab");
        anchorWithTargetBlank.setHref("https://www.faktorzehn.com/de/");
        anchorWithTargetBlank.setTarget(AnchorTarget.BLANK);
        formLayout.add(anchorWithTargetBlank);

        LinkkiAnchor anchorWithPrefixAndSuffix = new LinkkiAnchor();
        anchorWithPrefixAndSuffix.setText("Anchor with prefix and suffix");
        anchorWithPrefixAndSuffix.setHref("https://www.faktorzehn.com/de/");
        anchorWithPrefixAndSuffix.setPrefixComponent(new Button("Prefix Button", VaadinIcon.AIRPLANE.create()));
        anchorWithPrefixAndSuffix.setSuffixComponent(new Button("Suffix Button", VaadinIcon.ARCHIVE.create()));
        formLayout.add(anchorWithPrefixAndSuffix);

        add(formLayout);
    }
}
