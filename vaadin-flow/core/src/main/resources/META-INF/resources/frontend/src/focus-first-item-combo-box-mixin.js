import '@vaadin/combo-box/src/vaadin-combo-box-mixin.js';

import {_focusFirst} from './focus-first-item';

(function () {
    window.customComboBoxMixinConnector = {
        initLazy: (customComboBox) => {

            // Add support for tab key events
            const _boundOnKeyDown = customComboBox._onKeyDown.bind(customComboBox);
            customComboBox._onKeyDown = function (e) {
                if (e.key === "Tab") {
                    if ((this.opened || this.autoOpenDisabled) &&
                        (this.allowCustomValue || this._inputElementValue === "" || this._focusedIndex > -1)
                    ) {
                        this._closeOrCommit();
                    } else {
                        this.$.overlay.restoreFocusOnClose = false;
                    }
                } else {
                    _boundOnKeyDown(e);
                }
            }.bind(customComboBox);

            // Focus the first item from the filtered list
            const _boundFilterChanged = customComboBox._filterChanged.bind(customComboBox);
            customComboBox._filterChanged = function (filter) {
                _boundFilterChanged(filter);
                _focusFirst(customComboBox);
            };

            // Ensures that the focus item is updated when overlay is opened and an item is selected
            const _boundOpen = customComboBox.open.bind(customComboBox);
            customComboBox.open = function () {
                _boundOpen();
                if (this.opened) {
                    _focusFirst(customComboBox);
                }
            };
        },
    };

})();