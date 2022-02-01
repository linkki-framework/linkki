#set($symbol_pound='#')#set($symbol_dollar='$')#set($symbol_escape='\')
package ${package}.view;

import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.vaadin.component.page.AbstractPage;
import org.linkki.framework.ui.dialogs.ConfirmationDialog;
import org.linkki.util.handler.Handler;

public class ${ApplicationName}Page extends AbstractPage{

    private BindingManager bindingManager = new DefaultBindingManager();
    private HelloPmo hellopmo;

    public ${ApplicationName}Page() {
        this.hellopmo = new HelloPmo(()->createOwnDialog());
    }

    @Override
    public void createContent() {
        addSection(hellopmo);
    }

    @Override
    protected BindingManager getBindingManager() {
        return bindingManager;
    }

    public void createOwnDialog() {
        Handler handler = Handler.NOP_HANDLER;
        ConfirmationDialog dialog = new ConfirmationDialog("Now try to create your own linkki Web Application!",
                handler);
        dialog.setWidth("40em");
        dialog.open();
    }

}
