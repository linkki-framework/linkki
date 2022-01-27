#set($symbol_pound='#')#set($symbol_dollar='$')#set($symbol_escape='\')
package ${package}.view;

import org.apache.commons.lang3.StringUtils;
import org.linkki.core.defaults.ui.aspects.types.EnabledType;
import org.linkki.core.defaults.ui.aspects.types.VisibleType;
import org.linkki.core.ui.aspects.annotation.BindPlaceholder;
import org.linkki.core.ui.element.annotation.UITextField;
import org.linkki.util.handler.Handler;
import org.linkki.core.ui.element.annotation.UILabel;
import org.linkki.core.ui.element.annotation.UIButton;
import com.vaadin.flow.component.icon.VaadinIcon;

public class HelloPmo {

    private String name;
    private Handler handler;

    public HelloPmo(Handler handler) {
        this.handler = handler;
    }

    @BindPlaceholder("Name")
    @UITextField(position = 10, label = "Please type your name and press Enter")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @UILabel(position = 20)
    public String getHello() {
        if (name != null) {
            return "Hello " + name + "! This is a linkki example web application running on Spring Boot.";
        } else {
            return "";
        }
    }

    @UIButton(position = 30, showIcon = true, icon = VaadinIcon.INFO, enabled = EnabledType.DYNAMIC, visible = VisibleType.DYNAMIC)
    public void button() {
        handler.apply();
    }

    public boolean isButtonEnabled() {
        return StringUtils.isNotBlank(name);
    }

    public boolean isButtonVisible() {
        return StringUtils.isNotBlank(name);
    }
}
