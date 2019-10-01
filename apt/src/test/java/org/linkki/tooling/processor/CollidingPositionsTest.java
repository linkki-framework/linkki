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
import org.linkki.tooling.validator.PositionValidator;

public class CollidingPositionsTest extends BaseAnnotationProcessorTest {

    @Nested
    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_FAILURE)
    class CompilationFailure {

        @Test
        @DisplayName("caused by two ui components that have the same position")
        void shouldFailWhenPositionsCollide() {
            compile(asList(getSourceFile("Person.java"),
                           getSourceFile("componentPositionValidator/CollidingPositionsPmo.java")));

            String msg = Messages.getString(PositionValidator.POSITION_CLASH);
            List<String> logs = getLogs();
            assertThat(logs, containsError());
            assertThat(logs, hasMessage(String.format(msg.substring(0, msg.indexOf("property")), 10)));
        }

    }

    @Nested
    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_SUCCESS)
    class CompilationSuccess {

        @Test
        @DisplayName("every component has its unique position")
        void shouldSucceedWhenEveryPositionIsUnique() {
            compile(asList(getSourceFile("Person.java"),
                           getSourceFile("componentPositionValidator/UniquePositionsPmo.java")));
            verifyNoErrors();
        }

    }

    @Override
    protected Processor createProcessor() {
        return new LinkkiAnnotationProcessor();
    }

}
