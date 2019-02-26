package ${package}.view;

import org.linkki.framework.ui.component.Headline;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.VerticalLayout;

import ${package}.page.${ApplicationName}Page;


@SpringView(name = MainView.NAME)
public class MainView extends VerticalLayout implements View {

    private static final long serialVersionUID = 1L;

    public static final String NAME = "";

    @Autowired
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