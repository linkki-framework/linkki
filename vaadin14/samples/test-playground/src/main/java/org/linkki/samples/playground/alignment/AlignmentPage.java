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

package org.linkki.samples.playground.alignment;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.ui.creation.VaadinUiCreator;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AlignmentPage extends HorizontalLayout {

    private static final long serialVersionUID = 1L;

    private static final String HEIGHT = "100px";

    public AlignmentPage() {
        setPadding(false);
        setSizeFull();

        BindingContext context = new BindingContext();

        VerticalLayout left = new VerticalLayout();
        left.setSizeFull();

        left.add(new H3("Vertical Alignment Top"));
        left.add(new Text("The following component should be at the top"));
        Component leftTop = VaadinUiCreator.createComponent(new HorizontalLayoutTopPmo(), context);
        if (leftTop instanceof HasSize) {
            ((HasSize)leftTop).setHeight(HEIGHT);
        }
        left.add(leftTop);

        left.add(new H3("Vertical Alignment Middle (Default)"));
        left.add(new Text("The following component should be in the middle"));
        Component leftMiddle = VaadinUiCreator.createComponent(new HorizontalLayoutMiddlePmo(), context);
        if (leftMiddle instanceof HasSize) {
            ((HasSize)leftMiddle).setHeight(HEIGHT);
        }
        left.add(leftMiddle);

        left.add(new H3("Vertical Alignment Bottom"));
        left.add(new Text("The following component should be at the bottom"));
        Component leftBottom = VaadinUiCreator.createComponent(new HorizontalLayoutBottomPmo(), context);
        left.add(leftBottom);
        if (leftBottom instanceof HasSize) {
            ((HasSize)leftBottom).setHeight(HEIGHT);
        }

        add(left);

        VerticalLayout right = new VerticalLayout();
        right.setSizeFull();

        right.add(new H3("Horizontal Alignment Left (Default)"));
        right.add(new Text("The following component should be on the left"));
        right.add(VaadinUiCreator.createComponent(new VerticalLayoutLeftPmo(), context));

        right.add(new H3("Horizontal Alignment Middle"));
        right.add(new Text("The following component should be in the middle"));
        right.add(VaadinUiCreator.createComponent(new VerticalLayoutMiddlePmo(), context));

        right.add(new H3("Horizontal Alignment Right"));
        right.add(new Text("The following component should be on the right"));
        right.add(VaadinUiCreator.createComponent(new VerticalLayoutRightPmo(), context));

        add(right);

    }

}
