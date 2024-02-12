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

package org.linkki.ips.binding.dispatcher;

import java.util.Optional;
import java.util.WeakHashMap;
import java.util.function.Supplier;

import org.faktorips.runtime.IModelObject;
import org.faktorips.runtime.model.IpsModel;
import org.faktorips.runtime.model.type.Attribute;
import org.faktorips.runtime.model.type.ModelElement;
import org.faktorips.runtime.model.type.PolicyAttribute;
import org.faktorips.runtime.model.type.Type;
import org.faktorips.valueset.UnrestrictedValueSet;
import org.faktorips.valueset.ValueSet;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.descriptor.modelobject.ModelObjects;
import org.linkki.core.binding.descriptor.property.BoundProperty;
import org.linkki.core.binding.dispatcher.AbstractPropertyDispatcherDecorator;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;
import org.linkki.core.binding.dispatcher.PropertyDispatcherFactory;
import org.linkki.core.defaults.ui.aspects.EnabledAspectDefinition;
import org.linkki.core.defaults.ui.aspects.VisibleAspectDefinition;
import org.linkki.core.ui.aspects.AvailableValuesAspectDefinition;
import org.linkki.core.ui.aspects.RequiredAspectDefinition;
import org.linkki.core.uiframework.UiFramework;

/**
 * {@link PropertyDispatcher} to answer some aspects using Faktor-IPS model information.
 * <p>
 * It returns Faktor-IPS labels for String valued aspects marked with
 * {@link LinkkiAspectDefinition#DERIVED_BY_LINKKI} if the bound object is a Faktor-IPS model
 * object.
 * <p>
 * It answers the required aspect with <code>true</code> in case of the bound property is a
 * {@link PolicyAttribute} with a {@link ValueSet} that does not contain <code>null</code>.
 */
public class IpsPropertyDispatcher extends AbstractPropertyDispatcherDecorator {

    private final WeakHashMap<Class<?>, Optional<ModelElement>> modelElementCache = new WeakHashMap<>(2, 1);
    private final Supplier<?> modelObjectSupplier;
    private final Supplier<Class<?>> modelObjectClassSupplier;
    private final String modelAttribute;

    /**
     * @deprecated Use
     *             {@link #IpsPropertyDispatcher(Supplier, Supplier, String, PropertyDispatcher)}
     *             instead.
     */
    @Deprecated(since = "2.5.0")
    public IpsPropertyDispatcher(Supplier<?> modelObjectSupplier, String modelAttribute,
            PropertyDispatcher wrappedDispatcher) {
        this(modelObjectSupplier, () -> null, modelAttribute, wrappedDispatcher);
    }

    public IpsPropertyDispatcher(Supplier<?> modelObjectSupplier, Supplier<Class<?>> modelObjectClassSupplier,
            String modelAttribute,
            PropertyDispatcher wrappedDispatcher) {
        super(wrappedDispatcher);
        this.modelObjectSupplier = modelObjectSupplier;
        this.modelObjectClassSupplier = modelObjectClassSupplier;
        this.modelAttribute = modelAttribute;
    }

    @Override
    public <T> T pull(Aspect<T> aspect) {
        if (aspect.isValuePresent()) {
            T staticValue = aspect.getValue();
            if (LinkkiAspectDefinition.DERIVED_BY_LINKKI.equals(staticValue)) {
                return getDerivedByLinkkiValue(aspect);
            } else if (RequiredAspectDefinition.NAME.equals(aspect.getName())) {
                return getRequiredValue(aspect);
            } else if (VisibleAspectDefinition.NAME.equals(aspect.getName())
                    || EnabledAspectDefinition.NAME.equals(aspect.getName())) {
                return getVisibleOrEnabledValue(aspect);
            } else if (AvailableValuesAspectDefinition.NAME.equals(aspect.getName()) && aspect.isValuePresent()) {
                return getAvailableValuesValue(aspect);
            }
        }
        return super.pull(aspect);
    }

    @SuppressWarnings("unchecked")
    private <T> T getDerivedByLinkkiValue(Aspect<T> aspect) {
        return (T)findModelElement()
                .map(this::getLabel)
                .orElseGet(() -> (String)super.pull(aspect));
    }

