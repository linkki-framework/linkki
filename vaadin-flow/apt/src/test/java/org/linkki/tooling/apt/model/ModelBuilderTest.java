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

package org.linkki.tooling.apt.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
import org.linkki.tooling.apt.validator.AbstractAnnotationProcessorTest;

class ModelBuilderTest extends AbstractAnnotationProcessorTest {

    private MockProcessorForModelCreation processor = null;

    @Override
    protected Processor createProcessor() {
        return processor = new MockProcessorForModelCreation();
    }

    @Test
    void shouldCreateNonEmptyPmo() {
        var pmo = createTestPmo(List.of("Person.java", "model/NonEmptyPmo.java"));

        assertThat(pmo.getModelObjects())
                .map(o -> o.getAnnotation().name())
                .containsExactly("modelObject");

        var components = pmo.getComponents();
        assertThat(components).map(AptComponent::isDynamicField)
                .containsExactly(true, true);

        var firstnameComponent = components.getFirst();
        assertThat(firstnameComponent.getComponentDeclarations()).satisfiesExactly(d -> {
            assertThat(d.getPropertyName()).isEqualTo("firstname");
            assertThat(annotationNameOf(d)).isEqualTo(UITextField.class.getSimpleName());
            assertThat(d.getPosition()).isEqualTo(10);
            assertThat(d.getLabel()).hasValue("firstname");
            assertThat(d.getModelObject().map(o -> o.getAnnotation().name())).hasValue("modelObject");
            assertThat(d.getModelAttribute().map(AptModelAttribute::getName)).hasValue("firstname");
        }, d -> {
            assertThat(d.getPropertyName()).isEqualTo("firstname");
            assertThat(annotationNameOf(d)).isEqualTo(UITextArea.class.getSimpleName());
            assertThat(d.getPosition()).isEqualTo(10);
            assertThat(d.getLabel()).hasValue("firstname");
            assertThat(d.getModelObject().map(o -> o.getAnnotation().name())).hasValue("modelObject");
            assertThat(d.getModelAttribute().map(AptModelAttribute::getName)).hasValue("firstname");
        });
        assertThat(firstnameComponent.getAspectBindings()).satisfiesExactly(a -> {
            assertThat(a.getName()).isEqualTo(BindReadOnly.class.getSimpleName());
            assertThat(a.getAttributes()).satisfiesExactly(attr -> {
                assertThat(attr.getName()).isEqualTo("value");
                assertThat(((VariableElement)attr.getValue()).getSimpleName())
                        .hasToString(ReadOnlyType.ALWAYS.name());
            });
        });

        var lastnameComponent = components.get(1);
        assertThat(lastnameComponent.getComponentDeclarations()).satisfiesExactly(d -> {
            assertThat(d.getPropertyName()).isEqualTo("lastname");
            assertThat(annotationNameOf(d)).isEqualTo(UITextField.class.getSimpleName());
            assertThat(d.getPosition()).isEqualTo(20);
            assertThat(d.getLabel()).hasValue("lastname");
            assertThat(d.getModelObject().map(o -> o.getAnnotation().name())).hasValue("modelObject");
            assertThat(d.getModelAttribute().map(AptModelAttribute::getName)).hasValue("lastname");
        }, d -> {
            assertThat(d.getPropertyName()).isEqualTo("lastname");
            assertThat(annotationNameOf(d)).isEqualTo(UITextArea.class.getSimpleName());
            assertThat(d.getPosition()).isEqualTo(20);
            assertThat(d.getLabel()).hasValue("lastname");
            assertThat(d.getModelObject().map(o -> o.getAnnotation().name())).hasValue("modelObject");
            assertThat(d.getModelAttribute().map(AptModelAttribute::getName)).hasValue("lastname");
        });
        assertThat(lastnameComponent.getAspectBindings()).satisfiesExactly(a -> {
            assertThat(a.getName()).isEqualTo(BindTooltip.class.getSimpleName());
            assertThat(a.getAttributes()).satisfiesExactly(attr -> {
                assertThat(attr.getName()).isEqualTo("value");
                assertThat(attr.getValue()).isEqualTo("lastname");
            }, attr -> {
                assertThat(attr.getName()).isEqualTo("tooltipType");
                assertThat(((VariableElement)attr.getValue()).getSimpleName())
                        .hasToString(TooltipType.AUTO.name());
            });
        }, a -> {
            assertThat(a.getName()).isEqualTo(BindVisible.class.getSimpleName());
            assertThat(a.getAttributes()).isEmpty();
            assertThat(a.getElement().getSimpleName()).hasToString("lastname");
            assertThat(pmo.getElement().getEnclosedElements())
                    .anyMatch(e -> e.getSimpleName().contentEquals("isLastnameVisible"));
        });
    }

    private static String annotationNameOf(AptComponentDeclaration d) {
        return d.getAnnotationMirror().getAnnotationType().asElement().getSimpleName().toString();
    }

    private AptPmo createTestPmo(List<String> sources) {
        var files = sources.stream()
                .map(AbstractAnnotationProcessorTest::getSourceFile)
                .toList();

        compile(files);

        var pmo = processor.getPmo();
        assertThat(pmo).isPresent();
        return pmo.get();
    }
}
