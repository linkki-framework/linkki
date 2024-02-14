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
package org.linkki.core.binding.dispatcher.staticvalue;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.LinkkiAspectDefinition;
import org.linkki.core.binding.dispatcher.AbstractPropertyDispatcherDecorator;
import org.linkki.core.binding.dispatcher.PropertyDispatcher;

/**
 * This dispatcher returns the static value of an {@link Aspect} if it has a value. If no value
 * exists for the given property the wrapped dispatcher is accessed for a value.
 */
public class StaticValueDispatcher extends AbstractPropertyDispatcherDecorator {

    /**
     * Creating a new {@link StaticValueDispatcher} passing the wrapped dispatcher that should be
     * decorated by this {@link StaticValueDispatcher}
     *
     * @param wrappedDispatcher The decorated dispatcher
     */
    public StaticValueDispatcher(PropertyDispatcher wrappedDispatcher) {
        super(wrappedDispatcher);
    }

    /**
     * Returns the value of the {@link Aspect} if the value is static.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T pull(Aspect<T> aspect) {
        if (aspect.isValuePresent()) {
            T staticValue = aspect.getValue();
            Object boundObject = getBoundObject();
            if (staticValue instanceof String && boundObject != null) {
                Class<?> pmoClass = getTypeForKey(boundObject);
                staticValue = (T)StaticValueNlsService.getInstance()
                        .getString(pmoClass, getProperty(), aspect.getName(), (String)staticValue);
            }
            if (LinkkiAspectDefinition.DERIVED_BY_LINKKI.equals(staticValue)) {
                return (T)StringUtils.capitalize(getProperty());
            }
            return staticValue;
        } else {
            return super.pull(aspect);
        }
    }

    private Class<?> getTypeForKey(Object boundObject) {
        return boundObject instanceof Class ? (Class<?>)boundObject : boundObject.getClass();
    }

}