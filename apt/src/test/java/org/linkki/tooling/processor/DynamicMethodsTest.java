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


package org.linkki.tooling.processor;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.annotation.processing.Processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.linkki.tooling.compiler.SourceFile;
import org.linkki.tooling.validator.Messages;

public class DynamicMethodsTest extends BaseAnnotationProcessorTest {

    @Nested
    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_FAILURE)
    class CompilationFailure {

        @Test
        @DisplayName("caused by missing is<PropertyName>Visible method")
        void shouldFailWhenVisibleMethodIsNotPresent() {
            List<SourceFile> sources = asList(
                                              getSourceFile("ReportType.java"),
                                              getSourceFile("Report.java"),
                                              getSourceFile("dynamicMethodValidator/MissingDynamicMethodPmo.java"));

            compile(sources);
            List<String> logs = getLogs();
            String msg = Messages.getString("MissingMethod_error");
            assertTrue(logs.stream().anyMatch(log -> log.contains("kind=ERROR")));
            assertTrue(logs.stream().anyMatch(log -> hasMessage(log, String.format(msg, "isTypeVisible", "type"))));
        }
    }

    @Nested
    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_SUCCESS)
    class CompilationSuccess {
        @Test
        @DisplayName("visible method is provided")
        void shouldSucceedWhenVisibleMethodIsPresent() {
            List<SourceFile> sources = asList(
                                              getSourceFile("ReportType.java"),
                                              getSourceFile("Report.java"),
                                              getSourceFile("dynamicMethodValidator/DynamicMethodProvidedPmo.java"));
            compile(sources);
            verifyNoErrors();
        }
    }

    @Override
    protected Processor createProcessor() {
        return new LinkkiAnnotationProcessor();
    }
}