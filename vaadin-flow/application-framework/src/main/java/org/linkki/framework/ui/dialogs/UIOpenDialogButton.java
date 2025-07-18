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
package org.linkki.framework.ui.dialogs;

import static org.linkki.util.Objects.requireNonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.util.function.Consumer;
import java.util.function.Function;

import org.linkki.core.binding.LinkkiBindingException;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.aspect.annotation.AspectDefinitionCreator;
import org.linkki.core.binding.descriptor.aspect.annotation.LinkkiAspect;
import org.linkki.core.binding.descriptor.aspect.base.CompositeAspectDefinition;
import org.linkki.core.binding.descriptor.property.annotation.BoundPropertyCreator.SimpleMemberNameBoundPropertyCreator;
import org.linkki.core.binding.descriptor.property.annotation.LinkkiBoundProperty;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.behavior.PropertyBehaviorProvider;
import org.linkki.core.binding.uicreation.LinkkiComponent;
import org.linkki.core.binding.uicreation.LinkkiComponentDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.ui.aspects.types.CaptionType;
import org.linkki.core.ui.aspects.CaptionAspectDefinition;
import org.linkki.core.ui.aspects.LabelAspectDefinition;
import org.linkki.core.uicreation.ComponentDefinitionCreator;
import org.linkki.core.uicreation.LinkkiPositioned;
import org.linkki.util.handler.Handler;

import com.vaadin.flow.component.button.Button;

/**
 * Creates a button that opens a dialog upon click and updates the underlying binding context after
 * OK Button click in dialog.
 * <p>
 * The annotated method must be a getter method. The return value defines how to create the dialog
 * using one of the options:
 * <ol>
 * <li>Return a {@link DialogPmo} (<code>public DialogPmo get[propertyName]()</code>). This is only
 * viable if the content of the dialog can be defined with a PMO.</li>
 * <li>Return a {@link Function} that creates a {@link OkCancelDialog} from a {@link Handler}
 * (<code>public Function&lt;Handler, OkCancelDialog&gt; get[propertyName]()</code>). The argument
 * of the function is the model changed handler that will update the binding context. The created
 * dialog must make sure that this handler is called when the OK button is clicked.
 * {@link Handler#compose(Consumer)} and {@link Handler#compose(Handler)} may come in handy.</li>
 * </ol>
 * <p>
 * To display an icon on the button use {@link org.linkki.core.ui.aspects.annotation.BindIcon}.
 * <p>
 * Note that this annotation is an experimental feature that may be subject of API change in the
 * near future.
 *
 * @since 2.8.0 (experimental)
 */

@LinkkiAspect(UIOpenDialogButton.DialogButtonAspectDefinitionCreator.class)
@LinkkiComponent(UIOpenDialogButton.DialogButtonComponentDefinitionCreator.class)
@LinkkiBoundProperty(SimpleMemberNameBoundPropertyCreator.class)
@LinkkiPositioned
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UIOpenDialogButton {

    /** Defines the order in which this element is displayed in a layout. */
    @LinkkiPositioned.Position
    int position();

    /**
     * Provides a description label next to the button
     */
    String label() default "";

    /**
     * Text displayed on the button. If the value should be determined dynamically, use
     * {@link CaptionType#DYNAMIC} instead and ignore this attribute.
     */
    String caption() default LinkkiAspectDefinition.DERIVED_BY_LINKKI;

    /**
     * Defines how the caption text should be retrieved
     */
    CaptionType captionType() default CaptionType.STATIC;

    class DialogButtonComponentDefinitionCreator implements ComponentDefinitionCreator<UIOpenDialogButton> {

        public LinkkiComponentDefinition create(UIOpenDialogButton annotation, AnnotatedElement annotatedElement) {
            return pmo -> new Button(annotation.caption());
        }
    }

    class DialogButtonAspectDefinitionCreator implements AspectDefinitionCreator<UIOpenDialogButton> {

        public LinkkiAspectDefinition create(UIOpenDialogButton annotation) {
            return new CompositeAspectDefinition(
                    new OpenDialogButtonAspectDefinition(),
                    new CaptionAspectDefinition(annotation.captionType(), annotation.caption()),
                    new LabelAspectDefinition(annotation.label()));
        }
    }

    class OpenDialogButtonAspectDefinition implements LinkkiAspectDefinition {

        @Override
        public void initModelUpdate(PropertyDispatcher propertyDispatcher,
                ComponentWrapper componentWrapper,
                Handler modelChanged) {
            var button = (Button)componentWrapper.getComponent();
            button.addClickListener(e -> {
                var aspectValue = propertyDispatcher.pull(Aspect.of(VALUE_ASPECT_NAME));
                requireNonNull(aspectValue, "Aspect method must not return null");
                if (aspectValue instanceof DialogPmo dialogPmo) {
                    var okHandler = dialogPmo.getOkHandler()
                            .andThen(modelChanged);
                    new PmoBasedDialogFactory(dialogPmo::validate, PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER,
                            dialogPmo.getPropertyDispatcherFactory())
                                    .openOkCancelDialog(dialogPmo.getCaption(), okHandler, dialogPmo.getContentPmo());
                } else {
                    try {
                        @SuppressWarnings("unchecked")
                        var dialogCreator = (Function<Handler, OkCancelDialog>)aspectValue;
                        dialogCreator.apply(modelChanged).open();
                    } catch (ClassCastException exception) {
                        throw new LinkkiBindingException(
                                "Aspect method must either return a DialogPmo or a Function<Handler, OkCancelDialog>",
                                exception);
                    }
                }
            });
        }

        public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
            return Handler.NOP_HANDLER;
        }
    }
}
