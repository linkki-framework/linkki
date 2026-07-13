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

package org.linkki.tooling.apt.compiler;

import static java.util.stream.Collectors.toSet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import javax.annotation.processing.Processor;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.ui.aspects.annotation.BindAutoFocus;
import org.linkki.core.ui.aspects.annotation.BindCaption;
import org.linkki.core.ui.aspects.annotation.BindComboBoxDynamicItemCaption;
import org.linkki.core.ui.aspects.annotation.BindComboBoxItemStyle;
import org.linkki.core.ui.aspects.annotation.BindIcon;
import org.linkki.core.ui.aspects.annotation.BindLabel;
import org.linkki.core.ui.aspects.annotation.BindPlaceholder;
import org.linkki.core.ui.aspects.annotation.BindReadOnly;
import org.linkki.core.ui.aspects.annotation.BindReadOnlyBehavior;
import org.linkki.core.ui.aspects.annotation.BindSlot;
import org.linkki.core.ui.aspects.annotation.BindStyleNames;
import org.linkki.core.ui.aspects.annotation.BindSuffix;
import org.linkki.core.ui.aspects.annotation.BindVariantNames;
import org.linkki.core.ui.aspects.annotation.BindVisible;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UICheckboxes;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UICustomField;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UIDateTimeField;
import org.linkki.core.ui.element.annotation.UIDoubleField;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UILink;
import org.linkki.core.ui.element.annotation.UILongField;
import org.linkki.core.ui.element.annotation.UIMultiSelect;
import org.linkki.core.ui.element.annotation.UIRadioButtons;
import org.linkki.core.ui.element.annotation.UITableComponent;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.element.annotation.UITimeField;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.framework.ui.dialogs.UIOpenDialogButton;
import org.linkki.tooling.apt.processor.LinkkiAnnotationProcessor;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * A class for compiling and testing Java source files with annotation processors. This class sets
 * up a temporary compilation environment, manages classpaths, and provides methods to compile
 * source files using specified annotation processors.
 * <p>
 * The TestCompiler is particularly useful for testing the linkki annotation processor and related
 * UI component annotations.
 * </p>
 */
public class TestCompiler {
    private static final BiFunction<String, String, String> ANNOTATION_OPTION = (option, value) -> "-A" + option + '='
            + value;

    private final List<String> classPathEntries = new ArrayList<>();

    private final File sourceDir;
    private final File outputDir;

    private final List<String> compilerOptions;
    private final Map<String, String> annotationProcessorOptions;

    private final LogWriter logWriter;

    /**
     * Constructs a new TestCompiler, setting up directories and default compiler options.
     *
     * @throws IOException if there's an error creating directories
     */
    public TestCompiler(boolean logOnConsole) throws IOException {
        this.classPathEntries.addAll(Stream.concat(Stream.of(TestCompiler.class,
                                                             LinkkiAnnotationProcessor.class),
                                                   classOfUIComponents().stream())
                .map(TestCompiler::classPathOf).collect(toSet()));
        this.logWriter = new LogWriter(logOnConsole);
        this.sourceDir = createTempDir("sourceDir");
        this.outputDir = createTempDir("outputDir");
        this.compilerOptions = List.of("-cp", buildClassPath(),
                                       "-d", outputDir.getAbsolutePath());
        this.annotationProcessorOptions = new HashMap<>();
    }

    /**
     * Adds the output directory to the annotation processor classpath.
     */
    public void addOutputToAptClasspath() {
        annotationProcessorOptions.put("classpath", outputDir.getAbsolutePath());
    }

    /**
     * Adds a linkki-specific option for the annotation processor.
     *
     * @param option the option name
     * @param value the option value
     */
    public void addLinkkiAnnotationProcessorOption(String option, String value) {
        this.annotationProcessorOptions.put(LinkkiAnnotationProcessor.LINKKI_OPTION_PREFIX + '.' + option, value);
    }

    /**
     * Cleans up directories created by this compiler.
     */
    public void cleanUp() {
        FileUtils.deleteQuietly(sourceDir);
        FileUtils.deleteQuietly(outputDir);
    }

    /**
     * Compiles the given list of source files using the specified Processor. The sources are
     * compiled twice: The first run compiles all source files to classes - including the
     * annotations. The annotation processor is not used in this step. In the second compilation
     * run, the annotation processor is used and can properly analyze the compiled sources.
     */
    public boolean compile(Processor processor, List<SourceFile> sourceFiles) throws IOException {
        return runCompilation(null, sourceFiles,
                              Stream.concat(compilerOptions.stream(), Stream.of("-proc:none")).toList())
                &&
                runCompilation(processor, sourceFiles, Stream.concat(compilerOptions.stream(),
                                                                     getAnnotationProcessorCompilerOptions().stream())
                        .toList());
    }

