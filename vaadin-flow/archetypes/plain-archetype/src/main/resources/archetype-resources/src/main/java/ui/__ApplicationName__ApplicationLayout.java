#set($symbol_pound='#')#set($symbol_dollar='$')#set($symbol_escape='\')
package ${package}.ui;

import org.linkki.framework.ui.application.ApplicationLayout;

import java.io.Serial;

public class ${ApplicationName}ApplicationLayout extends ApplicationLayout {

    @Serial
    private static final long serialVersionUID = 1L;

    public ${ApplicationName}ApplicationLayout() {
        super(new ${ApplicationName}ApplicationConfig());
    }
}

