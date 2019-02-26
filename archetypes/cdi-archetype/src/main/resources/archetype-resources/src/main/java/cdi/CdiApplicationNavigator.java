/*
 * Copyright Faktor Zehn AG.
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
package \${package}.cdi;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.linkki.framework.ui.application.ApplicationLayout;
import org.linkki.framework.ui.application.ApplicationNavigator;

import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.cdi.UIScoped;
import com.vaadin.cdi.internal.Conventions;
import com.vaadin.navigator.View;
import com.vaadin.ui.UI;


/**
 * Subclass of the {@link ApplicationNavigator} to fix a CDI problem when switching to the same
 * view.
 * 
 * Switching to the same view does not clear the view scope
 * (https://github.com/vaadin/cdi/issues/166). To get a clean view scope we first navigate to the
 * {@link EmptyCdiView} before navigating to the correct new view.
 *
 */
@UIScoped
public class CdiApplicationNavigator extends ApplicationNavigator {

    private static final long serialVersionUID = 1L;

    /**
     * Navigation state is updated before the {@link #navigateTo(String)} method is called. So we
     * need to save the name of the current view on our own.
     */
    private String currentView = StringUtils.EMPTY;

    private String emptyView = Conventions.deriveMappingForView(EmptyCdiView.class);

    @Inject
    private CDIViewProvider cdiViewProvider;

    public CdiApplicationNavigator() {
        super();
    }

    @Override
    protected void init(UI ui, ApplicationLayout applicationLayout) {
        super.init(ui, applicationLayout);
        addProvider(cdiViewProvider);
    }

    public <T extends View> void showView(Class<T> clazz, String parameters) {
        showView(Conventions.deriveMappingForView(clazz), parameters);
    }

    public <T extends View> void showView(Class<T> clazz) {
        showView(Conventions.deriveMappingForView(clazz));
    }

    @Override
    public void navigateTo(String navigationState) {
        String newViewName = getViewName(navigationState);
        if (newViewName.equals(currentView)) {
            super.navigateTo(emptyView);
        }
        super.navigateTo(navigationState);
        if (isNavigationSuccessful(newViewName)) {
            currentView = newViewName;
        }
    }

    private boolean isNavigationSuccessful(String newViewName) {
        return getViewName(getState()).equals(newViewName);
    }

    /* private */ String getViewName(String fragment) {
        return fragment == null ? "" : StringUtils.substringBefore(fragment, "/");
    }
}
