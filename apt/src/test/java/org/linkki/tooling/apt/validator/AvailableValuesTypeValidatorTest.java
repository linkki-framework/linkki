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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.processing.Processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.types.AvailableValuesType;
import org.linkki.tooling.apt.processor.LinkkiAnnotationProcessor;

public class AvailableValuesTypeValidatorTest extends BaseAnnotationProcessorTest {

    @Override
    protected Processor createProcessor() {
        return new LinkkiAnnotationProcessor();
    }

    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_FAILURE)
    @Nested
    class CompilationFailure {

        @Test
        public void shouldFailWhenModelAttributeIsNotEnumOrBoolean() {
            Stream.of("Report.java",
                      "availableValuesValidator/AvailableValuesTypeEnumWhileUsingNoEnumOrBooleanPmo.java")
                    .map(BaseAnnotationProcessorTest::getSourceFile)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), c -> compile(c)));

            List<String> logs = getLogs();
            assertEquals(2, logs.size());
            {
                String log = logs.get(0);

                assertTrue(log.contains(AvailableValuesTypeValidator.WRONG_CONTENT_TYPE));
                assertTrue(log.contains("UIComboBox"));
                assertTrue(log.contains("\"description\""));
                assertTrue(log.contains(AvailableValuesType.ENUM_VALUES_INCL_NULL.name()));

            }

            {
                String log = logs.get(1);

                assertTrue(log.contains("WRONG_CONTENT_TYPE"));
                assertTrue(log.contains("UIComboBox"));
                assertTrue(log.contains("\"id\""));
                assertTrue(log.contains(AvailableValuesType.ENUM_VALUES_EXCL_NULL.name()));
            }
        }

    }


    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_SUCCESS)
    @Nested
    class CompilationSuccess {

        @Test
        public void shouldSucceedWhenModelAttributeIsEnum() {
            Stream.of("Report.java",
                      "availableValuesValidator/AvailableValuesTypeEnumWhileUsingEnumPmo.java")
                    .map(BaseAnnotationProcessorTest::getSourceFile)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), c -> compile(c)));

            verifyNoErrors();
        }

        @Test
        public void shouldSucceedWhenModelAttributeIsBoolean() {
            Stream.of("Report.java",
                      "availableValuesValidator/AvailableValuesTypeEnumWhileUsingBooleanPmo.java")
                    .map(BaseAnnotationProcessorTest::getSourceFile)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), c -> compile(c)));
            verifyNoErrors();
        }

    }
}
