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
import org.linkki.tooling.validator.Messages;

public class PublicModifierTest extends BaseAnnotationProcessorTest {

    @Nested
    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_FAILURE)
    class CompilationFailure {

        @Test
        @DisplayName("caused by missing public modifier")
        void shouldFailWhenPublicModifierIsMissing() {
            compile(asList(getSourceFile("publicModifierValidator/PublicModifierMissingOnAnnotatedMethodPmo.java"),
                           getSourceFile("Person.java")));
            List<String> logs = getLogs();
            assertTrue(logs.stream().anyMatch(it -> isError(it)));
            String msg = Messages.getString("MethodNotPublic_error");
            assertTrue(logs.stream().anyMatch(it -> hasMessage(it, String.format(msg, "label"))));
            assertTrue(logs.stream().anyMatch(it -> hasMessage(it, String.format(msg, "getPerson"))));
        }

        @Test
        @DisplayName("caused by missing public modifier on aspect method")
        void shoulFailWhenPublicModifierIsMissingOnApsectMethod() {
            compile(asList(getSourceFile("publicModifierValidator/PublicModifierMissingOnAspectMethodPmo.java"),
                           getSourceFile("Person.java")));

            List<String> logs = getLogs();
            assertTrue(logs.stream().anyMatch(it -> isError(it)));
            String msgTemplate = Messages.getString("MethodNotPublic_error");
            assertTrue(logs.stream().anyMatch(it -> hasMessage(it, String.format(msgTemplate, "isFirstnameEnabled"))));
        }

        @Test
        @DisplayName("caused by missing public modifier on \"get<propertyName>ComponentType\" method")
        void shoulFailWhenPublicModifierIsMissingOnComponentTypeMethod() {
            compile(asList(getSourceFile("publicModifierValidator/PublicModifierMissingOnComponentTypeMethodPmo.java"),
                           getSourceFile("Person.java")));

            List<String> logs = getLogs();
            assertTrue(logs.stream().anyMatch(it -> isError(it)));
            String msgTemplate = Messages.getString("MethodNotPublic_error");
            assertTrue(logs.stream()
                    .anyMatch(it -> hasMessage(it, String.format(msgTemplate, "getFirstnameComponentType"))));
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
