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


package org.linkki.tooling.compiler;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Completion;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class TestProcessor implements Processor {

    private final Processor delegate;
    private final Messager messager;

    public TestProcessor(Processor delegate, Messager messager) {
        this.delegate = delegate;
        this.messager = messager;
    }

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        ProcessingEnvironment testProcessingEnvironment = new TestProcessingEnvironment(processingEnv, messager);
        delegate.init(testProcessingEnvironment);
    }

    @Override
    public Iterable<? extends Completion> getCompletions(Element element,
            AnnotationMirror annotation,
            ExecutableElement member,
            String userText) {
        return delegate.getCompletions(element, annotation, member, userText);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return delegate.getSupportedAnnotationTypes();
    }

    @Override
    public Set<String> getSupportedOptions() {
        return delegate.getSupportedOptions();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return delegate.getSupportedSourceVersion();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return delegate.process(annotations, roundEnv);
    }

    private static final class TestProcessingEnvironment implements ProcessingEnvironment {

        private final ProcessingEnvironment processingEnv;
        private final Messager messager;

        private TestProcessingEnvironment(ProcessingEnvironment processingEnv, Messager messager) {
            this.processingEnv = processingEnv;
            this.messager = messager;
        }

        @Override
        public Types getTypeUtils() {
            return processingEnv.getTypeUtils();
        }

        @Override
        public SourceVersion getSourceVersion() {
            return processingEnv.getSourceVersion();
        }

        @Override
        public Map<String, String> getOptions() {
            return processingEnv.getOptions();
        }

        @Override
        public Messager getMessager() {
            return messager;
        }

        @Override
        public Locale getLocale() {
            return processingEnv.getLocale();
        }

        @Override
        public Filer getFiler() {
            return processingEnv.getFiler();
        }

        @Override
        public Elements getElementUtils() {
            return processingEnv.getElementUtils();
        }
    }

}
