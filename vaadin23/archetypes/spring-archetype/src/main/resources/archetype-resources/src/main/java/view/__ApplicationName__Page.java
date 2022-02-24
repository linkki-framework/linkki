#set($symbol_pound='#')#set($symbol_dollar='$')#set($symbol_escape='\')
package ${package}.view;

import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.vaadin.component.page.AbstractPage;
import org.linkki.framework.ui.dialogs.ConfirmationDialog;
import org.linkki.util.handler.Handler;

public class ${ApplicationName}Page extends AbstractPage {

    private static final long serialVersionUID = 1L;

    private final BindingManager bindingManager;

    public ${ApplicationName}Page() {
        this.bindingManager = new DefaultBindingManager();
    }

    @Override
    public void createContent() {
        addSection(new HelloPmo(this::createOwnDialog));
    }

    @Override
    protected BindingManager getBindingManager() {
        return bindingManager;
    }

    public void createOwnDialog() {
        new ConfirmationDialog("Now try to create your own linkki web application!", Handler.NOP_HANDLER).open();
    }

}
