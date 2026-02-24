#set($symbol_pound='#')#set($symbol_dollar='$')#set($symbol_escape='\')
package ${package}.ui;

import org.linkki.framework.ui.application.ApplicationConfig;
import org.linkki.framework.ui.application.menu.ApplicationMenuItemDefinition;
import org.linkki.util.Sequence;

public class ${ApplicationName}ApplicationConfig implements ApplicationConfig {

    @Override
    public ${ApplicationName}ApplicationInfo getApplicationInfo() {
        return new ${ApplicationName}ApplicationInfo();
    }

    @Override
    public Sequence<ApplicationMenuItemDefinition> getMenuItemDefinitions() {
        return Sequence.empty();
    }

}