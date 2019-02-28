package \${package};

import org.linkki.framework.ui.application.LinkkiUi;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;

import \${package}.view.MainView;

@Theme("\${theme-name}")
@Widgetset("com.vaadin.v7.Vaadin7WidgetSet")
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
