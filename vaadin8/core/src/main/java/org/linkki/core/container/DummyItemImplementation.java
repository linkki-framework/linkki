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

package org.linkki.core.container;

import java.util.Collection;
import java.util.Collections;


/**
 * A dummy {@link com.vaadin.v7.data.Item Item} implementation for methods in
 * {@link LinkkiInMemoryContainer} that return {@link com.vaadin.v7.data.Item items} although no
 * {@link com.vaadin.v7.data.Item Item} should be needed there.
 */
@SuppressWarnings({ "deprecation", "javadoc" })
public class DummyItemImplementation implements com.vaadin.v7.data.Item {

    private static final long serialVersionUID = -8329741338143404383L;

    @Override
    @SuppressWarnings("null")
    public com.vaadin.v7.data.Property<?> getItemProperty(Object id) {
        return null;
    }

    @Override
    public Collection<?> getItemPropertyIds() {
        return Collections.emptySet();
    }

    @Override
    public boolean addItemProperty(Object id, @SuppressWarnings("rawtypes") com.vaadin.v7.data.Property property)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported in the dummy implementation!");
    }

    @Override
    public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported in the dummy implementation!");
    }


}
