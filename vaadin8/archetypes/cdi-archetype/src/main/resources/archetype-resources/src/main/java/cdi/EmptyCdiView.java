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
package \${package}.cdi;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.Navigator.EmptyView;

/**
 * An extension of the {@link EmptyView} that adds a {@code @CDIView} annotation so that it can be
 * displayed with a {@link com.vaadin.cdi.CDIViewProvider CDIViewProvider}.
 */
@CDIView
public class EmptyCdiView extends EmptyView {

    private static final long serialVersionUID = 1L;

}