/*
 * Copyright Faktor Zehn GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.linkki.samples.playground.ui;

import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.layout.annotation.UISection;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.server.VaadinService;

@UISection
public class LocaleInfoPmo {

    @UILabel(position = 0, label = "UI Locale")
    public String getUILocale() {
        return UI.getCurrent().getLocale().toString();
    }

    @UILabel(position = 10, label = "Session Locale")
    public String getSessionLocale() {
        return UI.getCurrent().getSession().getLocale().toString();
    }

    @UILabel(position = 30, label = "Request Locale")
    public String getRequestLocale() {
        return VaadinService.getCurrentRequest().getLocale().toString();
    }

    @UILabel(position = 40, label = "Accept-Language Header")
    public String getAcceptLanguageHeader() {
        String header = VaadinService.getCurrentRequest().getHeader("Accept-Language");
        return header != null ? header : "Not present";
    }

    @UILabel(position = 50, label = "I18N Languages")
    public String getI18NLanguages() {
        I18NProvider provider = VaadinService.getCurrent().getInstantiator().getI18NProvider();
        if (provider != null) {
            return provider.getProvidedLocales().toString();
        } else {
            return "No I18NProvider defined";
        }
    }

    @UIButton(position = 60, caption = "Close Session to Refresh Locale Settings")
    public void terminateSession() {
        UI.getCurrent().getSession().close();
    }
}