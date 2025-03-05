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

package org.linkki.core.ui.aspects;

import static org.linkki.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.ui.nls.NlsText;
import org.linkki.util.handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;

import edu.umd.cs.findbugs.annotations.CheckForNull;

public abstract class FutureAwareAspectDefinition<T> extends ModelToUiAspectDefinition<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FutureAwareAspectDefinition.class);

    private static final String MSG_CODE_ERROR_MESSAGE = "FutureAwareAspectDefinition.loadingError";

    private static final String ATTR_LOADING = "content-loading";
    private static final String ATTR_HAS_ERROR = "has-loading-error";
    private static final String CSS_PROPERTY_ERROR_MESSAGES = "--loading-error-message";

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        var aspect = createAspect();
        var ui = UI.getCurrent();
        if (CompletableFuture.class.isAssignableFrom(propertyDispatcher.getValueClass())) {
            if (!ui.getPushConfiguration().getPushMode().isEnabled() && LOGGER.isWarnEnabled()) {
                LOGGER.warn(("""
                        CompletableFuture is used for aspect %s although server push is not \
                        enabled.

                        This will cause update to be not reflected in the UI immediately. (%s)""")
                        .formatted(getClass().getSimpleName(),
                                   Optional.ofNullable(propertyDispatcher.getBoundObject())
                                           .map(Object::getClass)
                                           .map(Class::getName)
                                           .orElse("null") + "#"
                                           + propertyDispatcher.getProperty()));
            }
            var futureValuedAspect = aspect.isValuePresent()
                    ? Aspect.of(aspect.getName(), CompletableFuture.completedFuture(aspect.getValue()))
                    : Aspect.<CompletableFuture<T>> of(aspect.getName());

            return handlingUiUpdateException(() -> {
                var component = (Component)componentWrapper.getComponent();
                if (component instanceof HasValue<?, ?> hasValue) {
                    hasValue.setReadOnly(true);
                }
                component.getElement().setAttribute(ATTR_LOADING, true);
                component.getElement().setAttribute(ATTR_HAS_ERROR, false);
                component.getElement().getStyle().remove(CSS_PROPERTY_ERROR_MESSAGES);
                var future = requireNonNull(propertyDispatcher.pull(futureValuedAspect), "Future must not be null");
                future.whenComplete(onFutureComplete(ui, componentWrapper));
            }, propertyDispatcher, aspect);
        } else {
            return super.createUiUpdater(propertyDispatcher, componentWrapper);
        }
    }

    private BiConsumer<T, Throwable> onFutureComplete(UI ui, ComponentWrapper componentWrapper) {
        var setter = createComponentValueSetter(componentWrapper);
        var element = ((Component)componentWrapper.getComponent()).getElement();
        return (value, throwable) -> ui.access(() -> {
            if (throwable != null) {
                LOGGER.error("An error occurred when retrieving future valued aspect for {}",
                             getClass().getSimpleName(), throwable);
                setter.accept(getValueOnError());

                element.setAttribute(ATTR_HAS_ERROR, true);
                element.getStyle().set(CSS_PROPERTY_ERROR_MESSAGES,
                                       "'" + NlsText.getString(MSG_CODE_ERROR_MESSAGE) + "'");
            } else {
                setter.accept(value);
                if (componentWrapper.getComponent() instanceof HasValue<?, ?> hasValue) {
                    hasValue.setReadOnly(false);
                }
            }
            element.removeAttribute(ATTR_LOADING);
        });
    }

    @CheckForNull
    protected abstract T getValueOnError();
}
