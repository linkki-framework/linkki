package ${package}.view;

import org.linkki.framework.ui.component.Headline;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

import ${package}.page.${ApplicationName}Page;

public class MainView extends VerticalLayout implements View {

    private static final long serialVersionUID = 1L;

    public static final String NAME = "";

    public MainView() {
        addComponent(new Headline("linkki Example Web Application"));
        ${ApplicationName}Page page = new ${ApplicationName}Page();
        page.createContent();
        addComponent(page);

        setSizeFull();
        setExpandRatio(page, 1f);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub
    }

}