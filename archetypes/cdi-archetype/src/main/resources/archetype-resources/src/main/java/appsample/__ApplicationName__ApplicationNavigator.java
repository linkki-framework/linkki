package \${package}.appsample;

import java.util.function.Consumer;

import javax.enterprise.inject.Specializes;

import org.linkki.framework.ui.application.ApplicationLayout;

import com.vaadin.navigator.View;
import com.vaadin.ui.UI;

import \${package}.appsample.cdi.CdiApplicationNavigator;

@Specializes
public class \${ApplicationName}ApplicationNavigator extends CdiApplicationNavigator {

    private static final long serialVersionUID = 1L;

    @Override
    protected void init(UI ui, ApplicationLayout applicationLayout) {
        super.init(ui, applicationLayout);
    }

    public <T extends View> void showView(Class<T> clazz, Consumer<T> viewInitializer, String parameters) {
        super.showView(clazz, parameters);
    }

    public <T extends View> void showView(Class<T> clazz, Consumer<T> viewInitializer) {
        super.showView(clazz);
    }

    @Override
    public void refreshCurrentView() {
        super.refreshCurrentView();
    }

}