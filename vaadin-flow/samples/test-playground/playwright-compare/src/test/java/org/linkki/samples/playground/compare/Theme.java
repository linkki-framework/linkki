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
package org.linkki.samples.playground.compare;

import java.util.EnumSet;

import com.microsoft.playwright.Page;

/**
 * Individual theme variants that can be combined into an {@link EnumSet} and applied together. Each
 * value knows how to activate itself on a page via the app menu.
 * <p>
 * Names and IDs mirror {@code ThemeVariantToggleMenuItemDefinition}.
 */
public enum Theme {

    CARD("appmenu-theme-card", "card-sections"),
    DARK("appmenu-theme-dark", "dark");

    private final String menuItemId;
    private final String expectedBodyTheme;

    Theme(String menuItemId, String expectedBodyTheme) {
        this.menuItemId = menuItemId;
        this.expectedBodyTheme = expectedBodyTheme;
    }

    /**
     * Activates this variant on the given page via the app menu.
     * <p>
     * The menu item is NOT in the DOM until the menu is opened — Vaadin renders it lazily into an
     * overlay. Access it via the Vaadin {@code items} API on the menu bar component, which holds
     * live component references regardless of overlay state. Plain {@code .click()} does not trigger
     * the handler — a composed bubbling MouseEvent is required.
     *
     * @throws IllegalStateException if the item cannot be found or activation fails
     */
    public void activate(Page page) {
        var script = """
                (function() {
                    function findItem(items) {
                        for (var i = 0; i < items.length; i++) {
                            if (items[i].component?.id === '%s') return items[i];
                            var found = findItem(items[i].children || []);
                            if (found) return found;
                        }
                    }
                    findItem(document.querySelector('#appmenu-right').items)
                        .component.dispatchEvent(new MouseEvent('click', {bubbles: true, composed: true}));
                })()
                """.formatted(menuItemId);
        try {
            var alreadyActive = Boolean.TRUE.equals(page.evaluate(
                    "(document.body.getAttribute('theme') || '').includes('%s')".formatted(expectedBodyTheme)));
            if (alreadyActive) return;
            page.waitForFunction("""
                    (function() {
                        function findItem(items) {
                            for (var i = 0; i < items.length; i++) {
                                if (items[i].component?.id === '%s') return true;
                                if (findItem(items[i].children || [])) return true;
                            }
                            return false;
                        }
                        var mb = document.querySelector('#appmenu-right');
                        return mb && findItem(mb.items || []);
                    })()
                    """.formatted(menuItemId));
            page.evaluate(script);
            for (var i = 0; i < 20; i++) {
                var bodyThemeNow = (String)page.evaluate("document.body.getAttribute('theme')");
                if (bodyThemeNow != null && bodyThemeNow.contains(expectedBodyTheme)) break;
                if (i == 19) throw new IllegalStateException("body[theme]=" + bodyThemeNow);
                page.waitForTimeout(100);
            }
            PlaywrightHelper.log("    [%s] body[theme]=%s%n", name(),
                    page.evaluate("document.body.getAttribute('theme')"));
        } catch (Exception e) {
            var bodyTheme = page.evaluate("document.body.getAttribute('theme')");
            var menuItems = page.evaluate(
                    "(document.querySelector('#appmenu-right')?.items || []).length");
            throw new IllegalStateException(
                    "%s activation failed on %s — body[theme]=%s, #appmenu-right items=%s: %s"
                            .formatted(name(), page.url(), bodyTheme, menuItems, e.getMessage()),
                    e);
        }
    }
}
