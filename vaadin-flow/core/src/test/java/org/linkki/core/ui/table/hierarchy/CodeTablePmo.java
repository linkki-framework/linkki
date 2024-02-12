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

package org.linkki.core.ui.table.hierarchy;

import java.util.Comparator;
import java.util.stream.Collectors;

import org.linkki.core.defaults.columnbased.pmo.SimpleItemSupplier;
import org.linkki.core.defaults.columnbased.pmo.SimpleTablePmo;

public class CodeTablePmo extends SimpleTablePmo<String, AbstractCodeRow> {

    private final Codes codes;

    public CodeTablePmo() {
        this(new Codes());
    }

    public CodeTablePmo(Codes codes) {
        super(() -> codes.get().stream().map(Code::getUpperCaseLetter).distinct().sorted()
                .collect(Collectors.toList()));
        this.codes = codes;
    }

    @Override
    protected UpperCaseRowPmo createRow(String upperCaseLetter) {
        return new UpperCaseRowPmo(upperCaseLetter,
                new SimpleItemSupplier<>(
                        () -> codes.byUpperCase(upperCaseLetter).map(Code::getLowerCaseLetter).distinct().sorted()
                                .collect(Collectors.toList()),
                        l -> new LowerCaseRowPmo(upperCaseLetter, l, new SimpleItemSupplier<>(
                                () -> codes.byUpperAndLowerCase(upperCaseLetter, l)
                                        .sorted(Comparator.comparing(Code::getNumber)).collect(Collectors.toList()),
                                NumberRowPmo::new))));
    }

    @Override
    public boolean isHierarchical() {
        return true;
    }

}
