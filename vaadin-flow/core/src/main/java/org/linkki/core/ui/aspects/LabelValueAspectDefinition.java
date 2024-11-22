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

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.wrapper.ComponentWrapper;
import org.linkki.core.defaults.ui.aspects.UiUpdateUtil;
import org.linkki.core.defaults.ui.element.ItemCaptionProvider;
import org.linkki.core.ui.element.annotation.UILabel.DefaultLabelCaptionProvider;
import org.linkki.core.util.HtmlContent;
import org.linkki.core.vaadin.component.base.LinkkiText;
import org.linkki.util.handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.UI;

/**
 * The value aspect for label components. The label is a read-only component, hence this aspect only
 * reads the value from model and updates the UI.
 */
public class LabelValueAspectDefinition implements LinkkiAspectDefinition {

    public static final String NAME = LabelAspectDefinition.VALUE_ASPECT_NAME;

    private static final Logger LOGGER = LoggerFactory.getLogger(LabelValueAspectDefinition.class);
    private static final String ATTR_LOADING = "value-loading";
    private static final String ATTR_HAS_ERRORS = "has-errors";

    @Deprecated(since = "2.5.0")
    private final boolean htmlContent;

    private final ItemCaptionProvider<?> itemCaptionProvider;

    /**
     * Creates a new {@link LabelValueAspectDefinition} using the
     * {@link DefaultLabelCaptionProvider}
     * 
     * @since 2.5.0
     */
    public LabelValueAspectDefinition() {
        this(new DefaultLabelCaptionProvider());
    }

    /**
     *
     * Creates a new {@link LabelValueAspectDefinition} using the provided
     * {@link ItemCaptionProvider}
     * 
     * @since 2.5.0
     */
    public LabelValueAspectDefinition(ItemCaptionProvider<?> itemCaptionProvider) {
        this.itemCaptionProvider = itemCaptionProvider;
        htmlContent = false;
    }

    /**
     * @deprecated Use {@link LabelValueAspectDefinition#LabelValueAspectDefinition()} instead.<br>
     *             Content that should be interpreted as HTML should be returned as
     *             {@link HtmlContent}.
     */
    @Deprecated(since = "2.5.0")
    public LabelValueAspectDefinition(boolean htmlContent) {
        this(htmlContent, new DefaultLabelCaptionProvider());
    }

    /**
     * @deprecated Use
     *             {@link LabelValueAspectDefinition#LabelValueAspectDefinition(ItemCaptionProvider)}
     *             instead. <br>
     *             Content that should be interpreted as HTML should be returned as
     *             {@link HtmlContent}.
     */
    @Deprecated(since = "2.5.0")
    public LabelValueAspectDefinition(boolean htmlContent, ItemCaptionProvider<?> itemCaptionProvider) {
        this.htmlContent = htmlContent;
        this.itemCaptionProvider = itemCaptionProvider;
    }

    @Override
    public Handler createUiUpdater(PropertyDispatcher propertyDispatcher, ComponentWrapper componentWrapper) {
        Consumer<Object> setter = createComponentValueSetter(componentWrapper);
        Aspect<Object> aspect = createAspect();
        return () -> {
            try {
                var aspectValue = propertyDispatcher.pull(aspect);

                if (aspectValue != null) {
                    if (aspectValue instanceof CompletableFuture<?>) {
                        var ui = UI.getCurrent();
                        var label = (LinkkiText)componentWrapper.getComponent();

                        if (!ui.getPushConfiguration().getPushMode().isEnabled()) {
                            if (LOGGER.isWarnEnabled()) {
                                LOGGER.warn(("""
                                        CompletableFuture is used to retrieve label value with %s although server push is not \
                                        enabled.

                                        This will cause update to be not reflected in the UI immediately. (%s)""")
                                        .formatted(LabelValueAspectDefinition.class.getSimpleName(),
                                                   Optional.ofNullable(propertyDispatcher.getBoundObject())
                                                           .map(Object::getClass)
                                                           .map(Class::getName)
                                                           .orElse("null") + "#"
                                                           + propertyDispatcher.getProperty()));
                            }
                        }

                        label.getElement().setAttribute(ATTR_LOADING, true);
                        label.getElement().getClassList().add("loading");
                        label.getElement().setAttribute(ATTR_HAS_ERRORS, false);
                        @SuppressWarnings("unchecked")
                        var future = (CompletableFuture<Object>)aspectValue;
                        future.whenComplete(onFutureComplete(ui, label));

                    } else {
                        setter.accept(aspectValue);
                    }
                } else {
                    setter.accept(null);
                }
                // CSOFF: IllegalCatch
            } catch (RuntimeException e) {
                UiUpdateUtil.handleUiUpdateException(e, propertyDispatcher, aspect);
                // CSON: IllegalCatch
            }
        };
    }

    public Aspect<Object> createAspect() {
        return Aspect.of(NAME);
    }

    public Consumer<Object> createComponentValueSetter(ComponentWrapper componentWrapper) {
        LinkkiText label = (LinkkiText)componentWrapper.getComponent();
        return v -> label.setText(itemCaptionProvider.getUnsafeCaption(v), v instanceof HtmlContent || htmlContent);
    }

    private BiConsumer<Object, Throwable> onFutureComplete(UI ui, LinkkiText label) {
        return (item, throwable) -> ui.access(() -> {
            if (throwable != null) {
                LOGGER.error("An error occurred when retrieving label value", throwable);
                label.setText(throwable.getLocalizedMessage());
                label.getElement().setAttribute(ATTR_HAS_ERRORS, true);
            } else {
                label.setText(item.toString());
            }
            label.getElement().removeAttribute(ATTR_LOADING);
            label.getElement().getClassList().remove("loading");
        });
    }

}
