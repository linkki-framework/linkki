#set($symbol_pound='#')#set($symbol_dollar='$')#set($symbol_escape='\')
package ${package}.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.linkki.framework.ui.component.Headline;
import ${package}.ui.${ApplicationName}Layout;

@PageTitle("${ApplicationName}")
@Route(value = "", layout = ${ApplicationName}Layout.class)
public class ${ApplicationName}View extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    public ${ApplicationName}View() {
        add(new Headline("${ApplicationName}"));
        setSizeFull();
        ${ApplicationName}Page page = new ${ApplicationName}Page();
        page.init();
        add(page);
    }
}