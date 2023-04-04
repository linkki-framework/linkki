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
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic.Kind;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.linkki.tooling.apt.compiler.SourceFile;
import org.linkki.tooling.apt.processor.LinkkiAnnotationProcessor;

public class SuppressedWarningsTest extends BaseAnnotationProcessorTest {

    @Override
    void setUp() throws IOException {
        // just to delete the @BeforeEach annotation
        super.setUp();
    }

    @Nested
    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_SUCCESS)
    class CompilationSuccess {

        private static final String SUPPRESSED_WARNINGS = "suppressedWarnings/";

        @Test
        @DisplayName("missing isTypeEnabled error is suppressed on component")
        void shouldSucceedWhenMissingEnabledMethodErrorIsSuppressed() throws IOException {
            setUp();

            addLinkkiAptOption(AspectMethodValidator.MISSING_METHOD_ABSTRACT_TYPE, Kind.WARNING.name());
            compile(asList(getSourceFile("Report.java"), getSourceFile("ReportType.java"),
                           getSourceFile("suppressedWarnings/SuppressedEnabledWarningPmo.java")));
            verifyNoWarnings();
        }

        @TestFactory
        Stream<DynamicContainer> shouldSucceedWhenWarningIsSuppressedOnMethod() {
            return Stream.of(dynamicContainer("suppressing a warning where the effected method is suppressed",
                                              getSuppressWarningsSucceedsTests()));
        }

        class FileToCode {
            private final String file;
            private final String code;

            public FileToCode(String file, String code) {
                this.file = file;
                this.code = code;
            }

            public String getFile() {
                return file;
            }

            public String getCode() {
                return code;
            }
        }

        private Stream<? extends DynamicNode> getSuppressWarningsSucceedsTests() {
            return Stream.of("method", "type")
                    .map(it -> dynamicContainer(
                                                it,
                                                getTestData(it)
                                                        .map(data -> testFileIsOk(data))));
        }

        private Stream<FileToCode> getTestData(String it) {
            return Stream.of(new FileToCode(SUPPRESSED_WARNINGS + it
                    + "/Position_PositionClash.java",
                    PositionValidator.POSITION_CLASH),
                             new FileToCode(SUPPRESSED_WARNINGS + it
                                     + "/AspectMethod_MissingMethod.java",
                                     AspectMethodValidator.MISSING_METHOD),
                             new FileToCode(
                                     SUPPRESSED_WARNINGS + it
                                             + "/AspectMethod_MissingMethodAbstractType.java",
                                     AspectMethodValidator.MISSING_METHOD_ABSTRACT_TYPE),
                             new FileToCode(SUPPRESSED_WARNINGS + it
                                     + "/DynamicField_PositionMismatch.java",
                                     DynamicFieldValidator.DYNAMIC_FIELD_MISMATCH),
                             new FileToCode(SUPPRESSED_WARNINGS + it
                                     + "/DynamicField_MissingMethod.java",
                                     DynamicFieldValidator.MISSING_METHOD),
                             new FileToCode(SUPPRESSED_WARNINGS + it
                                     + "/ModelBinding_ImplicitModelBinding.java",
                                     ModelBindingValidator.IMPLICIT_MODEL_BINDING),
                             new FileToCode(SUPPRESSED_WARNINGS + it
                                     + "/ModelBinding_InvalidModelAttribute.java",
                                     ModelBindingValidator.MISSING_MODEL_ATTRIBUTE),
                             new FileToCode(SUPPRESSED_WARNINGS + it
                                     + "/ModelBinding_InvalidModelObject.java",
                                     ModelBindingValidator.MISSING_MODEL_OBJECT),
                             new FileToCode(SUPPRESSED_WARNINGS + it
                                     + "/ModelObject_ModelObjectClash.java",
                                     ModelObjectValidator.MODEL_OBJECT_CLASH),
                             new FileToCode(SUPPRESSED_WARNINGS + it
                                     + "/PublicModifier_NonPublicMethod.java",
                                     PublicModifierValidator.NON_PUBLIC_METHOD),
                             new FileToCode(SUPPRESSED_WARNINGS + it
                                     + "/BoundProperty_SetterOnlyInModelObject.java",
                                     BoundPropertyValidator.SETTER_ONLY_IN_MODEL_OBJECT));
        }

        private DynamicNode testFileIsOk(FileToCode it) {
            return dynamicTest(it.getFile(), () -> {
                setUp();
                addLinkkiAptOption(it.getCode(), Kind.WARNING.name());

                List<SourceFile> sourceFiles = getSourceFiles("Report.java",
                                                              "ReportType.java",
                                                              it.getFile());

                compile(sourceFiles);
                verifyNoWarnings();
                verifyNoErrors();
            });
        }
    }

    @Nested
    @DisplayName(TESTS_THAT_EXPECT_A_COMPILATION_FAILURE)
    class CompilationFailure {
        @DisplayName("attribute not found error is not suppressed")
        @Test
        void shouldFailWhenAttributeCannotBeFoundAndComponentIsNotSuppressed() throws IOException {
            setUp();
            compile(asList(getSourceFile("Report.java"), getSourceFile("ReportType.java"),
                           getSourceFile("suppressedWarnings/UnsuppressedMissingAttributePmo.java")));
            List<String> logs = getLogs();
            assertEquals(1, logs.size());
            String log = logs.get(0);

            assertThat(log, containsString(ModelBindingValidator.MISSING_MODEL_ATTRIBUTE));
            assertThat(log, containsString("UITextArea"));
            assertThat(log,
                       containsString("Model object \"modelObject\" of type org.linkki.tooling.apt.test.Report does not have the property \"not present\"."));
        }
    }

    @Override
    protected Processor createProcessor() {
        return new LinkkiAnnotationProcessor();
    }

}