    private List<String> getAnnotationProcessorCompilerOptions() {
        return annotationProcessorOptions.entrySet().stream()
                .map(entry -> ANNOTATION_OPTION.apply(entry.getKey(), entry.getValue()))
                .toList();
    }

    private boolean runCompilation(Processor processor, List<SourceFile> sourceFiles, List<String> options)
            throws IOException {
        var files = sourceFiles.stream()
                .map(this::writeSourceFile)
                .toList();

        var compiler = ToolProvider.getSystemJavaCompiler();
        try (var fileManager = compiler.getStandardFileManager(null, null, null)) {
            var javaFileObjects = fileManager
                    .getJavaFileObjects(files.toArray(new File[0]));

            var compilationTask = compiler.getTask(logWriter, null, null, options, null, javaFileObjects);

            if (processor != null) {
                compilationTask.setProcessors(List.of(processor));
            }
            var result = compilationTask.call();
            logWriter.flush();
            return result;
        }
    }

    private String buildClassPath() {
        var classPathElements = new ArrayList<>(classPathEntries);

        classPathElements.add(outputDir.getAbsolutePath());
        return String.join(File.pathSeparator, classPathElements);
    }

    private File writeSourceFile(SourceFile sourceFile) {
        var file = new File(sourceDir, sourceFile.getFileName());
        try {
            FileUtils.writeLines(file, StandardCharsets.UTF_8.name(), sourceFile.getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    public String getLogs() {
        return logWriter.getLogs();
    }

    private static String classPathOf(Class<?> clazz) {
        try {
            var file = new File(clazz.getProtectionDomain().getCodeSource().getLocation().toURI());
            return file.getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Failed to find location of " + clazz.getName(), e);
        }
    }

    private static File createTempDir(String prefix) throws IOException {
        var file = File.createTempFile(prefix, null);
        if (!file.delete()) {
            throw new IOException("Unable to delete temporary file " + file.getAbsolutePath());
        }

        if (!file.mkdir()) {
            throw new IOException("Unable to create temp directory " + file.getAbsolutePath());
        }
        return file;
    }

    private static Collection<Class<?>> classOfUIComponents() {
        var annotations = List.of(
                                  UIButton.class,
                                  UICheckBox.class,
                                  UICheckboxes.class,
                                  UIComboBox.class,
                                  UICustomField.class,
                                  UIDateField.class,
                                  UIDateTimeField.class,
                                  UIDoubleField.class,
                                  UIIntegerField.class,
                                  UILabel.class,
                                  UILink.class,
                                  UILongField.class,
                                  UIMultiSelect.class,
                                  UIOpenDialogButton.class,
                                  UIRadioButtons.class,
                                  UITableComponent.class,
                                  UITableColumn.class,
                                  UITextArea.class,
                                  UITextField.class,
                                  UITimeField.class,
                                  BindTooltip.class,
                                  BindAutoFocus.class,
                                  BindCaption.class,
                                  BindComboBoxDynamicItemCaption.class,
                                  BindComboBoxItemStyle.class,
                                  BindIcon.class,
                                  BindLabel.class,
                                  BindPlaceholder.class,
                                  BindReadOnly.class,
                                  BindReadOnlyBehavior.class,
                                  BindSlot.class,
                                  BindStyleNames.class,
                                  BindSuffix.class,
                                  BindVariantNames.class,
                                  BindVisible.class);
        // add return types of annotation methods (e.g. ButtonVariant from variants())
        var returnTypes = annotations.stream()
                .flatMap(c -> Arrays.stream(c.getDeclaredMethods()))
                .map(m -> m.getReturnType().isArray()
                        ? m.getReturnType().getComponentType()
                        : m.getReturnType())
                .filter(t -> !t.isPrimitive() && !t.getPackageName().startsWith("java."));
        return Stream.concat(annotations.stream(), returnTypes).toList();
    }

    private static final class LogWriter extends Writer {

        @CheckForNull
        private final Writer consoleWriter;
        private final Writer stringWriter;

        public LogWriter(boolean logOnConsole) {
            consoleWriter = logOnConsole ? new OutputStreamWriter(System.err) : null;
            stringWriter = new StringWriter();
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            if (consoleWriter != null) {
                consoleWriter.write(cbuf, off, len);
            }
            stringWriter.write(cbuf, off, len);
        }

        @Override
        public void flush() throws IOException {
            if (consoleWriter != null) {
                consoleWriter.flush();
            }
            stringWriter.flush();
        }

        @Override
        public void close() throws IOException {
            if (consoleWriter != null) {
                consoleWriter.close();
            }
            stringWriter.close();
        }

        public String getLogs() {
            return stringWriter.toString();
        }
    }
}
