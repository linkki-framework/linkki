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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.linkki.tooling.compiler.ErrorMessageMatcher.containsAll;
import static org.linkki.tooling.compiler.ErrorMessageMatcher.containsAny;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.tools.Diagnostic.Kind;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.linkki.tooling.compiler.SourceFile;
import org.linkki.tooling.compiler.TestCompiler;
import org.linkki.tooling.compiler.TestMessager;
import org.linkki.tooling.compiler.TestProcessor;
import org.mockito.Mockito;

public abstract class BaseAnnotationProcessorTest {
    public static final String TESTS_THAT_EXPECT_A_COMPILATION_FAILURE = "Tests that expect a compilation failure";
    public static final String TESTS_THAT_EXPECT_A_COMPILATION_SUCCESS = "Tests that expect a compilation success";

    private Messager messager;
    private TestProcessor processorWrapper;
    private TestMessager testMessager;
    private TestCompiler compiler;

    private Processor processor;

    @BeforeEach
    void setUp() throws IOException {
        testMessager = new TestMessager(isLogging());
        messager = Mockito.spy(testMessager);
        processor = createProcessor();
        processorWrapper = new TestProcessor(processor, messager);
        compiler = new TestCompiler();
    }

    protected boolean isLogging() {
        return false;
    }

    protected final Messager getMessager() {
        return messager;
    }

    protected final List<String> getLogs() {
        return testMessager.getLogs();
    }

    protected final Processor getProcessor() {
        return processorWrapper;
    }

    protected final TestCompiler getCompiler() {
        return compiler;
    }

    protected abstract Processor createProcessor();

    protected void messageContainsAny(Collection<String> msgSnippets, Collection<String> elementNames) {
        Mockito.verify(getMessager(), Mockito.atLeast(1)).printMessage(eq(Kind.ERROR),
                                                                       argThat(containsAny(msgSnippets)),
                                                                       argThat(containsAny(elementNames)));
    }

    protected void messageContainsAny(Collection<String> msgSnippets,
            Collection<String> elementNames,
            Collection<String> annotationData) {
        Mockito.verify(getMessager()).printMessage(eq(Kind.ERROR),
                                                   argThat(containsAny(msgSnippets)),
                                                   argThat(containsAll(elementNames)),
                                                   argThat(containsAll(annotationData)));
    }

    protected boolean compile(List<SourceFile> sourceFiles) {
        try {
            return getCompiler().compile(getProcessor(), sourceFiles);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static SourceFile getSourceFile(String fileName) {
        File file = new File(
                Objects.requireNonNull(BaseAnnotationProcessorTest.class.getClassLoader().getResource(fileName),
                                       "File not found")
                        .getFile());

        try {
            return new SourceFile(fileName, Files.readAllLines(file.toPath(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new AssertionError(e.getMessage());
        }
    }

    protected static List<SourceFile> getSourceFiles(String... fileNames) {
        return Stream.of(fileNames)
                .map(fileName -> getSourceFile(fileName))
                .collect(Collectors.toList());
    }

    protected final void addClassPathOf(Class<?> clazz) {
        compiler.addClassPathOf(clazz);
    }

    protected final void addClassPathsOf(Collection<Class<?>> classes) {
        compiler.addClassPathsOf(classes);
    }

    protected final void verifyNoWarnings() {
        verifyNo(Kind.WARNING);
    }

    protected final void verifyNoErrors() {
        verifyNo(Kind.ERROR);
    }

    private final void verifyNo(Kind kind) {
        assertTrue(testMessager.getLogs().isEmpty(), "expected logs to be empty but was: " + testMessager.getLogs());
        verify(getMessager(), never()).printMessage(eq(kind), any(), any(), any());
        verify(getMessager(), never()).printMessage(eq(kind), any(), any());
    }


    protected static boolean isError(String log) {
        return log.contains("kind=ERROR");
    }

    public static Matcher<List<String>> containsError() {
        return new TypeSafeMatcher<List<String>>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("a log statement of kind=ERROR");
            }

            @Override
            protected boolean matchesSafely(List<String> logs) {
                return logs.stream().anyMatch(BaseAnnotationProcessorTest::isError);
            }
        };
    }

    protected static boolean hasMessage(String log, String msg) {
        return log.contains("msg=" + msg);
    }

    public static Matcher<List<String>> hasMessage(String msg) {
        return new TypeSafeMatcher<List<String>>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("a log statement containing " + msg);
            }

            @Override
            protected boolean matchesSafely(List<String> logs) {
                return logs.stream().anyMatch(it -> hasMessage(it, msg));
            }
        };
    }

    protected final void addOption(String option, String value) {
        getCompiler().addAnnotationProcessorOption(option, value);
    }

    protected final void addOptions(Map<String, String> options) {
        getCompiler().addOptions(options);
    }

}

