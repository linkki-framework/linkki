#set($symbol_pound='#')#set($symbol_dollar='$')#set($symbol_escape='\')
package ${package};

import org.linkki.core.ui.theme.LinkkiTheme;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;

import java.io.Serial;

@StyleSheet(LinkkiTheme.STYLESHEET)
public class ${ApplicationName}AppShellConfigurator implements AppShellConfigurator {

    @Serial
    private static final long serialVersionUID = 1L;

}
