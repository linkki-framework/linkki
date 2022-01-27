#set($symbol_pound='#')#set($symbol_dollar='$')#set($symbol_escape='\')
package ${package};

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ${ApplicationName} implements AppShellConfigurator{

    public static void main(String[] args) {
        SpringApplication.run(${ApplicationName}.class, args);
    }

}
