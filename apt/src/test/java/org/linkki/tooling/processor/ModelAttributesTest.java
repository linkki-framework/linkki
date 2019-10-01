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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.annotation.processing.Processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.linkki.tooling.compiler.SourceFile;
import org.linkki.tooling.validator.ModelBindingValidator;

public class ModelAttributesTest extends BaseAnnotationProcessorTest {

    @Nested
    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_FAILURE)
    class CompilationFailure {

        @Test
        @DisplayName("caused by no getter and model attribute not present on model object")
        void shouldFailIfAttributeNotPresentOnModelObject() {
            List<SourceFile> sources = asList(getSourceFile("ReportType.java"), getSourceFile("Report.java"),
                                              getSourceFile("modelAttributeValidator/ModelAttributeMissingPmo.java"));

            compile(sources);
            List<String> logs = getLogs();
            assertEquals(1, logs.size());

            String log = logs.get(0);
            assertThat(log, containsString("UILabel"));
            assertThat(log, containsString(ModelBindingValidator.MISSING_MODEL_ATTRIBUTE));
            assertThat(log, containsString("does not have property \"not present\""));
        }

        @Test
        @DisplayName("caused by no model object found")
        void shouldFailIfModelObjectCouldNotBeFound() {
            List<SourceFile> sourceFiles = getSourceFiles("ReportType.java",
                                                          "Report.java",
                                                          "modelAttributeValidator/ModelObjectMissingPmo.java");

            compile(sourceFiles);

            List<String> logs = getLogs();
            assertEquals(1, logs.size());

            String log = logs.get(0);
            assertTrue(log.contains("ModelObject(name = \"modelObject\""));
            assertTrue(log.contains("UITextField"));
            assertTrue(log.contains(ModelBindingValidator.MISSING_MODEL_OBJECT));
        }
    }

    @Nested
    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_SUCCESS)
    class CompilationSuccess {

        @Test
        @DisplayName("getter is not present but ModelAttribute is present")
        void shouldSucceedFindingModelAttribute() {
            List<SourceFile> sources = asList(getSourceFile("ReportType.java"), getSourceFile("Report.java"),
                                              getSourceFile("modelAttributeValidator/ModelAttributeNotMissingPmo.java"));

            compile(sources);
            verifyNoErrors();
        }

    }

    @Override
    protected Processor createProcessor() {
        return new LinkkiAnnotationProcessor();
    }

}
