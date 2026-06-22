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

import com.microsoft.playwright.Page;

/**
 * Themes under which the playground is compared.
 * Each value knows how to activate itself on a page.
 */
public enum Theme {

    DEFAULT("default") {
        @Override
        public void activate(Page page) {
            // no-op: default theme requires no action
        }
    },

    CARD("card") {
        /**
         * The card theme menu item is NOT in the DOM until the menu is opened — Vaadin renders it
         * lazily into an overlay. Access it via the Vaadin {@code items} API on the menu bar
         * component, which holds live component references regardless of overlay state.
         * Path: {@code #appmenu-right}.items[1].children[0].children[1] = "Card Theme".
         * Plain {@code .click()} does not trigger the handler — a composed bubbling MouseEvent is required.
         * Throws {@link IllegalStateException} if the item cannot be found or activation fails.
         */
        @Override
        public void activate(Page page) {
            var script = """
                    (function() {
                        var body = document.body;
                        if ((body.getAttribute('theme') || '').indexOf('card-sections') >= 0) return 'already-active';
                        var menuBar = document.querySelector('#appmenu-right');
                        if (!menuBar || !menuBar.items || menuBar.items.length === 0)
                            throw new Error('#appmenu-right not ready (items not initialized)');
                        function findItem(items) {
                            for (var i = 0; i < items.length; i++) {
                                if (items[i].component?.id === 'appmenu-theme-card') return items[i];
                                var found = findItem(items[i].children || []);
                                if (found) return found;
                            }
                            return null;
                        }
                        var cardItem = findItem(menuBar.items);
                        if (!cardItem) throw new Error('appmenu-theme-card not found in items tree');
                        cardItem.component.dispatchEvent(new MouseEvent('click', {bubbles: true, composed: true}));
                        return 'clicked';
                    })()
                    """;
            try {
                page.waitForFunction("document.querySelector('#appmenu-right')?.items?.length > 0");
                page.evaluate(script);
                page.waitForTimeout(500);
                var bodyTheme = (String)page.evaluate("document.body.getAttribute('theme')");
                if (bodyTheme == null || !bodyTheme.contains("card-sections")) {
                    throw new IllegalStateException("body[theme]=" + bodyTheme);
                }
                System.out.printf("    [card theme] body[theme]=%s%n", bodyTheme);
            } catch (Exception e) {
                throw new IllegalStateException("Card theme activation failed: " + e.getMessage(), e);
            }
        }
    };

    private final String label;

    Theme(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public abstract void activate(Page page);
}
