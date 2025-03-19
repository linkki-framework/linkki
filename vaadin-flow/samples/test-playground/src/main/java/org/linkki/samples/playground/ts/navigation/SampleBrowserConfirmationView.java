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
package org.linkki.samples.playground.ts.navigation;

import static org.linkki.util.Objects.requireNonNull;

import java.io.Serial;

import org.linkki.core.binding.dispatcher.behavior.PropertyBehavior;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.framework.ui.HasBrowserConfirmation;
import org.linkki.framework.ui.dialogs.OkCancelDialog;
import org.linkki.samples.playground.ui.PlaygroundAppLayout;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.Route;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * This is a sample route to demonstrate the browser and vaadin confirmation in case of unsaved data
 * in an edit mode.
 */
@Route(value = SampleBrowserConfirmationView.ROUTE, layout = PlaygroundAppLayout.class)
public class SampleBrowserConfirmationView extends VerticalLayout
        implements BeforeEnterObserver, AfterNavigationObserver, HasBrowserConfirmation {

    public static final String ROUTE_SEGMENT = "confirms/";
    public static final String PARAM_ID = "id";
    public static final String ROUTE = ROUTE_SEGMENT + ":" + PARAM_ID;
    public static final String PARAM_EDIT = "edit";

    @Serial
    private static final long serialVersionUID = 1L;

    private final SampleConfirmService confirmService = new SampleConfirmService();

    private final EditSaveButtonPmo pmo;

    @CheckForNull
    private SampleConfirmObject currentObject;

    /**
     * Simple example to have the state of unsaved data, may be in a more complex UI state or by
     * asking a service whether the object is changed.
     */
    private boolean unsavedData;

    public SampleBrowserConfirmationView() {
        pmo = new EditSaveButtonPmo(this::getCurrentObject, this::save);
    }

    /**
     * Comes from {@link BeforeEnterObserver}, could be used to read URL parameters and initializes
     * the business object
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        currentObject = confirmService.loadSampleObject(event.getRouteParameters().get(PARAM_ID).orElseThrow());
    }

    /**
     * Comes from {@link AfterNavigationObserver} and initializes the view as well as the UI state
     * concerning the edit mode
     */
    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (isEditUrl(event)) {
            enableBrowserConfirmation();
            initEdit();
        } else {
            initView();
        }
    }

    private boolean isEditUrl(AfterNavigationEvent event) {
        return event.getLocation().getQueryParameters().getParameters().containsKey(PARAM_EDIT);
    }

    private void initEdit() {
        unsavedData = true;
        createContent(new DefaultBindingManager(ValidationService.NOP_VALIDATION_SERVICE));
    }

    private void initView() {
        unsavedData = false;
        createContent(new DefaultBindingManager(ValidationService.NOP_VALIDATION_SERVICE,
                PropertyBehaviorProvider.with(PropertyBehavior.readOnly())));
    }

    private void createContent(BindingManager bindingManager) {
        removeAll();
        add(VaadinUiCreator.createComponent(pmo,
                                            bindingManager.getContext(SampleBrowserConfirmationView.class)));
    }

    public void save() {
        unsavedData = false;
        confirmService.saveSampleObject(getCurrentObject());
    }

    /**
     * Comes from {@link BeforeLeaveObserver}. The current state and the navigation target are
     * checked to determine whether the user could safely continue or should be prompted to abort
     * the current processing.
     */
    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        if (!isTargetEditUrl(event) && unsavedData) {
            var continueNavigationAction = event.postpone();
            showAbortConfirmation(() -> {
                disableBrowserConfirmation();
                continueNavigationAction.proceed();
            });
        } else {
            disableBrowserConfirmation();
        }
    }

    private boolean isTargetEditUrl(BeforeLeaveEvent event) {
        return event.getNavigationTarget().isAssignableFrom(getClass()) &&
                event.getLocation().getQueryParameters().getParameters().containsKey(PARAM_EDIT);
    }

    private void showAbortConfirmation(Handler okHandler) {
        OkCancelDialog.builder("Really abort?")
                .content(new Span("Unsafed data will be lost."))
                .okHandler(okHandler)
                .cancelHandler(UI.getCurrent().getPage().getHistory()::forward)
                .build().open();
    }

    private SampleConfirmObject getCurrentObject() {
        return requireNonNull(currentObject, "currentObject must be set in beforeEnter");
    }

}
