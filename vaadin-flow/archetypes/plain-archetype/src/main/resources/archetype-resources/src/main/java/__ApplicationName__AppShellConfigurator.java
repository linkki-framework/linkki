#set($symbol_pound='#')#set($symbol_dollar='$')#set($symbol_escape='\')
package ${package};

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;

@Theme("linkki")
@SuppressWarnings("HideUtilityClassConstructor")
public class ${ApplicationName}AppShellConfigurator implements AppShellConfigurator {

    private static final long serialVersionUID = 1L;

}
