var showConfirmation = false;
window.onbeforeunload = function() {
    if(showConfirmation) {
        return '';
    }
};
window.enableOnbeforeunloadConfirmation = function() {
    showConfirmation = true;
}
window.disableOnbeforeunloadConfirmation = function() {
    showConfirmation = false;
}
