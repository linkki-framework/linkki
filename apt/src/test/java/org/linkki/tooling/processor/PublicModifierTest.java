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

import java.util.List;

import javax.annotation.processing.Processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.linkki.tooling.validator.Messages;
import org.linkki.tooling.validator.PublicModifierValidator;

public class PublicModifierTest extends BaseAnnotationProcessorTest {

    @Nested
    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_FAILURE)
    class CompilationFailure {

        String msg = Messages.getString(PublicModifierValidator.NON_PUBLIC_METHOD);

        @Test
        @DisplayName("caused by missing public modifier")
        void shouldFailWhenPublicModifierIsMissing() {
            compile(asList(getSourceFile("publicModifierValidator/PublicModifierMissingOnAnnotatedMethodPmo.java"),
                           getSourceFile("Person.java")));
            List<String> logs = getLogs();
            assertThat(logs, containsError());
            assertThat(logs, hasMessage(String.format(msg, "label", PublicModifierValidator.NON_PUBLIC_METHOD)));
            assertThat(logs, hasMessage(String.format(msg, "getPerson", PublicModifierValidator.NON_PUBLIC_METHOD)));
        }

        @Test
        @DisplayName("caused by missing public modifier on aspect method")
        void shoulFailWhenPublicModifierIsMissingOnApsectMethod() {
            compile(asList(getSourceFile("publicModifierValidator/PublicModifierMissingOnAspectMethodPmo.java"),
                           getSourceFile("Person.java")));

            List<String> logs = getLogs();
            assertThat(logs, containsError());
            assertThat(logs,
                       hasMessage(String.format(msg, "isFirstnameEnabled", PublicModifierValidator.NON_PUBLIC_METHOD)));
        }

        @Test
        @DisplayName("caused by missing public modifier on \"get<propertyName>ComponentType\" method")
        void shoulFailWhenPublicModifierIsMissingOnComponentTypeMethod() {
            compile(asList(getSourceFile("publicModifierValidator/PublicModifierMissingOnComponentTypeMethodPmo.java"),
                           getSourceFile("Person.java")));

            List<String> logs = getLogs();
            assertThat(logs, containsError());
            assertThat(logs, hasMessage(String.format(msg, "getFirstnameComponentType",
                                                      PublicModifierValidator.NON_PUBLIC_METHOD)));
        }

    }

    @Nested
    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_SUCCESS)
    class CompilationSuccess {

        @Test
        @DisplayName("all components have a public modifer")
        void shouldSucceedWhenAllComponentsHaveAPublicModifier() {
            compile(asList(getSourceFile("publicModifierValidator/AllComponentsPublicPmo.java"),
                           getSourceFile("Person.java")));
            verifyNoErrors();
        }

    }

    @Override
    protected Processor createProcessor() {
        return new LinkkiAnnotationProcessor();
    }

}
