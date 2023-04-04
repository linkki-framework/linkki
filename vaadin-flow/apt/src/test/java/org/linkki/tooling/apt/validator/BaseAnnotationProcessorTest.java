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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.tools.Diagnostic.Kind;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.linkki.tooling.apt.compiler.SourceFile;
import org.linkki.tooling.apt.compiler.TestCompiler;
import org.linkki.tooling.apt.compiler.TestMessager;
import org.linkki.tooling.apt.compiler.TestProcessor;
import org.mockito.Mockito;

public abstract class BaseAnnotationProcessorTest {

    /**
     * Useful for local debugging, set <code>true</code> for more console output like compiler errors or
     * APT findings
     */
    public static final Boolean DEBUG_TO_CONSOLE = false;

    public static final String TESTS_THAT_EXPECT_A_COMPILATION_FAILURE = "Tests that expect a compilation failure";
    public static final String TESTS_THAT_EXPECT_A_COMPILATION_SUCCESS = "Tests that expect a compilation success";

    private Messager messager;
    private TestProcessor processorWrapper;
    private TestMessager testMessager;
    private TestCompiler compiler;
    private final Writer logWriter = new CompilerLogWriter();

    private Processor processor;

    @BeforeEach
    void setUp() throws IOException {
        testMessager = new TestMessager(DEBUG_TO_CONSOLE);
        messager = Mockito.spy(testMessager);
        processor = createProcessor();
        processorWrapper = new TestProcessor(processor, messager);
        compiler = new TestCompiler();
        compiler.setLogWriter(logWriter);
    }

    @AfterEach
    void cleanUp() {
        compiler.cleanUp();
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

    protected boolean compile(List<SourceFile> sourceFiles) {
        try {
            return getCompiler().compile(getProcessor(), sourceFiles);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static SourceFile getSourceFile(String testFile) {
        String fileName = "org/linkki/tooling/apt/test/" + testFile;
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


    protected static boolean isOfKind(String log, Kind kind) {
        return log.contains("kind=" + kind.toString());
    }

    public static Matcher<List<String>> containsError() {
        return contains(Kind.ERROR);
    }

    public static Matcher<List<String>> contains(Kind kind) {
        return new TypeSafeMatcher<List<String>>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("a log statement of kind=" + kind);

            }

            @Override
            protected boolean matchesSafely(List<String> logs) {
                return logs.stream().anyMatch(log -> isOfKind(log, kind));
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

    protected final void addLinkkiAptOption(String option, String value) {
        getCompiler().addLinkkiAnnotationProcessorOption(option, value);
    }

    /**
     * Some tests use annotations that have just been compiled. In this case the compiler output path
     * must be made known to the APT.
     */
    protected final void addOutputToAptClasspath() {
        getCompiler().addOutputToAptClasspath();
    }

    private final class CompilerLogWriter extends Writer {

        private final Writer consoleWriter = new OutputStreamWriter(System.err);
        private final Writer stringWriter = new StringWriter();

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            if (DEBUG_TO_CONSOLE) {
                consoleWriter.write(cbuf, off, len);
            }
            stringWriter.write(cbuf, off, len);
        }

        @Override
        public void flush() throws IOException {
            consoleWriter.flush();
            stringWriter.flush();
            if (!stringWriter.toString().isEmpty()) {
                fail("Compiler Error:\n" + stringWriter.toString());
            }
        }

        @Override
        public void close() throws IOException {
            consoleWriter.close();
            stringWriter.close();
        }
    }

}

