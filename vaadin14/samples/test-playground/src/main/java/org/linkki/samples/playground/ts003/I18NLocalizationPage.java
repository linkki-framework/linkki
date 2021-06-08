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

package org.linkki.samples.playground.ts003;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.vaadin.component.page.AbstractPage;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabLayout;
import org.linkki.core.vaadin.component.tablayout.LinkkiTabSheet;
import org.linkki.samples.playground.TestCaseSection;

import com.vaadin.flow.component.tabs.Tabs.Orientation;

public class I18NLocalizationPage extends AbstractPage {

    private static final long serialVersionUID = 4913196258806885693L;

    private final BindingContext bindingContext;

    public I18NLocalizationPage() {
        bindingContext = new BindingContext(getClass().getName());
        init();
    }

    @Override
    public void createContent() {

        LinkkiTabLayout testCasesTabs = new LinkkiTabLayout(Orientation.HORIZONTAL);
        testCasesTabs.setId("test-case-selector");
        testCasesTabs.addTabSheets(
                                   LinkkiTabSheet.builder("TC001")
                                           .content(new TestCaseSection("TS003.TC001",
                                                   VaadinUiCreator.createComponent(new I18NElementsLocalizationPmo(),
                                                                                   getBindingContext())))
                                           .build());

        add(testCasesTabs);
    }

    /**
     * Ignored by {@link #getBindingContext()} to make sure that everything works without a
     * {@link BindingManager}.
     */
    @Override
    protected BindingManager getBindingManager() {
        return new DefaultBindingManager(ValidationService.NOP_VALIDATION_SERVICE);
    }

    @Override
    protected BindingContext getBindingContext() {
        return bindingContext;
    }

}
