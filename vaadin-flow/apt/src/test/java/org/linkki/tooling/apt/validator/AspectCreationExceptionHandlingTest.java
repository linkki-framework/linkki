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

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic.Kind;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.linkki.tooling.apt.compiler.SourceFile;
import org.linkki.tooling.apt.processor.LinkkiAnnotationProcessor;
import org.linkki.tooling.apt.util.DynamicMethodUtils;

public class AspectCreationExceptionHandlingTest extends BaseAnnotationProcessorTest {

    private static final String SOURCE_SUBFOLDER = "aspectCreationExceptionHandling/";

    @Override
    protected Processor createProcessor() {
        return new LinkkiAnnotationProcessor();
    }

    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_SUCCESS)
    @Nested
    class CompilationSuccess {

        @Test
        public void shouldOnlyWarnEvenIfAspectCreationThrowsException() {
            addOutputToAptClasspath();
            List<SourceFile> sources = getSourceFiles(
                                                      SOURCE_SUBFOLDER + "FailingAspectDefinition.java",
                                                      SOURCE_SUBFOLDER + "FailingAspect.java",
                                                      SOURCE_SUBFOLDER + "FailingAspectPmo.java");

            compile(sources);

            List<String> logs = getLogs();
            assertThat(logs, contains(Kind.WARNING));
            String warningMessage = Messages.getString(DynamicMethodUtils.ASPECT_CREATION_FAILED);
            assertThat(logs, hasMessage(String.format(warningMessage,
                                                      "FailingAspectDefinition",
                                                      "Aspect creation failing intentionally for testing")));
        }

    }
}
