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
package org.linkki.samples.playground.ui;

import org.linkki.framework.ui.application.LinkkiUi;
import org.linkki.samples.playground.state.PlaygroundApplicationConfig;
import org.linkki.samples.playground.ui.dialogs.DialogView;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;

@Theme(value = "sample")
@Widgetset("com.vaadin.v7.Vaadin7WidgetSet")
@PreserveOnRefresh
public class PlaygroundApplicationUI extends LinkkiUi {

    private static final long serialVersionUID = 1L;

    public PlaygroundApplicationUI() {
        super(new PlaygroundApplicationConfig());
    }

    @Override
    protected void init(VaadinRequest request) {
        super.init(request);
        addView(PlaygroundView.NAME, PlaygroundView.class);
        addView(DialogView.NAME, DialogView.class);
    }

}
