import "@vaadin/combo-box/src/vaadin-combo-box-scroller.js";

import {_focusFirst} from "./focus-first-item";

(function () {
    window.customComboBoxScrollerConnector = {
        initLazy: (customComboBox) => {

            // Focus the first item when overlay(scroller) is opened
            const _boundLoadingChanged = customComboBox._scroller.__loadingChanged.bind(customComboBox);
            customComboBox._scroller.__loadingChanged = function () {
                _boundLoadingChanged();
                if (!this.loading) {
                    _focusFirst(customComboBox);
                }
            };
        },
    };
})();
