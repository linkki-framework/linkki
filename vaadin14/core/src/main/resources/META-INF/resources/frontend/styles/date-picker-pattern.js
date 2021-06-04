import { DateTime } from "luxon";

const tryCatchWrapper = function (callback) {
    return window.Vaadin.Flow.tryCatchWrapper(callback, 'Vaadin Date Picker', 'vaadin-date-picker-flow');
};

const cleanString = tryCatchWrapper(function (string) {
    // Clear any non ascii characters from the date string,
    // mainly the LEFT-TO-RIGHT MARK.
    // This is a problem for many Microsoft browsers where `toLocaleDateString`
    // adds the LEFT-TO-RIGHT MARK see https://en.wikipedia.org/wiki/Left-to-right_mark
    return string.replace(/[^\x00-\x7F]/g, "");
});

window.setDatePickerPatterns = (datepicker, patterns) => {
  // Execute in a raf so the date-picker connector's i18n properties get overridden
    requestAnimationFrame(() => {
        datepicker.i18n.parseDate = tryCatchWrapper(function (dateString) {
            dateString = cleanString(dateString);

            for(let pattern of patterns) {
                let parsedDate = DateTime.fromFormat(dateString, pattern);
                if(parsedDate.isValid) {
                    return {
                        day: parsedDate.day,
                        month: parsedDate.month - 1,
                        year: parsedDate.year
                  }
                }
            }

            return false;
        });

        datepicker.i18n.formatDate = tryCatchWrapper(function (dateObject) {
            return DateTime.fromObject({day: dateObject.day, month: dateObject.month + 1, year: dateObject.year}).toFormat(patterns[0]);
        });

        // Use new format
        datepicker._selectedDateChanged(datepicker._selectedDate, datepicker.i18n.formatDate)
    });
}