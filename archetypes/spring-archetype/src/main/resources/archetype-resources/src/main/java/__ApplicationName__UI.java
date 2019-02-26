package ${package};

import org.linkki.framework.ui.application.LinkkiUi;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringNavigator;

@Theme("\${theme-name}")
@SpringUI
@SpringViewDisplay
public class \${ApplicationName}UI extends LinkkiUi implements ViewDisplay {

    private static final long serialVersionUID = 1L;

    private final SpringNavigator springNavigator;

    @Autowired
    public \${ApplicationName}UI(SpringNavigator springNavigator) {
        super(new ${ApplicationName}ApplicationConfig());
        this.springNavigator = springNavigator;
        setNavigator(springNavigator);
    }

    @Override
    protected void init(VaadinRequest request) {
        super.init(request);
    }

    @Override
    public SpringApplicationNavigator getNavigator() {
        return new SpringApplicationNavigator(springNavigator);
    }

    @Override
    public void showView(View view) {
        getApplicationLayout().showView(view);
    }

}
