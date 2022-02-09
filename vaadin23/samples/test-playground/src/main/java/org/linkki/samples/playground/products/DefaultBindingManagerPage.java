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

package org.linkki.samples.playground.products;

import java.util.Arrays;

import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.vaadin.component.page.AbstractPage;

public class DefaultBindingManagerPage extends AbstractPage {

    private static final long serialVersionUID = 1L;

    private BindingManager bindingManager;

    public DefaultBindingManagerPage(Object... sectionPmos) {
        this.bindingManager = new DefaultBindingManager();
        Arrays.asList(sectionPmos).forEach(pmo -> addSection(pmo));
    }

    @Override
    public void createContent() {
        // content is directly created in constructor
    }

    @Override
    protected BindingManager getBindingManager() {
        return bindingManager;
    }

}
