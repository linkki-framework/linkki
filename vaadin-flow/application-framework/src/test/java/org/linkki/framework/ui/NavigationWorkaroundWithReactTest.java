package org.linkki.framework.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.linkki.framework.ui.NavigationWorkaroundTest.navigateToSamePathTwice;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.UI;

class NavigationWorkaroundWithReactTest {

    @BeforeEach
    void setupVaadin() {
        MockVaadin.setup(new Routes(
                Set.of(NavigationWorkaroundTest.StartView.class),
                Collections.emptySet(), true));
        assertThat(UI.getCurrent().getSession().getConfiguration().isReactEnabled())
                .as("React should be enabled to test that the test setup is correct.")
                .isTrue();
    }

    @Test
    void testBugDoesNotOccurWithReact() {
        assertThatNoException()
                .as("If navigating to the same path fails even if react is enabled, the test setup is probably not correct.")
                .isThrownBy(() -> navigateToSamePathTwice(() -> UI.getCurrent().navigate("view/new")));
    }
}
