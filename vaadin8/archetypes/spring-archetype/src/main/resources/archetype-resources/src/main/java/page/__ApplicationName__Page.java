package \${package}.page;

import org.linkki.core.binding.manager.BindingManager;
import org.linkki.core.binding.manager.DefaultBindingManager;
import org.linkki.core.binding.validation.ValidationService;
import org.linkki.core.ui.component.page.AbstractPage;
import org.linkki.framework.ui.dialogs.ConfirmationDialog;
import org.linkki.util.handler.Handler;

import ${package}.pmo.HelloPmo;


public class \${ApplicationName}Page extends AbstractPage {

    private static final long serialVersionUID = 1L;

    private BindingManager bindingManager = new DefaultBindingManager(ValidationService.NOP_VALIDATION_SERVICE);
    private HelloPmo helloPmo;

    public \${ApplicationName}Page() {
        this.helloPmo = new HelloPmo(() -> createOwnDialog());
    }

    @Override
    public void createContent() {
        addSection(helloPmo);
    }

    @Override
    public BindingManager getBindingManager() {
        return bindingManager;
    }

    public void createOwnDialog() {
        Handler handler = Handler.NOP_HANDLER;
        ConfirmationDialog dialog = new ConfirmationDialog("Now try to create your own linkki Web Application!", handler);
        dialog.setWidth("40em");
        dialog.open();
    }

}