    @SuppressWarnings("unchecked")
    private <T> T getAvailableValuesValue(Aspect<T> aspect) {
        return findModelElement()
                .map(this::getValueSet)
                .filter(ValueSet::isDiscrete)
                .map(vs -> (T)vs.getValues(false))
                .orElseGet(() -> super.pull(aspect));
    }

    private String getLabel(ModelElement modelElement) {
        return modelElement.getLabel(UiFramework.getLocale());
    }

    @SuppressWarnings("unchecked")
    private <T> T getRequiredValue(Aspect<T> aspect) {
        return (T)getRequiredTyped((Aspect<Boolean>)aspect);
    }

    @SuppressWarnings("unchecked")
    private <T> T getVisibleOrEnabledValue(Aspect<T> aspect) {
        return (T)getVisibleOrEnabledTyped((Aspect<Boolean>)aspect);
    }

    /**
     * Evaluates the required aspect. Ask the other dispatchers first, as they may evaluate more
     * quickly than to retrieve the value set from Faktor-IPS model.
     */
    private Boolean getRequiredTyped(Aspect<Boolean> aspect) {
        boolean otherDispatcherRequired = Optional.ofNullable(super.pull(aspect)).orElse(false);
        return otherDispatcherRequired || isRequiredInModel();
    }

    private Boolean getVisibleOrEnabledTyped(Aspect<Boolean> aspect) {
        boolean otherDispatcherValue = Optional.ofNullable(super.pull(aspect)).orElse(true);
        return otherDispatcherValue ? isVisibleOrEnabledInModel() : otherDispatcherValue;
    }

    /**
     * Checks, if the Faktor-IPS attribute needs to be visible or enabled in the UI.<br>
     * <ul>
     * <li>Attributes are not visible, if the corresponding value set is null or empty.</li>
     * <li>Attributes are disabled, if the corresponding value set is null or empty.</li>
     * </ul>
     */
    private boolean isVisibleOrEnabledInModel() {
        return !findModelElement()
                .map(this::getValueSet)
                .map(ValueSet::isEmpty)
                .orElse(false);
    }

    private boolean isRequiredInModel() {
        return !findModelElement()
                .map(this::getValueSet)
                .map(this::isNotRequired)
                .orElse(true);
    }

    private boolean isNotRequired(ValueSet<?> valueSet) {
        return valueSet.isEmpty() || valueSet.containsNull();
    }

    private ValueSet<?> getValueSet(ModelElement modelElement) {
        if (modelElement instanceof PolicyAttribute) {
            Object modelObject = modelObjectSupplier.get();
            if (modelObject instanceof IModelObject) {
                PolicyAttribute policyAttribute = (PolicyAttribute)modelElement;
                return policyAttribute.getValueSet((IModelObject)modelObject);
            }
        }
        return new UnrestrictedValueSet<>();
    }

    private Optional<ModelElement> findModelElement() {
        Object modelObject = modelObjectSupplier.get();
        Class<?> modelObjectClass = modelObject != null ? modelObject.getClass() : modelObjectClassSupplier.get();

        if (modelObjectClass != null) {
            return modelElementCache.computeIfAbsent(modelObjectClass, this::findModelElement);
        } else {
            return Optional.empty();
        }
    }

    private Optional<ModelElement> findModelElement(Class<?> modelObjectClass) {
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
     * Returns an {@link IpsPropertyDispatcher} wrapping the given standard dispatchers if the given
     * PMO is a Faktor-IPS object and returns the standard dispatchers unchanged otherwise.
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
        if (ModelObjects.isAccessible(pmo, boundProperty.getModelObject())) {
            return new IpsPropertyDispatcher(
                    ModelObjects.supplierFor(pmo, boundProperty.getModelObject()),
                    ModelObjects.classSupplierFor(pmo, boundProperty.getModelObject()),
                    boundProperty.getModelAttribute(),
                    standardDispatchers);
        } else {
            return standardDispatchers;
        }
    }

}
