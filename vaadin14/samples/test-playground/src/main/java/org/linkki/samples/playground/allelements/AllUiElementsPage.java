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

package org.linkki.samples.playground.allelements;

import java.util.function.BooleanSupplier;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehavior;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.vaadin.component.base.LinkkiText;
import org.linkki.core.vaadin.component.page.AbstractPage;
import org.linkki.core.vaadin.component.section.AbstractSection;
import org.linkki.samples.playground.TestCaseSection;
import org.linkki.samples.playground.allelements.AbstractAllUiElementsSectionPmo.AllUiElementsUiSectionPmo;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


public class AllUiElementsPage extends AbstractPage {

    private static final long serialVersionUID = 1L;

    private final BindingContext bindingContext;

    private DynamicFieldPmo dynamicFieldPmo;
    private AbstractSection dynamicFieldSection;

    public AllUiElementsPage(BooleanSupplier readOnlySupplier) {
        bindingContext = new BindingContext(getClass().getName(),
                PropertyBehaviorProvider.with(PropertyBehavior.readOnly(readOnlySupplier)), Handler.NOP_HANDLER);
        init();
    }

    @Override
    public final void createContent() {
        addSection(new AllUiElementsUiSectionPmo());
        add(VaadinUiCreator.createComponent(new SectionHeaderPmo(), getBindingContext()));
        add(VaadinUiCreator.createComponent(new ReadOnlyBehaviorPmo(), getBindingContext()));

        addSection(new NumberFieldsPmo());

        dynamicFieldPmo = new DynamicFieldPmo(() -> {
            remove(dynamicFieldSection);
            dynamicFieldSection = addSection(dynamicFieldPmo);
        });
        dynamicFieldSection = addSection(dynamicFieldPmo);

        add(createLinkkiTextBehaviourTest());

    }

    private Component createLinkkiTextBehaviourTest() {

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);

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

        content.add(formLayout, actions);

        return new TestCaseSection("linkkiText", content);
    }

    private void updateIconWithText(LinkkiText text) {
        text.setText("Label with Icon");
        text.setIcon(VaadinIcon.ABACUS);
    }

    private void updateIconWithTextRandom(LinkkiText text) {
        text.setText("" + System.currentTimeMillis());
        text.setIcon(VaadinIcon.TIMER);
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