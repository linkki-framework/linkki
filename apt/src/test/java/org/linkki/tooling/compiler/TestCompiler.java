package org.linkki.tooling.compiler;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.annotation.processing.Processor;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.ui.aspects.annotation.BindReadOnly;
import org.linkki.core.ui.element.annotation.UIButton;
import org.linkki.core.ui.element.annotation.UICheckBox;
import org.linkki.core.ui.element.annotation.UIComboBox;
import org.linkki.core.ui.element.annotation.UIDateField;
import org.linkki.core.ui.element.annotation.UIDoubleField;
import org.linkki.core.ui.element.annotation.UIIntegerField;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UISubsetChooser;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.element.annotation.UIYesNoComboBox;
import org.linkki.core.ui.table.column.annotation.UITableColumn;
import org.linkki.tooling.processor.LinkkiAnnotationProcessor;

public class TestCompiler {
    private static final BiFunction<String, String, String> ANNOTATION_OPTION = (option, value) -> "-A" + option + '='
            + value;

    private List<String> classPathEntries = new ArrayList<>();

    private final File sourceDir;
    private final File outputDir;
    private final List<String> options;
    private final Map<String, String> linkkiOptions;

    public TestCompiler() throws IOException {
        addClassPathsOf(asList(TestCompiler.class, LinkkiAnnotationProcessor.class));
        addClassPathsOf(classOfUIComponents());

        this.sourceDir = createTempDir("sourceDir");
        this.outputDir = createTempDir("outputDir");
        options = new ArrayList<>(asList(
                                         "-cp", buildClassPath(),
                                         "-d", outputDir.getAbsolutePath()));
        this.linkkiOptions = new HashMap<>();
    }

    public void addAnnotationProcessorOption(String option, String value) {
        this.linkkiOptions.put(LinkkiAnnotationProcessor.LINKKI_OPTION_PREFIX + '.' + option, value);
    }

    public void addOptions(Map<String, String> newOptions) {
        this.linkkiOptions.putAll(newOptions.entrySet().stream()
                .collect(toMap(
                               it -> LinkkiAnnotationProcessor.LINKKI_OPTION_PREFIX + '.' + it.getKey(),
                               it -> it.getValue())));
    }

    public void cleanUp() {
        FileUtils.deleteQuietly(outputDir);
        FileUtils.deleteQuietly(sourceDir);
    }

    public boolean compile(Processor processor, SourceFile... sourceFiles) throws IOException {
        return compile(processor, asList(sourceFiles));
    }

    public boolean compile(Processor processor, List<SourceFile> sourceFiles) throws IOException {
        List<File> files = sourceFiles.stream()
                .map(this::writeSourceFile)
                .collect(toList());

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            Iterable<? extends JavaFileObject> javaFileObjects = fileManager
                    .getJavaFileObjects(files.toArray(new File[0]));
            List<String> formatedOptions = linkkiOptions.entrySet().stream()
                    .map(it -> ANNOTATION_OPTION.apply(it.getKey(), it.getValue()))
                    .collect(toList());
            options.addAll(formatedOptions);
            CompilationTask compilationTask = compiler.getTask(null, null, null, options, null, javaFileObjects);
            compilationTask.setProcessors(asList(processor));
            boolean success = compilationTask.call();
            return success;
        }
    }

    public void addClassPathOf(Class<?> clazz) {
        classPathEntries.add(classPathOf(clazz));
    }

    public void addClassPathsOf(Collection<Class<?>> classes) {
        classPathEntries.addAll(classes.stream().map(TestCompiler::classPathOf).collect(toSet()));
    }

    private String buildClassPath() {
        ArrayList<String> classPathElements = new ArrayList<>(classPathEntries);

        classPathElements.add(outputDir.getAbsolutePath());
        return classPathElements.stream().collect(joining(System.getProperty("path.separator")));
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
        return clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
    }

    private static Collection<Class<?>> classOfUIComponents() {
        return asList(
                      UIButton.class,
                      UICheckBox.class,
                      UIComboBox.class,
                      UIDateField.class,
                      UIDoubleField.class,
                      UIIntegerField.class,
                      UILabel.class,
                      UISubsetChooser.class,
                      UITableColumn.class,
                      UITextArea.class,
                      UITextField.class,
                      BindTooltip.class,
                      BindReadOnly.class,
                      UIYesNoComboBox.class);
    }
}
