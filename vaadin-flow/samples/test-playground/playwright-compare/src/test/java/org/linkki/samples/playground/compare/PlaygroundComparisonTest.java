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

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Compares two deployments of the linkki test playground per TS tab and theme.
 * <p>
 * Configure via system properties:
 * <ul>
 * <li>{@code compare.url.test} — base URL of the test deployment</li>
 * <li>{@code compare.url.reference} — base URL of the reference deployment</li>
 * </ul>
 * Run with:
 * {@code mvn test -Dcompare.url.test=http://host-test/.../playground -Dcompare.url.reference=http://host-ref/.../playground}
 * <p>
 * To run a single TS:
 * {@code mvn test "-Dtest=PlaygroundComparisonTest#testCompareTestCases(String, Theme)[TS001, *]"}
 */
@TestInstance(Lifecycle.PER_CLASS)
class PlaygroundComparisonTest {

    public static final String OUTPUT_DIR = "target/playwright-compare";

    private PlaygroundComparisonEngine engine;
    private List<String> tsTabs;
    private final List<TestCaseResult> allDiffs = Collections.synchronizedList(new ArrayList<>());

    @BeforeAll
    void setup() {
        var testDeploymentUrl = System.getProperty("compare.url.test",
                                                   "http://localhost:8080/linkki-sample-test-playground-vaadin-flow/playground");
        var referenceDeploymentUrl = System.getProperty("compare.url.reference",
                                                        "http://linkki-master.dockerhost.i.faktorzehn.de/linkki-sample-test-playground-vaadin-flow/playground");
        var outputDir = Paths.get(OUTPUT_DIR);

        engine = new PlaygroundComparisonEngine(testDeploymentUrl, referenceDeploymentUrl, outputDir);
        engine.start();
        tsTabs = engine.discoverTsTabs();
    }

    @AfterAll
    void teardown() {
        TestReportWriter.write(engine.getOutputDir(), allDiffs,
                               engine.getTestDeploymentUrl(), engine.getReferenceDeploymentUrl());
        if (engine != null) {
            engine.close();
        }
    }

    @ParameterizedTest
    @MethodSource("getTestArguments")
    void testCompareTestCases(String tsId, Theme theme) {
        var diffs = engine.compareTestCases(tsId, theme);
        allDiffs.addAll(diffs);

        var tcDiffs = diffs.stream().filter(TestCaseResult::hasDifferences).toList();
        PlaywrightHelper.log("[%s/%s] %d/%d TCs differ",
                             tsId, theme.label(), tcDiffs.size(), diffs.size());

        assertThat(tcDiffs)
                .as("TCs with differences [ts=%s, theme=%s]", tsId, theme.label())
                .map(TestCaseResult::differences)
                .isEmpty();
    }

    Stream<Arguments> getTestArguments() {
        return tsTabs.stream()
                .flatMap(tsId -> Arrays.stream(Theme.values())
                        .map(theme -> Arguments.of(tsId, theme)));
    }
}
