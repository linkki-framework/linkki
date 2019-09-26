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

package org.linkki.tooling.model;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Optional;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import org.linkki.tooling.util.ElementUtils;
import org.linkki.tooling.util.ModelBuilder;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("*")
@SuppressFBWarnings(value = "NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR", justification = "Processor needs a zero args constructor, fields are set in init")
public class MockProcessorForModelCreation extends AbstractProcessor {

    private Optional<AptPmo> pmo = Optional.empty();
    private ElementUtils elementUtils;
    private ModelBuilder modelBuilder;

    @SuppressWarnings("resource")
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.elementUtils = new ElementUtils(
                processingEnvironment.getTypeUtils(),
                processingEnvironment.getElementUtils(),
                new URLClassLoader(new URL[0]));
        this.modelBuilder = new ModelBuilder(elementUtils);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }

        if (!pmo.isPresent()) {

            pmo = elementUtils.getClassElements(annotations, roundEnv)
                    .findFirst()
                    .map(modelBuilder::convertPmo);
        }

        return true;
    }


    public Optional<AptPmo> getPmo() {
        return pmo;
    }
}
