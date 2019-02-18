package \${package}.appsample;

import org.linkki.framework.ui.application.ApplicationNavigator;

import com.vaadin.spring.navigator.SpringNavigator;

public class SpringApplicationNavigator extends ApplicationNavigator {

    private static final long serialVersionUID = 1L;

    public final SpringNavigator springNavigator;

    public SpringApplicationNavigator(SpringNavigator springNavigator) {
        this.springNavigator = springNavigator;
    }

    @Override
    public String getState() {
        return springNavigator.getState();
    }

    @Override
    public void navigateTo(String navigationState) {
        springNavigator.navigateTo(navigationState);
    }


}
