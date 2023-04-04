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
package org.linkki.samples.playground.customlayout;

import org.linkki.core.ui.aspects.annotation.BindSlot;
import org.linkki.samples.playground.ts.aspects.BindSlotPmo;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;

/**
 * Custom layout for testing {@link BindSlot @BindSlot} by using {@link BindSlotPmo}.
 */
// tag::bindSlot-layout-java[]
@Tag("sample-slot-layout")
@JsModule("./layouts/sample-slot-layout.ts")
public class BindSlotLayout extends LitTemplate {

    public static final String SLOT_LEFT = "left-slot";
    public static final String SLOT_RIGHT = "right-slot";

    private static final long serialVersionUID = 1L;
}
// end::bindSlot-layout-java[]