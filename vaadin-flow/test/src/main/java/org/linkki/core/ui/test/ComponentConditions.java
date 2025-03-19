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

package org.linkki.core.ui.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Condition;
import org.assertj.core.presentation.Representation;
import org.jetbrains.annotations.NotNull;
import org.linkki.util.Consumers;

import com.github.mvysny.kaributesting.v10.LocatorJ;
import com.github.mvysny.kaributesting.v10.PrettyPrintTreeKt;
import com.github.mvysny.kaributesting.v10.SearchSpec;
import com.github.mvysny.kaributesting.v10.SearchSpecJ;
import com.vaadin.flow.component.Component;

import kotlin.ranges.IntRange;

/**
 * AssertJ conditions to be used with {@link AbstractAssert#is(Condition)} or
 * {@link AbstractAssert#has(Condition)}.
 * <p>
 * For a better representation of the actual component in case of test failure, use
 * {@link ComponentTreeRepresentation} with
 * {@link AbstractAssert#withRepresentation(Representation)}.
 * 
 * @since 2.8.0
 */
public class ComponentConditions {

    private ComponentConditions() {
        // utility class
    }

    public static Condition<Component> childOf(Component parent) {
        return new Condition<>(c -> isChildOf(c, parent),
                "a child of %s, but the parent has the component tree \n%s",
                PrettyPrintTreeKt.toPrettyString(parent),
                KaribuUtils.getComponentTree(parent));
    }

    private static boolean isChildOf(Component child, Component parent) {
        var childParent = child.getParent();
        if (childParent.isEmpty()) {
            return false;
        } else if (childParent.get().equals(parent)) {
            return true;
        } else {
            return isChildOf(childParent.get(), parent);
        }
    }

    public static Condition<Component> anyChildSatisfying(Predicate<Component> predicate, String description) {
        return new Condition<>(c -> isAnyChildSatisfying(c, predicate),
                "any child that " + description);
    }

    private static boolean isAnyChildSatisfying(Component component, Predicate<Component> predicate) {
        if (component.getChildren().anyMatch(predicate)) {
            return true;
        } else {
            return component.getChildren().anyMatch(c -> isAnyChildSatisfying(c, predicate));
        }
    }

    public static Condition<Component> exactlyOneVisibleChildOfType(Class<? extends Component> type) {
        return exactlyOneVisibleChildOfType(type, Consumers.nopConsumer());
    }

    public static <T extends Component> Condition<Component> exactlyOneVisibleChildOfType(Class<T> type,
            Consumer<SearchSpecJ<T>> searchSpec) {
        return new Condition<>(c -> findChildren(type, searchSpec, c).size() == 1,
                "exactly one visible child matching type %s", displaySearchSpec(type, searchSpec));
    }

    private static <T extends Component> @NotNull List<T> findChildren(Class<T> type,
            Consumer<SearchSpecJ<T>> searchSpec,
            Component c) {
        return c
                .getChildren()
                .map(child -> LocatorJ._find(child, type, searchSpec))
                .flatMap(Collection::stream)
                .toList();
    }

    public static <T extends Component> Condition<Component> anyVisibleChildOfType(Class<T> type) {
        return anyVisibleChildOfType(type, Consumers.nopConsumer());
    }

    public static <T extends Component> Condition<Component> anyVisibleChildOfType(Class<T> type,
            Consumer<SearchSpecJ<T>> searchSpec) {
        return new Condition<>(c -> !LocatorJ._find(c, type, searchSpec).isEmpty(),
                "any visible child matching %s", displaySearchSpec(type, searchSpec));
    }

    private static <T extends Component> String displaySearchSpec(Class<T> type,
            Consumer<SearchSpecJ<T>> searchSpecJConsumer) {
        var searchSpec = createSearchSpec(type);
        searchSpecJConsumer.accept(new SearchSpecJ<>(searchSpec));
        return searchSpec.toString();
    }

    private static <T extends Component> SearchSpec<T> createSearchSpec(Class<T> type) {
        return new SearchSpec<>(type, null, null, null, null,
                null, new IntRange(0, Integer.MAX_VALUE), null, null,
                null, null, null, null, null,
                new ArrayList<>());
    }
}
