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
package org.linkki.core.binding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.TestBind.TestBindAnnotationAspectDefinitionCreator;
import org.linkki.core.binding.TestBind.TestBindAnnotationBoundPropertyCreator;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.base.StaticModelToUiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.base.TestComponentClickAspectDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.binding.wrapper.WrapperType;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.pmo.ModelObject;

/**
 * This annotation is used to bind manually created components with a PMO and optionally a model
 * object. It is used to manually create UI layouts where linkki @UI... annotations do not give the
 * necessary flexibility. To still keep the linkki binding it is possible to annotate the created
 * field components using this annotation. Afterwards just call
 * {@link Binder#setupBindings(org.linkki.core.binding.BindingContext)} to bring the UI and the
 * model together.
 * 
 * @see Binder
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
@LinkkiBoundProperty(TestBindAnnotationBoundPropertyCreator.class)
@LinkkiAspect(TestBindAnnotationAspectDefinitionCreator.class)
public @interface TestBind {

    /**
     * The name of the PMO's property to which the UI element is bound. If it is empty (default) the
     * property name is derived from the annotated element, that means:
     * <ul>
     * <li>in case of a field it is the field name</li>
     * <li>it case of a getter method it is the method name without is/get</li>
     * <li>otherwise it is the full name of the annotated method</li>
     * </ul>
     * <p>
     * Note that for each aspect, the {@link #pmoProperty()} is evaluated before
     * {@link #modelAttribute()}. That means if an aspect method can be found with the defined
     * {@link #pmoProperty()} in the PMO, that method is used. If no method can be found in the PMO,
     * the {@link #modelAttribute()} is then used to find the method in the {@link #modelObject()}.
     * If no {@link #modelAttribute()} is defined, {@link #pmoProperty()} is used to find the method
     * in the {@link #modelObject()}.
     */
    String pmoProperty() default "";

    /**
     * The name of the {@link ModelObject} this component is bound to, by default
     * {@link ModelObject#DEFAULT_NAME}.
     */
    String modelObject() default ModelObject.DEFAULT_NAME;

    /**
     * Name of the {@linkplain #modelObject() model object's} attribute that is bound to this
     * component. If none is specified, {@link #pmoProperty()} is used.
     */
    String modelAttribute() default "";

    /** If and how the enabled state of the UI element is bound to the PMO. */
    EnabledType enabled() default EnabledType.ENABLED;

    /** If and how the visible state of the UI element is bound to the PMO. */
    VisibleType visible() default VisibleType.VISIBLE;

    /** Provides a description label next to the UI element */
    String label() default "";

    class TestBindAnnotationBoundPropertyCreator implements BoundPropertyCreator<TestBind> {

        @Override
        public BoundProperty createBoundProperty(TestBind annotation, AnnotatedElement annotatedElement) {
            return getPmoProperty(annotation, annotatedElement)
                    .withModelObject(annotation.modelObject())
                    .withModelAttribute(annotation.modelAttribute());
        }

        private BoundProperty getPmoProperty(TestBind annotation, AnnotatedElement annotatedElement) {
            String pmoPropertyName = annotation.pmoProperty();
            if (StringUtils.isEmpty(pmoPropertyName)) {
                if (annotatedElement instanceof Method) {
                    return BoundProperty.of((Method)annotatedElement);
                } else if (annotatedElement instanceof Field) {
                    return BoundProperty.of((Field)annotatedElement);
                } else {
                    throw new IllegalArgumentException("The @" + TestBind.class.getSimpleName()
                            + " annotation only supports reading the property name from " + Field.class.getSimpleName()
                            + "s and " + Method.class.getSimpleName() + "s");
                }
            } else {
                return BoundProperty.of(pmoPropertyName);
            }
        }

    }

    static class TestBindAnnotationAspectDefinitionCreator implements AspectDefinitionCreator<TestBind> {

        @Override
        public LinkkiAspectDefinition create(TestBind annotation) {
            return new CompositeAspectDefinition(
                    new EnabledAspectDefinition(annotation.enabled()),
                    new VisibleAspectDefinition(annotation.visible()),
                    new TestLabelAspectDefinition(annotation.label()),
                    new TestComponentClickAspectDefinition());
        }

    }

    static class TestLabelAspectDefinition extends StaticModelToUiAspectDefinition<String> {

        public static final String NAME = "label";

        private final String label;

        public TestLabelAspectDefinition(String label) {
            super();
            this.label = label;
        }

        @Override
        public Aspect<String> createAspect() {
            return Aspect.of(NAME, label);
        }

        @Override
        public Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
            return l -> componentWrapper.setLabel(l);
        }

        @Override
        public boolean supports(WrapperType type) {
            return WrapperType.COMPONENT.isAssignableFrom(type);
        }

    }

}
