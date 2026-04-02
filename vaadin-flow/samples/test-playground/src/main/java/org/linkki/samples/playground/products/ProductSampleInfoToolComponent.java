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

package org.linkki.samples.playground.products;

import java.io.Serial;

import org.linkki.framework.ui.component.infotool.InfoTool;
import org.linkki.framework.ui.component.infotool.InfoToolsComponent;
import org.linkki.util.Sequence;

public class ProductSampleInfoToolComponent extends InfoToolsComponent<InfoTool> {

    @Serial
    private static final long serialVersionUID = 7108014108188631147L;

    public ProductSampleInfoToolComponent(Sequence<InfoTool> infoTools) {
        super(infoTools, infoTools);
        getAllToolDetails().forEach(d -> d.setOpened(true));
    }
}
