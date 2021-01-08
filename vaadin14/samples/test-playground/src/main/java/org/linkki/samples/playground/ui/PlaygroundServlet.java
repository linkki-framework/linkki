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

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.vaadin.flow.server.InitParameters;
import com.vaadin.flow.server.VaadinServlet;

@WebServlet(urlPatterns = "/*", name = "Playground", initParams = {
        @WebInitParam(name = InitParameters.I18N_PROVIDER, value = "org.linkki.samples.playground.nls.DummyI18NProvider") })
public class PlaygroundServlet extends VaadinServlet {

    private static final long serialVersionUID = 1L;

}
