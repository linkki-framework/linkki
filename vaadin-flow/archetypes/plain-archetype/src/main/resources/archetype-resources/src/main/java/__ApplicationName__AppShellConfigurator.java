#set($symbol_pound='#')#set($symbol_dollar='$')#set($symbol_escape='\')
package ${package};

import org.linkki.core.ui.theme.LinkkiTheme;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;

@StyleSheet(LinkkiTheme.STYLESHEET)
public class ${ApplicationName}AppShellConfigurator implements AppShellConfigurator {

    private static final long serialVersionUID = 1L;

}
