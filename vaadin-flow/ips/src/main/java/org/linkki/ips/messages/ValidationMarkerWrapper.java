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
package org.linkki.ips.messages;

import org.faktorips.runtime.IMarker;
import org.linkki.util.validation.ValidationMarker;

/**
 * Wrapper class for {@link IMarker Faktor-IPS IMarkers} that implements linkki's
 * {@link ValidationMarker}.
 */
public class ValidationMarkerWrapper implements ValidationMarker {

    private final IMarker wrapped;

    public ValidationMarkerWrapper(IMarker fipsMarker) {
        this.wrapped = fipsMarker;
    }

    @Override
    public boolean isRequiredInformationMissing() {
        return wrapped.isRequiredInformationMissing();
    }

    /**
     * Provides access to the original wrapped {@link IMarker}.
     * <p>
     * Usage: {@code MyMarker marker = getWrapped();}
     * <p>
     * NOTE: if the original instance is not of type (for example) MyMarker, a
     * {@link ClassCastException} will be thrown.
     * 
     * @param <T> The type of the wrapped marker
     *
     * @return the original {@link IMarker}
     *
     * @throws ClassCastException if the original instance is not of type {@code <T>}
     */
    @SuppressWarnings("unchecked")
    public <T extends IMarker> T getWrapped() {
        return (T)wrapped;
    }

}
