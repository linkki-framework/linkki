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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.linkki.tooling.apt.validator.BindClearButtonValidator.BIND_CLEAR_BUTTON_WITH_PRIMITIVE;

import java.util.List;

import javax.annotation.processing.Processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.linkki.tooling.apt.processor.LinkkiAnnotationProcessor;

class BindClearButtonValidatorTest extends BaseAnnotationProcessorTest {

    @Override
    protected Processor createProcessor() {
        return new LinkkiAnnotationProcessor();
    }

    @Nested
    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_FAILURE)
    class CompilationFailure {

        @Test
        void shouldFailWhenAnnotatingMethodWithPrimitiveReturnType() {
            compile(List.of(getSourceFile("bindClearButtonValidator/BindClearButtonWithPrimitiveTypePmo.java")));

            List<String> logs = getLogs();
            assertThat(logs, containsError());
            assertThat(logs, hasMessage(Messages.getString(BIND_CLEAR_BUTTON_WITH_PRIMITIVE)));
        }
    }

    @Nested
    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_SUCCESS)
    class CompilationSuccess {

        @Test
        void shouldSucceedWhenAnnotatingMethodWithLocalDateReturnType() {
            compile(List.of(getSourceFile("bindClearButtonValidator/BindClearButtonWithLocalDatePmo.java")));

            verifyNoErrors();
        }
    }
}