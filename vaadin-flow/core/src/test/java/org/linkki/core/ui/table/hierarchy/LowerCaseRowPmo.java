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

package org.linkki.core.ui.table.hierarchy;

import java.util.List;

import org.linkki.core.defaults.columnbased.pmo.HierarchicalRowPmo;
import org.linkki.core.defaults.columnbased.pmo.SimpleItemSupplier;

public class LowerCaseRowPmo extends AbstractCodeRow implements HierarchicalRowPmo<NumberRowPmo> {

    private final String upperCaseLetter;
    private final String lowerCaseLetter;
    private final SimpleItemSupplier<NumberRowPmo, Code> numberRowSupplier;

    public LowerCaseRowPmo(String upperCaseLetter, String lowerCaseLetter,
            SimpleItemSupplier<NumberRowPmo, Code> numberRowSupplier) {
        this.upperCaseLetter = upperCaseLetter;
        this.lowerCaseLetter = lowerCaseLetter;
        this.numberRowSupplier = numberRowSupplier;
    }

    @Override
    public String getUpperCaseLetter() {
        return upperCaseLetter;
    }

    @Override
    public String getLowerCaseLetter() {
        return lowerCaseLetter;
    }

    @Override
    public int getNumber() {
        return numberRowSupplier.get().size();
    }

    @Override
    public List<? extends NumberRowPmo> getChildRows() {
        return numberRowSupplier.get();
    }

    @Override
    public String toString() {
        return super.toString() + " \"" + getUpperCaseLetter() + getLowerCaseLetter() + "\"";
    }

}
