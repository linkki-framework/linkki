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
package org.linkki.samples.playground.compare;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Holds the comparison result for one TS/TC tab pair under a specific theme.
 */
public record TestCaseResult(String tsId, String tcId, Theme theme, List<String> differences) {

    public boolean hasDifferences() {
        return !differences.isEmpty();
    }

    @Override
    public List<String> differences() {
        return List.copyOf(differences);
    }

    String toStringLine() {
        return tsId + "|" + tcId + "|" + theme.label() + "|" + String.join("§", differences);
    }

    static TestCaseResult fromStringLine(String line) {
        var p = line.split("\\|", 4);
        var theme = Stream.of(Theme.values())
                .filter(t -> t.label().equals(p[2]))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown theme: " + p[2]));
        var diffs = p.length > 3 && !p[3].isEmpty()
                ? List.of(p[3].split("§"))
                : List.<String>of();
        return new TestCaseResult(p[0], p[1], theme, diffs);
    }
}
