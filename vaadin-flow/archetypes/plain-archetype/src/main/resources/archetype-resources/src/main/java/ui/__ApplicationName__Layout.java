#set($symbol_pound='#')#set($symbol_dollar='$')#set($symbol_escape='\')
package ${package}.ui;

import org.linkki.framework.ui.application.ApplicationLayout;

public class ${ApplicationName}Layout extends ApplicationLayout {

    private static final long serialVersionUID = 1L;

    public ${ApplicationName}Layout() {
        super(new ${ApplicationName}Config());
    }
}

