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
package org.linkki.samples.appsample;

import javax.annotation.Nullable;
import javax.inject.Inject;

import org.linkki.framework.ui.application.ApplicationFrame;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@Theme(value = "sample")
@CDIUI("main")
@PreserveOnRefresh
public class ApplicationSampleUI extends UI {

    private static final long serialVersionUID = 1L;

    @Inject
    private ApplicationFrame applicationFrame;

    @Override
    protected void init(VaadinRequest request) {
        Page.getCurrent().setTitle("Linkki :: Application Sample");

        applicationFrame.init(this);
        setContent(applicationFrame.getContent());

    }

    @Override
    protected void refresh(@Nullable VaadinRequest request) {
        super.refresh(request);
        applicationFrame.refreshCurrentView();
    }

}
