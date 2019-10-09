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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.function.Function;

import javax.annotation.processing.Processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.tooling.apt.compiler.SourceFile;
import org.linkki.tooling.apt.processor.LinkkiAnnotationProcessor;
import org.linkki.tooling.apt.util.DynamicAspectMethodName;
import org.linkki.tooling.apt.validator.AspectMethodValidator;
import org.linkki.tooling.apt.validator.Messages;
import org.linkki.tooling.apt.validator.ModelBindingValidator;

public class CompilerOptionsTest extends BaseAnnotationProcessorTest {

    private static final String ERROR = "ERROR";
    private static final String WARNING = "WARNING";
    private static final String NOTE = "NOTE";
    private static final String IGNORE = "IGNORE";

    @Nested
    @DisplayName("Tests for the option " + AspectMethodValidator.MISSING_METHOD_ABSTRACT_TYPE)
    class AbstractTypeDynamicMethodsTest {
        Function<String, List<SourceFile>> sourceFileProvider = className -> asList(getSourceFile("Report.java"),
                                                                                    getSourceFile("compilerOptionsTest/linkki/abstractType/dynamic/"
                                                                                            + className + ".java"));

        @Nested
        @DisplayName("Tests option on interface")
        class InterfaceDynamicMethodsTest {
            private final String className = "InterfaceWithoutDynamicMethod";
            private final List<SourceFile> sourceFiles = sourceFileProvider.apply(className);

            @ParameterizedTest
            @ValueSource(strings = { ERROR, WARNING, NOTE })
            @DisplayName("should produce message when option is set to ")
            void compilerOptionsWithMessagesInInterface(String kind) {
                compilerOptionsWithMessages(kind, sourceFiles);
            }

            @Test
            @DisplayName("should ignore missing dynamic method")
            void shouldIgnoreMissingDynamicMethodInInterface() {
                shouldIgnoreMissingDynamicMethod(sourceFiles);
            }
        }

        @Nested
        @DisplayName("Tests option on abstract class")
        class AbstractClassDynamicMethodsTest {
            private final String className = "AbstractClassWithoutDynamicMethod";
            private final List<SourceFile> sourceFiles = sourceFileProvider.apply(className);

            @ParameterizedTest
            @ValueSource(strings = { ERROR, WARNING, NOTE })
            @DisplayName("should procude message when option is set to ")
            void compilerOptionsWithMessagesInAbstractClass(String kind) {
                compilerOptionsWithMessages(kind, sourceFiles);
            }

            @Test
            @DisplayName("should ignore missing dynamic method")
            void shouldIgnoreMissingDynamicMethodInAbstractClass() {
                shouldIgnoreMissingDynamicMethod(sourceFiles);
            }
        }

        private void compilerOptionsWithMessages(String kind, List<SourceFile> sourceFiles) {
            addOption(AspectMethodValidator.MISSING_METHOD_ABSTRACT_TYPE, kind);
            compile(sourceFiles);

            List<String> logs = getLogs();
            assertTrue(logs.stream().anyMatch(log -> isMissingDynamicMethodLog(log)));
        }

        private void shouldIgnoreMissingDynamicMethod(List<SourceFile> sourceFiles) {
            addOption(AspectMethodValidator.MISSING_METHOD_ABSTRACT_TYPE, IGNORE);
            compile(sourceFiles);

            List<String> logs = getLogs();
            assertTrue(logs.stream().noneMatch(log -> isMissingDynamicMethodLog(log)));
        }

        private boolean isMissingDynamicMethodLog(String log) {
            String methodName = "type";
            DynamicAspectMethodName dynamicMethod = new DynamicAspectMethodName(methodName,
                    EnabledAspectDefinition.NAME, true);
            String msg = Messages.getString(AspectMethodValidator.MISSING_METHOD);
            return hasMessage(log, String.format(msg, dynamicMethod.getExpectedMethodName(), methodName));
        }
    }

    @Nested
    @DisplayName("Tests for the option " + ModelBindingValidator.IMPLICIT_MODEL_BINDING)
    class MissingModelAttributeTest {
        Function<String, List<SourceFile>> sourceFileProvider = className -> asList(
                                                                                    getSourceFile("compilerOptionsTest/linkki/modelAttribute/badStyle/"
                                                                                            + className + ".java"),
                                                                                    getSourceFile("Report.java"));

        @Nested
        @DisplayName("Tests option on a class with bad style")
        class BadStyleModelBinding {
            private final String className = "BadStyleModelBinding";
            private final List<SourceFile> sourceFiles = sourceFileProvider.apply(className);

            @Test
            @DisplayName("ignore bad style with model attributes")
            void shouldIgnoreBadStyle() {
                addOption(ModelBindingValidator.IMPLICIT_MODEL_BINDING, IGNORE);
                compile(sourceFiles);

                List<String> logs = getLogs();
                assertTrue(logs.stream().noneMatch(log -> isBadStyleLog(log, "type")));
            }

            private boolean isBadStyleLog(String log, String propertyName) {
                String msg = Messages.getString(ModelBindingValidator.IMPLICIT_MODEL_BINDING);
                return hasMessage(log, String.format(msg, propertyName, propertyName));
            }


            @ParameterizedTest
            @ValueSource(strings = { ERROR, WARNING, NOTE })
            void shouldProduceMessage(String kind) {
                addOption(ModelBindingValidator.IMPLICIT_MODEL_BINDING, kind);
                compile(sourceFiles);

                List<String> logs = getLogs();
                assertFalse(logs.isEmpty());
                assertTrue(logs.stream().allMatch(log -> isBadStyleLog(log, "type")));
            }
        }
    }

    @Override
    protected Processor createProcessor() {
        return new LinkkiAnnotationProcessor();
    }

}
