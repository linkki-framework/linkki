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
import org.linkki.tooling.apt.processor.LinkkiAnnotationProcessor;

public class DynamicFieldValidatorTest extends BaseAnnotationProcessorTest {

    @Nested
    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_FAILURE)
    class CompilationFailure {
        @Test
        @DisplayName("caused by two ui components on one method have different positions")
        void shouldFailWhenTwoUIComponentsOnOneMethodHaveDifferentPositions() {
            compile(asList(getSourceFile("dynamicFieldsValidator/DynamicFieldsWithDifferentPositionsPmo.java")));

            List<String> logs = getLogs();
            String msg = Messages.getString(DynamicFieldValidator.DYNAMIC_FIELD_MISMATCH);
            String propertyName = "component";
            assertThat(logs, containsError());
            assertThat(logs, hasMessage(String.format(msg, propertyName)));
        }

    }

    @Nested
    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_SUCCESS)
    class CompilationSuccess {
        @Test
        @DisplayName("dynamic component type method is present")
        void shouldSucceedWhenDynamicComponentTypeMethodIsPresent() {
            compile(asList(
                           getSourceFile("Report.java"),
                           getSourceFile("dynamicFieldsValidator/CorrectDynamicFieldsPmo.java"),
                           getSourceFile("ReportType.java")));
            verifyNoErrors();
        }

        @Test
        @DisplayName("component has multiple LinkkiAspects but only one LinkkiBindingDefinition")
        void shouldSucceedWhenComponentHasMultipleLinkkiAspectsButOnlyOneLinkkiBindingDefinition() {
            compile(asList(
                           getSourceFile("Report.java"),
                           getSourceFile("ReportType.java"),
                           getSourceFile("dynamicFieldsValidator/SimpleComponentWithTwoLinkkiAspectsAnnotation.java")));

            verifyNoErrors();
        }
    }

    @Override
    protected Processor createProcessor() {
        return new LinkkiAnnotationProcessor();
    }
}