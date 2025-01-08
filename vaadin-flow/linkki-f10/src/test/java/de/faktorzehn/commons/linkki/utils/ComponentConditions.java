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

package de.faktorzehn.commons.linkki.utils;

import java.util.function.Predicate;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Condition;

import com.vaadin.flow.component.Component;

/**
 * AssertJ Conditions to be used with {@link AbstractAssert#is(Condition)} or
 * {@link AbstractAssert#has(Condition)}.
 */
public class ComponentConditions {

    public static Condition<Component> childOf(Component parent) {
        return new Condition<>(c -> isChildOf(c, parent), "a child of %s", parent);
    }

    public static boolean isChildOf(Component child, Component parent) {
        if (child.getParent().isEmpty()) {
            return false;
        } else if (child.getParent().get().equals(parent)) {
            return true;
        } else {
            return isChildOf(child.getParent().get(), parent);
        }
    }

    public static Condition<Component> anyChildrenSatisfying(Predicate<Component> predicate, String description) {
        return new Condition<>(c -> isAnyChildSatisfying(c, predicate),
                "any child " + description);
    }

    public static boolean isAnyChildSatisfying(Component component, Predicate<Component> predicate) {
        if (component.getChildren().anyMatch(predicate)) {
            return true;
        } else {
            return component.getChildren().anyMatch(c -> isAnyChildSatisfying(c, predicate));
        }
    }
}
