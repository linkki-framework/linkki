package org.linkki.samples.gettingstarted;

import org.linkki.core.binding.BindingContext;
import org.linkki.core.binding.dispatcher.PropertyBehaviorProvider;
import org.linkki.core.ui.section.AbstractSection;
import org.linkki.core.ui.section.DefaultPmoBasedSectionFactory;
import org.linkki.samples.gettingstarted.model.Report;
import org.linkki.samples.gettingstarted.pmo.ReportPmo;
import org.linkki.util.handler.Handler;

import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

// tag::ui-class[]
@Theme(value = "valo")
public class GettingStartedUI extends UI {

    private static final long serialVersionUID = 1L;

    @Override
    protected void init(VaadinRequest request) {

        Page.getCurrent().setTitle("Linkki :: Getting Started");

        DefaultPmoBasedSectionFactory sectionFactory = new DefaultPmoBasedSectionFactory();
        AbstractSection section = sectionFactory.createSection(new ReportPmo(new Report()),
                                                               new BindingContext("report-context",
                                                                       PropertyBehaviorProvider.NO_BEHAVIOR_PROVIDER,
                                                                       Handler.NOP_HANDLER));

        setContent(section);
    }
}
// end::ui-class[]
