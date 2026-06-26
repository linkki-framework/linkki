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
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds the comparison result for one TS/TC tab pair under a specific theme combination.
 */
public record TestCaseResult(String tsId, String tcId, EnumSet<Theme> themes, List<Difference> differences) {

    public boolean hasDifferences() {
        return !differences.isEmpty();
    }

    String toStringLine() {
        return tsId + "|" + tcId + "|" + themes + "|"
                + differences.stream().map(Difference::serialize).collect(Collectors.joining("§"));
    }

    static TestCaseResult fromStringLine(String line) {
        var p = line.split("\\|", 4);
        var themes = Arrays.stream(p[2].replaceAll("[\\[\\]\\s]", "").split(","))
                .filter(s -> !s.isEmpty())
                .map(s -> Enum.valueOf(Theme.class, s))
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Theme.class)));
        var diffs = p.length > 3 && !p[3].isEmpty()
                ? Arrays.stream(p[3].split("§")).map(Difference::parse).toList()
                : List.<Difference>of();
        return new TestCaseResult(p[0], p[1], themes, diffs);
    }

    /**
     * A single comparison difference with an optional screenshot label and a human-readable
     * message. The label identifies which screenshot interaction produced this difference; empty for
     * text-only differences.
     */
    public record Difference(String label, String message) {

        @Override
        public String toString() {
            return label.isEmpty() ? message : label + ": " + message;
        }

        static Difference parse(String line) {
            var sep = line.indexOf('\t');
            if (sep < 0) return new Difference("", line);
            return new Difference(line.substring(0, sep), line.substring(sep + 1));
        }

        String serialize() {
            return label + '\t' + message;
        }
    }
}
