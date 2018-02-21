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
package org.linkki.core.nls.pmo.sample;

import java.util.ArrayList;
import java.util.List;

import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.table.ContainerPmo;

@UISection(caption = NoNlsTablePmo.CAPTION)
public class NoNlsTablePmo implements ContainerPmo<NlsTableRowPmo> {

    public static final String CAPTION = "Some Caption";

    private final List<NlsTableRowPmo> rows = new ArrayList<>();

    @Override
    public List<NlsTableRowPmo> getItems() {
        return rows;
    }

}
