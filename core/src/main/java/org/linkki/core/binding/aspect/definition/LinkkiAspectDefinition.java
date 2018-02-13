/*
 * Copyright Faktor Zehn AG.
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

package org.linkki.core.binding.aspect.definition;

import java.lang.annotation.Annotation;

import org.linkki.core.binding.Binding;
import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.binding.aspect.LinkkiAspect;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.ui.components.ComponentWrapper;
import org.linkki.util.handler.Handler;

/**
 * The definition for {@link Aspect aspects} that applies to a {@link Binding}.
 * <p>
 * One typical example for such an aspect is the value of a property. When the value of a property
 * is changed in the model, the UI components that are binded to this property are has to be updated
 * accordingly. Analogously, the model has to be updated if the the value is changed by an input in
 * a UI component.
 * <p>
 * A {@link LinkkiAspectDefinition} is instantiated for every occurrence in the pmo for example for
 * every annotation that references an aspect definition. Therefore it is allowed to keep and reuse
 * the annotation that is provided by {@link #initialize(Annotation)}. But, for example, in a table
 * the same PMO might be used for multiple rows. That means it is not allowed to keep any state
 * about the {@link PropertyDispatcher} or the {@link ComponentWrapper}.
 */
public interface LinkkiAspectDefinition {

    /**
     * Defines how the the asepct has to be updated.
     * <p>
     * Depending on the aspect, the aspect has to react to changes in the model and update the UI
     * accordingly, or/and vice versa.
     * <p>
     * An aspect does not have to be bidirectional. Some aspects of an UI component such as the
     * visibility can only be modified by the model. Thus the update of the aspect only have to
     * react to changes in the model.
     * 
     * @param propertyDispatcher dispatcher chain that may define/overwrite the value of an
     *            {@link Aspect}
     * @param componentWrapper UI component that may need to be updated
     */
    Handler createUiUpdater(PropertyDispatcher propertyDispatcher,
            ComponentWrapper componentWrapper);

    /**
     * Initialize UI component to notify for changes and updates the model accordingly.
     * <p>
     * Important: it is not allowed to keep the state of any parameter because this method might be
     * called for multiple components.
     * 
     * @param propertyDispatcher dispatcher chain that may define/overwrite the value of an
     *            {@link Aspect}
     * @param componentWrapper UI component to watch for changes
     * @param modelUpdated update logic that needs to be applied to update the UI upon model changes
     */
    default void initModelUpdate(PropertyDispatcher propertyDispatcher,
            ComponentWrapper componentWrapper,
            Handler modelUpdated) {
        // does nothing
    }

    /**
     * Initializes the aspect by providing the annotation that is responsible for this aspect. The
     * annotation may hold information such as a static value or anything necessary for value post
     * processing.
     * <p>
     * In contrast to the other methods this method is called directly after instantiating. This is
     * necessary because the information about the annotation is accessible at a different time.
     * 
     * @param annotation the annotation that defines the UI element and is annotated with
     *            {@link LinkkiAspect}
     */
    void initialize(Annotation annotation);
}
