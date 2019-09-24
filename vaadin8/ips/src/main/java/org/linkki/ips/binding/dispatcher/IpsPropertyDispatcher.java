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

package org.linkki.ips.binding.dispatcher;

import java.util.Optional;
import java.util.WeakHashMap;
import java.util.function.Supplier;

import org.faktorips.runtime.model.IpsModel;
import org.faktorips.runtime.model.type.Attribute;
import org.faktorips.runtime.model.type.ModelElement;
import org.faktorips.runtime.model.type.Type;
import org.linkki.core.binding.descriptor.UIElementAnnotationReader;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.AbstractPropertyDispatcherDecorator;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.uiframework.UiFramework;

/**
 * {@link PropertyDispatcher} that returns Faktor-IPS labels for String valued aspects marked with
 * {@link LinkkiAspectDefinition#DERIVED_BY_LINKKI} if the bound object is a Faktor-IPS model object.
 */
public class IpsPropertyDispatcher extends AbstractPropertyDispatcherDecorator {

    private final WeakHashMap<Object, Optional<ModelElement>> modelElementCache = new WeakHashMap<>(2, 1);
    private final Supplier<?> modelObjectSupplier;
    private final String modelAttribute;

    public IpsPropertyDispatcher(Supplier<?> modelObjectSupplier, String modelAttribute,
            PropertyDispatcher wrappedDispatcher) {
        super(wrappedDispatcher);
        this.modelObjectSupplier = modelObjectSupplier;
        this.modelAttribute = modelAttribute;
    }

    @Override
    public <T> T pull(Aspect<T> aspect) {
        if (aspect.isValuePresent()) {
            T staticValue = aspect.getValue();
            if (LinkkiAspectDefinition.DERIVED_BY_LINKKI.equals(staticValue)) {
                @SuppressWarnings("unchecked")
                T label = (T)findModelElement()
                        .map(this::getLabel)
                        .orElseGet(() -> (String)super.pull(aspect));
                return label;
            }
        }
        return super.pull(aspect);
    }

    private String getLabel(ModelElement modelElement) {
        return modelElement.getLabel(UiFramework.getLocale());
    }

    private Optional<ModelElement> findModelElement() {
        Object modelObject = modelObjectSupplier.get();
        if (modelObject != null) {
            return modelElementCache.computeIfAbsent(modelObject, this::findModelElement);
        } else {
            return Optional.empty();
        }
    }

    private Optional<ModelElement> findModelElement(Object modelObject) {
        Class<?> modelObjectClass = modelObject.getClass();
        if (IpsModel.isPolicyCmptType(modelObjectClass) || IpsModel.isProductCmptType(modelObjectClass)) {
            Type type = IpsModel.getType(modelObjectClass);
            if (modelAttribute.isEmpty()) {
                return Optional.of(type);
            } else if (type.isAttributePresent(modelAttribute)) {
                Attribute attribute = type.getAttribute(modelAttribute);
                return Optional.of(attribute);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns an {@link IpsPropertyDispatcher} wrapping the given standard dispatchers if the given PMO
     * is a Faktor-IPS object and returns the standard dispatchers unchanged otherwise.
     * 
     * @param pmo a presentation model object
     * @param boundProperty a {@link BoundProperty} of the PMO
     * @param standardDispatchers a standard dispatcher chain created for the PMO, for example from
     *            {@link PropertyDispatcherFactory}
     * @return an {@link IpsPropertyDispatcher} wrapping the given standard dispatchers or the given
     *         standard dispatchers
     */
    public static PropertyDispatcher createIpsPropertyDispatcher(Object pmo,
            BoundProperty boundProperty,
            PropertyDispatcher standardDispatchers) {
        if (UIElementAnnotationReader.hasModelObjectAnnotation(pmo, boundProperty.getModelObject())) {
            return new IpsPropertyDispatcher(
                    UIElementAnnotationReader.getModelObjectSupplier(pmo, boundProperty.getModelObject()),
                    boundProperty.getModelAttribute(),
                    standardDispatchers);
        } else {
            return standardDispatchers;
        }
    }


}
