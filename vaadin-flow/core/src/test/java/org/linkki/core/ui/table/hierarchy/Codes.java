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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Codes {

    private final List<Code> codes = Arrays.asList(new Code("A", "a", 1), new Code("A", "a", 2),
                                                   new Code("A", "b", 1), new Code("A", "b", 2),
                                                   new Code("B", "a", 1), new Code("B", "a", 2),
                                                   new Code("B", "b", 1), new Code("B", "b", 2));

    public List<Code> get() {
        return codes;
    }

    public Stream<Code> byUpperCase(String upperCaseLetter) {
        return codes.stream().filter(c -> c.getUpperCaseLetter().equals(upperCaseLetter));
    }

    public Stream<Code> byUpperAndLowerCase(String upperCaseLetter, String lowerCaseLetter) {
        return byUpperCase(upperCaseLetter).filter(c -> c.getLowerCaseLetter().equals(lowerCaseLetter));
    }

}
