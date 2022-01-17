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


package org.linkki.tooling.apt.validator;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import javax.annotation.processing.Processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.linkki.tooling.apt.compiler.SourceFile;
import org.linkki.tooling.apt.processor.LinkkiAnnotationProcessor;

public class AspectMethodValidatorTest extends BaseAnnotationProcessorTest {

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
            String msg = Messages.getString(AspectMethodValidator.MISSING_METHOD);
            assertThat(logs, containsError());
            assertThat(logs, hasMessage(String.format(msg, "isTypeVisible", "type")));
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