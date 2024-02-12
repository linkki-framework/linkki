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

package org.linkki.tooling.apt.validator;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import javax.annotation.processing.Processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.linkki.tooling.apt.compiler.SourceFile;
import org.linkki.tooling.apt.processor.LinkkiAnnotationProcessor;

public class ModelObjectValidatorTest extends BaseAnnotationProcessorTest {

    @Nested
    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_FAILURE)
    class CompilationFailure {

        @Test
        @DisplayName("name clash of ModelObjects")
        void shouldFailIfMultipleModelObjectsWithSameNameAreFound() {
            List<SourceFile> sources = getSourceFiles("Person.java",
                                                      "modelObjectValidator/ModelObjectNameClashPmo.java");

            compile(sources);
            List<String> logs = getLogs();

            assertThat(logs.size(), equalTo(2));

            getLogContaining(logs, "getPerson1()")
                    .ifPresentOrElse(
                                     log -> {
                                         assertThat(log,
                                                    containsString(ModelObjectValidator.MODEL_OBJECT_CLASH));
                                         assertThat(log,
                                                    containsString("\"person\" is already used at \"getPerson2\""));
                                     },
                                     () -> fail("expected to find log about method \"getPerson1()\""));

            getLogContaining(logs, "getPerson2()")
                    .ifPresentOrElse(
                                     log -> {
                                         assertThat(log,
                                                    containsString(ModelObjectValidator.MODEL_OBJECT_CLASH));
                                         assertThat(log,
                                                    containsString("\"person\" is already used at \"getPerson1\""));
                                     },
                                     () -> fail("expected to find log about method \"getPerson2()\""));

        }

        private Optional<String> getLogContaining(List<String> logs, String string) {
            return logs.stream()
                    .filter(it -> it.contains(string))
                    .findFirst();
        }
    }

    @Nested
    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_SUCCESS)
    class CompilationSuccess {

        @Test
        @DisplayName("no name clash of ModelObjects")
        void shouldNotFailWhenModelObjectsHaveDifferentNames() {
            List<SourceFile> sources = getSourceFiles("Person.java",
                                                      "modelObjectValidator/ModelObjectNoNameClashPmo.java");

            compile(sources);
            verifyNoErrors();
        }
    }

    @Override
    protected Processor createProcessor() {
        return new LinkkiAnnotationProcessor();
    }

}
