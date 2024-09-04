export function _focusFirst(comboBox) {
    if (comboBox.filteredItems && comboBox.filteredItems.length === 1) {
            comboBox._focusedIndex = 0;
    }
}