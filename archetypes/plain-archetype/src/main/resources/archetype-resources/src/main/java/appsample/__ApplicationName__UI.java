package \${package}.appsample;

import org.linkki.framework.ui.application.LinkkiUi;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;

import \${package}.appsample.view.MainView;

@Theme("\${theme-name}")
public class \${ApplicationName}UI extends LinkkiUi implements ViewDisplay {

    private static final long serialVersionUID = 1L;

    public \${ApplicationName}UI() {
        super(new \${ApplicationName}ApplicationConfig());
    }

    @Override
    protected void init(VaadinRequest request) {
        super.init(request);
        addView(MainView.NAME, MainView.class);
    }

    @Override
    public void showView(View view) {
        getApplicationLayout().showView(view);
    }

}
