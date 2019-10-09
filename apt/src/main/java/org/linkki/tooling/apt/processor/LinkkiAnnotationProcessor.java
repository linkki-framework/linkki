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

package org.linkki.tooling.apt.processor;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.linkki.tooling.apt.util.ElementUtils;
import org.linkki.tooling.apt.util.ModelBuilder;
import org.linkki.tooling.apt.validator.AspectMethodValidator;
import org.linkki.tooling.apt.validator.AvailableValuesTypeValidator;
import org.linkki.tooling.apt.validator.DynamicFieldValidator;
import org.linkki.tooling.apt.validator.ModelBindingValidator;
import org.linkki.tooling.apt.validator.ModelObjectValidator;
import org.linkki.tooling.apt.validator.PositionValidator;
import org.linkki.tooling.apt.validator.PublicModifierValidator;
import org.linkki.tooling.apt.validator.Validator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * This {@link Processor} creates a model from the processed class and checks it with a list of
 * {@link Validator Validators}.
 */
@SupportedAnnotationTypes("*")
@SuppressFBWarnings(value = "NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR", justification = "Processor needs a zero args constructor, fields are set in init")
public class LinkkiAnnotationProcessor extends AbstractProcessor {

    public static final String LINKKI_OPTION_PREFIX = "linkki.apt";

    private static final Set<ElementKind> SUPPORTED_ELEMENT_KINDS = new HashSet<>(
            asList(ElementKind.CLASS, ElementKind.INTERFACE));

    private Types types;
    private Elements elements;

    private ElementUtils elementUtils;
    private ModelBuilder modelBuilder;

    private List<Validator> validators;

    private ClassLoader classLoader;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public Set<String> getSupportedOptions() {
        Stream<String> classpathOption = Stream.of("classpath");

        Stream<String> validatorOptions = validators.stream()
                .map(Validator::getClass)
                .map(Validator::getMessageCodes)
                .flatMap(List::stream)
                .map(LinkkiAnnotationProcessor::toLinkkiAptOption);

        return Stream.concat(classpathOption, validatorOptions)
                .collect(Collectors.toSet());
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        Map<String, String> options = processingEnvironment.getOptions();

        classLoader = getClassLoader(options);
        types = processingEnvironment.getTypeUtils();
        elements = processingEnvironment.getElementUtils();
        elementUtils = new ElementUtils(types, elements, classLoader);
        modelBuilder = new ModelBuilder(elementUtils);
        validators = asList(new PublicModifierValidator(options, elementUtils),
                            new ModelObjectValidator(options),
                            new PositionValidator(options),
                            new AspectMethodValidator(options, elementUtils),
                            new ModelBindingValidator(options),
                            new DynamicFieldValidator(options, elementUtils),
                            new AvailableValuesTypeValidator(options, elementUtils));
    }

    private ClassLoader getClassLoader(Map<String, String> options) {
        String option = options.get("classpath");

        if (option == null) {
            return getClass().getClassLoader();
        } else {
            URL[] urls = Stream.of(option.split(";", -1))
                    .filter(it -> !it.isEmpty())
                    .map(t -> {
                        try {
                            return new File(t).toURI().toURL();
                        } catch (IOException e) {
                            // ignore
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList())
                    .toArray(new URL[0]);

            return new URLClassLoader(urls, getClass().getClassLoader());
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }

        if (annotations.isEmpty()) {
            return false;
        }
        try {
            Messager messager = processingEnv.getMessager();
            elementUtils.getClassElements(annotations, roundEnv)
                    .filter(this::isPublic)
                    .filter(this::isClassOrInterface)
                    .map(modelBuilder::convertPmo)
                    .forEach(pmo -> {
                        try {
                            validators.forEach(val -> val.validate(pmo, messager));
                        } catch (IllegalStateException | IllegalArgumentException | NoSuchElementException t) {
                            String qualifiedName = pmo.getElement().getQualifiedName().toString();
                            printExceptionInfo(messager, qualifiedName, t);
                        }
                    });
        } finally {
            if (classLoader != getClass().getClassLoader()) {
                if (classLoader instanceof URLClassLoader) {
                    try {
                        ((URLClassLoader)classLoader).close();
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }
        }

        return true;
    }

    /**
     * Tells whether an {@link Element element} is public.
     * 
     * @param element the element
     * @return <code>true</code> if there is the public modifier among its modifiers, otherwise
     *         <code>false</code>.
     */
    private boolean isPublic(Element element) {
        return element.getModifiers().contains(Modifier.PUBLIC);
    }

    /**
     * Tells whether the {@link TypeElement} represents a class or an interface.
     * 
     * @param element the type
     * @return <code>true</code> if kind is class or interface, otherwise <code>false</code>.
     */
    private boolean isClassOrInterface(TypeElement element) {
        return SUPPORTED_ELEMENT_KINDS.contains(element.getKind());
    }

    private void printExceptionInfo(Messager messager, String className, Throwable thrown) {
        String trace = ExceptionUtils.getStackTrace(thrown);

        messager.printMessage(Kind.ERROR,
                              "An exception was thrown while processing the class "
                                      + className + ":\n" + trace);
    }

    public static String toLinkkiAptOption(String messageCode) {
        return LinkkiAnnotationProcessor.LINKKI_OPTION_PREFIX + '.' + messageCode;
    }

    public Types getTypes() {
        return types;
    }

    public Elements getElements() {
        return elements;
    }

    public ElementUtils getElementUtils() {
        return elementUtils;
    }

}