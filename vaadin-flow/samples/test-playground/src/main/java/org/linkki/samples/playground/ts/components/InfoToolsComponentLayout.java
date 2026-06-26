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
package org.linkki.samples.playground.ts.components;

import java.io.Serial;
import java.util.List;
import java.util.function.Supplier;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.defaults.columnbased.pmo.ContainerPmo;
import org.linkki.core.ui.creation.VaadinUiCreator;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.core.ui.layout.annotation.UICssLayout;
import org.linkki.core.ui.layout.annotation.UISection;
import org.linkki.framework.ui.component.infotool.InfoTool;
import org.linkki.framework.ui.component.infotool.InfoToolsComponent;
import org.linkki.util.Sequence;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class InfoToolsComponentLayout extends VerticalLayout {
    @Serial
    private static final long serialVersionUID = -8676478558788414743L;

    public InfoToolsComponentLayout() {
        var bindingContext = new BindingContext();
        var inputPmo = new SingleTextFieldPmo();

        add(VaadinUiCreator.createComponent(inputPmo, bindingContext));

        var component = new InfoToolsComponent<>(
                Sequence.of(
                        new InfoTool("tool-vaadin-component", "Tool with a Vaadin Component",
                                new Span("Information")),
                        new InfoTool("tool-section", "Tool with a Section",
                                VaadinUiCreator.createComponent(new SingleLabelPmo(inputPmo::getValue),
                                        bindingContext)),
                        new InfoTool("tool-table", "Tool with a grid",
                                VaadinUiCreator.createComponent(new MinimalTablePmo(), bindingContext))));
        add(component);

        var defaultTool = new InfoTool("tool-url-1", "URL aware Tool tool-url-1 (default)",
                new Span("This should be open with tools=tool-url-1, " +
                        "or if no query param is set"));
        var componentWithUrlSupport = new UrlAwareInfoToolComponent<>(
                Sequence.of(defaultTool,
                        new InfoTool("tool-url-2", "URL aware Tool tool-url-2",
                                new Span("This should be opened with tools=tool-url-2"))),
                Sequence.of(defaultTool));
        add(componentWithUrlSupport);

        setSpacing(false);
        setPadding(false);
    }

    @UICssLayout
    public static class SingleTextFieldPmo {

        private String value = "initial";

        @UITextField(position = 0)
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @UISection
    public static class SingleLabelPmo {

        private final Supplier<String> valueSupplier;

        public SingleLabelPmo(Supplier<String> valueSupplier) {
            this.valueSupplier = valueSupplier;
        }

        @UILabel(position = 0, label = "Value")
        public String getLabel() {
            return valueSupplier.get();
        }
    }

    @UISection
    public static class MinimalTablePmo implements ContainerPmo<SingleLabelPmo> {

        @Override
        public List<SingleLabelPmo> getItems() {
            return List.of(new SingleLabelPmo(() -> "1"), new SingleLabelPmo(() -> "2"));
        }

        @Override
        public int getPageLength() {
            return 0;
        }
    }

    public static final class UrlAwareInfoToolComponent<T extends InfoTool> extends InfoToolsComponent<T> {

        @Serial
        private static final long serialVersionUID = 7202255963988847760L;

        public UrlAwareInfoToolComponent(Sequence<T> tools, Sequence<T> defaultTools) {
            super(tools, defaultTools);
        }

        @Override
        protected void updateUrlParameters(List<String> openedTools) {
            UI.getCurrent().navigate(UI.getCurrent().getActiveViewLocation().getPath(),
                    UI.getCurrent().getActiveViewLocation().getQueryParameters()
                            .merging(InfoToolsComponent.QUERY_PARAM, String.join(",", openedTools)));
            super.updateUrlParameters(openedTools);
        }
    }
}
