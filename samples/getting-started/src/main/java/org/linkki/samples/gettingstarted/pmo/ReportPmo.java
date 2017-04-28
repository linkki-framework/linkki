package org.linkki.samples.gettingstarted.pmo;

import static java.util.Objects.requireNonNull;

import org.linkki.core.PresentationModelObject;
import org.linkki.core.ui.section.annotations.EnabledType;
import org.linkki.core.ui.section.annotations.ModelObject;
import org.linkki.core.ui.section.annotations.RequiredType;
import org.linkki.core.ui.section.annotations.UIButton;
import org.linkki.core.ui.section.annotations.UIComboBox;
import org.linkki.core.ui.section.annotations.UISection;
import org.linkki.core.ui.section.annotations.UITextArea;
import org.linkki.samples.gettingstarted.model.Report;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Notification;

// tag::report-pmo[]
@UISection
public class ReportPmo implements PresentationModelObject {

    private final Report report;

    public ReportPmo(Report report) {
        this.report = requireNonNull(report, "report must not be null");
    }

    // end::report-pmo[]

    // tag::model-binding[]
    @ModelObject
    public Report getReport() {
        return report;
    }
    // end::model-binding[]

    // tag::textfield[]
    @UITextArea(position = 10,
                label = "Description",
                modelAttribute = "description",
                required = RequiredType.REQUIRED,
                rows = 5, columns = 50)
    public void description() {
    /* Use description from report (model object) directly */
    }
    // end::textfield[]

    // tag::combobox[]
    @UIComboBox(position = 20,
                label = "Type",
                modelAttribute = "type",
                required = RequiredType.REQUIRED)
    public void type() {
	/* - bind value to the property "type" from report
	 * - use enum constants from ReportType as available values
	 */
    }
    // end::combobox[]

    // tag::button[]
    @UIButton(position = 30,
              caption = "Send",
              icon = FontAwesome.SEND, showIcon = true,
              enabled = EnabledType.DYNAMIC)
    public void send() {
        report.save();
        Notification.show(
                String.format("Report with id %d filed!", report.getId()),
                "Thank you for reporting!",
                Notification.Type.TRAY_NOTIFICATION);
    }

    /**
     * Enable button only if description and type is present.
     *
     * @return {@code true} if button is enabled otherwise {@code false}
     */
    public boolean isSendEnabled() {
        String description = report.getDescription();
        return description != null && !description.isEmpty()
                && report.getType() != null;
    }
    // tag::button[]
}