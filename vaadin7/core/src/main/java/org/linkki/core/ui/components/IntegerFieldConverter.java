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
package org.linkki.core.ui.components;

import java.text.NumberFormat;

import org.eclipse.jdt.annotation.Nullable;

class IntegerFieldConverter extends AbstractNumberFieldConverter<Integer> {

    private static final long serialVersionUID = 6756969882235490962L;

    IntegerFieldConverter(NumberFormat format) {
        super(format);
    }

    @Override
    public Class<Integer> getModelType() {
        return Integer.class;
    }

    @Override
    @Nullable
    protected Integer getNullValue() {
        return null;
    }

    @Override
    protected Integer convertToModel(Number value) {
        return value.intValue();
    }

}