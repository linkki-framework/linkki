#set($symbol_pound='#')#set($symbol_dollar='$')#set($symbol_escape='\')
package ${package}.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.linkki.framework.ui.component.Headline;
import ${package}.ui.${ApplicationName}ApplicationLayout;

import java.io.Serial;

@PageTitle("${ApplicationName}")
@Route(value = "", layout = ${ApplicationName}ApplicationLayout.class)
public class ${ApplicationName}View extends VerticalLayout {

    @Serial
    private static final long serialVersionUID = 1L;

    public ${ApplicationName}View() {
        add(new Headline("${ApplicationName}"));
        setSizeFull();
        var page = new ${ApplicationName}Page();
        page.init();
        add(page);
    }
}