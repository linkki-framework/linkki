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
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic.Kind;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.linkki.tooling.apt.compiler.SourceFile;
import org.linkki.tooling.apt.processor.LinkkiAnnotationProcessor;

public class BoundPropertyValidatorTest extends BaseAnnotationProcessorTest {

    @BeforeEach
    @Override
    public void setUp() throws IOException {
        super.setUp();
        addOption(BoundPropertyValidator.SETTER_ONLY_IN_MODEL_OBJECT, Kind.ERROR.name());
    }

    @Nested
    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_FAILURE)
    class CompilationFailure {

        @Test
        @DisplayName("caused by a setter method present on model object but not on PMO")
        void shouldFailIfSetterExistsOnlyOnModelObject() {
            List<SourceFile> sources = asList(getSourceFile("ReportType.java"), getSourceFile("Report.java"),
                                              getSourceFile("boundPropertyValidator/NoSetterPmo.java"));

            compile(sources);
            List<String> logs = getLogs();
            assertEquals(1, logs.size());

            String log = logs.get(0);
            assertThat(log, containsString("UITextField"));
            assertThat(log, containsString(BoundPropertyValidator.SETTER_ONLY_IN_MODEL_OBJECT));
            assertThat(log, containsString("setter"));
        }

        @Test
        @DisplayName("caused by a setter method present on model object but not on PMO, even when the getter method is named differently")
        void shouldFailIfSetterExistsOnlyOnModelObjectAndGetterIsNamedDifferently() {
            List<SourceFile> sources = asList(getSourceFile("ReportType.java"), getSourceFile("Report.java"),
                                              getSourceFile("boundPropertyValidator/NoSetterDifferentGetterNamePmo.java"));

            compile(sources);
            List<String> logs = getLogs();
            assertEquals(1, logs.size());

            String log = logs.get(0);
            assertThat(log, containsString("UITextField"));
            assertThat(log, containsString(BoundPropertyValidator.SETTER_ONLY_IN_MODEL_OBJECT));
            assertThat(log, containsString("setter"));
        }
    }

    @Nested
    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_SUCCESS)
    class CompilationSuccess {

        @Test
        @DisplayName("both getter and setter are present on PMO and model object")
        void shouldSucceedWhenSetterIsPresent() {
            List<SourceFile> sources = asList(getSourceFile("ReportType.java"), getSourceFile("Report.java"),
                                              getSourceFile("boundPropertyValidator/GetterAndSetterPmo.java"));

            compile(sources);
            verifyNoErrors();
        }

        @Test
        @DisplayName("both getter and setter are present on PMO and model object, but with different names")
        void shouldSucceedWhenSetterForPmoPropertyIsPresent() {
            List<SourceFile> sources = asList(getSourceFile("ReportType.java"), getSourceFile("Report.java"),
                                              getSourceFile("boundPropertyValidator/GetterAndSetterDifferentNamePmo.java"));

            compile(sources);
            verifyNoErrors();
        }

        @Test
        @DisplayName("model binding is used")
        void shouldSucceedWhenUsingModelBinding() {
            List<SourceFile> sources = asList(getSourceFile("ReportType.java"), getSourceFile("Report.java"),
                                              getSourceFile("boundPropertyValidator/ModelBindingPmo.java"));

            compile(sources);
            verifyNoErrors();
        }

    }

    @Override
    protected Processor createProcessor() {
        return new LinkkiAnnotationProcessor();
    }

}
