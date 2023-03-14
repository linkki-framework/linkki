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

package org.linkki.samples.playground.ts.tablayout;

import java.security.SecureRandom;
import java.util.function.Supplier;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.pmo.SectionID;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.SectionLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.core.vaadin.component.tablayout.AfterTabSelectedObserver;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.html.Div;

/**
 * Component to demonstrate {@link org.linkki.core.vaadin.component.tablayout.AfterTabSelectedObserver}.
 */
public class AfterTabSelectedComponent extends LinkkiTabLayout {

    private static final long serialVersionUID = 4832515058793367536L;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private String value;

    public AfterTabSelectedComponent() {
        this.value = "initial value";
        addTabSheets(LinkkiTabSheet.builder("sheet-without-observer")
                .caption("Sheet without AfterTabSelectedObserver")
                .content(() -> VaadinUiCreator
                        .createComponent(new AfterTabSelectedPmo("withoutObserver", () -> value, this::changeValue),
                                         new BindingContext()))
                .build(),
                     LinkkiTabSheet.builder("sheet-with-observer")
                             .caption("Sheet with AfterTabSelectedObserver")
                             .content(() -> new ComponentWithAfterTabSelectedObserver(
                                     new AfterTabSelectedPmo("withObserver", () -> value, this::changeValue)))
                             .build());
    }

    private void changeValue() {
        value = String.valueOf(SECURE_RANDOM.nextInt());
    }

    private static class ComponentWithAfterTabSelectedObserver extends Div implements AfterTabSelectedObserver {

        private static final long serialVersionUID = -3735861576022649902L;

        private final BindingContext bindingContext;

        public ComponentWithAfterTabSelectedObserver(Object pmo) {
            this.bindingContext = new BindingContext();
            add(VaadinUiCreator.createComponent(pmo, bindingContext));
        }

        @Override
        public void afterTabSelected(LinkkiTabSheet.TabSheetSelectionChangeEvent event) {
            bindingContext.updateUi();
        }
    }

    @UISection(layout = SectionLayout.HORIZONTAL)
    public static class AfterTabSelectedPmo {

        private final String id;
        private final Supplier<String> value;
        private final Handler changeValue;

        public AfterTabSelectedPmo(String id, Supplier<String> value, Handler changeValue) {
            this.id = id;
            this.value = value;
            this.changeValue = changeValue;
        }

        @UILabel(position = 10, label = "")
        public String getValue() {
            return value.get();
        }

        @UIButton(position = 20)
        public void changeValue() {
            changeValue.apply();
        }

        @SectionID
        public String getId() {
            return id;
        }
    }
}
