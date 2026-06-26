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

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Compares two deployments of the linkki test playground per TS tab and theme combination.
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
 * {@code mvn test "-Dtest=PlaygroundComparisonTest#testCompareTestCases(String, EnumSet)[TS001, *]"}
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
        if (engine != null) {
            TestReportWriter.write(engine.getOutputDir(), allDiffs,
                                   engine.getTestDeploymentUrl(), engine.getReferenceDeploymentUrl());
            engine.close();
        }
    }

    @ParameterizedTest
    @MethodSource("getTestArguments")
    @Execution(ExecutionMode.CONCURRENT)
    void testCompareTestCases(String tsId, EnumSet<Theme> themes) {
        var diffs = engine.compareTestCases(tsId, themes);
        allDiffs.addAll(diffs);

        var tcDiffs = diffs.stream().filter(TestCaseResult::hasDifferences).toList();
        PlaywrightHelper.log("[%s/%s] %d/%d TCs differ",
                             tsId, themes, tcDiffs.size(), diffs.size());

        System.out.println(tcDiffs);
    }

    Stream<Arguments> getTestArguments() {
        List<EnumSet<Theme>> themeCombinations = List.of(
                                                         EnumSet.noneOf(Theme.class),
                                                         EnumSet.of(Theme.CARD),
                                                         EnumSet.of(Theme.DARK),
                                                         EnumSet.of(Theme.CARD, Theme.DARK));
        return tsTabs.stream()
                .flatMap(tsId -> themeCombinations.stream()
                        .map(themes -> Arguments.of(tsId, themes)));
    }
}
