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
package org.linkki.core.ui.section;

/**
 * This is the default implementation of {@link PmoBasedSectionFactory}. If you do not need any
 * specialization of {@link PmoBasedSectionFactory} you are perfectly right to use this one.
 * 
 * @deprecated After removing CDI dependency there is no need for
 *             {@link DefaultPmoBasedSectionFactory}. Just use {@link PmoBasedSectionFactory}
 *             directly.
 */
@Deprecated
public class DefaultPmoBasedSectionFactory extends PmoBasedSectionFactory {

    public DefaultPmoBasedSectionFactory() {
        super();
    }

}