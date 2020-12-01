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

package org.linkki.core.ui.aspects;

import java.util.function.Consumer;

import org.linkki.core.binding.descriptor.aspect.Aspect;
import org.linkki.core.binding.descriptor.aspect.base.ModelToUiAspectDefinition;
import org.linkki.core.binding.wrapper.ComponentWrapper;

import com.vaadin.ui.Link;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * This aspect sets the target attribute of a {@link Link}.
 */
public class LinkTargetAspectDefinition extends ModelToUiAspectDefinition<String> {

    public static final String NAME = "target";

    @CheckForNull
    private final String target;

    private final boolean dynamic;


    public LinkTargetAspectDefinition(String target, boolean dynamic) {
        this.target = target;
        this.dynamic = dynamic;
    }

    @Override
    public Aspect<String> createAspect() {
        return dynamic ? Aspect.of(NAME) : Aspect.of(NAME, target);
    }

    @Override
    public Consumer<String> createComponentValueSetter(ComponentWrapper componentWrapper) {
        Link link = (Link)componentWrapper.getComponent();
        return link::setTargetName;
    }

}
