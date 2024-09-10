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
import org.linkki.tooling.apt.processor.LinkkiAnnotationProcessor;

import javax.annotation.processing.Processor;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * A class for compiling and testing Java source files with annotation processors.
 * This class sets up a temporary compilation environment, manages classpaths, and provides
 * methods to compile source files using specified annotation processors.
 * <p>
 * The TestCompiler is particularly useful for testing the linkki annotation processor and related UI component annotations.
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

    private Writer logWriter;

    /**
     * Constructs a new TestCompiler, setting up directories and default compiler options.
     *
     * @throws IOException if there's an error creating directories
     */
    public TestCompiler() throws IOException {
        addClassPathsOf(asList(TestCompiler.class, LinkkiAnnotationProcessor.class));
        addClassPathsOf(classOfUIComponents());

        this.sourceDir = createTempDir("sourceDir");
        this.outputDir = createTempDir("outputDir");

        compilerOptions = new ArrayList<>(asList(
                "-cp", buildClassPath(),
                "-d", outputDir.getAbsolutePath()));
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
     * @param value  the option value
     */
    public void addLinkkiAnnotationProcessorOption(String option, String value) {
        this.annotationProcessorOptions.put(LinkkiAnnotationProcessor.LINKKI_OPTION_PREFIX + '.' + option, value);
    }

    public void setLogWriter(Writer logWriter) {
        this.logWriter = logWriter;
    }

    /**
     * Cleans up directories created by this compiler.
     */
    public void cleanUp() {
        FileUtils.deleteQuietly(sourceDir);
        FileUtils.deleteQuietly(outputDir);
    }

    public boolean compile(Processor processor, SourceFile... sourceFiles) throws IOException {
        return compile(processor, asList(sourceFiles));
    }

    /**
     * Compiles the given list of source files using the specified Processor. The sources are
     * compiled twice: The first run compiles all source files to classes - including the
     * annotations. The annotation processor is not used in this step. In the second compilation
     * run, the annotation processor is used and can properly analyze the compiled sources.
     */
    public boolean compile(Processor processor, List<SourceFile> sourceFiles) throws IOException {
        return runCompilation(null, sourceFiles, compilerOptions) &&
                runCompilation(processor, sourceFiles, createOptions());
    }

    private List<String> createOptions() {
        List<String> formattedLinkkiOptions = annotationProcessorOptions.entrySet().stream()
                .map(entry -> ANNOTATION_OPTION.apply(entry.getKey(), entry.getValue()))
                .toList();

        return Stream.concat(compilerOptions.stream(), formattedLinkkiOptions.stream()).collect(toList());
    }

    private boolean runCompilation(Processor processor, List<SourceFile> sourceFiles, List<String> options)
            throws IOException {
        List<File> files = sourceFiles.stream()
                .map(this::writeSourceFile)
                .toList();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            Iterable<? extends JavaFileObject> javaFileObjects = fileManager
                    .getJavaFileObjects(files.toArray(new File[0]));

            CompilationTask compilationTask = compiler.getTask(logWriter, null, null, options, null, javaFileObjects);

            if (processor != null) {
                compilationTask.setProcessors(List.of(processor));
            }
            return compilationTask.call();
        }
    }

    /**
     * Adds the classpath of the specified class to the compiler's classpath.
     *
     * @param clazz the class whose classpath should be added
     */
    public void addClassPathOf(Class<?> clazz) {
        classPathEntries.add(classPathOf(clazz));
    }

    /**
     * Adds the classpaths of the specified classes to the compiler's classpath.
     *
     * @param classes the classes whose classpaths should be added
     */
    public void addClassPathsOf(Collection<Class<?>> classes) {
        classPathEntries.addAll(classes.stream().map(TestCompiler::classPathOf).collect(toSet()));
    }

    private String buildClassPath() {
        ArrayList<String> classPathElements = new ArrayList<>(classPathEntries);

        classPathElements.add(outputDir.getAbsolutePath());
        return String.join(File.pathSeparator, classPathElements);
    }

    private File createTempDir(String prefix) throws IOException {
        File file = File.createTempFile(prefix, null);
        if (!file.delete()) {
            throw new IOException("Unable to delete temporary file " + file.getAbsolutePath());
        }

        if (!file.mkdir()) {
            throw new IOException("Unable to create temp directory " + file.getAbsolutePath());
        }
        return file;
    }

    private File writeSourceFile(SourceFile sourceFile) {
        File file = new File(sourceDir, sourceFile.getFileName());
        try {
            FileUtils.writeLines(file, StandardCharsets.UTF_8.name(), sourceFile.getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    private static String classPathOf(Class<?> clazz) {
        try {
            File file = new File(clazz.getProtectionDomain().getCodeSource().getLocation().toURI());
            return file.getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Failed to find location of " + clazz.getName(), e);
        }
    }

    private static Collection<Class<?>> classOfUIComponents() {
        return asList(
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
    }

}
