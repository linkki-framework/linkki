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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.processing.Processor;
import javax.lang.model.element.VariableElement;

import org.junit.jupiter.api.Test;
import org.linkki.core.defaults.ui.aspects.annotations.BindTooltip;
import org.linkki.core.defaults.ui.aspects.types.TooltipType;
import org.linkki.core.ui.aspects.annotation.BindReadOnly;
import org.linkki.core.ui.aspects.annotation.BindReadOnly.ReadOnlyType;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.tooling.compiler.SourceFile;
import org.linkki.tooling.processor.BaseAnnotationProcessorTest;

public class ModelBuilderTest extends BaseAnnotationProcessorTest {

    private MockProcessorForModelCreation processor = null;

    @Override
    protected Processor createProcessor() {
        return processor = new MockProcessorForModelCreation();
    }

    @Test
    public void shouldCreateNonEmptyPmo() {
        testPmo(Arrays.asList("Person.java", "model/NonEmptyPmo.java"), pmo -> {

            List<AptModelObject> modelObjects = pmo.getModelObjects();
            assertEquals(modelObjects.size(), 1);

            AptModelObject modelObject = modelObjects.get(0);
            assertEquals(modelObject.getAnnotation().name(), "modelObject");

            List<AptComponent> components = pmo.getComponents();
            assertEquals(components.size(), 2);

            {
                AptComponent component = components.get(0);
                assertTrue(component.isDynamicField());
                assertEquals(component.getComponentDeclarations().size(), 2);
                assertEquals(component.getAspectBindings().size(), 1);

                {
                    AptComponentDeclaration componentDeclaration1 = component.getComponentDeclarations().get(0);
                    assertEquals(componentDeclaration1.getPropertyName(), "firstname");
                    String annotationName = componentDeclaration1.getAnnotationMirror().getAnnotationType().asElement()
                            .getSimpleName().toString();
                    assertEquals(annotationName, UITextField.class.getSimpleName());
                    assertEquals(componentDeclaration1.getPosition(), 10);
                    assertEquals(componentDeclaration1.getLabel(), Optional.of("firstname"));
                    assertEquals(componentDeclaration1.getModelObject().map(it -> it.getAnnotation().name()),
                                 Optional.of("modelObject"));
                    assertEquals(componentDeclaration1.getModelAttribute().map(it -> it.getName()),
                                 Optional.of("firstname"));
                }

                {
                    AptComponentDeclaration componentDeclaration2 = component.getComponentDeclarations().get(1);
                    assertEquals(componentDeclaration2.getPropertyName(), "firstname");
                    String annotationName = componentDeclaration2.getAnnotationMirror().getAnnotationType().asElement()
                            .getSimpleName().toString();
                    assertEquals(annotationName, UITextArea.class.getSimpleName());
                    assertEquals(componentDeclaration2.getPosition(), 10);
                    assertEquals(componentDeclaration2.getLabel(), Optional.of("firstname"));
                    assertEquals(componentDeclaration2.getModelObject().map(it -> it.getAnnotation().name()),
                                 Optional.of("modelObject"));
                    assertEquals(componentDeclaration2.getModelAttribute().map(it -> it.getName()),
                                 Optional.of("firstname"));
                }

                AptAspectBinding aspectBinding = component.getAspectBindings().get(0);
                assertEquals(aspectBinding.getName(), BindReadOnly.class.getSimpleName());
                assertEquals(aspectBinding.getAttributes().size(), 1);

                AptAttribute attribute = aspectBinding.getAttributes().get(0);
                assertEquals(attribute.getName(), "value");
                assertEquals(((VariableElement)attribute.getValue()).getSimpleName().toString(),
                             ReadOnlyType.ALWAYS.name());
            }

            {

                AptComponent component = components.get(1);
                assertTrue(component.isDynamicField());
                assertEquals(component.getComponentDeclarations().size(), 2);
                assertEquals(component.getAspectBindings().size(), 1);

                {
                    AptComponentDeclaration componentDeclaration1 = component.getComponentDeclarations().get(0);
                    assertEquals(componentDeclaration1.getPropertyName(), "lastname");
                    String annotationName = componentDeclaration1.getAnnotationMirror().getAnnotationType().asElement()
                            .getSimpleName().toString();
                    assertEquals(annotationName, UITextField.class.getSimpleName());
                    assertEquals(componentDeclaration1.getPosition(), 20);
                    assertEquals(componentDeclaration1.getLabel(), Optional.of("lastname"));
                    assertEquals(componentDeclaration1.getModelObject().map(it -> it.getAnnotation().name()),
                                 Optional.of("modelObject"));
                    assertEquals(componentDeclaration1.getModelAttribute().map(it -> it.getName()),
                                 Optional.of("lastname"));
                }

                {
                    AptComponentDeclaration componentDeclaration2 = component.getComponentDeclarations().get(1);
                    assertEquals(componentDeclaration2.getPropertyName(), "lastname");
                    String annotationName = componentDeclaration2.getAnnotationMirror().getAnnotationType().asElement()
                            .getSimpleName().toString();
                    assertEquals(annotationName, UITextArea.class.getSimpleName());
                    assertEquals(componentDeclaration2.getPosition(), 20);
                    assertEquals(componentDeclaration2.getLabel(), Optional.of("lastname"));
                    assertEquals(componentDeclaration2.getModelObject().map(it -> it.getAnnotation().name()),
                                 Optional.of("modelObject"));
                }

                AptAspectBinding aspectBinding = component.getAspectBindings().get(0);
                assertEquals(aspectBinding.getName(), BindTooltip.class.getSimpleName());
                assertEquals(aspectBinding.getAttributes().size(), 2);

                AptAttribute attribute1 = aspectBinding.getAttributes().get(0);
                assertEquals(attribute1.getName(), "value");
                assertEquals(attribute1.getValue(), "lastname");

                AptAttribute attribute2 = aspectBinding.getAttributes().get(1);
                assertEquals(attribute2.getName(), "tooltipType");
                assertEquals(((VariableElement)attribute2.getValue()).getSimpleName().toString(),
                             TooltipType.STATIC.name());
            }
        });
    }

    private void testPmo(List<String> sources, Consumer<AptPmo> testFunction) {
        List<SourceFile> files = sources.stream()
                .map(BaseAnnotationProcessorTest::getSourceFile)
                .collect(Collectors.toList());

        compile(files);

        Optional<AptPmo> pmo = processor.getPmo();
        assertTrue(pmo.isPresent(), "expected pmo to be present");
        pmo.ifPresent(testFunction);
    }

}
