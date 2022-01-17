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

package org.linkki.tooling.apt.model;

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
import org.linkki.core.ui.aspects.annotation.BindVisible;
import org.linkki.core.ui.element.annotation.UITextArea;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.tooling.apt.compiler.SourceFile;
import org.linkki.tooling.apt.validator.BaseAnnotationProcessorTest;

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
            assertEquals(1, modelObjects.size());

            AptModelObject modelObject = modelObjects.get(0);
            assertEquals("modelObject", modelObject.getAnnotation().name());

            List<AptComponent> components = pmo.getComponents();
            assertEquals(2, components.size());

            {
                AptComponent component = components.get(0);
                assertTrue(component.isDynamicField());
                assertEquals(2, component.getComponentDeclarations().size());
                assertEquals(1, component.getAspectBindings().size());

                {
                    AptComponentDeclaration componentDeclaration1 = component.getComponentDeclarations().get(0);
                    assertEquals("firstname", componentDeclaration1.getPropertyName());
                    String annotationName = componentDeclaration1.getAnnotationMirror().getAnnotationType().asElement()
                            .getSimpleName().toString();
                    assertEquals(UITextField.class.getSimpleName(), annotationName);
                    assertEquals(10, componentDeclaration1.getPosition());
                    assertEquals(Optional.of("firstname"), componentDeclaration1.getLabel());
                    assertEquals(Optional.of("modelObject"),
                                 componentDeclaration1.getModelObject().map(it -> it.getAnnotation().name()));
                    assertEquals(Optional.of("firstname"),
                                 componentDeclaration1.getModelAttribute().map(it -> it.getName()));
                }

                {
                    AptComponentDeclaration componentDeclaration2 = component.getComponentDeclarations().get(1);
                    assertEquals("firstname", componentDeclaration2.getPropertyName());
                    String annotationName = componentDeclaration2.getAnnotationMirror().getAnnotationType().asElement()
                            .getSimpleName().toString();
                    assertEquals(UITextArea.class.getSimpleName(), annotationName);
                    assertEquals(10, componentDeclaration2.getPosition());
                    assertEquals(Optional.of("firstname"), componentDeclaration2.getLabel());
                    assertEquals(Optional.of("modelObject"),
                                 componentDeclaration2.getModelObject().map(it -> it.getAnnotation().name()));
                    assertEquals(Optional.of("firstname"),
                                 componentDeclaration2.getModelAttribute().map(it -> it.getName()));
                }

                AptAspectBinding aspectBinding = component.getAspectBindings().get(0);
                assertEquals(BindReadOnly.class.getSimpleName(), aspectBinding.getName());
                assertEquals(1, aspectBinding.getAttributes().size());

                AptAttribute attribute = aspectBinding.getAttributes().get(0);
                assertEquals("value", attribute.getName());
                assertEquals(((VariableElement)attribute.getValue()).getSimpleName().toString(),
                             ReadOnlyType.ALWAYS.name());
            }

            {

                AptComponent component = components.get(1);
                assertTrue(component.isDynamicField());
                assertEquals(2, component.getComponentDeclarations().size());
                assertEquals(2, component.getAspectBindings().size());

                {
                    AptComponentDeclaration componentDeclaration1 = component.getComponentDeclarations().get(0);
                    assertEquals("lastname", componentDeclaration1.getPropertyName());
                    String annotationName = componentDeclaration1.getAnnotationMirror().getAnnotationType().asElement()
                            .getSimpleName().toString();
                    assertEquals(UITextField.class.getSimpleName(), annotationName);
                    assertEquals(20, componentDeclaration1.getPosition());
                    assertEquals(Optional.of("lastname"), componentDeclaration1.getLabel());
                    assertEquals(componentDeclaration1.getModelObject().map(it -> it.getAnnotation().name()),
                                 Optional.of("modelObject"));
                    assertEquals(componentDeclaration1.getModelAttribute().map(it -> it.getName()),
                                 Optional.of("lastname"));
                }

                {
                    AptComponentDeclaration componentDeclaration2 = component.getComponentDeclarations().get(1);
                    assertEquals("lastname", componentDeclaration2.getPropertyName());
                    String annotationName = componentDeclaration2.getAnnotationMirror().getAnnotationType().asElement()
                            .getSimpleName().toString();
                    assertEquals(UITextArea.class.getSimpleName(), annotationName);
                    assertEquals(20, componentDeclaration2.getPosition());
                    assertEquals(Optional.of("lastname"), componentDeclaration2.getLabel());
                    assertEquals(componentDeclaration2.getModelObject().map(it -> it.getAnnotation().name()),
                                 Optional.of("modelObject"));
                }

                AptAspectBinding aspectBinding = component.getAspectBindings().get(0);
                assertEquals(BindTooltip.class.getSimpleName(), aspectBinding.getName());
                assertEquals(2, aspectBinding.getAttributes().size());

                AptAttribute attribute1 = aspectBinding.getAttributes().get(0);
                assertEquals("value", attribute1.getName());
                assertEquals("lastname", attribute1.getValue());

                AptAttribute attribute2 = aspectBinding.getAttributes().get(1);
                assertEquals("tooltipType", attribute2.getName());
                assertEquals(((VariableElement)attribute2.getValue()).getSimpleName().toString(),
                             TooltipType.AUTO.name());

                AptAspectBinding aspectBindingVisible = component.getAspectBindings().get(1);
                assertEquals(BindVisible.class.getSimpleName(), aspectBindingVisible.getName());
                assertEquals(0, aspectBindingVisible.getAttributes().size());
                assertTrue(aspectBindingVisible.getElement().getSimpleName().contentEquals("lastname"));
                assertTrue(pmo.getElement().getEnclosedElements().stream()
                        .filter(e -> e.getSimpleName().contentEquals("isLastnameVisible")).findFirst().isPresent());
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
