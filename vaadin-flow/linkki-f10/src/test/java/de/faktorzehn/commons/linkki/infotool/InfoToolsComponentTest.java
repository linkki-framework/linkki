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

package de.faktorzehn.commons.linkki.infotool;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.Serial;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.linkki.core.ui.test.KaribuUIExtension;
import org.linkki.util.Sequence;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.Details.OpenedChangeEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.NavigationTrigger;
import com.vaadin.flow.router.QueryParameters;

@ExtendWith(KaribuUIExtension.class)
public class InfoToolsComponentTest {

    static class TestInfoToolsComponent extends InfoToolsComponent<InfoTool> {

        public static final String QUERY_PARAM = "tools";

        @Serial
        private static final long serialVersionUID = 1L;

        public TestInfoToolsComponent(Sequence<InfoTool> tools, Sequence<InfoTool> defaultTools) {
            super(tools, defaultTools);
        }

        @Override
        protected void updateUrlParameters(List<String> openedTools) {
            // nothing to do
        }

        @Override
        protected List<String> getOpenToolsIdsFromUrl(Location location) {
            var parameters = location.getQueryParameters().getParameters();
            return parameters.getOrDefault(QUERY_PARAM, List.of());
        }
    }

    @Test
    public void testPolicyToolsArea_NoTools() {
        var tools = Sequence.<InfoTool> of();
        var defaults = Sequence.<InfoTool> of();
        var infoToolsComponent = new TestInfoToolsComponent(tools, defaults);

        assertThat(infoToolsComponent.getChildren()).isEmpty();
    }

    @Test
    public void testPolicyToolsArea_SomeTools() {
        var component1 = new Div();
        var component2 = new Div();
        var tools = Sequence.of(new InfoTool("id1", "Caption1", component1),
                                new InfoTool("id2", "Caption2", component2));
        var defaults = Sequence.<InfoTool> of();

        var infoToolsComponent = new TestInfoToolsComponent(tools, defaults);

        assertThat(infoToolsComponent.getChildren()
                .map(Details.class::cast)
                .flatMap(ap -> ap.getContent())).contains(component1, component2);
    }

    @Test
    public void testPolicyToolsArea_DefaultOpened() {
        var component1 = new Div();
        var component2 = new Div();
        var infoTool1 = new InfoTool("id1", "Caption1", component1);
        var infoTool2 = new InfoTool("id2", "Caption2", component2);
        var tools = Sequence.of(infoTool1, infoTool2);
        var defaults = Sequence.of(infoTool2);

        var infoToolsComponent = new TestInfoToolsComponent(tools, defaults);

        var location = new Location("test");
        var locationChangeEvent = new LocationChangeEvent(UI.getCurrent().getInternals().getRouter(),
                UI.getCurrent(), NavigationTrigger.CLIENT_SIDE, location, List.of());
        // default is set by after navigation event if there is no other tool specified in the url
        infoToolsComponent.afterNavigation(new AfterNavigationEvent(locationChangeEvent));

        assertThat(infoToolsComponent.getOpenedTools().flatMap(Details::getContent)).containsExactly(component2);
    }

    @Test
    public void testPolicyToolsArea_ToolSpecifiedInUrlOpened() {
        var component1 = new Div();
        var component2 = new Div();
        var component3 = new Div();
        var infoTool1 = new InfoTool("id1", "Caption1", component1);
        var infoTool2 = new InfoTool("id2", "Caption2", component2);
        var infoTool3 = new InfoTool("id3", "Caption3", component3);
        var tools = Sequence.of(infoTool1, infoTool2, infoTool3);
        var defaults = Sequence.of(infoTool1);

        var infoToolsComponent = new TestInfoToolsComponent(tools, defaults);

        var location = new Location("test",
                QueryParameters.full(Map.of(TestInfoToolsComponent.QUERY_PARAM, new String[] { "id2", "id3" })));
        var locationChangeEvent = new LocationChangeEvent(UI.getCurrent().getInternals().getRouter(),
                UI.getCurrent(), NavigationTrigger.CLIENT_SIDE, location, List.of());
        infoToolsComponent.afterNavigation(new AfterNavigationEvent(locationChangeEvent));

        assertThat(infoToolsComponent.getOpenedTools().flatMap(Details::getContent)).contains(component2,
                                                                                              component3);
    }

    @Test
    public void testPolicyToolsArea_AllToolsClosed() {
        var component1 = new Div();
        var component2 = new Div();
        var component3 = new Div();
        var infoTool1 = new InfoTool("id1", "Caption1", component1);
        var infoTool2 = new InfoTool("id2", "Caption2", component2);
        var infoTool3 = new InfoTool("id3", "Caption3", component3);
        var tools = Sequence.of(infoTool1, infoTool2, infoTool3);
        var defaults = Sequence.of(infoTool2);

        var infoToolsComponent = spy(new TestInfoToolsComponent(tools, defaults));

        // close all tools
        infoToolsComponent.getAllToolDetails().forEach(d -> d.setOpened(false));
        // is sent by browser only --> call manually
        infoToolsComponent.onOpenedChange(new OpenedChangeEvent((Details)infoToolsComponent.getComponentAt(0), true));

        // verify updateUrlParameters was called with NO_TOOLS
        verify(infoToolsComponent).updateUrlParameters(List.of(InfoToolsComponent.NO_TOOL_OPEN));
    }

}
