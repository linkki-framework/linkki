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
package org.linkki.util.cdi;

/**
 * Provider for objects that cannot be injected directly, e.g. because they are obtained using
 * static utility methods and the like.
 * 
 * @deprecated since April 2018. Will be removed in the next release. Use
 *             org.apache.deltaspike.core.api.provider.BeanProvider instead.
 */
@Deprecated
public interface IpmProvider<T> {

    /**
     * Returns the value this provider provides.
     */
    public T get();

}
