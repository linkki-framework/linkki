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
package de.faktorzehn.commons.linkki.search.pmo;

import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.layout.annotation.UIHorizontalLayout;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.button.ButtonVariant;

import de.faktorzehn.commons.linkki.search.util.NlsSearch;

@UIHorizontalLayout
public class SearchButtonsPmo {

    private final Handler search;

    private final Handler reset;

    public SearchButtonsPmo(Handler search, Handler reset) {
        this.search = search;
        this.reset = reset;
    }

    @UIButton(position = 20, caption = NlsSearch.I18N, variants = { ButtonVariant.LUMO_TERTIARY })
    public void reset() {
        reset.apply();
    }

    @UIButton(position = 10, caption = NlsSearch.I18N, variants = { ButtonVariant.LUMO_PRIMARY })
    // TODO LIN-2616 shortcutKeyCode = KeyCode.ENTER
    // Scope of key binding have to be reduced to search input layout
    public void search() {
        search.apply();
    }
}
