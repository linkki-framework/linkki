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
package org.linkki.core.binding.dispatcher;

import javax.annotation.CheckForNull;

import org.linkki.core.binding.aspect.Aspect;
import org.linkki.core.nls.pmo.PmoNlsService;
import org.linkki.core.ui.section.descriptor.BindingDescriptor;

/**
 * This dispatcher returns the static value of an {@link Aspect} if it has a value. If no value
 * exists for the given property the wrapped dispatcher is accessed for a value.
 */
public class StaticValueDispatcher extends AbstractPropertyDispatcherDecorator {

    /**
     * Creating a new {@link StaticValueDispatcher} for an {@link BindingDescriptor}, passing the
     * wrapped dispatcher that should be decorated by this {@link StaticValueDispatcher}
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
    @CheckForNull
    @Override
    public <T> T pull(Aspect<T> aspect) {
        if (aspect.isValuePresent()) {
            T staticValue = aspect.getValue();
            Object boundObject = getBoundObject();
            if (staticValue instanceof String && boundObject != null) {
                Class<? extends Object> pmoClass = boundObject.getClass();
                return (T)PmoNlsService.get()
                        .getLabel(pmoClass, getProperty(), aspect.getName(), (String)staticValue);
            } else {
                return staticValue;
            }
        } else {
            return super.pull(aspect);
        }
    }


}