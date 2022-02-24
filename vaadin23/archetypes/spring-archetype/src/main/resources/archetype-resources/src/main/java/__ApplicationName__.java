#set($symbol_pound='#')#set($symbol_dollar='$')#set($symbol_escape='\')
package ${package};

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Theme("linkki")
@SpringBootApplication
public class ${ApplicationName} implements AppShellConfigurator {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        SpringApplication.run(${ApplicationName}.class, args);
    }

}
